package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import org.silverpeas.benchmark.{HttpProtocol, SilverpeasConnection, SimulationContext, SilverpeasFilters, UsersFeeder}
import scala.concurrent.duration._
import scala.language.reflectiveCalls

/**
  * Simulates a sequence of connection/disconnection against a Silverpeas platform by using a
  * different user each time. The users are all provided by a SSV feeder consuming the
  * <code>users.ssv</code> data file.
  * @author mmoquillon
  */
class LoginLogoutSimulation extends Simulation with HttpProtocol with SimulationContext {

  override val filters = new SilverpeasFilters(conf)

  val users = UsersFeeder.circular()
  val silverpeas = new SilverpeasConnection(conf)
  val headers = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")

  val scn: ScenarioBuilder = scenario("Login and logout")
    .feed(users)
    .exec(http("First access")
      .get(s"${appPath}/")
      .headers(headers))
    .pause(2)
    .exec(silverpeas.login)
    .pause(2)
    .exec(silverpeas.logout)

  setUp(scn.inject(rampUsers(usersInjection.count) during (usersInjection.duration minutes)))
    .protocols(httpProtocol)
}
