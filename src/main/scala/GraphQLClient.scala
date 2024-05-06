import graphql.codegen.GraphQLQuery
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model.headers.RawHeader
import org.apache.pekko.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, HttpResponse, MediaTypes}
import play.api.libs.json.{JsValue, Json, Writes}
import sangria.ast.Document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

case class GraphQLRequest[Query <: GraphQLQuery](uri: String, query: Document, variables: Query#Variables, operation: String)

object GraphQLRequest {
  def apply[Query <: GraphQLQuery](query: Query, variables: Query#Variables): GraphQLRequest[Query] = {
    GraphQLRequest (
      uri = "https://api.github.com/graphql",
      query = query.document,
      variables = variables,
      operation = query.document.operations.values.flatMap(_.name).mkString(",")
    )
  }
}

object GraphQLClient {
  private implicit val system: ActorSystem = ActorSystem("SingleRequest")
  def execute[Query <: GraphQLQuery](request: GraphQLRequest[Query])(implicit jsonWrites: Writes[Query#Variables]): Future[HttpResponse] = {
    val rawRequest = Json.obj(
      "operationName" -> Json.toJsFieldJsValueWrapper(request.operation),
      "variables" -> request.variables,
      "query" -> request.query.source.get, // FIXME: Should use implicit writer
    )

    val httpRequest = HttpRequest(
      method = HttpMethods.POST,
      uri = request.uri,
      entity = HttpEntity(
        MediaTypes.`application/json`,
        rawRequest.toString()
      )
    ).withHeaders(
      headers = Seq(RawHeader("Authorization", "bearer XXXXXX"))
    )

    Http().singleRequest(httpRequest)
  }

  def parseResponse(response: HttpResponse): Future[JsValue] = {
    response.entity.toStrict(10.seconds).map(entity => Json.parse(entity.data.utf8String))
  }
}
