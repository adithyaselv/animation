package picture

import graphic.Color
import region.{Translate, Region}

/**
 * Picture add color to the region, also pictures can be composed.
 */
trait Picture

/**
 * picture representing colored region
 */
case class Basic(color: Color, region: Region) extends Picture

/**
 * composition of two pictures
 */
case class Over(picture1: Picture, picture2: Picture) extends Picture

case object EmptyPic extends Picture


object Picture {
  type Angle = Double
  type Point = (Double, Double)

  /**
   * Moves a given picture by given amount
   *
   */
  def translate(picture: Picture, point: Point): Picture = picture match {
    case Basic(c, r) => Basic(c, Translate(point, r))
    case Over(p1, p2) => Over(translate(p1, point), translate(p2, point))
    case EmptyPic => EmptyPic
  }
}