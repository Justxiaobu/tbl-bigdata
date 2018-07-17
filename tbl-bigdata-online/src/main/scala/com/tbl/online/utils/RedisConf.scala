package com.tbl.online.utils

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool


/**
  * author sunbin
  * date 2018/7/17 15:11
  * description 
  */
object RedisConf extends Serializable {
  val redisHost = LoadConfs.getProperty("redis_host")
  val redisPort = LoadConfs.getInteger("redis_port")
  val redisPsw = LoadConfs.getProperty("redis_password")
  val redisTimeout = LoadConfs.getInteger("redis_timeout")

  var config = new GenericObjectPoolConfig()
  config.setMaxTotal(LoadConfs.getInteger("jedis_pool_maxTotal"))
  config.setMaxIdle(LoadConfs.getInteger("jedis_pool_maxIdle"))
  config.setMinIdle(LoadConfs.getInteger("jedis_pool_minIdle")) //设置最小空闲数

  config.setMaxWaitMillis(LoadConfs.getLong("jedis_pool_maxWaitMillis"))
  config.setTestOnBorrow(LoadConfs.getBoolean("jedis_pool_testOnBorrow"))
  config.setTestOnReturn(LoadConfs.getBoolean("jedis_pool_testOnReturn"))
  //Idle时进行连接扫描//Idle时进行连接扫描
  config.setTestWhileIdle(LoadConfs.getBoolean("jedis_pool_testWhileIdle"))
  //表示idle object evitor两次扫描之间要sleep的毫秒数
  config.setTimeBetweenEvictionRunsMillis(LoadConfs.getLong("jedis_pool_timeBetweenEvictionRunsMillis"))
  //表示idle object evitor每次扫描的最多的对象数
  config.setNumTestsPerEvictionRun(LoadConfs.getInteger("jedis_pool_numTestsPerEvictionRun"))
  //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
  config.setMinEvictableIdleTimeMillis(LoadConfs.getLong("jedis_pool_minEvictableIdleTimeMillis"))

  val pool = new JedisPool(config, redisHost, redisPort, redisTimeout,redisPsw)

}
