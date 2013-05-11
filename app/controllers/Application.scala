package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Akka
import play.api.libs.iteratee.{ Enumerator, Iteratee }
import play.api.Play.current

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import akka.zeromq._

import scala.concurrent.duration.DurationInt

import actors._

object Application extends Controller {
  implicit val timeout = Timeout(1.second)

  val zmq = ZeroMQExtension(Akka.system)
  val listener = Akka.system.actorOf(Props[Fedmsg], name = "fedmsg")
  val pullSocket = zmq.newSocket(SocketType.Sub, Listener(listener), Connect("tcp://hub.fedoraproject.org:9940"), Subscribe("org.fedoraproject"))

  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

  def stream = WebSocket.async[String] { request =>
    val future = listener ? WebSocketSubscribe()
    future.mapTo[(Iteratee[String, _], Enumerator[String])]
  }
}
