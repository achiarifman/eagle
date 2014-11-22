package com.eagle.commons;

import com.eagle.consts.PHASES;
import com.eagle.entity.EagleUploadEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.apache.commons.net.ftp.FTPClient;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

/**
 * Created by Achia.Rifman on 22/08/2014.
 */
@Component
@Scope("prototype")
public class UploadPhase extends AbstractPhase {

    @Autowired
    FTPClient ftpClient;

    EagleUploadEntity eagleUploadEntity;


    @Value("${ftp.host.folder}")
    private String hostDirectory;

    @PostConstruct
    protected void init() {

        setPHASE_NAME(PHASES.UPLOAD);

    }

    @Override
    public Boolean call() throws Exception {

        Path uploadFolder = eagleUploadEntity.getUploadFolder();
        FileUploadVisitor fileUploadVisitor= new FileUploadVisitor(ftpClient);
        fileUploadVisitor.setMainFolder(uploadFolder.getFileName().toString());
        ftpClient.makeDirectory(hostDirectory + File.separator + uploadFolder.getFileName().toString());
        Files.walkFileTree(uploadFolder, fileUploadVisitor);
        disconnect();
        return true;

    }

    public EagleUploadEntity getEagleUploadEntity() {
        return eagleUploadEntity;
    }

    public void setEagleUploadEntity(EagleUploadEntity eagleUploadEntity) {
        this.eagleUploadEntity = eagleUploadEntity;
    }

    public void disconnect(){
        if (this.ftpClient.isConnected()) {
            try {
                this.ftpClient.logout();
                this.ftpClient.disconnect();
            } catch (IOException f) {
                // do nothing as file is already saved to server
            }
        }
    }

    @Override
    public ObjectId getPhaseId() {
        return eagleUploadEntity.getJobId();
    }
}
