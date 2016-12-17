package drivers.awt

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Graphics, Graphics2D}
import javax.swing.{JPanel, Timer}

import animation.Animation
import implicits.AWTImplicits._
import picture.Picture

/**
 * Created by rakesh.h on 20/08/15.
 */
class AnimatedCanvas(animate: Animation[Picture])  extends JPanel with ActionListener{
  val timer = new Timer(10, this)
  val startTime = System.currentTimeMillis
  timer.start()

  override def actionPerformed(e: ActionEvent): Unit = repaint()

  override def paintComponent(g: Graphics): Unit = {
    implicit  val size  = (getWidth, getHeight)
    g.clearRect(0,0,size._1, size._2)
    val g2D = g.asInstanceOf[Graphics2D]
    val currentTime = System.currentTimeMillis
    val time = (currentTime - startTime)

    val da = Animation.lift1(animate) {pic =>
      pic.draw.unsafeRun(g2D)
    }
    da.run(time)
  }
}