package minesweeper

import minesweeper.{Grid, GridImpl}
import polyglot.{OptionToOptional, Pair}
import util.Optionals.Optional as ScalaOptional

import java.awt.GridBagConstraints
import scala.jdk.javaapi.OptionConverters

trait Grid:
  def hasHittenBomb(coordinates: Pair[Integer, Integer]): Boolean
  def click(coordinates: Pair[Integer, Integer]): Unit
  def getCell(coordinates: Pair[Integer, Integer]): Cell

case class GridImpl() extends Grid:
  override def hasHittenBomb(coordinates: Pair[Integer, Integer]): Boolean = false
  override def click(coordinates: Pair[Integer, Integer]): Unit = println("click")

  override def getCell(coordinates: Pair[Integer, Integer]): Cell = CellImpl()

trait Cell:
  def click: Unit
  def getFlag: Boolean

case class CellImpl() extends Cell:
  override def click: Unit = println("click")
  override def getFlag: Boolean = false


case class LogicsImpl(private val size: Int, private val mines: Int) extends Logics:
  override def click(coordinates: Pair[Integer, Integer]): Unit =
    println(coordinates)

  override def isBombSelected(pos: Pair[Integer, Integer]): Boolean = false

  override def isWin: Boolean = false

  override def getGrid: Grid = GridImpl()

  override def getCellStamp(pos: Pair[Integer, Integer]): String = "X"

  override def isDiscovered(pos: Pair[Integer, Integer]): Boolean = false

  override def setFlag(pos: Pair[Integer, Integer]): Unit = println("setFlag")