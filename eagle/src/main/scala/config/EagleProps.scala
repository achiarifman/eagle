package config

import java.io.File

import com.typesafe.config.ConfigFactory

import scala.util.Properties

/**
 * Created by Achia.Rifman on 10/12/2014.
 */
object EagleProps {

  val fileName = "eagle.conf"
  val config = {
    val directoryOption = Properties.propOrNone("vidmind.ext.prop.dir")
    directoryOption match {
      case Some(fullPathToFile) => {
        val parseFile = ConfigFactory.parseFile(new File(fullPathToFile + File.separator + fileName))
        ConfigFactory.load(parseFile)
      }
      case None => ConfigFactory.load()
    }
  }

}
