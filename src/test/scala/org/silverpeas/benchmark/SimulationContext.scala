package org.silverpeas.benchmark

import com.typesafe.config.{Config, ConfigFactory}

/**
 * Context of the remote Silverpeas platform against to the tests will be ran.
 *
 * @author mmoquillon
 */
trait SimulationContext {

  implicit val conf: Config = ConfigFactory.load()

  val usersInjection = new {
    val count: Int = conf.getInt("users.count")
    val duration: Int = conf.getInt("users.duration")
  }

  val host: String = conf.getString("host")

  val appPath: String = conf.getString("silverpeas")

}
