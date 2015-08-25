package drivers.awt

import java.awt.Graphics2D
import javax.swing.JFrame

import graphic.Draw

/**
 * Created by rakesh.h on 19/08/15.
 */
class DrawableWindow(draw: Draw[Unit, Graphics2D], x: Int = 0, y: Int = 0, width: Int = 800, height: Int = 600) {

  val frame = new JFrame
  frame.setBounds(x, y, width, height)
  frame.add(new DrawableCanvas(draw))

  def open(): Unit = frame.setVisible(true)
}
