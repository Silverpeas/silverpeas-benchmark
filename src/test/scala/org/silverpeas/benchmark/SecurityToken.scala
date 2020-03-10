package org.silverpeas.benchmark

import io.gatling.core.Predef.regex
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

/**
 * Asks for the current security token in use in the current web page so that the next statements
 * can indicate it to authenticate themselves. The token will be set in the scenario session. It
 * expects the Silverpeas application context is provided by the session's attribute appPath.
 *
 * @author mmoquillon
 */
object SecurityToken {

  private val headers =
    Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

  val fetchToken: ChainBuilder = exec(http("Gets the token")
    .get("${appPath}/util/javaScript/silverpeas-tkn.js?_=1583764891952")
    .headers(headers)
    .check(regex("(?s).+__stampURL\\(url, 'X-STKN', '([0-9a-zA-Z]+)'.+").saveAs("stkn")))
}
