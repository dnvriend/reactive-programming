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

package com.test.week3

import java.util.NoSuchElementException

import com.test.TestSpec

import scala.util.{ Failure, Success, Try }

class AdventureGameTwoTest extends TestSpec {

  class Adventure(eatenByMonster: Boolean) {
    val treasureCost = Integer.MAX_VALUE
    def collectCoins(): Try[List[Coin]] = Try {
      if (eatenByMonster)
        throw new GameOverException("Game over man, game over!")
      else (1 to 10).toList.map(Coin)
    }

    def buyTreasure(coins: List[Coin]): Try[Treasure] = Try {
      if (coins.map(_.value).sum < treasureCost)
        throw new GameOverException("Nice try!")
      else Treasure()
    }
  }

  "Adventure" should "hero got eaten" in {
    val adventure = new Adventure(eatenByMonster = true)
    adventure.collectCoins() should be a 'failure // hero got eaten by monster
  }

  it should "hero has magic armor" in {
    val adventure = new Adventure(eatenByMonster = false)
    adventure.collectCoins() should be a 'success // hero upgraded with magic armor
  }

  it should "buy treasure" in {
    val adventure = new Adventure(eatenByMonster = false)
    val coins = adventure.collectCoins().success.value // using ScalaTest's TryValues trait see TestSpec
    adventure.buyTreasure(coins) should be a 'failure // too expensive
  }

  it should "buy treasure matching, returning coins" in {
    val adventure = new Adventure(eatenByMonster = false)
    val coins: List[Coin] = adventure.collectCoins() match {
      case Success(listOfCoins) ⇒ listOfCoins
      case Failure(t)           ⇒ Nil
    }
    adventure.buyTreasure(coins) should be a 'failure // too expensive
  }

  it should "buy treasure matching, returning treasure" in {
    val adventure = new Adventure(eatenByMonster = false)
    val treasure: Try[Treasure] = adventure.collectCoins() match {
      case Success(coins) ⇒ adventure.buyTreasure(coins)
      case Failure(e)     ⇒ Failure(e)
    }
    treasure should be a 'failure // too expensive
  }

  it should "buy treasure using composition" in {
    val adventure = new Adventure(eatenByMonster = false)
    val treasure: Try[Treasure] = adventure.collectCoins().flatMap(coins ⇒ adventure.buyTreasure(coins))
    treasure should be a 'failure // too expensive
  }

  it should "buy treasure using composition, shorter version" in {
    val adventure = new Adventure(eatenByMonster = false)
    val treasure: Try[Treasure] = adventure.collectCoins().flatMap(adventure.buyTreasure)
    treasure should be a 'failure // too expensive
  }

  it should "buy treasure using for comprehension" in {
    val adventure = new Adventure(eatenByMonster = false)
    val treasure: Try[Treasure] = for {
      coins ← adventure.collectCoins()
      treasure ← adventure.buyTreasure(coins)
    } yield treasure
    treasure should be a 'failure // too expensive
  }

  it should "hero retries when failed" in {
    val adventure = new Adventure(eatenByMonster = true)
    val coins: Try[List[Coin]] = adventure.collectCoins() recoverWith {
      case t: Throwable ⇒
        // hero failed, but he tries again
        val newadventure = new Adventure(eatenByMonster = false)
        newadventure.collectCoins()
    }
    val treasure: Try[Treasure] = coins.flatMap(adventure.buyTreasure)
    treasure should be a 'failure // too expensive
  }

  it should "hero is Indiana Jones, he always gets treasure" in {
    val adventure = new Adventure(eatenByMonster = true)
    val treasure: Try[Treasure] =
      adventure.collectCoins()
        .recoverWith {
          case t: Throwable ⇒
            new Adventure(eatenByMonster = false).collectCoins()
        }
        .flatMap { coins ⇒
          adventure.buyTreasure(coins)
            .recover {
              case t: Throwable ⇒
                Treasure() // Indiana Jones always gets treasure!
            }
        }

    treasure should be a 'success
  }

  it should "support other higher order functions" in {
    val x = Try(1)
    x should be a 'success
    x should not be 'failure
    x shouldBe Success(1)
    x should not be Failure
    x.get shouldBe 1
    x.getOrElse(0) shouldBe 1
    x.filter(_ == 1) shouldBe Success(1)
    x.filter(_ == 0) should be a 'failure
    x.foreach { x ⇒ assert(x == 1) }
    x.isFailure shouldBe false
    x.isSuccess shouldBe true
    x.toOption shouldBe Some(1)
    x.recoverWith { case _ ⇒ Try(2) } recover { case _ ⇒ 3 } shouldBe Success(1)
  }
}
