package animation.example

import animation.{Animation, Animate}
import graphic.{Blue, Black, Red}
import picture.{Basic, Over, Picture}
import region._
import shape.{Shape => PShape, Rectangle}

/**
 * Created by rakesh.h on 24/08/15.
 */
object TrainAnimation {

  def get: Animation[Picture] = Animate[Picture](createTrain)

  def createTire(initialAngle: Double, radius: Double): Picture = {
    val stick = Shape(Rectangle(2 * radius, .1))
    val rotatedStick = Region.rotate(stick, initialAngle)
    val sticks = (1 to 6).foldRight(rotatedStick){
      case (i, us) =>
        val nextStick = Region.rotate(stick, initialAngle  +  (i + 1) * 30)
        OrRegion(us, nextStick)
    }
    val rimInner = PShape.circle(radius - .05)
    val rimOuter = PShape.circle(radius + .05)
    val rim = SubtractRegion(Shape(rimOuter), Shape(rimInner))
    Over(Basic(Red, rim), Basic(Red, sticks))
  }

  def createTrain(time: Double): Picture = {
    val length = 8
    val height = 3.0
    val radius = 1
    val td = 5.0 //tyreDistance
    val distance =  (Math.PI / 2) * (time/ 1000) * radius //distance traveled as a function of time
    val angle = -.09 * time //angle of sticks w.r.t time

    //link to connect two coaches
    val link = Basic(Black,Translate((distance + (length - 1), radius + height/2), Shape(Rectangle(1.2, .2))))

    val body = Basic(Blue, Translate((td/2, radius + height/2), Shape(Rectangle(length, height))))
    val  bodyMoved = moveFwd(body, distance)
    val backTyre = moveFwd(createTire(angle, radius), distance)
    val frontType = moveFwd(createTire(angle , radius), distance + td)

    val wholeBody = Over(bodyMoved, Over(frontType, backTyre))

    (1 to 20).foldRight(wholeBody){ //make 20 coaches
      case (i, train) =>
        val pos = - (length + 1) * i
        val newCoach = moveFwd(wholeBody, pos)
        val linkPic = moveFwd(link, pos)
        Over(linkPic, Over(train, newCoach))
    }
  }

  def moveFwd(picture: Picture, distance: Double): Picture =
    Picture.translate(picture, (distance, 0))
}
