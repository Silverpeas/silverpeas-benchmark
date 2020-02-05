package org.silverpeas.benchmark

import com.typesafe.config.Config
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
 * The SilverpeasConnection class is responsible to open a connection to the remote Silverpeas
 * application. Id est to sign in with the credentials of a user and to logout.
 *
 * @author mmoquillon
 */
class SilverpeasConnection(val conf: Config) {

  private val appPath: String = conf.getString("silverpeas")
  private val defaultUserDomainId: String = conf.getString("users.domainId")

  private val headers = List(
    Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
    Map(
      "Accept" -> "application/json, text/javascript, */*; q=0.01",
      "X-Requested-With" -> "XMLHttpRequest",
      "X-STKN" -> "ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1"),
    Map(
      "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
      "X-STKN" -> "ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1"),
    Map(
      "Accept" -> "text/plain, */*; q=0.01",
      "X-Requested-With" -> "XMLHttpRequest",
      "X-STKN" -> "ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1"),
    Map(
      "Accept" -> "application/json, text/plain, */*",
      "X-STKN" -> "ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1")
  )

  val login = exec(http("Authentication")
    .post(appPath + "/AuthenticationServlet")
    .headers(headers(0))
    .formParam("Login", "${Login}")
    .formParam("cryptedPassword", "")
    .formParam("Password", "${Password}")
    .formParam("DomainId", defaultUserDomainId)
    .resources(http("SilverpeasMainPage")
      .get(appPath + "/admin/jsp/MainFrameSilverpeasV5.jsp"),
      http("IDLE_JSP_IFRAME")
        .get(appPath + "/clipboard/jsp/Idle.jsp")
        .headers(headers(0)),
      http("IMPORT_CALENDAR_IFRAME")
        .get(appPath + "/Ragenda/jsp/importCalendar")
        .headers(headers(0)),
      http("SilverpeasMainPage_Body")
        .get(appPath + "/admin/jsp/bodyPartSilverpeasV5.jsp?Login=1")
        .headers(headers(2)),
      http("SilverpeasMainPage_TopBar")
        .get(appPath + "/admin/jsp/TopBarSilverpeasV5.jsp")
        .headers(headers(2)),
      http("SivlerpeasMainPage_DomainsBar")
        .get(appPath + "/admin/jsp/DomainsBarSilverpeasV5.jsp?privateDomain=null")
        .headers(headers(2)),
      http("TICKER_NEWS")
        .get(appPath + "/services/news/ticker?_=1497450728169")
        .headers(headers(1)),
      http("SPACE_UPDATER")
        .get(appPath + "/RAjaxSilverpeasV5/dummy?ResponseId=spaceUpdater&Init=1&GetPDC=false&SpaceId=null&ComponentId=null&&ietrick=117ie5t14r16i32c9k168")
        .headers(headers(2)),
      http("PDC_SEARCH")
        .get(appPath + "/RpdcSearch/jsp/ChangeSearchTypeToExpert?SearchPage=/admin/jsp/pdcSearchSilverpeasV5.jsp&spaces=null&componentSearch=null&FromPDCFrame=true")
        .headers(headers(2)),
      http("PDC_I18N_BUNDLE")
        .get(appPath + "/services/bundles/org/silverpeas/pdcPeas/multilang/pdcBundle.properties?_=1497450728176")
        .headers(headers(3)),
      http("PDC_USED")
        .get(appPath + "/services/pdc/filter/used?_=1497450728178")
        .headers(headers(1)),
      http("SOCIAL_NETWORD_I18N_BUNDLE")
        .get(appPath + "/services/bundles/org/silverpeas/social/multilang/socialNetworkBundle.properties?_=1497450729913")
        .headers(headers(3)),
      http("USER_PROFILE_ME")
        .get(appPath + "/services/profile/users/me")
        .headers(headers(4)),
      http("NEWS_FEED")
        .get(appPath + "/RnewsFeedJSONServlet?userId=3&View=MyFeed&type=ALL&offset=0&Init=true&IEFix=1497450730832")
        .headers(headers(1))))

  val logout = exec(http("Logout")
    .get(appPath + "/LogoutServlet?X-STKN=ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1")
    .headers(headers(0)))
}
