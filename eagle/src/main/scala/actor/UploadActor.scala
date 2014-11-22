package actor

import java.io.{File, IOException}
import java.nio.file.{Files, Path}

import akka.actor.{ActorRef}
import com.eagle.commons.FileUploadVisitor
import com.eagle.entity.EagleRecordEntity
import entity.{FailedEntity}
import org.apache.commons.net.ftp.{FTP, FTPClient}
import util.EagleSpringProperties

import scala.collection.mutable.Stack

/**
 * Created by Achia.Rifman on 12/09/2014.
 */
class UploadActor() extends AbstractActor{

  val ftpClient: FTPClient = new FTPClient


  def receive = {

    case (uploadRecordJob: EagleRecordEntity, it : Stack[ActorRef]) => {
      val result = startUploadingProcess(uploadRecordJob)
      if(!result) handleFailedUploading(uploadRecordJob, it)
      else handleSuccessUploading(uploadRecordJob,it)
    }
    case (eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef], failed: FailedEntity) => {
      handleFailedEntity(eagleRecordJob, it, failed)
    }
  }

  def startUploadingProcess(uploadRecordJob: EagleRecordEntity) : Boolean = {
    try {
      initialFtpClient()
    }
    catch {
      case f: IOException => {
        return false
      }
    }
    val uploadFolder: Path = uploadRecordJob.getOutPutFolderPath
    val fileUploadVisitor: FileUploadVisitor = new FileUploadVisitor(ftpClient)
    fileUploadVisitor.setMainFolder(uploadFolder.getFileName.toString)
    ftpClient.makeDirectory(EagleSpringProperties.hostDirectory + File.separator + uploadFolder.getFileName.toString)
    Files.walkFileTree(uploadFolder, fileUploadVisitor)
    disconnect
    log.info("Start uploading process")
    true
  }

  def initialFtpClient(){
    ftpClient.connect(EagleSpringProperties.host, 21)
    val login: Boolean = ftpClient.login(EagleSpringProperties.userName, EagleSpringProperties.password)
    if (!login) {
      throw new IOException
    }
    ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
    ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE)
    ftpClient.enterLocalPassiveMode
  }

  def disconnect {
    if (this.ftpClient.isConnected) {
      try {
        this.ftpClient.logout
        this.ftpClient.disconnect
      }
      catch {
        case f: IOException => {
        }
      }
    }
  }

  def handleSuccessUploading(uploadRecordJob: EagleRecordEntity, it : Stack[ActorRef]){
    log.info("Handling success uploading")
    val act = it.pop()
    act ! (uploadRecordJob,it)
  }

  def handleFailedUploading(eagleRecordJob: EagleRecordEntity, it: Stack[ActorRef]) {
    log.error("Handling failed uploading")
    val failed = new FailedEntity("The upload phase as failed")
    eagleRecordJob.setFailed(true, failed)
    val act = it.pop()
    act !(eagleRecordJob, it, failed)
  }
}
