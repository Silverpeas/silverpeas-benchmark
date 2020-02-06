package simulations

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.silverpeas.benchmark.SilverpeasConnection

import scala.concurrent.duration._

/**
  * Simulates a sequence of connection/disconnection against a Silverpeas platform by using a
  * different user each time. The users are all provided by a SSV feeder consuming the
  * <code>users.ssv</code> data file; there is around 2773 users.
  */
class LoginLogoutSimulation extends Simulation {

  val conf: Config = ConfigFactory.load()
  val host: String = conf.getString("host")
  val userCount: Int = conf.getInt("users.count")
  val duration: Int = conf.getInt("users.duration")

  val users = ssv("data/users.ssv")
  val silverpeas = new SilverpeasConnection(conf)
  val headers = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")

  val httpProtocol = http
    .baseUrl(host)
    .inferHtmlResources(BlackList(""".*\.js\?v=.*""", """.*\.css\?v=.*""", """.*\.css""", """.*\.js""", """.*\.jpg""", """.*\.gif""", """.*\.png""", """.*\.svg""", """.*\.ico"""), WhiteList())
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")

  val scn = scenario("Login and logout")
    .feed(users)
    .exec(http("First access")
      .get("/silverpeas/")
      .headers(headers))
    .pause(2)
    .exec(silverpeas.login)
    .pause(2)
    .exec(silverpeas.logout)

  setUp(scn.inject(rampUsers(userCount) during (duration minutes))).protocols(httpProtocol)
}
