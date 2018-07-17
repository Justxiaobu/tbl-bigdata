package com.tbl.online.process

import com.google.gson.Gson
import com.tbl.online.utils.KafkaConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext, TaskContext}

/**
  * author sunbin
  * date 2018/6/13 9:18
  * description 运维数据监控
  */
object YwMonitor {
  case class People(name:String,age:Int,sex:String)

  def main(args: Array[String]): Unit = {

    val kafkaParams = KafkaConf.kafkaParams
    val topics = KafkaConf.topics
    //三板斧
    val sparkConf = new SparkConf().setAppName("YwMonitor")
//      .setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val ssc = new StreamingContext(sc,Seconds(10))
    //本地策略
    val preferredHosts = LocationStrategies.PreferConsistent
    val stream = KafkaUtils.createDirectStream[String,String](
      ssc,
      preferredHosts,
      Subscribe[String,String](topics,kafkaParams)
    )

    //业务处理
    val lines = stream.map(_.value())

    lines.foreachRDD(rdd =>{
      rdd.foreachPartition(part => {
        part.foreach(person => {
          val gson = new Gson()
          val per = gson.fromJson(person,classOf[People])
          println(s"***********============>name:${per.name}  age:${per.age}  sex:${per.sex}")
        })
      })
    })

    stream.foreachRDD{rdd =>
      //获取偏移量
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      rdd.foreachPartition{iter =>
        val o:OffsetRange = offsetRanges(TaskContext.get.partitionId)
        println(s"============================>${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
      }
      //存储偏移量到kafka自身
      stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
    }


    //spark streaming工作
    ssc.start()
    ssc.awaitTermination()

  }
}
