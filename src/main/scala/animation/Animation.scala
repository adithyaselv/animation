package animation

import animation.Animation._


trait Animation[A] {self =>
  def map[B](f : A => B): Animation[B] = flatMap(a => unit(f(a)))

  /**
   * it passes the time as it is from first animation to second one.
   */
  def flatMap[B](f: A => Animation[B]):Animation[B] = self match {
    case Animate(fa) => Animate{time =>
      val animB = f(fa(time))
      animB match {
        case Animate(fb) => fb(time)
      }
    }
  }

  def run(time: Time): A = self match {
    case Animate(fa) => fa(time)
  }
}

case class Animate[A](f: Time => A) extends Animation[A]

object Animation{
  type Time = Double

  def unit[A](a: => A): Animation[A] = Animate(_ => a)

  def lift0[A](a: => A): Animation[A] = unit(a)

  def lift1[A,B](an: Animation[A])(f: A => B): Animation[B] = an.map(f)

  def lift2[A,B,C](aa: Animation[A])(ab: Animation[B])(f: (A, B) => C): Animation[C] =
      for {
        a <- aa
        b <- ab
      } yield f(a,b)
}
