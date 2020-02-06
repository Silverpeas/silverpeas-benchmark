package org.silverpeas.benchmark

import com.typesafe.config.Config
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
 * The SilverpeasConnection class is responsible to open a connection to the remote Silverpeas
 * application. It provides two features for scenario: one to login and another to logout the
 * Silverpeas portal.
 *
 * @author mmoquillon
 */
class SilverpeasConnection(val conf: Config) {

  private val appPath: String = conf.getString("silverpeas")
  private val defaultUserDomainId: String = conf.getString("users.domainId")

  private val headers =
    Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

  val login = exec(http("Authentication")
    .post(appPath + "/AuthenticationServlet")
    .headers(headers)
    .formParam("Login", "${Login}")
    .formParam("cryptedPassword", "")
    .formParam("Password", "${Password}")
    .formParam("DomainId", defaultUserDomainId))

  val logout = exec(http("Logout")
    .get(appPath + "/Logout")
    .headers(headers))
}
