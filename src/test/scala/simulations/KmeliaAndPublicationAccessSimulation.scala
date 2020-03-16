package simulations

import io.gatling.core.Predef._
import io.gatling.core.feeder.Record
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import org.silverpeas.benchmark._

import scala.concurrent.duration._
import scala.language.reflectiveCalls

/**
 * Simulates the access of a Kmelia instance with several folders and publications in that
 * folders. The view of a publication within an inner folder is also simulated.
 *
 * @author mmoquillon
 */
class KmeliaAndPublicationAccessSimulation extends Simulation with HttpProtocol with SimulationContext {
  // the Kmelia instance to access. We choose a specific one here to ensure to have a deep folders
  // tree hierarchy and some publications
  println("Find the targeted Kmelia instance...")
  val targetedKmelia: Seq[Record[Any]] = ssv("data/components.ssv")
    .readRecords
    .filter(a => a("Application").equals("kmelia"))
    .filter(a => a("TechId").equals("27859"))
  val kmeliaId: Any = targetedKmelia.head("Id")
  val spaceId: Any = targetedKmelia.head("Space")

  // the users to use in the simulation
  print("Load users... ")
  val users: Array[Map[String, Any]] = UsersFeeder.filter
    .onDomain("3")
    .onAccessRightTo(kmeliaId)
    .feed()
  println(s"${users.length} users")
  val pubLastModifierId: Any = users.head("Id")
  val pubCreatorId: Any = users(1)("Id")
  val pubValidatorId: Any = users(2)("Id")

  // we look for an existing publication in the Kmelia that is in an inner folder
  println(s"Find a publication to view in ${kmeliaId}...")
  val targetedPub: Seq[Record[Any]] = ssv("data/publications.ssv")
    .readRecords
    .filter(p => p("AppInstId").equals(kmeliaId))
    .filter(p => !p("NodeId").equals("0"))
  val pubId: Any = targetedPub.head("Id")

  // we look for attachments
  println(s"Load the attachments of publication ${pubId}...")
  val attachments: Seq[Record[Any]] = ssv("data/attachments.ssv")
    .readRecords
    .filter(a => a("Server").equals(host))
    .filter(a => a("AppInstId").equals(kmeliaId))
    .filter(a => a("PubId").equals(pubId))
  if (attachments.isEmpty) {
    throw new RuntimeException("This simulation requires a targeted publication with at least one attachment")
  }
  val attachId: Any = attachments.head("Id")
  val attachLang: Any = attachments.head("Lang")

  // now we build the path of the publication in the Kmelia's folders tree
  println(s"Creates the path of the folder containing the publication ${pubId}...")
  val foldersInKmelia: Seq[Record[Any]] = ssv("data/nodes.ssv").readRecords
    .filter(n => n("AppInstId").equals(kmeliaId))
  var currentFolder: Any = targetedPub.head("NodeId")
  var folderPath: Seq[Any] = Seq()
  while(!currentFolder.equals("0")) {
    folderPath = currentFolder +: folderPath
    currentFolder = foldersInKmelia.filter(f => f("Id").equals(currentFolder)).head("Parent")
  }

  override val filters = new SilverpeasFilters(conf)
  val headers = new SilverpeasHeaders(conf)
  val silverpeas = new SilverpeasConnection(conf)

  val scn: ScenarioBuilder = scenario("Access a given Kmelia instance and view one of its publications")
    .feed(users)
    .exec(session => {
      session.set("appPath", appPath)
        .set("spaceId", spaceId)
        .set("kmeliaId", kmeliaId)
        .set("pubId", pubId)
        .set("attachId", attachId)
        .set("attachLang", attachLang)
    })
    .exec(silverpeas.login)
    .pause(8)

    // access the Kmelia instance
    .exec(http("Access to the Kmelia instance ${kmeliaId}")
      .get("${appPath}/look/jsp/bodyPartAurora.jsp?ComponentId=${kmeliaId}")
      .headers(headers.page)
      .resources(
        http("The Kmelia instance main page")
          .get("${appPath}/Rkmelia/${kmeliaId}/Main")
          .headers(headers.page),
        http("Space's apps navigation")
          .get("${appPath}/look/jsp/DomainsBar.jsp?component_id=${kmeliaId}")
          .headers(headers.ajax),
        http("Space home page navigation")
          .get("${appPath}/RAjaxSilverpeasV5/dummy?ResponseId=spaceUpdater&Init=1&GetPDC=false&SpaceId=${spaceId}&ComponentId=${kmeliaId}&&ietrick=120ie2t2r13i44c58k810")
          .headers(headers.ajax),
        http("Treeview content")
          .get("${appPath}/services/folders/${kmeliaId}/0/treeview?lang=fr&IEFix=1583422501762")
          .headers(headers.ajax),
        http("List of publications in the root folder")
          .get("${appPath}/RAjaxPublicationsListServlet?Id=0&ComponentId=${kmeliaId}&PubIdToHighlight=null&IEFix=1583422502312")
          .headers(headers.ajax),
        http("The children folders of the root folder")
          .get("${appPath}/services/folders/${kmeliaId}/0/path?lang=fr")
          .headers(headers.ajax),
        http("Set of available operations for the root folder")
          .get("${appPath}/KmeliaJSONServlet?Id=0&Action=GetOperations&ComponentId=${kmeliaId}&IEFix=1583422502318")
          .headers(headers.ajax),
        http("Root folder's WYSIWYG description")
          .get("${appPath}/KmeliaAJAXServlet?Id=0&Action=GetTopicWysiwyg&ComponentId=${kmeliaId}&IEFix=1583422502320")
          .headers(headers.ajax),
        http("Current user's profile")
          .get("${appPath}/services/profile/users/me")
          .headers(headers.ajax)))
    .pause(2)

    // now we go down to the folder that contains the aimed publication
    .foreach(folderPath, "folder") {
        exec(http("Go to the folder ${folder}")
          .get("${appPath}/services/folders/${kmeliaId}/${folder}/children?lang=fr&IEFix=1583512035587")
          .headers(headers.page)
          .resources(
            http("List the publications in the folder ${folder}")
              .get("${appPath}/RAjaxPublicationsListServlet?Id=${folder}&ComponentId=${kmeliaId}&PubIdToHighlight=null&IEFix=1583512362011")
              .headers(headers.ajax),
            http("Set of available operations for the folder ${folder}")
              .get("${appPath}/KmeliaJSONServlet?Id=${folder}&Action=GetOperations&ComponentId=${kmeliaId}&IEFix=1583512362015")
              .headers(headers.ajax),
            http("The children folders of folder ${folder}")
              .get("${appPath}/services/folders/${kmeliaId}/${folder}/path?lang=fr")
              .headers(headers.ajax),
            http("WYSIWYG description of the folder ${folder}")
              .get("${appPath}/KmeliaAJAXServlet?Id=${folder}&Action=GetTopicWysiwyg&ComponentId=${kmeliaId}&IEFix=1583512362020")
              .headers(headers.ajax),
            http("The data on the folder ${folder}")
              .get("${appPath}/services/folders/${kmeliaId}/${folder}")
              .headers(headers.ajax)))
        .pause(2)
    }

    // now we access information about the publication (we don't fetch the last viewers)
    .exec(SecurityToken.fetchToken)
    .exec(http("View the publication ${pubId}")
        .post("${appPath}/Rkmelia/${kmeliaId}/ViewPublication")
        .headers(headers.page)
        .formParam("PubId", pubId)
        .formParam("CheckPath", "")
        .formParam("X-STKN", "${stkn}")
        .resources(
          http("The PdC fragment on which the publication can be classified")
            .get("${appPath}/services/pdc/${kmeliaId}?contentId=${pubId}&_=1583513988008")
            .headers(headers.ajax),
          http("The attachments of the publication")
            .get("${appPath}/services/documents/${kmeliaId}/resource/${pubId}/types/attachment/fr?viewIndicators=true&highestUserRole=writer")
            .headers(headers.ajax),
          http("The attachments widget")
            .get("${appPath}/upload/Attachment_fr.jsp")
            .headers(headers.ajax),
          http("The classification of the publication on the PdC")
            .get("${appPath}/services/pdc/classification/${kmeliaId}/${pubId}?_=1583513988012")
            .headers(headers.ajax),
          http("The profile of the last modifier of the publication")
            .get(s"${appPath}/services/profile/users/${pubLastModifierId}")
            .headers(headers.ajax),
          http("The profile of the creator of the publication")
            .get(s"${appPath}/services/profile/users/${pubCreatorId}")
            .headers(headers.ajax),
          http("The profile of the validator of the publication")
            .get(s"${appPath}/services/profile/users/${pubValidatorId}")
            .headers(headers.ajax),
          http("The profile of the current user")
            .get("${appPath}/services/profile/users/me")
            .headers(headers.ajax),
          http("Information about the attachment of the publication")
            .get("${appPath}/services/view/${kmeliaId}/attachment/${attachId}?lang=${attachLang}&_=1583748201031")
            .headers(headers.ajax),
          http("The viewer on the attachments")
            .get("${appPath}/services/media/viewer/embed/pdf?documentId=${attachId}&language=${attachLang}&embedPlayer=true&width=680&height=480&_=1583748202069")
            .headers(headers.page),
          http("Render the attachment's content")
            .get("${appPath}/services/media/viewer/embed/pdf/content?language=${attachLang}&width=680&documentId=${attachId}&height=480&_=1583748202069&embedPlayer=true")
            .headers(headers.ajax)))
    .pause(2)
    .exec(silverpeas.logout)

  setUp(scn.inject(rampUsers(usersInjection.count) during (usersInjection.duration minutes))).protocols(httpProtocol)
}