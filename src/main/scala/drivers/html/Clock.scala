package drivers.html

import animation.example.{ClockAnimation, TrainAnimation}
import org.scalajs.dom
import org.scalajs.dom.html.Canvas
import shape.{Shape => PShape}

import scala.scalajs.js.annotation.JSExport

@JSExport
object Clock {
  @JSExport
  def main(canvas: Canvas): Unit = {
    val ctx = canvas.getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]

    implicit val size = (canvas.height, canvas.width)
    import implicits.HTMLImplicits._

    val ca = ClockAnimation.get
    val startTime = System.currentTimeMillis


    dom.setInterval(() => {
      val currentTime = System.currentTimeMillis
      val time = currentTime - startTime
      val clock = ca.run(time)
      ctx.clearRect(0, 0, canvas.width, canvas.height)
      clock.draw.run(canvas)
    }, 30)
  }
}
