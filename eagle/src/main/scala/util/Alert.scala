package util


import config.{PropsConst, EagleProps}
import util.Utils.{send, Mail}

/**
 * Created by Achia.Rifman on 21/03/2015.
 */
object Alert {

  val alertMail =  EagleProps.config.getString(PropsConst.ALERT_MAIL)

  val SUBJECT_ERROR_EMBED = "Error - Embed ads"

  def sendAlertMail(subject : String, body : String) = {
    send a new Mail (
      from = ("vidmindnexus@gmail.com", "Eagle alert system"),
      to = Seq(alertMail),
      subject = subject,
      message = body
    )
  }
}
