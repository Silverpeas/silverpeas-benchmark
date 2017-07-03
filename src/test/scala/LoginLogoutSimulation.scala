
import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.silverpeas.benchmark.Silverpeas

import scala.concurrent.duration._

class LoginLogoutSimulation extends Simulation {

  val conf: Config = ConfigFactory.load()
  val host: String = conf.getString("host")
  val userCount: Int = conf.getInt("users.count")
  val duration: Int = conf.getInt("users.duration")

  val users = ssv("users.ssv")
  val silverpeas = new Silverpeas(conf)

  val httpProtocol = http
    .baseURL(host)
    .inferHtmlResources(BlackList(""".*\.js\?v=.*""", """.*\.css\?v=.*""", """.*\.css""", """.*\.jpg""", """.*\.gif""", """.*\.png""", """.*\.svg""", """.*\.ico"""), WhiteList())
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0")

  val scn = scenario("LoginLogoutSimulation")
    .feed(users)
    .exec(silverpeas.login)
    .pause(2)
    .exec(silverpeas.logout)

  setUp(scn.inject(rampUsers(userCount) over (duration minutes))).protocols(httpProtocol)
}
