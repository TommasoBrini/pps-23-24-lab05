package minesweeper

import polyglot.{OptionToOptional, Pair}
import util.Optionals.Optional as ScalaOptional

import scala.jdk.javaapi.OptionConverters
import util.Sequences.Sequence

enum CellState:
  case Bomb
  case Empty

trait Cell:
  def position: Pair[Int, Int]
  def state: CellState
  def isDiscovered: Boolean
  def isFlagged: Boolean
  def click(): Unit
  def flag(): Unit
  def isAdjacent(otherPosition: Pair[Int, Int]): Boolean

object Cell:
  def apply(position: Pair[Int,Int], state: CellState, isDiscovered: Boolean, isFlagged: Boolean) : Cell =
    CellImpl(position, state, isDiscovered = false, isFlagged = false)

case class CellImpl(override val position: Pair[Int, Int], override val state: CellState, var isDiscovered: Boolean, var isFlagged: Boolean) extends Cell:
  override def click(): Unit = isDiscovered = true
  override def flag(): Unit = isFlagged = !isFlagged
  override def isAdjacent(otherPosition: Pair[Int, Int]): Boolean =
    Math.abs(otherPosition.getX - this.position.getX) <= 1
      && Math.abs(otherPosition.getY - this.position.getY) <= 1
      && !otherPosition.equals(position);


trait Grid:
  def size: Int
  def cells: Sequence[Cell]
  def getCell(coordinates: Pair[Integer, Integer]): Cell
  def hasHittenBomb(coordinates: Pair[Integer, Integer]): Boolean
  def click(coordinates: Pair[Integer, Integer]): Unit

object Grid:
  def apply(gridSize: Int, numberMines: Int): Grid =
    GridImpl(gridSize, numberMines)

  case class GridImpl(override val size: Int, mines: Int) extends Grid:
    def cells: Sequence[Cell]
    override def hasHittenBomb(coordinates: Pair[Integer, Integer]): Boolean = false

    override def click(coordinates: Pair[Integer, Integer]): Unit = println("click")

    override def getCell(coordinates: Pair[Integer, Integer]): Cell = cells.head.orElse(CellImpl(CellState.Empty))

case class LogicsImpl(private val size: Int, private val mines: Int) extends Logics:
  override def click(coordinates: Pair[Integer, Integer]): Unit =
    println(coordinates)

  override def isBombSelected(pos: Pair[Integer, Integer]): Boolean = true

  override def isWin: Boolean = false

  override def getGrid: Grid = Grid(size, mines)

  override def getCellStamp(pos: Pair[Integer, Integer]): String = "X"

  override def isDiscovered(pos: Pair[Integer, Integer]): Boolean = false

  override def setFlag(pos: Pair[Integer, Integer]): Unit = println("setFlag")