package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import org.silverpeas.benchmark.{HttpProtocol, SilverpeasConnection, SimulationContext, SilverpeasFilters, SilverpeasHeaders, UsersFeeder}
import scala.concurrent.duration._
import scala.language.reflectiveCalls

/**
 * Simulates a sequence where publication services are involved into the getting of last
 * publications of a Silverpeas's platform. The users are all provided by a SSV feeder consuming the
 * <code>users.ssv</code> data file; there is around 2773 users.
 * After a successful connexion, the look homepage is accessed. This one if configured as well
 * displays the last publications. Then a space homepage is also accessed. As for look homepage,
 * if configured as well, the page displays also the last publications.
 * @author silveryocha
 */
class LastPublicationsSimulation extends Simulation with HttpProtocol with SimulationContext {

  val users: Array[Map[String, Any]] = UsersFeeder.onDomain("3")
  val aimedFullSpaceId: Any = ssv("data/spaces.ssv").readRecords.filter(s => s("TechId").equals("290")).head("Id");
  val otherUserProfileIdThanMe: Any = users.head("Id")

  override val filters = new SilverpeasFilters(conf)
  val silverpeasHeaders = new SilverpeasHeaders(conf)
  val silverpeas = new SilverpeasConnection(conf)

  val scn: ScenarioBuilder = scenario("Access to a space with the last publications and the user contacts")
    .feed(users)
    .exec(silverpeas.login)
    .pause(2)
    .exec(http("request_space-homepage-navigation")
      .get(s"${appPath}/RAjaxSilverpeasV5/dummy?ResponseId=spaceUpdater&Init=0&GetPDC=false&SpaceId=${aimedFullSpaceId}&&ietrick=120ie1t6r9i11c55k580")
      .headers(silverpeasHeaders.ajax)
      .resources(http("request_space-homepage-content")
        .get(s"${appPath}/dt?SpaceId=${aimedFullSpaceId}")
        .headers(silverpeasHeaders.page)))
    .pause(1)
    .exec(http("request_contact_portlet")
      .get(s"${appPath}/services/profile/users/${otherUserProfileIdThanMe}?extended=true&_=1580976717425")
      .headers(silverpeasHeaders.ajax)
      .resources(http(s"request_user-profile_${otherUserProfileIdThanMe}")
        .get(s"${appPath}/services/profile/users/${otherUserProfileIdThanMe}")
        .headers(silverpeasHeaders.ajax),
        http("request_user-profile_me")
          .get(s"${appPath}/services/profile/users/me")
          .headers(silverpeasHeaders.ajax)))
    .pause(2)
    .exec(silverpeas.logout)

  setUp(scn.inject(rampUsers(usersInjection.count) during (usersInjection.duration minutes)))
    .protocols(httpProtocol)
}