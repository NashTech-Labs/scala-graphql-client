import graphql.codegen.repository.GetRepository
import graphql.codegen.repository.GetRepository.Variables.jsonWrites
import graphql.codegen.user.GetUser
import org.apache.pekko.http.scaladsl.model.HttpResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}


object GraphQLMain extends App {
  private val repositoryVariables: GetRepository.Variables = GetRepository.Variables(name = "scala", owner = "scala")
  private val repositoryGraphQlRequest = GraphQLRequest.apply(GetRepository, repositoryVariables)
  private val repositoryResponse: Future[HttpResponse] = GraphQLClient.execute(repositoryGraphQlRequest)
  private val repositoryParsedResponse = repositoryResponse.flatMap(GraphQLClient.parseResponse)

  repositoryParsedResponse.foreach(println)

  private val userVariables: GetUser.Variables = GetUser.Variables(login = "prakharsingh2818")
  private val userGraphQlRequest = GraphQLRequest.apply(GetUser, userVariables)
  private val userResponse: Future[HttpResponse] = GraphQLClient.execute(userGraphQlRequest)
  private val userParsedResponse = userResponse.flatMap(GraphQLClient.parseResponse)

  userParsedResponse.foreach { res =>
    println(s"\n\n=========================\n\n")
    println(res)
  }

  Await.result(repositoryParsedResponse, 10.seconds)
  Await.result(userParsedResponse, 10.seconds)
}
