package actor

import java.io.{File, IOException}
import java.nio.file.{Paths, Files, Path}

import actor.message.{PostUploadMessage, PreUploadMessage}
import akka.actor.{ActorRef}
import com.eagle.commons.{CopyFileVisitor, FileUploadVisitor}
import com.eagle.entity.EagleRecordEntity
import com.eagle.dao.entity.{FailedEntity}
import config.{PropsConst, EagleProps}
import org.apache.commons.net.ftp.{FTP, FTPClient}
import org.bson.types.ObjectId


import scala.collection.mutable.Stack

/**
 * Created by Achia.Rifman on 12/09/2014.
 */
class UploadActor extends AbstractActor{

  val ftpClient: FTPClient = new FTPClient
  val ftpHost = EagleProps.config.getString(PropsConst.FTP_HOST)
  val ftpDirectory = EagleProps.config.getString(PropsConst.FTP_FOLDER)
  val ftpUsername = EagleProps.config.getString(PropsConst.FTP_USERNAME)
  val ftpPassword = EagleProps.config.getString(PropsConst.FTP_PASSWORD)

  val dropboxFolder = EagleProps.config.getString(PropsConst.DROPBOX_UPLOAD_PATH)
  val dropboxPublishUrl = EagleProps.config.getString(PropsConst.DROPBOX_PUBLISH_URL)


  def receive = {

    case (preUploadMessage: PreUploadMessage) => {
      val result = startUploadingProcess(preUploadMessage)
      if(!result) handleFailedUploading(preUploadMessage.id)
      else handleSuccessUploading(preUploadMessage)
    }
  }

  def startUploadingProcess(preUploadMessage: PreUploadMessage) : Boolean = {

    copyFileToFolder(preUploadMessage)
    /*try {
      initialFtpClient()
    }
    catch {
      case f: IOException => {
        return false
      }
    }
    val uploadFolder: Path = preUploadMessage.uploadPath
    val fileUploadVisitor: FileUploadVisitor = new FileUploadVisitor(ftpClient)
    fileUploadVisitor.setMainFolder(uploadFolder.getFileName.toString)
    ftpClient.makeDirectory(ftpDirectory + File.separator + uploadFolder.getFileName.toString)
    Files.walkFileTree(uploadFolder, fileUploadVisitor)
    disconnect
    log.info("Start uploading process")
    true*/
  }

  def copyFileToFolder(preUploadMessage: PreUploadMessage) = {
    val uploadFolder: Path = preUploadMessage.uploadPath
    val copyFileVisitor : CopyFileVisitor = new CopyFileVisitor(Paths.get(dropboxFolder))
    Files.walkFileTree(uploadFolder,copyFileVisitor)
    true
  }

  def initialFtpClient(){
    ftpClient.connect(ftpHost, 21)
    val login: Boolean = ftpClient.login(ftpUsername, ftpPassword)
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

  def handleSuccessUploading(m : PreUploadMessage){
    log.info("Handling success uploading")
    val publishUrl = dropboxPublishUrl + m.uploadPath.getFileName
    sender() ! PostUploadMessage(m.id,true,publishUrl)
  }

  def handleFailedUploading(id : String) {
    log.error("Handling failed uploading")
    sender() ! PostUploadMessage(id,false,"")
  }
}
