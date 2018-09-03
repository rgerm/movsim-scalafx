package de.movsim.scalafx

import java.util.Properties

import org.movsim.input.{MovsimCommandLine, ProjectMetaData}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Group, Scene}

object Viewer extends JFXApp {
  val projectMetaData: ProjectMetaData = ProjectMetaData.getInstance()
  var properties : Properties = null

  override def main(args: Array[String]): Unit = {
    var properties : Properties = new Properties()
    val projectMetaData: ProjectMetaData = ProjectMetaData.getInstance
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
  }


}
