package region

import Region.Vector
import shape.{Shape => PlainShape}

/**
 * Represents complex graphical structures created out of simpler ones.
 */
sealed trait Region

/**
 * Simple region created out of shape, ex: Rectangle
 */
case class Shape(shape: PlainShape) extends Region

/**
 * Zoomed region
 */
case class Scale(s: Vector, region: Region) extends Region

/**
 * Region moved by a certain distance
 */
case class Translate(t: Vector, region: Region) extends Region

/**
 * Intersection of two regions
 */
case class AndRegion(region1: Region, region2: Region) extends Region

/**
 * Union of two regions.
 */
case class OrRegion(region1: Region, region: Region) extends Region

/**
 * Subtracts second shape from the first one.
 */
case class SubtractRegion(region1: Region, region2: Region) extends Region

object Region {
  type Vector = (Double, Double)
  type Angle = Double
  type Coordinate = (Double, Double)

  /**
   * rotate the given region(counterclockwise) by the given angle.
   */
  def rotate(r: Region, angle: Angle): Region = r match {
    case Shape(s) => Shape(PlainShape.rotate(s, angle))
    case Scale(s, r) => Scale(s, rotate(r, angle))
    case Translate(t, r) => Translate(PlainShape.rotate(t, angle), rotate(r, angle))
    case AndRegion(r1, r2) => AndRegion(rotate(r1, angle), rotate(r2, angle))
    case OrRegion(r1, r2) => OrRegion(rotate(r1, angle), rotate(r2, angle))
    case SubtractRegion(r1, r2) => SubtractRegion(rotate(r1, angle), rotate(r2, angle))
  }

  /**
   * Zooms a passed region by a given factor and returns the zoomed region.
   */
  def scale(f: (Double, Double), r: Region): Region = r match {
    case Shape(s) => Shape(PlainShape.scale(s, f))
    case Translate(t, r) => Translate(t, scale(f, r))
    case Scale(f2, r) => scale(((f._1 * f2._1), (f._2 * f._2)), r)
    case OrRegion(r1, r2) => OrRegion(scale(f, r1), scale(f, r2))
    case AndRegion(r1, r2) => AndRegion(scale(f, r1), scale(f, r2))
    case SubtractRegion(r1, r2) => SubtractRegion(scale(f, r1), scale(f, r2))
  }

}