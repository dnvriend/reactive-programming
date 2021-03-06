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

package com.test.week5

import akka.actor.Status.Failure
import akka.actor.{ Actor, ActorRef, Props }
import akka.event.LoggingReceive
import akka.pattern.ask
import com.test.TestSpec

class BankAccountTest extends TestSpec {

  "Actors" should "know itself" in {
    val ref: ActorRef = system.actorOf(Props(new Actor {
      override def receive: Receive = {
        case _ ⇒ sender() ! self
      }
    }))

    (ref ? "").futureValue shouldBe ref
    cleanup(ref)
  }

  it should "count" in {
    val ref: ActorRef = system.actorOf(Props(new Actor {
      def count(num: Int): Receive = LoggingReceive {
        case _ ⇒
          context.become(count(num + 1))
          sender() ! num
      }
      override def receive: Receive = LoggingReceive(count(0))
    }))
    (ref ? "").futureValue shouldBe 0
    (ref ? "").futureValue shouldBe 1
    (ref ? "").futureValue shouldBe 2
    (ref ? "").futureValue shouldBe 3
    cleanup(ref)
  }

  it should "be a BankAccount" in {
    object BankAccount {
      case class Transfer(from: ActorRef, to: ActorRef, amount: BigInt)
      case class Deposit(amount: BigInt)
      case class Withdraw(amount: BigInt)
      case object Info
      case class Done(amount: BigInt)
      case object Failed
    }
    class BankAccount extends Actor {
      import BankAccount._
      var balance: BigInt = BigInt(0)
      override def receive: Receive = LoggingReceive {
        case Deposit(amount) ⇒
          balance += amount
          sender() ! Done(balance)
        case Withdraw(amount) ⇒
          balance -= amount
          sender() ! Done(balance)
        case Info ⇒
          sender() ! Done(balance)
        case _ ⇒ sender() ! Failure
      }
    }

    import BankAccount._
    val account1 = system.actorOf(Props(new BankAccount))
    (account1 ? Info).futureValue shouldBe Done(0)
    (account1 ? Deposit(100)).futureValue shouldBe Done(100)
    (account1 ? Deposit(100)).futureValue shouldBe Done(200)

    val account2 = system.actorOf(Props(new BankAccount))

    val tom = system.actorOf(Props(new Actor {
      def awaitDeposit(client: ActorRef): Receive = LoggingReceive {
        case Done(amount) ⇒
          client ! Done(amount)
          context.stop(self)
      }
      def awaitWithdraw(to: ActorRef, amount: BigInt, client: ActorRef): Receive = LoggingReceive {
        case Done(_) ⇒
          to ! Deposit(amount)
          context.become(awaitDeposit(client))
        case Failed ⇒
          client ! Failed
          context.stop(self)
      }
      override def receive = {
        case Transfer(from, to, amount) ⇒
          from ! Withdraw(amount)
          context.become(awaitWithdraw(to, amount, sender()))
      }
    }))

    (tom ? Transfer(account1, account2, 50)).futureValue shouldBe Done(50)
    (account1 ? Info).futureValue shouldBe Done(150)
    (account2 ? Info).futureValue shouldBe Done(50)
    cleanup(account1, account2, tom)
  }
}
