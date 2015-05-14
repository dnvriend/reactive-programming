package com.github.dnvriend

import java.net.URLEncoder

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import com.typesafe.config.Config
import spray.client.pipelining._
import spray.http.ContentTypes._
import spray.http._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

trait GenericHttpClient {
  def protocol: String
  def hostname: String
  def port: Int
  def path: Option[String]
  def username:Option[String]
  def password:Option[String]

  implicit def system: ActorSystem
  implicit def ec: ExecutionContext
  implicit def log: LoggingAdapter

  implicit def urlResolver: UrlResolver

  def pipeline: HttpRequest => Future[HttpResponse] = {
    logRequest(log, Logging.InfoLevel) ~>
      (for {
        user <- username
        pass <- password
      } yield {
          log.info("Adding BasicHttpCredentials")
          addCredentials(BasicHttpCredentials(user, pass)) ~>
            sendReceive
        }).getOrElse(sendReceive) ~>
      logResponse(log, Logging.InfoLevel)
  }

  def get(resource: String, params:Option[Map[String,String]] = None): Future[String]
  def post(resource: String, body:Option[String]= None, contentType: ContentType = `application/json`, params: Option[Map[String,String]] = None): Future[String]
  def put(resource: String, body:Option[String] = None, contentType: ContentType = `application/json`, params :Option[Map[String,String]] = None): Future[String]
  def delete(resource: String, body: Option[String] = None, contentType: ContentType = `application/json`, params: Option[Map[String,String]] = None): Future[String]
}

case class UrlResolver(protocol: String, hostname: String, port: Int, path: Option[String]) {

  def resolve(resource: String):String = {
    val url = s"$protocol://$hostname:$port${path.map(p => if(!p.startsWith("/")) s"/$p" else p).getOrElse("")}/$resource"
    if(resource.trim == "")
      url.reverse.tail.reverse
    else
      url
  }

  def resolve(resource:String, params:Map[String, String]):String = addParameters(resolve(resource), params)

  def addParameters(baseUrl:String, params:Map[String,String]):String = {
    var glue = "?"
    params.foldLeft(baseUrl){ case (acc, (key, value)) =>
      val url = s"$acc$glue${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
      glue = "&"
      url
    }
  }
}

object SprayHttpClient {

  def apply(config:Config)(implicit system:ActorSystem) = {
    new SprayHttpClient(
      config.getString("httpclient.procotol"),
      config.getString("httpclient.host"),
      config.getInt("httpclient.port"),
      Option(config.getString("httpclient.path")),
      Try(config.getString("httpclient.username")).toOption,
      Try(config.getString("httpclient.password")).toOption
    )
  }

  def apply(hostname: String, port: Int = 80, path: Option[String] = None, username: Option[String] = None,  password: Option[String] = None, protocol: String = "http")(implicit system:ActorSystem): SprayHttpClient =
    new SprayHttpClient(protocol, hostname, port, path, username, password)
}

class SprayHttpClient(val protocol:String, val hostname:String, val port:Int, val path: Option[String], val username:Option[String] = None, val password:Option[String] = None)(implicit val system: ActorSystem) extends GenericHttpClient {

  implicit val ec = system.dispatcher

  implicit val log = system.log

  val urlResolver = new UrlResolver(protocol, hostname, port, path)

  log.info("Base Url:" + urlResolver.resolve(""))

  val genericPipeline: HttpRequest => Future[String] =
    pipeline ~> notFail ~> unmarshal[String]

  def addContentTypePipeline(contentType: ContentType): HttpRequest => Future[String] =
    addContentType(contentType) ~> genericPipeline

  def addContentType(contentType: ContentType): RequestTransformer = { req =>
    req.copy(entity = HttpEntity(contentType, data = req.entity.data))
  }

  private def notFail(response: Future[HttpResponse]): Future[HttpResponse] =
    response.map {
      case res@HttpResponse(StatusCodes.NotFound, _, _, _) => // 404
        res.copy(status = StatusCodes.NotFound)
      case res => res
    }

  def get(resource: String, params: Option[Map[String, String]]): Future[String] =
    genericPipeline(params.map(pr => Get(urlResolver.resolve(resource, pr))).getOrElse(Get(urlResolver.resolve(resource))))

  def post(resource: String, body: Option[String], contentType: ContentType, params: Option[Map[String, String]]): Future[String] =
    addContentTypePipeline(contentType)(params.map(pr => Post(urlResolver.resolve(resource, pr), body.getOrElse(""))).getOrElse(Post(urlResolver.resolve(resource), body.getOrElse(""))))

  def put(resource: String, body: Option[String] = None, contentType: ContentType, params: Option[Map[String, String]]): Future[String] =
    addContentTypePipeline(contentType)(params.map(pr => Put(urlResolver.resolve(resource, pr), body.getOrElse(""))).getOrElse(Put(urlResolver.resolve(resource), body.getOrElse(""))))

  def delete(resource: String, body: Option[String], contentType: ContentType, params: Option[Map[String, String]]): Future[String] =
    addContentTypePipeline(contentType)(params.map(pr => Delete(urlResolver.resolve(resource, pr), body)).getOrElse(Delete(urlResolver.resolve(resource), body)))
}