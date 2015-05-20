package com.test.week6

import akka.actor._
import akka.event.LoggingReceive
import akka.pattern.ask
import akka.testkit.TestProbe
import com.test.TestSpec

import scala.concurrent.duration._

class SupervisionTest extends TestSpec {

  case class Command(f: () => Unit)
  case object Count
  case object GetState
  case class CounterState(counter: Long)

  class Supervisor(tp: TestProbe, svs: SupervisorStrategy) extends Actor {
    val worker: ActorRef = context.actorOf(Props(new Actor with ActorLogging {
      var counter = 0L

      override def receive: Receive = LoggingReceive {
        case Command(f) => f()
        case Count => counter += 1
        case GetState => sender() ! CounterState(counter)
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

  "SupervisorStrategy" should "resume the worker, state should not change, so should be 1" in {
    val tp = probe
    val sup = createSupervisor(tp) { OneForOneStrategy() {
        case t: RuntimeException => SupervisorStrategy.Resume
      }
    }
    sup ! Count
    (sup ? GetState).futureValue shouldBe CounterState(1L)
    sup ! Command (() => throw new RuntimeException("resume"))
    (sup ? GetState).futureValue shouldBe CounterState(1L)
    tp.expectNoMsg(100.millis) // no Terminated message
    cleanup(sup)
  }


  it should "restart the worker, so the worker instance has been replaced, and state should be 0 again" in {
    val tp = probe
    val sup = createSupervisor(tp) { OneForOneStrategy() {
      case t: RuntimeException => SupervisorStrategy.Restart
    }
    }
    sup ! Count
    (sup ? GetState).futureValue shouldBe CounterState(1L)
    sup ! Command (() => throw new RuntimeException("restart"))
    (sup ? GetState).futureValue shouldBe CounterState(0L)
    tp.expectNoMsg(100.millis) // no Terminated message
    cleanup(sup)
  }

  it should "stop the worker, so worker in not there anymore and should not answer" in {
    val tp = probe
    val sup = createSupervisor(tp) { OneForOneStrategy() {
        case t: RuntimeException => SupervisorStrategy.Stop
      }
    }
    sup ! Command (() => throw new RuntimeException("stop"))
    tp.expectMsgPF[Unit](100.millis) {
      case Terminated(_) =>
    }
    cleanup(sup)
  }
}
