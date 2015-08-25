package implicits

import graphic._
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.html.Canvas
import picture.{EmptyPic, Over, Basic, Picture}
import region._
import shape.{Shape => PlainShape, _}

/**
 * Contains enrichment in context of html canvas.
 * 1)provides a html canvas specific draw operation
 */
object HTMLImplicits {
  type Point = (Int, Int)

  implicit class HtmlRegionWrapper(region: Region)(implicit size: (Int, Int))  {

    def draw: Draw[Unit, Canvas] = region match {
      case Shape(s) => s.draw
      case AndRegion(r1, r2) => Drawing(compositeOp("destination-in", r1, r2))
      case SubtractRegion(r1, r2) => Drawing(compositeOp("destination-out", r1, r2))
      case OrRegion(r1, r2) => Drawing(compositeOp("source-over", r1, r2))
      case Translate(t, r) => Drawing{canvas=>
        val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
        val xt = cmToPixel(t._1)
        val yt = -cmToPixel(t._2)
        ctx.translate(xt, yt)
        r.draw.run(canvas)
        ctx.translate(-xt, -yt)
      }
      case Scale(s, r) => Region.scale(s, r).draw
    }

    def draw(color: Color): Draw[Unit, Canvas] = Drawing{canvas=>
      val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
      val old = ctx.fillStyle
      ctx.fillStyle = color.toHColor
      region.draw.run(canvas)
      ctx.fillStyle = old
    }

    def compositeOp(op: String, r1: Region, r2: Region): html.Canvas => Unit = {canvas =>
      val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
      r1.draw.run(canvas)
      val old = ctx.globalCompositeOperation
      ctx.globalCompositeOperation = op
      r2.draw.run(canvas)
      ctx.globalCompositeOperation = old
    }

  }

  implicit class PictureWrapper(picture: Picture)(implicit size: (Int, Int))  {
    def draw: Draw[Unit, Canvas] = picture match {
      case Basic(c, r) => r.draw(c)
      case Over(o1, o2) => for {
        _ <- o1.draw
        _ <- o2.draw
      } yield ()
      case EmptyPic => Drawing(_ => ())
    }

  }

  implicit class HtmlColorWrapper(color: Color)  {
    def toHColor = color.toString.toLowerCase
  }

  implicit  class HtmlShapeWrapper(shape: PlainShape)(implicit size: (Int, Int)) {

    def draw: Draw[Unit, Canvas] = shape match {
      case Polygon(points) => Drawing{canvas =>
        val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
        val xList = points.map(x => cmToPixel(x._1)).map(transformX).toArray
        val yList = points.map(x => cmToPixel(x._2)).map(transformY).toArray
        val pts = xList zip yList
        ctx.beginPath()
        ctx.moveTo(xList.head, yList.head)
        pts.tail.foreach(x => ctx.lineTo(x._1.toDouble, x._2.toDouble))
        ctx.closePath()
        ctx.fill()
      }

      case r @ Rectangle(_, _) => PlainShape.getPolygon(r).draw
      case rt @ RtTriangle(_, _) => PlainShape.getPolygon(rt).draw
      case Ellipse(r1, r2) => Drawing { canvas =>

        val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
        val rw = cmToPixel(r1)
        val rh = cmToPixel(r2)
        val x = transformX(0)
        val y = transformY(0)
        ctx.save();
        ctx.scale(1,  rh/rw);
        ctx.beginPath();
        ctx.arc(x, y, rw, 0, 2 * Math.PI);
        ctx.restore();
        ctx.fill()
        ctx.closePath()
      }
    }

    private def transformX(x: Int): Int = size._1/2 + x
    private def transformY(y: Int): Int = size._2/2 - y
  }

  private def cmToPixel(cm: Double): Int = (cm * 37.8).toInt
}