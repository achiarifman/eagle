package com.eagle.jobs;

import java.util.concurrent.Callable;

/**
 * Created by Achia.Rifman on 28/06/2014.
 */
public abstract class AbstractJob implements Callable<Boolean> {


    public abstract String getJobName();
}
