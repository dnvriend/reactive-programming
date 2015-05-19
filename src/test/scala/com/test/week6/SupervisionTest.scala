package com.test.week6

import akka.actor.SupervisorStrategy.{Restart, Resume, Stop}
import akka.actor._
import akka.event.LoggingReceive
import akka.testkit.TestProbe
import com.test.TestSpec
import scala.concurrent.duration._

class SupervisionTest extends TestSpec {

  case class Command(f: () => Unit)

  class Supervisor(tp: TestProbe, svs: SupervisorStrategy) extends Actor {
    val worker: ActorRef = context.actorOf(Props(new Actor with ActorLogging {
      override def receive: Receive = LoggingReceive {
        case Command(f) => f()
      }

      override def preStart(): Unit =
        log.debug("Started")

      override def postStop(): Unit =
        log.debug("Stopped")
    }), "worker")
    tp watch worker

    override def receive = LoggingReceive {
      case msg => worker forward msg
    }

    override def supervisorStrategy: SupervisorStrategy = svs
  }

  def createSupervisor(tp: TestProbe)(svs: SupervisorStrategy) =
    system.actorOf(Props(new Supervisor(tp, svs)), s"sup-${randomId.take(3)}")

  "SupervisorStrategy" should "resume the worker" in {
    val tp = probe
    val sup = createSupervisor(tp) { OneForOneStrategy() {
        case t: RuntimeException => Resume
      }
    }
    sup ! Command (() => throw new RuntimeException("stop"))
    tp.expectNoMsg(1.second)
    cleanup(sup)
  }

  it should "stop the worker" in {
    val tp = probe
    val sup = createSupervisor(tp) { OneForOneStrategy() {
        case t: RuntimeException => Stop
      }
    }
    sup ! Command (() => throw new RuntimeException("stop"))
    tp.expectMsgPF[Unit](1.second) {
      case Terminated(_) =>
    }
    cleanup(sup)
  }
}
