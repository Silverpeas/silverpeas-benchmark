package org.silverpeas.benchmark

import com.typesafe.config.Config
import io.gatling.core.filter.{BlackList, WhiteList}

import collection.JavaConverters._

/**
 * The SilverpeasFilters class permits to prepare blacklist and whitelist to specify to a scenario.
 * @author silveryocha
 */
class SilverpeasFilters(val conf: Config) {
  private val blackList: List[String] = conf.getString("scenario.blacklist.default").split(",").toList.map(_.trim())
  private val whiteList: List[String] = conf.getString("scenario.whitelist.default").split(",").toList.map(_.trim())

  def addToBlackList(newPatterns: Seq[String]): Unit = {
    newPatterns.foreach(blackList.+)
  }
  def getBlackList: BlackList = {
    BlackList(blackList.map(escape))
  }
  def addToWhiteList(newPatterns: Seq[String]): Unit = {
    newPatterns.foreach(whiteList.+)
  }
  def getWhiteList: WhiteList = {
    WhiteList(whiteList.map(escape))
  }

  private def escape(pattern: String): String = {
    pattern
      .replace(""".*""", """@#@#@""")
      .replace(""".""", """[.]""")
      .replace("""@#@#@""", """.*""")
  }
}
