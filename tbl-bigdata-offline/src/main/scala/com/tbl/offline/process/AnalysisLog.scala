package com.tbl.offline.process

import java.text.SimpleDateFormat

import com.google.gson.Gson
import com.tbl.offline.entity.Protocols
import com.tbl.offline.utils.{LoadConfs, MysqlConf, NoahLogUtil}
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * author sunbin
  * date 2018-6-13 22:46:39
  * description 日志分析
  */
object AnalysisLog {

  //日志解析封装类
  case class logInfo(time:String,level:String,msgType:String,packageNo:String,pathData:String,command:String)
  //日志分析封装类
  case class logAnalyse(time:String,msgType:String,pack:String,command:String)

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
        .builder()
//        .master("local[2]")
        .appName("AnalysisLog")
        .config("spark.some.config.option", "some-value")
        .getOrCreate()

    import spark.implicits._
//    spark.read.textFile("D:\\noah_ack.log")   spark.read.textFile(LoadConfs.getProperty("hdfs.noah.path"))
    val noahLog2DF = spark.read.textFile(LoadConfs.getProperty("hdfs.noah.path")+"noah_ack.log")
        .filter(line=>NoahLogUtil.isValidateLogLine(line))
        .map(line=>{
            val noahlog= NoahLogUtil.parseLogLine(line)
            val protocols = new Gson().fromJson(noahlog.msg.toString,classOf[Protocols])
            logInfo(noahlog.time,noahlog.level,noahlog.msgType,protocols.getPackageNo,protocols.getPathData,protocols.getCommand)
          }
        ).toDF("time","level","msgType","packageNo","pathData","command")
    noahLog2DF.createOrReplaceTempView("tmp_log")

    /**
      * 统计每个包中sender和ack分别有多少次
      */
    spark.sql("select msgType,packageNo,pathData,command from tmp_log")
      .map(r=>{
        val pack = s"${r.getString(0)},${r.getString(1)},${r.getString(2)}"
        val command = r.getString(3)
        (pack,command)
      }).rdd
      .groupBy(_._1)
      .mapValues(v=>{
        val num = v.size
        val rs = v.minBy(_._2)
        val pack = rs._1.split(",")
        (num,pack(0),pack(1),pack(2),rs._2)
      }).values
      .toDF("num","msgtype","packageno","pathdata","command")
      .write
      .mode(SaveMode.Append)
      .option("driver",MysqlConf.driver)
      .jdbc(MysqlConf.url,"log_analysis_count",MysqlConf.prop)

    /**
      * 分析日志
      */
    val logRdd=spark.sql("select time,msgType,packageNo,pathData,command from tmp_log")
      .map(r => {
        val time = r.getString(0)
        val msgType = r.getString(1)
        val pack = r.getString(2)+"_"+r.getString(3)
        val command = r.getString(4)
        logAnalyse(time,msgType,pack,command)
      })
      .rdd
      .groupBy(_.pack)

    /**
      * 分析出每个包中最后一条日志为sender的情况
      * 认为存在包无响应
      */
    logRdd.mapValues(v =>{
        val rs = v.maxBy(_.time)
        var isErr = false
        //判断最后一条记录是否为sender
        if(rs.msgType.equals("noah_sender")){
          isErr=true
        }
        (isErr,rs.time,rs.msgType,rs.pack.split("_")(0),rs.pack.split("_")(1),rs.command)
      }).values
        .filter(_._1==true)
        .map(r=>{
          val logtime = r._2
          val msgtype = r._3
          val packageno = r._4
          val pathdata = r._5
          val command = r._6
          ("1",logtime,msgtype,packageno,pathdata,command)
        }).toDF("analysis_type","logtime","msgtype","packageno","pathdata","command")
        .write
        .mode(SaveMode.Append)
        .option("driver",MysqlConf.driver)
        .jdbc(MysqlConf.url,"log_analysis_result",MysqlConf.prop)


    /**
      * 分析出每个包中第一条记录和最后一条记录时间间隔大于10s小于30分钟的情况
      * 大于10s认为响应有延迟，小于30分钟防止packageNo重新计数
      */
    logRdd.filter(_._2.size >= 2)
      .mapValues(v=>{
        val df:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
        val minV = v.minBy(_.time)
        val maxV = v.maxBy(_.time)
        val diff = (df.parse(maxV.time).getTime-df.parse(minV.time).getTime)/1000
        var isErr = false
        if(10<=diff && (30*60)>diff){
          isErr=true
        }
        (isErr,diff,minV.time,maxV.time,maxV.pack.split("_")(0),maxV.pack.split("_")(1),maxV.command)
      }).values
      .filter(_._1==true)
      .map(r=>{
        val time_diff = r._2.toInt
        val logtime = s"${r._3},${r._4}"
        val packageno = r._5
        val pathdata = r._6
        val command = r._7
        ("2",time_diff,logtime,packageno,pathdata,command)
      }).toDF("analysis_type","time_diff","logtime","packageno","pathdata","command")
      .write
      .mode(SaveMode.Append)
      .option("driver",MysqlConf.driver)
      .jdbc(MysqlConf.url,"log_analysis_result",MysqlConf.prop)

    spark.stop()
  }

}
