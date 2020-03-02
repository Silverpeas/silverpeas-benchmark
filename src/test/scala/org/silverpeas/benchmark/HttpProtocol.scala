package org.silverpeas.benchmark

import com.typesafe.config.Config
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

/**
 * Initializer of the HTTP protocol object builder by setting some of the properties that are
 * common to all of the simulations.
 *
 * @author mmoquillon
 */
trait HttpProtocol {

  val host: String
  val filters: SilverpeasFilters

  def httpProtocol: HttpProtocolBuilder = http
    .baseUrl(host)
    .inferHtmlResources(filters.getBlackList, filters.getWhiteList)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
}
