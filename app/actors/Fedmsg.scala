package actors

import akka.actor.{Actor, ActorLogging}
import Actor._
import akka.event.Logging
import akka.zeromq._
import akka.util.ByteString

import play.api.libs.iteratee.{ Concurrent, Enumerator, Iteratee }

case class WebSocketSubscribe()
case class WebSocketUnsubscribe(enumerator: Enumerator[_])

class Fedmsg extends Actor with ActorLogging {
  var subscriberCount = 0
  val (enumerator, channel) = Concurrent.broadcast[String]

  def receive = {
    case WebSocketSubscribe() => {
      val iteratee = Iteratee.foreach[String](_ => ()).mapDone(_ =>
        self ! WebSocketUnsubscribe(enumerator))
      subscriberCount += 1
      log.info(s"Someone subscribed - ${subscriberCount} subscribers.")
      sender ! (iteratee, enumerator)
    }
    case WebSocketUnsubscribe(subscriber) => {
      subscriberCount -= 1
      log.info(s"Someone unsubscribed - ${subscriberCount} subscribers.")
    }
    case m: ZMQMessage => {
      val message = s"${new String(m.payload(0))} ${new String(m.payload(1))}"
      channel.push(new String(m.payload(1)))
      log.info(message)
    }
    case e => log.info(e.toString)
  }
}
