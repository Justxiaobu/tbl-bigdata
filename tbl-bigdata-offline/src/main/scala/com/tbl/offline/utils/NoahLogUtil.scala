package com.tbl.offline.utils

/**
  * author sunbin
  * date 2018/6/14 13:33
  * description noah日志工具类
  */

/**
  * 响应分析
  */
object NoahLogUtil {

  //结果封装类
  case class noahLog(time:String,other1:String,level:String,other2:String,msgType:String,other3:String,msg:String)

  val reg="^\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3})(.*\\])(.*) (.*) - (noah.*)>>>>(.*)\\{(.*),\"ri\":.*$".r

  //验证日志格式
  def isValidateLogLine(line: String): Boolean = {
    val options = reg.findFirstMatchIn(line)
    if (options.isEmpty) false else true
  }

  //解析日志
  def parseLogLine(line: String):noahLog = {
      if (!isValidateLogLine(line)) throw new IllegalArgumentException("日志格式异常")
      //从line中获取匹配的数据
      val matcher = reg.findFirstMatchIn(line).get
      // 构建返回值
      noahLog(
        matcher.group(1),
        matcher.group(2),
        matcher.group(3),
        matcher.group(4),
        matcher.group(5),
        matcher.group(6),
        "{"+matcher.group(7)+"}"

      )
  }

}

/**
  * 模块耗时分析
  */
object NoahModuleLog {
  //结果封装类
  case class moduleLog(time:String,other1:String,moduleAction:String,moduleId:String,moduleName:String)
  val moduleReg="^\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3})(.*)SPARK-(.*):(.*)_(.*).*$".r

  //验证日志格式
  def isValidateModuleLog(line: String): Boolean = {
    val options = moduleReg.findFirstMatchIn(line)
    if (options.isEmpty) false else true
  }

  //解析日志
  def parseModuleLog(line: String):moduleLog = {
    if (!isValidateModuleLog(line)) throw new IllegalArgumentException("日志格式异常")
    //从line中获取匹配的数据
    val matcher = moduleReg.findFirstMatchIn(line).get
    // 构建返回值
    moduleLog(
      matcher.group(1),
      matcher.group(2),
      matcher.group(3),
      matcher.group(4),
      matcher.group(5)
    )
  }
}


