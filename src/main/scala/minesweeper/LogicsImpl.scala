package minesweeper

import minesweeper.{Grid, GridImpl}
import polyglot.{OptionToOptional, Pair}
import util.Optionals.Optional as ScalaOptional

import java.awt.GridBagConstraints
import scala.jdk.javaapi.OptionConverters

trait Grid:
  def n: Unit

case class GridImpl() extends Grid:
  override def n: Unit = println("Inizio game")

trait Cell:
  def click(): Unit


/** solution and descriptions at https://bitbucket.org/mviroli/oop2019-esami/src/master/a01b/sol2/ */
class LogicsImpl(private val size: Int, private val mines: Int) extends Logics:
  override def click(coordinates: Pair[Integer, Integer]): Unit =
    println(coordinates)

  override def isBombSelected(pos: Pair[Integer, Integer]): Boolean = false

  override def isWin: Boolean = false

  override def getGrid: Grid = GridImpl()

  override def getCellStamp(pos: Pair[Integer, Integer]): String = "X"

  override def isDiscovered(pos: Pair[Integer, Integer]): Boolean = false

  override def setFlag(pos: Pair[Integer, Integer]): Unit = println("setFlag")