package com.example

import ch.qos.logback.classic.LoggerContext
import com.typesafe.scalalogging.LazyLogging
import net.logstash.logback.appender.LogstashUdpSocketAppender
import net.logstash.logback.argument.StructuredArguments.kv
import net.logstash.logback.layout.LogstashLayout
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

object TestSpark extends App with LazyLogging{

  val loggerFactory: LoggerContext =
    LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

  val rootLogger = loggerFactory.getLogger("ROOT")

  val exampleLogger = loggerFactory.getLogger("com.example")


  val logstashLayout = new LogstashLayout()
  logstashLayout.setContext(loggerFactory)
  logstashLayout.setIncludeContext(true)
  logstashLayout.setCustomFields("""{ "@es_index_name" : "log-test-index" }""")
  logstashLayout.start()
  val logstashUdpAppender = new LogstashUdpSocketAppender()
  logstashUdpAppender.setContext(loggerFactory)
  logstashUdpAppender.setHost("localhost")
  logstashUdpAppender.setPort(32020)
  logstashUdpAppender.setName("LOGSTASH_UDP_APPENDER")
  logstashUdpAppender.setLayout(logstashLayout)
  logstashUdpAppender.start()

  exampleLogger.addAppender(logstashUdpAppender)


  logger.info("Test log")


  override def main(args: Array[String]): Unit = {

    logger.info("Test log")

    val sc = new SparkContext(new SparkConf())



    val r: RDD[String] = sc.textFile("adl://home/user/technical_users/jose/test_logs/all_good_lines.csv", 3)

    r.foreach { l =>
      //logger.info("Obtained: {}",kv("obtained_string",l))
      print(s"l: $l")
      logger.info(s"Line is {}",kv("line",l))
    }

  }
}
