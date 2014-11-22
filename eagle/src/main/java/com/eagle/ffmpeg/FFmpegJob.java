package com.eagle.ffmpeg;

import com.eagle.jobs.AbstractJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Achia.Rifman on 20/06/2014.
 */
@Component
@Scope("prototype")
public class FFmpegJob extends AbstractJob {

    Logger LOGGER = LoggerFactory.getLogger(FFmpegJob.class);

    public final static String FFMPEG = "ffmpeg";

    Process process;
    StringBuilder stringBuilder;

    public FFmpegJob() {
        stringBuilder = new StringBuilder();
        stringBuilder.append(FFMPEG);

    }

    @Override
    public Boolean call() throws Exception {

        process = Runtime.getRuntime().exec(stringBuilder.toString());
        Scanner scanner = new Scanner(process.getErrorStream());
        String line = null;
        String regex;
        Pattern track = Pattern.compile("^\\bframe\\b.*");
        Pattern failedHostName = Pattern.compile(".*\\bFailed to resolve hostname\\b.*");
        boolean isStarted = false;
        boolean isFailed = false;
        try {
            while ((line = scanner.nextLine()) != null) {
                Matcher mTrack = track.matcher(line);
                if (mTrack.matches()) {
                    System.out.println("It is a frame line");
                    isStarted = true;
                }
                mTrack = failedHostName.matcher(line);
                if (mTrack.matches()) {
                    System.out.println("****Could not reach the url*****");
                    isFailed = true;
                }

                System.out.println("\t***" + line);
            }
        }
        catch (NoSuchElementException e){
            if(isStarted && !isFailed)
            LOGGER.info("Could not find more lines");
            else
                if (isStarted && isFailed)
                    LOGGER.error("Could not reach the url");
                else
                    LOGGER.error("There was an error to start the recording");

        }
        return true;
    }

    public void appendPair(String key, String value){

        stringBuilder.append(key);
        stringBuilder.append(value);
    }

    public void appendParam(String param){

        stringBuilder.append(param);
    }

    @Override
    public String getJobName() {
        return "FFmpeg record job";
    }
}
