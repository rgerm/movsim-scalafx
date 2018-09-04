package de.movsim.scalafx

import java.awt.geom.{GeneralPath, Path2D}
import java.awt.{BasicStroke, Color}
import java.util.Properties

import org.jfree.fx.FXGraphics2D
import org.movsim.autogen.Movsim
import org.movsim.roadmappings.RoadMapping
import org.movsim.simulator.SimulationRunnable.UpdateDrawingCallback
import org.movsim.simulator.Simulator
import org.movsim.simulator.vehicles.Vehicle
import org.movsim.xml.InputLoader
import scalafx.scene.canvas.{Canvas, GraphicsContext}

class SimulationCanvas(w : Int, h : Int, properties : Properties) extends Canvas with UpdateDrawingCallback {
  width = w
  height = h
  val movsimInput: Movsim = InputLoader.unmarshallMovsim(Viewer.projectMetaData.getInputFile)
  val simulator = new Simulator(movsimInput)
  val roadNetwork = simulator.getRoadNetwork
  val simulationRunnable = simulator.getSimulationRunnable
  val gc: GraphicsContext = graphicsContext2D
  val g: FXGraphics2D = new FXGraphics2D(gc)

  simulator.initialize()
  simulationRunnable.setUpdateDrawingCallback(this)
  simulationRunnable.start()

  roadNetwork.forEach(roadSegment => roadSegment.roadMapping().setRoadColor(-8355712))
  private val brakeLightColor = Color.RED
  var scale = 0.7071 //properties.getProperty("initialScale").toDouble
  var xOffset = 10 //properties.getProperty("xOffset").toInt
  var yOffset = 100 //properties.getProperty("yOffset").toInt
  private val clipPath = new GeneralPath((Path2D.WIND_EVEN_ODD))
  private val vehiclePath = new GeneralPath()

  setTransformObj()

  def setTransformObj(): Unit = {
    gc.scale(scale, scale);
    gc.translate(xOffset, yOffset)
  }

  def setTranslateObj(): Unit = {
    gc.translate(xOffset, yOffset)
  }

  override def updateDrawing(simulationTime: Double): Unit = {
    draw()
  }

  def draw(): Unit = {
    drawRoadNetwork(g)
    drawMovables(g)
  }

  def drawRoadNetwork(gc: FXGraphics2D): Unit = {
    drawRoadSegmentsAndLines(gc)
  }

  def drawMovables(gc: FXGraphics2D): Unit = {
    val simulationTime: Double = simulationRunnable.simulationTime
    import scala.collection.JavaConversions._
    for (roadSegment <- roadNetwork) {
      val roadMapping: RoadMapping = roadSegment.roadMapping
      assert(roadMapping != null)
      PaintRoadMapping.setClipPath(gc, roadMapping, clipPath)
      import scala.collection.JavaConversions._
      for (vehicle <- roadSegment) {
        drawVehicle(gc, simulationTime, roadMapping, vehicle)
      }
    }
  }

  def vehicleColor(vehicle: Vehicle, simulationTime: Double): Color = {
    Color.BLUE
  }

  def drawVehicle(g: FXGraphics2D, simulationTime: Double, roadMapping: RoadMapping, vehicle: Vehicle) = {
    // draw vehicle polygon at new position
    val polygon = roadMapping.mapFloat(vehicle)

    gc.beginPath()
    gc.moveTo(polygon.getXPoint(0), polygon.getYPoint(0))
    gc.lineTo(polygon.getXPoint(1), polygon.getYPoint(1))
    gc.lineTo(polygon.getXPoint(2), polygon.getYPoint(2))
    gc.lineTo(polygon.getXPoint(3), polygon.getYPoint(3))
    gc.closePath()
    gc.setFill(javafx.scene.paint.Color.BLUE)
    gc.fill()

    if (vehicle.isBrakeLightOn) {
      vehiclePath.reset()
      // points 2 & 3 are at the rear of vehicle
      if (roadMapping.isPeer) {
        vehiclePath.moveTo(polygon.getXPoint(0), polygon.getYPoint(0))
        vehiclePath.lineTo(polygon.getXPoint(1), polygon.getYPoint(1))
      }
      else {
        vehiclePath.moveTo(polygon.getXPoint(2), polygon.getYPoint(2))
        vehiclePath.lineTo(polygon.getXPoint(3), polygon.getYPoint(3))
      }
      vehiclePath.closePath()
      g.setPaint(brakeLightColor)
      g.draw(vehiclePath)
    }
  }

  def drawRoadSegment(g: FXGraphics2D, roadMapping: RoadMapping) = {
    val roadStroke: BasicStroke = new BasicStroke(
      roadMapping.roadWidth().toFloat,
      BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_MITER)
    g.setStroke(roadStroke)
    g.setColor(new Color(roadMapping.roadColor()))
    PaintRoadMapping.paintRoadMapping(g, roadMapping)
  }

  def drawRoadSegmentsAndLines(g: FXGraphics2D): Unit = {
    import scala.collection.JavaConversions._
    for (roadSegment <- roadNetwork) {
      val roadMapping = roadSegment.roadMapping
      if (!roadMapping.isPeer) {
        drawRoadSegment(g, roadMapping)
        //      drawRoadSegmentLines(g, roadMapping)
      }
    }
  }
}
