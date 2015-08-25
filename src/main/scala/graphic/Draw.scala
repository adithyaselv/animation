package graphic

/**
 * Represents a draw operation on canvas C , draw operation returns type A.
 */
trait Draw[A,C] { self =>

  /**
   * for chaining multiple draw operations, it just
   * passes the canvas to the next draw operation.
   */
  def flatMap[B](f: A => Draw[B,C]): Draw[B,C] = self match {
    case Drawing(fa: (C => A)) => Drawing{ graphic =>
        val drawB = f(fa(graphic))
        drawB match {
          case Drawing(fb) =>
            fb(graphic)
        }
    }
  }

  def map[B](f: A => B): Draw[B,C] = Drawing(graphic => f(self.run(graphic)))

  def unit[A](a: => A): Draw[A, C] =
    Drawing(_ => a)

  /**
   * executes the draw operation on passed canvas.
   */
  def run(canvas: C): A = self match {
    case Drawing(fa: (C => A)) => fa(canvas)
  }

}

/**
 * Simple drawing where the actual drawing operation is passed in constructor.
 */
case class Drawing[A,C](f: C => A) extends Draw[A, C]
