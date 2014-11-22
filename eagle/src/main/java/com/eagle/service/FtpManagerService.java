package com.eagle.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Achia.Rifman on 28/06/2014.
 */
@Service
public class FtpManagerService {

    Logger LOGGER = LoggerFactory.getLogger(FtpManagerService.class);

    //@Value("#{props['ftp.host']}")
    @Value("${ftp.host}")
    String host;

    //@Value("#{props['ftp.username']}")
    @Value("${ftp.username}")
    String userName;

    //@Value("#{props['ftp.password']}")
    @Value("${ftp.password}")
    String password;

    @Bean(name = "ftpClient")
    public FTPClient ftpClient(){
        try {
            return initiateFtpClient(host);
        } catch (IOException e) {
            LOGGER.error("Could not initial the ftp client " + e.getStackTrace());
            return null;
        }
    }

    private FTPClient initiateFtpClient(String serverIp) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(serverIp, 21);
        boolean login = ftpClient.login(userName, password);
        if (!login) {
            throw new IOException(); // catch it
        }

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        return ftpClient;
    }
}
