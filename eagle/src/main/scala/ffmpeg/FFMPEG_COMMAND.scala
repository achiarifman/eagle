package ffmpeg

/**
 * Created by Achia.Rifman on 22/11/2014.
 */
object FFMPEG_COMMAND {

  //ffmpeg -i sviaznoi_iz_otpuska1.mp4 -ss 00:00:05 -t 00:00:10 -f image2 -vf fps=fps=1 pics\out%d.png

  val INPUT = " -i "
  val TIME_LIMIT = " -t "
  val FORMAT = " -f "
  val VIDEO_CODEC = " -vcodec "
  val AUDIO_CODEC = " -acodec "
  val IMAGE2 = "image2"
  val VF = " -vf "
  val IMAGE_FPS = "fps=fps="
  val START_TIME = " -ss "

}
