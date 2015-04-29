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
      if(eatenByMonster)
        throw new GameOverException("Game over man, game over!")
      else (1 to 10).toList.map(Coin)

    def buyTreasure(coins: List[Coin]): Treasure =
      if(coins.map(_.value).sum < treasureCost)
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
