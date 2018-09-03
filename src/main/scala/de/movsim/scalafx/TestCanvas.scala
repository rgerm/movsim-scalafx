package de.movsim.scalafx

import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import scalafx.scene.canvas.{Canvas, GraphicsContext}

class TestCanvas(w : Int, h : Int) extends Canvas{
  width = w
  height = h
  val gc: GraphicsContext = graphicsContext2D
  drawShapes(gc)

  private def drawShapes(gc: GraphicsContext): Unit = {
    gc.setFill(Color.GREEN)
    gc.setStroke(Color.BLUE)
    gc.setLineWidth(5)
    gc.strokeLine(40, 10, 10, 40)
    gc.fillOval(10, 60, 30, 30)
    gc.strokeOval(60, 60, 30, 30)
    gc.fillRoundRect(110, 60, 30, 30, 10, 10)
    gc.strokeRoundRect(160, 60, 30, 30, 10, 10)
    gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN)
    gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD)
    gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND)
    gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN)
    gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD)
    gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND)
    gc.fillPolygon(Array[Double](10, 40, 10, 40), Array[Double](210, 210, 240, 240), 4)
    gc.strokePolygon(Array[Double](60, 90, 60, 90), Array[Double](210, 210, 240, 240), 4)
    gc.strokePolyline(Array[Double](110, 140, 110, 140), Array[Double](210, 210, 240, 240), 4)
  }
}
