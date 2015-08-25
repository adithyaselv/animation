package animation.example

import animation.{Animate, Animation}
import graphic.{Blue, Black}
import picture.{Basic, Over, Picture}
import region._
import shape.{Shape => PShape, Rectangle}

/**
 * Created by rakesh.h on 24/08/15.
 */
object ClockAnimation {

  def get: Animation[Picture] = Animate(createClock)

  def createClock(time: Double): Picture = {
    val rim = createRim(5)
    val sticks = createTimeSticks(5, time)
    Over(rim, sticks)
  }

  def createTimeSticks(radius: Double, time: Double): Picture = {
    import Region._
    val sec = (time/1000).toInt
    val sAngle = 90 - 6 * sec //seconds hand angle
    val mAngle = 90 - 6 * sec * (1.0 / 60)//minutes hand angle
    val hAngle = 90 - 6 * sec * (1.0 / 3600)//hours hand angle

    val ss = Basic(Blue,rotate(Translate((((radius - .5)/2 -.5), 0.0),Shape(Rectangle(radius, .1))), sAngle))
    val ms = Basic(Blue,rotate(Translate((((radius - 1)/2 -.5), 0.0),Shape(Rectangle((radius - .5), .15))), mAngle))
    val hs = Basic(Blue,rotate(Translate((((radius - 1.5)/2 -.5), 0.0),Shape(Rectangle((radius - 1), .15))), hAngle))
    Over(hs, Over(ms, ss))
  }

  def createRim(radius: Double): Picture = {
    val rimInner = PShape.circle(radius - .1)
    val rimOuter = PShape.circle(radius + .1)
    val rim = SubtractRegion(Shape(rimOuter), Shape(rimInner))
    val strip =  Translate((radius - .25, 0), Shape(Rectangle(.5, .2)))
    val strips = (1 to 12).foldRight(strip: Region){
      case (i, pic) => OrRegion(Region.rotate(strip, i * 30), pic)
    }
    val stripPic = Basic(Black, strips)
    Over(Basic(Black, rim), stripPic)
  }

}
