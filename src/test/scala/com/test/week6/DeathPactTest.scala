package com.test.week6

import akka.actor.Actor.emptyBehavior
import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import com.test.TestSpec

class DeathPactTest extends TestSpec {

  // let's create some lovers

  class Boy(girl: ActorRef) extends Actor {
    context.watch(girl) // sign deathpact
    override def receive = emptyBehavior
  }

  class Girl extends Actor {
    import scala.concurrent.duration._
    context.system.scheduler.scheduleOnce(100.millis, self, PoisonPill)
    override def receive: Receive = emptyBehavior
  }

  // yes I know, boy/girl, I am old fashioned..

  "Lovers" should "die together" in {
    val tp = probe
    val girl = system.actorOf(Props(new Girl))
    val boy = system.actorOf(Props(new Boy(girl)))
    tp watch boy
    tp watch girl
    tp.expectTerminated(girl)
    tp.expectTerminated(boy)
  }
}
