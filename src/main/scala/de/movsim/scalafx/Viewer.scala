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

  override def main(args: Array[String]): Unit = {
    var properties : Properties = new Properties()
    val projectMetaData: ProjectMetaData = ProjectMetaData.getInstance
    Logger.initializeLogger()
    MovsimCommandLine.parse(args)
    properties = ViewConfig.loadProperties(projectMetaData)
    this.properties = properties
    super.main(args)
  }

  stage = new PrimaryStage {
    title = "Movsim"
    scene = new Scene(1000,800) {
      root = new Group {
        children = List(new SimulationCanvas(1000, 800, properties))
      }
    }
    onCloseRequest = {
      new javafx.event.EventHandler[javafx.stage.WindowEvent] {
        def handle(ev: javafx.stage.WindowEvent): Unit = {
          Platform.exit()
          System.exit(0)
        }
      }
    }
  }


}
