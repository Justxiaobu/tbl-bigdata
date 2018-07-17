package com.tbl.offline.process

import org.apache.spark.{SparkConf, SparkContext}

/**
  * author sunbin
  * date 2018/7/6 13:35
  * description 
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("WordCount")
          .setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val book = sc.textFile("D:\\The Red and the Black.txt")
    println(book.flatMap(_.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _).count())
//        .sortBy(_._2)
//        .foreach(println)
  }

}
