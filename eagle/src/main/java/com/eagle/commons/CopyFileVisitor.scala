package com.eagle.commons

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file._

/**
 * Created by Achia.Rifman on 17/03/2015.
 */
class CopyFileVisitor(val destFolder : Path) extends SimpleFileVisitor[Path]{

  override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
    Files.move(file, destFolder.resolve(file.getFileName));
    FileVisitResult.CONTINUE
  }
}
