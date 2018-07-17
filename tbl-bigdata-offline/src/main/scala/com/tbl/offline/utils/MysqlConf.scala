package com.tbl.offline.utils

import java.sql.Connection
import java.util.Properties

import org.apache.commons.dbcp.BasicDataSource

/**
  * author sunbin
  * date 2018/6/15 15:00
  * description mysql连接工具类
  */
object MysqlConf {

  val driver ="com.mysql.jdbc.Driver"
  val url = LoadConfs.getProperty("mysql.url")
  val username = LoadConfs.getProperty("mysql.user")
  val password = LoadConfs.getProperty("mysql.password")
  val prop = new Properties
  prop.put("user", username)
  prop.put("password", password)

  object JDBCConnectionPool{
    private var dataSource: BasicDataSource = _
    //获取连接
    def getConnection(): Connection = {
      if (dataSource == null) {
        dataSource = new BasicDataSource()
        dataSource.setDriverClassName(driver)
        dataSource.setUrl(url)
        dataSource.setUsername(username)
        dataSource.setPassword(password)
        dataSource.setInitialSize(5)
        dataSource.setMaxActive(10)
        dataSource.setMaxWait(10000)
      }
      var con: Connection = null
      try {
        con = dataSource.getConnection()
        con.setAutoCommit(false)
      } catch {
        case e: Exception => println(e.getMessage)
      }
      con
    }

    //关闭连接
    def close(): Unit = {
      if (dataSource != null) dataSource.close()
    }
  }

}
