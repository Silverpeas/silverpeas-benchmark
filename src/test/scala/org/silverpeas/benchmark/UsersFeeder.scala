package org.silverpeas.benchmark

import io.gatling.core.Predef._
import io.gatling.core.feeder.{BatchableFeederBuilder, Record}

/**
 * Feeder of users to use in simulations for accessing and performing operations in Silverpeas
 *
 * @author mmoquillon
 */
object UsersFeeder {

  val users: BatchableFeederBuilder[String] = ssv("data/users.ssv")

  def circular(): users.F = users.circular

  def onDomain(domainId: String): Array[Record[Any]] =
    users.readRecords.filter(u => u("Domain").equals(domainId)).toArray

}
