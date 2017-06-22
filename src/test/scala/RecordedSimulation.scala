
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RecordedSimulation extends Simulation {

  val conf = jsonFile("silverpeas.json")
  val host: String = conf.records.apply(0).apply("host").asInstanceOf[String]
  val appContext: String = conf.records.apply(0).apply("silverpeas").asInstanceOf[String]

  val httpProtocol = http
    .baseURL(host)
    .inferHtmlResources(BlackList(""".*\.js\?v=.*""", """.*\.css\?v=.*""", """.*\.css""", """.*\.jpg""", """.*\.gif""", """.*\.png""", """.*\.svg""", """.*\.ico"""), WhiteList())
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0")

  val headers_0 = Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")

  val headers_24 = Map(
    "Accept" -> "application/json, text/javascript, */*; q=0.01",
    "X-Requested-With" -> "XMLHttpRequest",
    "X-STKN" -> "ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1")

  val headers_25 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "X-STKN" -> "ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1")

  val headers_29 = Map(
    "Accept" -> "application/font-woff2;q=1.0,application/font-woff;q=0.9,*/*;q=0.8",
    "Accept-Encoding" -> "identity")

  val headers_34 = Map(
    "Accept" -> "text/plain, */*; q=0.01",
    "X-Requested-With" -> "XMLHttpRequest",
    "X-STKN" -> "ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1")

  val headers_39 = Map("Accept" -> "image/png,image/*;q=0.8,*/*;q=0.5")

  val headers_48 = Map(
    "Accept" -> "application/json, text/plain, */*",
    "X-STKN" -> "ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1")

  val headers_62 = Map(
    "Accept" -> "text/event-stream",
    "Pragma" -> "no-cache")

  val uri1 = "http://www.silverpeas.com"

  val scn = scenario("RecordedSimulation2")
    .exec(http("request_0")
      .get(appContext + "/")
      .headers(headers_0)
      .resources(http("request_1")
        .get(appContext + "/util/javaScript/silverpeas-tkn.js?_=1497450723336")))
    .pause(4)
    .exec(http("request_2")
      .post(appContext + "/AuthenticationServlet")
      .headers(headers_0)
      .formParam("Login", "yocha")
      .formParam("cryptedPassword", "")
      .formParam("Password", "sasa")
      .formParam("DomainId", "0")
      .resources(http("request_3")
        .get(appContext + "/util/javaScript/silverpeas-tkn.js?_=1497450727732"),
        http("request_4")
          .get(appContext + "/util/javaScript/polyfill/array.generics.min.js"),
        http("request_5")
          .get(appContext + "/util/javaScript/polyfill/classList.min.js"),
        http("request_6")
          .get(appContext + "/util/javaScript/polyfill/es6-promise.min.js"),
        http("request_7")
          .get(appContext + "/util/javaScript/polyfill/customEventIEPolyfill.min.js"),
        http("request_8")
          .get(appContext + "/util/javaScript/polyfill/eventListenerIEPolyfill.min.js"),
        http("request_9")
          .get(appContext + "/util/javaScript/i18n.properties.js"),
        http("request_10")
          .get(appContext + "/util/javaScript/polyfill/eventsource.min.js"),
        http("request_11")
          .get(appContext + "/util/javaScript/jquery/jquery.cookie.js"),
        http("request_12")
          .get(appContext + "/util/javaScript/jquery/jquery-migrate-1.4.1.min.js"),
        http("request_13")
          .get(appContext + "/util/javaScript/jquery/jquery.json-2.3.min.js"),
        http("request_14")
          .get(appContext + "/util/javaScript/jquery/jquery-2.2.4.min.js"),
        http("request_15")
          .get(appContext + "/util/javaScript/moment-with-locales.min.js"),
        http("request_16")
          .get(appContext + "/util/javaScript/jquery/jquery-ui.min.js"),
        http("request_17")
          .get(appContext + "/util/yui/yahoo-dom-event/yahoo-dom-event.js"),
        http("request_18")
          .get(appContext + "/util/javaScript/silverpeas-jquery.js"),
        http("request_19")
          .get(appContext + "/util/yui/menu/menu-min.js"),
        http("request_20")
          .get(appContext + "/util/yui/container/container_core-min.js"),
        http("request_21")
          .get(appContext + "/util/javaScript/polyfill/silverpeas-polyfills.js"),
        http("request_22")
          .get(appContext + "/clipboard/jsp/Idle.jsp")
          .headers(headers_0),
        http("request_23")
          .get(appContext + "/Ragenda/jsp/importCalendar")
          .headers(headers_0),
        http("request_24")
          .get(appContext + "/services/messages/cbf99f62-3093-4ff0-83a5-314df5bdff64?_=1497450728163")
          .headers(headers_24),
        http("request_25")
          .get(appContext + "/admin/jsp/bodyPartSilverpeasV5.jsp?Login=1")
          .headers(headers_25),
        http("request_26")
          .get(appContext + "/admin/jsp/TopBarSilverpeasV5.jsp")
          .headers(headers_25),
        http("request_27")
          .get(appContext + "/util/javaScript/outlook_applet.js"),
        http("request_28")
          .get(appContext + "/admin/jsp/DomainsBarSilverpeasV5.jsp?privateDomain=null")
          .headers(headers_25),
        http("request_29")
          .get("/weblib/skinBigAndAngular/font/texgyreadventor-regular-webfont.woff")
          .headers(headers_29),
        http("request_30")
          .get(appContext + "/services/news/ticker?_=1497450728169")
          .headers(headers_24),
        http("request_31")
          .get(appContext + "/RAjaxSilverpeasV5/dummy?ResponseId=spaceUpdater&Init=1&GetPDC=false&SpaceId=null&ComponentId=null&&ietrick=117ie5t14r16i32c9k168")
          .headers(headers_25),
        http("request_32")
          .get(appContext + "/RpdcSearch/jsp/ChangeSearchTypeToExpert?SearchPage=/admin/jsp/pdcSearchSilverpeasV5.jsp&spaces=null&componentSearch=null&FromPDCFrame=true")
          .headers(headers_25),
        http("request_33")
          .get(appContext + "/dt")
          .headers(headers_0),
        http("request_34")
          .get(appContext + "/services/bundles/org/silverpeas/pdcPeas/multilang/pdcBundle.properties?_=1497450728176")
          .headers(headers_34),
        http("request_35")
          .get(appContext + "/util/javaScript/silverpeas-tkn.js?_=1497450729388"),
        http("request_36")
          .get(appContext + "/services/bundles/org/silverpeas/pdcPeas/multilang/pdcBundle_$$.properties?_=1497450728177")
          .headers(headers_34),
        http("request_37")
          .get(appContext + "/socialNetwork/jsp/js/newsfeed.js"),
        http("request_38")
          .get(appContext + "/portlet/jsp/jsr/js/demo.js"),
        http("request_39")
          .get(appContext + "/FileServer/thumbnail?ComponentId=quickinfo48&SourceFile=1460468188480.jpg&MimeType=image/jpeg&Directory=images&Size=350x")
          .headers(headers_39),
        http("request_40")
          .get(appContext + "/FileServer/thumbnail?ComponentId=quickinfo48&SourceFile=1485437817797.jpg&MimeType=image/jpeg&Directory=images&Size=350x")
          .headers(headers_39),
        http("request_41")
          .get(appContext + "/FileServer/thumbnail?ComponentId=quickinfo48&SourceFile=1485440315501.jpg&MimeType=image/jpeg&Directory=images&Size=350x")
          .headers(headers_39),
        http("request_42")
          .get(appContext + "/FileServer/thumbnail?ComponentId=quickinfo48&SourceFile=1456934659131.jpg&MimeType=image/jpeg&Directory=images&Size=350x")
          .headers(headers_39),
        http("request_43")
          .get(appContext + "/services/pdc/filter/used?_=1497450728178")
          .headers(headers_24),
        http("request_44")
          .get(uri1 + "/")
          .headers(headers_0),
        http("request_45")
          .get(appContext + "/services/messages/0d78aafe-7e6b-4ff9-be19-acd68e8b9cc9?_=1497450729911")
          .headers(headers_24),
        http("request_46")
          .get(appContext + "/services/bundles/org/silverpeas/social/multilang/socialNetworkBundle.properties?_=1497450729913")
          .headers(headers_34),
        http("request_47")
          .get(appContext + "/services/bundles/org/silverpeas/social/multilang/socialNetworkBundle.properties?_=1497450729914")
          .headers(headers_34),
        http("request_48")
          .get(appContext + "/services/profile/users/0")
          .headers(headers_48),
        http("request_49")
          .get(appContext + "/services/profile/users/1")
          .headers(headers_48),
        http("request_50")
          .get(appContext + "/services/profile/users/1")
          .headers(headers_48),
        http("request_51")
          .get(appContext + "/services/profile/users/0")
          .headers(headers_48),
        http("request_52")
          .get(appContext + "/services/bundles/org/silverpeas/social/multilang/socialNetworkBundle_$$.properties?_=1497450729915")
          .headers(headers_34),
        http("request_53")
          .get(appContext + "/services/bundles/org/silverpeas/social/multilang/socialNetworkBundle_$$.properties?_=1497450729916")
          .headers(headers_34),
        http("request_54")
          .get(appContext + "/services/profile/users/1")
          .headers(headers_48),
        http("request_55")
          .get(appContext + "/services/profile/users/0")
          .headers(headers_48),
        http("request_56")
          .get(appContext + "/services/profile/users/me")
          .headers(headers_48),
        http("request_57")
          .get(appContext + "/RnewsFeedJSONServlet?userId=3&View=MyFeed&type=ALL&offset=0&Init=true&IEFix=1497450730832")
          .headers(headers_24),
        http("request_58")
          .get(appContext + "/services/profile/users/1")
          .headers(headers_48),
        http("request_59")
          .get(appContext + "/services/profile/users/4")
          .headers(headers_48),
        http("request_60")
          .get(appContext + "/services/profile/users/5")
          .headers(headers_48)))
    .pause(2)
    .exec(http("request_61")
      .get(appContext + "/Rclipboard/jsp/Idle.jsp?message=IDLE")
      .headers(headers_0)
      .resources(http("request_62")
        .get(appContext + "/sse/common")
        .headers(headers_62),
        http("request_63")
          .get(appContext + "/LogoutServlet?X-STKN=ZDY1OTM1NTYtZTFkYS00ODNiLTkyMGYtOGExODk2NmY2ZGM1")
          .headers(headers_0),
        http("request_64")
          .get(appContext + "/util/javaScript/silverpeas-tkn.js?_=1497450738094")))

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
