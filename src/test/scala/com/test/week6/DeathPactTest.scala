/*
 * Copyright 2015 Dennis Vriend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.week6

import akka.actor.Actor.emptyBehavior
import akka.actor.{ Actor, ActorRef, PoisonPill, Props }
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
