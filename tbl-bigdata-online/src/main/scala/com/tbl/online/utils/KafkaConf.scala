package com.tbl.online.utils

import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.immutable.Map

/**
  * author sunbin
  * date 2018-6-12 16:45:02
  */
object KafkaConf {

  val brokers = LoadConfs.getProperty("metadata.broker.list")
  val groupId = LoadConfs.getProperty("group.id")
  val topics = Array(LoadConfs.getProperty("kafka.topic1"))

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> brokers,
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> groupId,
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )
}