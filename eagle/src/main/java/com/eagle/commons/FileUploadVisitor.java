package com.eagle.commons;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by Achia.Rifman on 23/08/2014.
 */

public class FileUploadVisitor extends SimpleFileVisitor<Path> {


    FTPClient ftpClient;

    public final static String EAGLE_FOLDER = "eagle";

    private String mainFolder;

    public FileUploadVisitor(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        ftpClient.storeFile(EAGLE_FOLDER + File.separator + mainFolder + File.separator + file.getFileName().toString(),new FileInputStream(new File(file.toUri())));
        return FileVisitResult.CONTINUE;

    }

    public String getMainFolder() {
        return mainFolder;
    }

    public void setMainFolder(String mainFolder) {
        this.mainFolder = mainFolder;
    }
}
