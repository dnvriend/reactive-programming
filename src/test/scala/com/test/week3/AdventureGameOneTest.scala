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

import com.test.TestSpec

case class Coin(value: Int = 1)
case class Treasure(value: Long = Long.MaxValue)

class GameOverException(message: String) extends RuntimeException(message)

class AdventureGameOneTest extends TestSpec {

  class SaveAdventure {
    def collectCoins(): List[Coin] = (1 to 10).toList.map(Coin(_))
    def buyTreasure(coins: List[Coin]): Treasure = Treasure()
  }

  class DangerousAdventure(eatenByMonster: Boolean) {
    val treasureCost = Integer.MAX_VALUE
    def collectCoins(): List[Coin] =
      if (eatenByMonster)
        throw new GameOverException("Game over man, game over!")
      else (1 to 10).toList.map(Coin)

    def buyTreasure(coins: List[Coin]): Treasure =
      if (coins.map(_.value).sum < treasureCost)
        throw new GameOverException("Nice try!")
      else Treasure()
  }

  // Note, all calls are blocking T => S

  //
  // Exceptions as an effect LOL :)
  //

  "SimpleAdventure" should "buy treasure" in {
    val adventure = new SaveAdventure()
    val coins = adventure.collectCoins()
    adventure.buyTreasure(coins) shouldBe Treasure()
  }

  "DangerousAdventure" should "hero got eaten" in {
    val adventure = new DangerousAdventure(eatenByMonster = true)
    intercept[GameOverException] {
      adventure.collectCoins()
    }
  }

  it should "hero has magic armor" in {
    val adventure = new DangerousAdventure(eatenByMonster = false)
    val coins = adventure.collectCoins()
    intercept[GameOverException] {
      adventure.buyTreasure(coins)
    }
  }
}
