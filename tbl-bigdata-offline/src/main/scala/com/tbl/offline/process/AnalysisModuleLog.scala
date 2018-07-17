package com.tbl.offline.process

import java.text.SimpleDateFormat

import com.tbl.offline.utils.{LoadConfs, MysqlConf, NoahModuleLog}
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * author sunbin
  * date 2018/7/10 14:00
  * description 统计每个模块流转的时间，分析出耗时超过3s的数据
  */
object AnalysisModuleLog {

  //结果封装类
  case class moduleInfo(time:String,moduleId:String,moduleName:String)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
//      .master("local[*]")
      .appName("Analysis Module Log")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    import spark.implicits._

    //解析日志数据
    //    spark.read.textFile("D:\\noah_info.log.1")   spark.read.textFile(LoadConfs.getProperty("hdfs.noah.path"))
    val moduleLog2DF = spark.read.textFile(LoadConfs.getProperty("hdfs.noah.path")+"noah_info.log.1")
      .filter(line=>NoahModuleLog.isValidateModuleLog(line))
      .map(line=>{
          val modulelog= NoahModuleLog.parseModuleLog(line)
          moduleInfo(modulelog.time,modulelog.moduleId,modulelog.moduleName)
        }
      ).toDF("time","moduleId","moduleName")
      .createOrReplaceTempView("module_log")

    /**
      * 分析日志
      * 将模块流转时间大于3s的数据进行入库
      */
    spark.sql("select time,moduleId,moduleName from module_log")
      .map(r=>moduleInfo(r.getString(0),r.getString(1),r.getString(2)))
      .rdd
      .groupBy(_.moduleId)
      .filter(_._2.size > 1)
      .mapValues(v=>{
        val df:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
        val minV = v.minBy(_.time)
        val maxV = v.maxBy(_.time)
        val diff = (df.parse(maxV.time).getTime-df.parse(minV.time).getTime)/1000
        (diff,minV.time,maxV.time,maxV.moduleId,maxV.moduleName)
      }).values
      .filter(_._1 >= 3L)
      .toDF("time_diff","time_min","time_max","moduleId","moduleName")
      .write
      .mode(SaveMode.Append)
      .option("driver",MysqlConf.driver)
      .jdbc(MysqlConf.url,"log_analysis_module",MysqlConf.prop)

    spark.stop()



  }

}
