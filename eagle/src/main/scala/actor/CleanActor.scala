package actor

import java.io.{IOException, File}
import actor.message.FilesCleanMessage
import com.eagle.dao.JobDao
import config.{PropsConst, EagleProps}
import org.apache.commons.io.FileUtils
import scala.util.matching.Regex
import com.eagle.dao.persistanceContext._
/**
 * Created by Achia.Rifman on 04/04/2015.
 */
class CleanActor extends AbstractActor{

  val OUTPUT_FOLDER = EagleProps.config.getString(PropsConst.RECORD_OUTPUT)
  val EMBED_FOLDER = EagleProps.config.getString(PropsConst.EMBED_OUTPUT)
  val SPLITTED_FOLDER = EagleProps.config.getString(PropsConst.IMG_SPLITTED_FOLDER)
  val DIFF_FOLDER = EagleProps.config.getString(PropsConst.IMG_DIFF_FOLDER)
  val SCALED_OUTPUT : String = EagleProps.config.getString(PropsConst.SCALED_OUTPUT)

  val foldersList = List(OUTPUT_FOLDER,EMBED_FOLDER,SPLITTED_FOLDER,DIFF_FOLDER,SCALED_OUTPUT)

  override def receive: Receive = {
    case message : FilesCleanMessage => cleanFiles
  }

  def cleanFiles = {
    transactional{
      val navigator = JobDao.getNotCleanedJobs
      while(navigator.hasNext){
        val cleanJobList = navigator.next
        foldersList.foreach(folder=> deleteFileFromFolder(folder,jobIdsToRegex(cleanJobList)))
      }
    }
  }

  def deleteFileFromFolder(folderName :String, jobsRegex : List[Regex]) = {
    log.info("Going to delete files from the folder " + folderName)
    val f = new File(folderName)
    //val these = f.listFiles
    if(f.exists()){
      val these = getRecursiveListOfFiles(f)
      val jobsFilesTuples = jobsRegex.flatMap(job => these.map(file => (job,if(job.findFirstIn(file.getName).isDefined)Some(file) else None)))
      val deleteTuples = jobsFilesTuples.filter(tuple => tuple._2.isDefined)
      log.info("Found " + deleteTuples.size + " files for delete in folder " + folderName + " ,going to delete those files")
      deleteTuples.foreach(tuple => {
        val file = tuple._2.get
        log.info("Going to delete the file " + file.getAbsolutePath)
        if (file.exists()) {
          try {
            FileUtils.forceDelete(file)
            JobDao.markJobAsCleaned(tuple._1.regex.stripPrefix("(").stripSuffix(")"))
          }catch{
            case e : IOException => log.error("Could not delete the file " + file.getAbsolutePath)
          }
        }else{
          log.warning("could not delete the file " + file.getAbsoluteFile)
        }
      }
      )
    }else{
      log.warning("could not get files from the folder " + folderName)
    }
  }

  def jobIdsToRegex(jobIds : List[String]) : List[Regex] = {
    val regexs = jobIds.map(j => new Regex("(" + j.toString + ")"))
    regexs
  }

  def getRecursiveListOfFiles(dir: File): Array[File] = {
    val these = dir.listFiles
    if(these != null) {
      these ++ these.filter(_.isDirectory).flatMap(getRecursiveListOfFiles)
    }
    these
  }
}
