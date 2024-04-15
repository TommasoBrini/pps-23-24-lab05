package minesweeper

import minesweeper.Cell.CellImpl
import polyglot.{OptionToOptional, Pair}
import util.Optionals.Optional as ScalaOptional

import scala.jdk.javaapi.OptionConverters
import util.Sequences.Sequence

import scala.util.Random

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
  private def initialize(size: Int, mines: Int): Sequence[Cell] =
    val allPositions = for {
      i <- 0 until size
      j <- 0 until size
    } yield (i,j)

    val shuffledPositions = Random.shuffle(allPositions)
    val cellList = shuffledPositions.zipWithIndex.map { case ((x, y), idx) =>
      if (idx < mines) {
        Cell(Pair(x,y), CellState.Bomb, false, false)
      } else {
        Cell(Pair(x,y), CellState.Empty, false, false)
      }
    }.toList // Convertiamo il risultato in una lista
    Sequence(cellList: _*)

  def apply(gridSize: Int, numberMines: Int): Grid =
    var cellsList: Sequence[Cell] = initialize(gridSize, numberMines)
    GridImpl(gridSize, cellsList)

  case class GridImpl(override val size: Int, override val cells: Sequence[Cell]) extends Grid:

    override def hasHittenBomb(coordinates: Pair[Integer, Integer]): Boolean =
      getCell(coordinates).state == CellState.Bomb

    override def click(coordinates: Pair[Integer, Integer]): Unit =
      getCell(coordinates).click()

    override def getCell(coordinates: Pair[Integer, Integer]): Cell =
      cells.find(x => x.position == coordinates).orElse(Cell(Pair(0,0),CellState.Empty, false, false))


case class LogicsImpl(private val size: Int, private val mines: Int) extends Logics:

  val gameGrid: Grid = Grid(size, mines)
  val directions: Sequence[Pair[Int, Int]] = Sequence(
    Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
    Pair(0, -1), Pair(0, 1),
    Pair(1, -1), Pair(1, 0), Pair(1, 1)
  )

  override def isBombSelected(pos: Pair[Integer, Integer]): Boolean =
    gameGrid.hasHittenBomb(pos)

  override def isWin: Boolean =
    val cells = gameGrid.cells.filter(x => x.state != CellState.Bomb)
    cells.forAll(_.isDiscovered)

  override def getGrid: Grid = gameGrid

  override def getCellStamp(pos: Pair[Integer, Integer]): String =
    if isDiscovered(pos) then
      "" + countMinesAdjacent(pos)
    else
      ""

  override def isDiscovered(pos: Pair[Integer, Integer]): Boolean =
    gameGrid.getCell(pos).isDiscovered

  private def countMinesAdjacent(pos: Pair[Integer, Integer]): Int =
    val counter = directions.map { dir =>
      val newX = pos.getX + dir.getX
      val newY = pos.getY + dir.getY
      if (isValid(newX, newY) && gameGrid.getCell(Pair(newX, newY)).state == CellState.Bomb) 1 else 0
    }.foldLeft(0)(_+_)
    counter

  override def click(coordinates: Pair[Integer, Integer]): Unit =
    if !gameGrid.getCell(coordinates).isDiscovered then
      gameGrid.click(coordinates)
      if getCellStamp(coordinates) == "0" then
        exploreAdjacent(coordinates)

  private def exploreAdjacent(pos: Pair[Integer, Integer]): Unit =
    directions.forEach(
      dir =>
        val newX = pos.getX + dir.getX
        val newY = pos.getY + dir.getY
        if isValid(newX, newY) then
          val newPos = Pair(Integer.valueOf(newX), Integer.valueOf(newY))
          click(newPos)
    )

  private def isValid(x: Int, y: Int): Boolean =
    x >= 0 && x < size && y >= 0 && y < size;

  override def setFlag(pos: Pair[Integer, Integer]): Unit =
    getGrid.getCell(pos).flag()