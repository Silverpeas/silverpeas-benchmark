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

  def filter: Filter = new Filter(users.readRecords)

  class Filter(val users: Seq[Record[Any]]) {

    def onDomain(domainId: Any): Filter =
      new Filter(users.filter(u => u("Domain").equals(domainId)))

    def onAccessRightTo(appInstId: Any): Filter = {
      val acl: Seq[Record[Any]] = ssv(s"data/${appInstId}.ssv").readRecords
      new Filter(users.filter(u => acl.exists(a => a("UserId").equals(u))))
    }

    def feed(): Array[Record[Any]] = users.toArray
  }

}
