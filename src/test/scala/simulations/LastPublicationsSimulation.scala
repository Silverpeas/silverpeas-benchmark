package simulations

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.silverpeas.benchmark.{SilverpeasConnection, SilverpeasFilters, SilverpeasHeaders}

import scala.concurrent.duration._

/**
 * Simulates a sequence where publication services are involved into the getting of last
 * publications of a Silverpeas's platform. The users are all provided by a SSV feeder consuming the
 * <code>users.ssv</code> data file; there is around 2773 users.
 * After a successful connexion, the look homepage is accessed. This one if configured as well
 * displays the last publications. Then a space homepage is also accessed. As for look homepage,
 * if configured as well, the page displays also the last publications.
 * @author silveryocha
 */
class LastPublicationsSimulation extends Simulation {

  val conf: Config = ConfigFactory.load()
  val host: String = conf.getString("host")
  val userCount: Int = conf.getInt("users.count")
  val duration: Int = conf.getInt("users.duration")

  val users: Array[Map[String, Any]] = ssv("data/users.ssv").readRecords.filter(u => u("Domain").equals("3")).toArray
  val aimedFullSpaceId: Any = ssv("data/spaces.ssv").readRecords.filter(s => s("TechId").equals("290")).head("Id");
  val otherUserProfileIdThanMe: Any = users.head("Id")

  val silverpeasCon = new SilverpeasConnection(conf)
  val silverpeasFilters = new SilverpeasFilters(conf)
  val silverpeasHeaders = new SilverpeasHeaders()

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(host)
    .inferHtmlResources(silverpeasFilters.getBlackList, silverpeasFilters.getWhiteList)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")

  val scn: ScenarioBuilder = scenario("LastPublicationsSimulation")
    .feed(users)
    .exec(silverpeasCon.login)
    .pause(2)
    .exec(http("request_space-homepage-navigation")
      .get(s"/silverpeas/RAjaxSilverpeasV5/dummy?ResponseId=spaceUpdater&Init=0&GetPDC=false&SpaceId=${aimedFullSpaceId}&&ietrick=120ie1t6r9i11c55k580")
      .headers(silverpeasHeaders.ajax)
      .resources(http("request_space-homepage-content")
        .get(s"/silverpeas/dt?SpaceId=${aimedFullSpaceId}")
        .headers(silverpeasHeaders.page)))
    .pause(1)
    .exec(http("request_contact_portlet")
      .get(s"/silverpeas/services/profile/users/${otherUserProfileIdThanMe}?extended=true&_=1580976717425")
      .headers(silverpeasHeaders.ajax)
      .resources(http(s"request_user-profile_${otherUserProfileIdThanMe}")
        .get(s"/silverpeas/services/profile/users/${otherUserProfileIdThanMe}")
        .headers(silverpeasHeaders.ajax),
        http("request_user-profile_me")
          .get("/silverpeas/services/profile/users/me")
          .headers(silverpeasHeaders.ajax)))
    .pause(2)
    .exec(silverpeasCon.logout)

  setUp(scn.inject(rampUsers(userCount) during (duration minutes))).protocols(httpProtocol)
}