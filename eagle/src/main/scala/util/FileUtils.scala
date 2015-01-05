package util

import java.io.File
import java.nio.file.Paths

/**
 * Created by Achia.Rifman on 13/12/2014.
 */
trait FileUtils {

  def createOutputFolder(folderName: String) : String = {
    new File(folderName).mkdir
    Paths.get(folderName).toString
  }

  def getListOfFiles(folderName : String) = {
    val f = new File(folderName)
    f.listFiles()
  }

}
