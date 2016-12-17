package graphic

import scalaz.effect.IO

/**
 * Represents a draw operation on canvas C , draw operation returns type A.
 */
sealed trait Draw[C, A] { self =>

  /**
   * for chaining multiple draw operations, it just
   * passes the canvas to the next draw operation.
   */
  def flatMap[B](f: A => Draw[C, B]): Draw[C, B] = self match {
    case Drawing(fca) => Drawing{c =>
        fca(c).flatMap{a => f(a) match {
          case Drawing(fcb) => fcb(c)
        }}}
  }

  def map[B](f: A => B): Draw[C, B] = self match {
    case Drawing(fca) => Drawing(c => fca(c).map(f))
  }



  /**
   * executes the draw operation on passed canvas.
   */
  def unsafeRun(canvas: C): A = self match {
    case Drawing(drawA) => drawA(canvas).unsafePerformIO()
  }

  def toIO(c: C):IO[A] = self match {
    case Drawing(f) => f(c)
  }

}

object Draw {
  def unit[A, C](a: => A): Draw[C, A] =
    Drawing(_ => IO(a))
}

/**
 * Simple drawing where the actual drawing operation is passed in constructor.
 */
case class Drawing[C, A](f: C => IO[A]) extends Draw[C, A]



