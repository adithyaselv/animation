package drivers.awt

import animation.example.ClockAnimation

/**
 * Created by rakesh.h on 23/08/15.
 */
object Clock extends App{
  val window = new  AnimatedWindow(ClockAnimation.get)
  window.open()
}
