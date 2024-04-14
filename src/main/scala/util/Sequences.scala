package util
import Optionals.Optional.*
import util.Optionals.Optional

object Sequences: // Essentially, generic linkedlists
  
  enum Sequence[E]:
    case Cons(head: E, tail: Sequence[E])
    case Nil()

    override def toString: String = this match
      case Cons(h, t) => s"$h :: $t"
      case Nil() => "nil"

  object Sequence:
    def apply[A](elements: A*): Sequence[A] =
      var sequence: Sequence[A] = Nil()
      for e <- elements do
        sequence = Cons(e, sequence)
      sequence.reverse()

    def empty[A]: Sequence[A] = Nil()

    extension [A](sequence: Sequence[A])
      def head: Optional[A] = sequence match
        case Cons(h, _) => Just(h)
        case _ => Empty()

      def concat(other: Sequence[A]): Sequence[A] = sequence match
        case Cons(h, t) => Cons(h, t.concat(other))
        case _ => other

      def flatMap[B](f: A => Sequence[B]): Sequence[B] = sequence match
        case Cons(h, t) => f(h).concat(t.flatMap(f))
        case _ => Nil()

      def map[B](f: A => B): Sequence[B] = sequence.flatMap(x => Cons(f(x), Nil()))

      def filter(f: A => Boolean): Sequence[A] = sequence.flatMap:
        case x if f(x) => Cons(x, Nil())
        case _ => Nil()

      def find(f: A => Boolean): Optional[A] = sequence match
        case Cons(h, t) if f(h) => Just(h)
        case Cons(_, t) => t.find(f)
        case _ => Empty()

      def contains(e: A): Boolean = !sequence.find(_ == e).isEmpty

      def intersect(seq2: Sequence[A]): Sequence[A] =
        sequence.filter(seq2.contains)

      def reverse(): Sequence[A] = sequence match
        case Cons(h, t) => t.reverse().concat(Cons(h, Nil()))
        case _ => Nil()

      def add(element: A): Sequence[A] = sequence.concat(Cons(element, Nil()))

      def foldLeft[B](default: B)(f: (B, A) => B): B = sequence match
        case Cons(x, y) => y.foldLeft(f(default, x))(f)
        case _ => default

      def length: Int = sequence match
          case Cons(_, t) => 1 + t.length
          case _ => 0

      def forEach(f: A=> Unit): Unit = sequence match
        case Cons(h,t) =>
          f(h)
          t.forEach(f)
        case _ => ()

      def forAll(f: A => Boolean): Boolean =
        def loop(s: Sequence[A]): Boolean = s match
          case Cons(h,t) if f(h) => loop(t)
          case Nil() => true
          case _ => false
        loop(sequence)

@main def trySequences =
  import Sequences.* 
  val sequence = Sequence(1, 2, 3)
  println(sequence)
  println(sequence.add(4))
  println(sequence.head)
  println(sequence.map(_ * 2))
  println(sequence.flatMap(x => Sequence(x, x * 2)))
  println(sequence.filter(_ % 2 == 0))
  println(sequence.concat(Sequence(4, 5, 6)))
  println(sequence.find(_ % 2 == 0))
  println(sequence.contains(2))
  println(sequence.intersect(Sequence(1,4,5,6)))
  println(sequence.forAll(_ > 0))


