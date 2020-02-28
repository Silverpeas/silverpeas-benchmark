package org.silverpeas.benchmark

import com.typesafe.config.Config
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
 * The SilverpeasConnection class is responsible to open a connection to the remote Silverpeas
 * application. It provides two features for scenario: one to login and another to logout the
 * Silverpeas portal.
 * @author silveryocha
 */
class SilverpeasHeaders() {
  val page = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "Origin" -> "https://localhost",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1")

  val nav = Map(
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Sec-Fetch-User" -> "?1")

  val ajax = Map(
    "Accept" -> "*/*",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-Requested-With" -> "XMLHttpRequest")
}
