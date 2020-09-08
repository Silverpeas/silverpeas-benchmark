package simulations

import io.gatling.core.Predef._
import io.gatling.core.feeder.Record
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import org.silverpeas.benchmark.{HttpProtocol, SilverpeasConnection, SilverpeasFilters, SilverpeasHeaders, SimulationContext, UsersFeeder}

import scala.concurrent.duration._
import scala.language.reflectiveCalls

/**
 * Simulates the import of a PDF file into a Kmelia instance and then the access of the newly
 * created publications (created by the import).
 *
 * @author mmoquillon
 */
class InKmeliaPdfFileImportSimulation extends Simulation with HttpProtocol with SimulationContext {

  val users: Array[Map[String, Any]] = UsersFeeder.filter.onDomain("3").feed()
  val targetedKmelia: Seq[Record[Any]] = ssv("data/components.ssv").readRecords
    .filter(s => s("Application").equals("kmelia"))
    .filter(s => s("TechId").equals("18546"))
  val kmeliaId: Any = targetedKmelia.head("Id")
  val spaceId: Any = targetedKmelia.head("Space")

  override val filters = new SilverpeasFilters(conf)
  val headers = new SilverpeasHeaders(conf)
  val silverpeas = new SilverpeasConnection(conf)

  val scn: ScenarioBuilder = scenario("Import a PDF file into a Kmelia and access to the new publication")
    .feed(users)
    .exec(silverpeas.login)
    .pause(8)
    .exec(http(s"Access to the Kmelia instance ${kmeliaId}")
      .get(s"${appPath}/look/jsp/bodyPartAurora.jsp?ComponentId=${kmeliaId}")
      .headers(headers.page)
      .resources(http("The Kmelia instance main page")
        .get(s"${appPath}/Rkmelia/${kmeliaId}/Main")
        .headers(headers.page),
        http("Space's apps navigation")
          .get(s"${appPath}/look/jsp/DomainsBar.jsp?component_id=${kmeliaId}")
          .headers(headers.ajax),
        http("Space home page navigation")
          .get(s"${appPath}/RAjaxSilverpeasV5/dummy?ResponseId=spaceUpdater&Init=1&GetPDC=false&SpaceId=${spaceId}&ComponentId=${kmeliaId}&&ietrick=120ie2t2r13i44c58k810")
          .headers(headers.ajax),
        http("Treeview content")
          .get(s"${appPath}/services/folders/${kmeliaId}/0/treeview?lang=fr&IEFix=1583422501762")
          .headers(headers.ajax),
        http("List of publications")
          .get(s"${appPath}/RAjaxPublicationsListServlet?Id=0&ComponentId=${kmeliaId}&PubIdToHighlight=null&IEFix=1583422502312")
          .headers(headers.ajax),
        http("Content of the root folder")
          .get(s"${appPath}/services/folders/${kmeliaId}/0/path?lang=fr")
          .headers(headers.ajax),
        http("Set of available operations in the Kmelia instance")
          .get(s"${appPath}/KmeliaJSONServlet?Id=0&Action=GetOperations&ComponentId=${kmeliaId}&IEFix=1583422502318")
          .headers(headers.ajax),
        http("Root folder's WYSIWYG description")
          .get(s"${appPath}/KmeliaAJAXServlet?Id=0&Action=GetTopicWysiwyg&ComponentId=${kmeliaId}&IEFix=1583422502320")
          .headers(headers.ajax),
        http("Current user's profile")
          .get(s"${appPath}/services/profile/users/me")
          .headers(headers.ajax)))
    /*

    .pause(8)
    .exec(http("Drop a file")
      .get(s"/${appPath}/util/icons/dropFile.png")
      .headers(headers.ajax))
    .pause(1)
    .exec(http("Verify the uploaded file")
      .post(s"/${appPath}/services/fileUpload/verify")
      .headers(headers.page)
      .body(RawFileBody("data/PdfFileImport.dat")))
    .pause(8)
    .exec(http("Upload effectively the file")
      .post(s"/${appPath}/services/fileUpload")
      .headers(headers.ajax)
      .body(RawFileBody("data/PdfFileImport.bin"))
      .resources(http("request_310")
        .post(s"/${appPath}/RImportDragAndDrop/jsp/Drop?ComponentId=kmelia18641&IgnoreFolders=1&ContentLanguage=en&Draft=0")
        .headers(headers.ajax),
        http("request_311")
          .get(s"/${appPath}/RAjaxPublicationsListServlet?Id=0&ComponentId=kmelia18641&IEFix=1583153138092")
          .headers(headers.ajax)))
    .pause(5)
    .exec(http("Access the new publication")
      .post(s"/${appPath}/Rkmelia/kmelia18641/ViewPublication")
      .headers(headers.page)
      .formParam("PubId", "929544")
      .formParam("CheckPath", "")
      .formParam("X-STKN", "ZWU2M2VjMTQtMTkzNC00YzE5LWFiMGItMmFhMDY1ZThhYTk5")
      .resources(http("request_352")
        .get(s"/${appPath}/services/pdc/kmelia18641?contentId=929544&_=1583153146062")
        .headers(headers.ajax),
        http("request_353")
          .get(s"/${appPath}/services/documents/kmelia18641/resource/929544/types/attachment/fr?viewIndicators=true&highestUserRole=publisher")
          .headers(headers.ajax),
        http("request_352")
          .get(s"/${appPath}/services/pdc/kmelia18641?contentId=929544&_=1583153146062")
          .headers(headers.ajax),
        http("request_353")
          .get(s"/${appPath}/services/documents/kmelia18641/resource/929544/types/attachment/fr?viewIndicators=true&highestUserRole=publisher")
          .headers(headers.ajax),
        http("request_356")
          .get(s"/${appPath}/upload/Attachment_fr.jsp")
          .headers(headers.ajax),
        http("request_357")
          .get(s"/${appPath}/services/pdc/classification/kmelia18641/929544?_=1583153146064")
          .headers(headers.ajax),
        http("request_358")
          .get(s"/${appPath}/services/view/kmelia18641/attachment/843b34c2-5335-4acb-adf5-bb9c6599f433?lang=en&_=1583153146065")
          .headers(headers.ajax),
        http("request_359")
          .get(s"/${appPath}/services/media/viewer/embed/pdf?documentId=843b34c2-5335-4acb-adf5-bb9c6599f433&language=en&embedPlayer=true&width=680&height=625.5&_=1583153147134")
          .headers(headers.ajax),
        http("request_380")
          .get(s"/${appPath}/media/jsp/pdf/core/pdf.worker.min.js"),
        http("request_381")
          .get(s"/${appPath}/services/media/viewer/embed/pdf/content?width=680&embedPlayer=true&height=625.5&_=1583153147134&language=en&documentId=843b34c2-5335-4acb-adf5-bb9c6599f433")))
    */

    .pause(2)
    .exec(silverpeas.logout)

  setUp(scn.inject(rampUsers(usersInjection.count) during (usersInjection.duration minutes))).protocols(httpProtocol)
}