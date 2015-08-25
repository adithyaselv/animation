package implicits

import java.awt.geom.{Area => JRegion, Ellipse2D, AffineTransform}
import java.awt.{Color => JColor, Polygon => JPolygon, Graphics2D}

import graphic._
import picture.{EmptyPic, Over, Basic, Picture}
import region._
import shape.{Shape => _, _}
import shape.{Shape => PlainShape}

/**
 * Contains enrichment in context of java awt.
 * 1)converts a shape and region to the java graphical region
 * 2)provides a java awt specific draw operation
 */
object AWTImplicits {

  type Point = (Int, Int)

  implicit class JavaRegionWrapper(region: Region)(implicit size: (Int, Int))  {

    /**
     * converts a region to a java graphical region
     */
    def toJRegion: JRegion = region match {
      case Shape(s) => s.toJRegion
      case Translate(t, r) =>
        val x = cmToPixel(t._1)
        val y = -cmToPixel(t._2)
        val transform = AffineTransform.getTranslateInstance(x, y)
        new JRegion(transform.createTransformedShape(r.toJRegion))
      case Scale(s, r) =>  new JRegion(Region.scale(s, r).toJRegion)
      case OrRegion(r1, r2) =>
        val jr = r1.toJRegion
        jr.add(r2.toJRegion)
        jr
      case AndRegion(r1, r2) =>
        val jr = r1.toJRegion
        jr.intersect(r2.toJRegion)
        jr
      case SubtractRegion(r1, r2) =>
        val jr = r1.toJRegion
        jr.subtract(r2.toJRegion)
        jr
    }

    /**
     * returns a Draw operation for the region with passed color.
     */
    def draw(color: Color): Draw[Unit, Graphics2D] = {
      Drawing{g =>
        val earlyColor = g.getColor
        g.setColor(color.toJColor)
        g.fill(region.toJRegion)
        g.setColor(earlyColor)
      }
    }

    def draw: Draw[Unit, Graphics2D] = Drawing(g => g.fill(region.toJRegion))
  }

  implicit class PictureWrapper(picture: Picture)(implicit size: (Int, Int))  {
    def draw: Draw[Unit, Graphics2D] = picture match {
      case Basic(c, r) => r.draw(c)
      case Over(o1, o2) => o2.draw.flatMap(_ => o1.draw)//first draw o2, then draw o1
      case EmptyPic => Drawing(_ => ()) //don't draw anything
    }

  }

 implicit class JavaColorWrapper(color: Color)  {
    def toJColor = color match {
      case Red => JColor.RED
      case Black => JColor.BLACK
      case Blue => JColor.BLUE
      case Green => JColor.GREEN
      case Cyan => JColor.CYAN
      case Magenta => JColor.MAGENTA
      case Yellow => JColor.YELLOW
      case White => JColor.WHITE
    }
  }

  implicit class JavaShapeWrapper(shape: PlainShape)(implicit size: (Int, Int)) {

    /**
     * converts a simple shape to the java graphical region.
     */
    def toJRegion:JRegion = shape match {
      case Polygon(points) =>
        val xList = points.map(x => cmToPixel(x._1)).map(transformX).toArray
        val yList = points.map(x => cmToPixel(x._2)).map(transformY).toArray
        new JRegion(new JPolygon(xList, yList, points.size))
      case r @ Rectangle(_,_) => PlainShape.getPolygon(r).toJRegion
      case rt @ RtTriangle(_,_) => PlainShape.getPolygon(rt).toJRegion
      case Ellipse(r1MM, r2MM) =>
        val r1 = cmToPixel(r1MM)
        val r2 = cmToPixel(r2MM)
        new JRegion(new Ellipse2D.Double(transformX(-r1) , transformY(r2), 2*r1, 2 * r2))
    }

    private def transformX(x: Int): Int = size._1/2 + x
    private def transformY(y: Int): Int = size._2/2 - y
  }


  private def cmToPixel(cm: Double): Int = (cm * 37.8).toInt

}
