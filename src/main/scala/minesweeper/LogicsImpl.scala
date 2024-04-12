package minesweeper

import polyglot.OptionToOptional
import minesweeper.Logics
import util.Optionals.Optional as ScalaOptional

import scala.jdk.javaapi.OptionConverters

/** solution and descriptions at https://bitbucket.org/mviroli/oop2019-esami/src/master/a01b/sol2/ */
class LogicsImpl(private val size: Int, private val mines: Int) extends Logics:
  def hit(x: Int, y: Int): java.util.Optional[Integer] =
    OptionToOptional(ScalaOptional.Empty()) // Option => Optional converter

  def won = false
