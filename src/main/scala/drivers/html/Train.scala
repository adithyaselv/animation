package drivers.html

import animation.example.{ClockAnimation, TrainAnimation}
import org.scalajs.dom
import org.scalajs.dom.html._

import scala.scalajs.js.annotation.JSExport
import scala.math._
/**
 * Created by rakesh.h on 24/08/15.
 */
@JSExport
object Train {
  @JSExport
  def main(canvas: Canvas): Unit = {
    val ctx = canvas.getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]

    implicit val size = (canvas.height, canvas.width)
    import implicits.HTMLImplicits._

    val ta = TrainAnimation.get
    val startTime = System.currentTimeMillis


    dom.window.setInterval(() => {
      val currentTime = System.currentTimeMillis
      val time = currentTime - startTime
      val train = ta.run(time)
      ctx.clearRect(0, 0, canvas.width, canvas.height)
      train.draw.unsafeRun(canvas)
    }, 30)
  }
}
