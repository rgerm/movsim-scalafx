package de.movsim.scalafx

import java.util.Properties

import org.movsim.input.{MovsimCommandLine, ProjectMetaData}
import org.movsim.logging.Logger
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.{JFXApp, Platform}
import scalafx.scene.{Group, Scene}

object Viewer extends JFXApp {
  val projectMetaData: ProjectMetaData = ProjectMetaData.getInstance()
  var properties : Properties = null
  var xOffsetSave : Int = 0
  var yOffsetSave : Int = 0
  var startDragX : Int = 0
  var startDragY : Int = 0

  override def main(args: Array[String]): Unit = {
    var properties : Properties = new Properties()
    val projectMetaData: ProjectMetaData = ProjectMetaData.getInstance
    Logger.initializeLogger()
    MovsimCommandLine.parse(args)
    properties = ViewConfig.loadProperties(projectMetaData)
    this.properties = properties
    super.main(args)
  }

  private val canvas = new SimulationCanvas(1000, 800, properties)
  stage = new PrimaryStage {
    title = "Movsim"
    scene = new Scene(1000,800) {
      root = new Group {

        children = List(canvas)
      }
    }
    onCloseRequest = {
      (ev: javafx.stage.WindowEvent) => {
        Platform.exit()
        System.exit(0)
      }
    }
  }
  canvas.onMousePressed = {
    (e) => {
      startDragX = e.getX.toInt
      startDragY = e.getY.toInt
      xOffsetSave = canvas.xOffset
      yOffsetSave = canvas.yOffset
    }
  }
  canvas.onMouseReleased = {
    (e) => {
      val xOffsetNew : Int = xOffsetSave + ((e.getX - startDragX) / canvas.scale).toInt
      val yOffsetNew : Int = yOffsetSave + ((e.getY - startDragY) / canvas.scale).toInt
      if ((xOffsetNew != canvas.xOffset) || (yOffsetNew != canvas.yOffset)) {
        canvas.xOffset = xOffsetNew
        canvas.yOffset = yOffsetNew
        canvas.setTranslateObj()
      }
    }
  }

}
