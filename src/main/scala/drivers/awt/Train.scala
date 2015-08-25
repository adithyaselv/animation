package drivers.awt

import animation.Animate
import animation.example.TrainAnimation
import graphic.{Blue, Red}
import picture.{Basic, Picture}
import region._
import shape.{Ellipse, Rectangle}

/**
 * Created by rakesh.h on 20/08/15.
 */
object Train extends App{
  val rectangle = Shape(Ellipse(1,1))
  val animatedRect = Animate[Picture](time => Basic(Red, Scale(((time/1000) % 1, (time/1000) % 1), rectangle)))

  val eng = Basic(Blue, Translate((2.5, 2.5), Shape(Rectangle(8, 3))))

  val window = new  AnimatedWindow(TrainAnimation.get)
  window.open()
}


