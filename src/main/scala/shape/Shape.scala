package shape

import shape.Shape.{Radius, Side, Vertex}

/**
 * Represents simplest graphical objects.
 */
sealed trait Shape
final case class Rectangle(width: Side, height: Side) extends Shape
final case class Ellipse(r1: Radius, r2: Radius) extends Shape
final case class Polygon(points: Seq[Vertex]) extends Shape
final case class RtTriangle(s1: Side, s2: Side) extends Shape


object Shape {
  type Side = Double
  type Radius = Double
  type Angle = Double
  type Vertex = (Double, Double)
  type Coordinate = (Double, Double)
  type Ray = (Coordinate, Coordinate)

  /**
   *distance between two vertices.
   */
  def length(v1: Vertex, v2: Vertex): Side = (v1, v2) match {
    case ((x1,y1), (x2,y2)) => Math.sqrt(Math.pow((x1 - x2), 2.0) + Math.pow((y1 - y2), 2.0))
  }

  def circle(radius: Radius): Shape = Ellipse(radius, radius)

  /**
   * Tells whether a give point lies inside shape or not
   */
  def contains(shape: Shape, point: Coordinate): Boolean = (shape, point) match {
    case (Rectangle(w, h), (x,y)) =>
        val s1 = w/2
        val s2 = h/2
        (x >= -s1 && x <= s1) && (y >= -s2 && y <= s2)
    case (Ellipse (r1, r2), (x,y)) => Math.pow((r1/x), 2.0) + Math.pow((r2/y), 2.0) <= 1
    case (Polygon(pts), _) =>
          val rays = pts zip (pts tail)
          rays.foldLeft(true)((b:Boolean, ray: Ray) => b && isLeftOf(point, ray))
    case (RtTriangle(s1, s2), _) =>
          contains(Polygon((0.0, 0.0) :: (s1, 0.0) :: (0.0, s2) :: Nil), point)
  }

  def isLeftOf(point: Coordinate, ray: Ray): Boolean = (point, ray) match {
    case ((px, py), ((r1x, r1y), (r2x, r2y))) =>
        val (s, t) = (px - r1x, py - r1y)
        val (u, v) = (px - r2x, py - r2y)
        s * v >= u * t
  }

  /**
   * zooms a shape by given factor
   */
  def scale(shape: Shape, factor: (Double, Double)): Shape = {
    val (xs,ys) = factor
    shape match {
      case Polygon(points) => Polygon(points.map{
        case (x,y) => (xs * x, ys* y)
      })
      case r @ Rectangle(_,_) => scale(getPolygon(r),factor)
      case rt @ RtTriangle(_,_) => scale(getPolygon(rt), factor)
      case Ellipse(r1, r2) => Ellipse(r1 * xs, r2 * ys)
    }
  }

  /**
   * Creates a polygon for a rectangle. Rectangle center is assumed to be coordinate origin.
   */
  def getPolygon(rectangle: Rectangle): Polygon = rectangle match {
    case Rectangle(w, h) => Polygon((-w/2, h/2) :: (w/2, h/2) :: (w/2, -h/2) :: (-w/2, -h/2) :: Nil)
  }

  /**
   * Creates a polygon for a right triangle. Right angled vertex is assumed to be center of coordinate
   * with sides on x axis and y axis.
   */
  def getPolygon(triangle: RtTriangle): Polygon = triangle match {
    case RtTriangle(s1, s2) => Polygon((0.0, 0.0) :: (0.0, s2) :: (s1, 0.0) :: Nil )
  }

  /**
   * Rotates a shape around origin
   *
   *TODO:make rotated images by having a shape constructor for angle? rotating
   *a ellipse is hard with what we have
   */
  def rotate(shape: Shape, angle: Angle): Shape = shape match {
    case Polygon(points) => Polygon(points.map(x => rotate(x, angle)))
    case r @ Rectangle(_,_)  => rotate(getPolygon(r), angle)
    case rt @ RtTriangle(_,_) => rotate(getPolygon(rt), angle)
    case e @ Ellipse(r1, r2) => e //rotation of ellipse not supported, return as it is
  }

  /**
   * rotates a vertex around origin.
   */
  def rotate(vertex: Vertex, angle: Angle): Vertex = {
    val (x,y) = vertex
    val c = Math.cos(Math.toRadians(angle))
    val s = Math.sin(Math.toRadians(angle))
    val newX = x * c - y * s
    val newY = x * s + y * c
    (newX, newY)
  }

}


