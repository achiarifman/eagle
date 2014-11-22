package com.eagle.commons;


import com.eagle.dao.queries.RecordsDao;
import com.eagle.dao.queries.StatusDao;
import com.eagle.dao.queries.UploadDao;
import com.eagle.entity.EagleStatusEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Achia.Rifman on 22/08/2014.
 */
@Component(BeansNames.PHASE_MANAGER)
@Scope("prototype")
public class PhasesManager implements Callable<Boolean> {


    private ExecutorService executor;

    private Future<Boolean> currentFutureTask;

    EagleStatusEntity eagleStatusEntity;

    List<AbstractPhase> phases;

    @Autowired
    StatusDao statusDao;

    @Autowired
    RecordsDao recordsDao;

    @Autowired
    UploadDao uploadDao;

    public void setPhases(List<AbstractPhase> phases){

        this.phases = phases;
    }



    @Override
    public Boolean call() throws Exception {
        executor = Executors.newFixedThreadPool(1);

        for (AbstractPhase phase : phases){
            currentFutureTask = executor.submit(phase);
            try {
                boolean flag = currentFutureTask.get();

                if(flag != true){
                    throw new RuntimeException("PhasesManager failed");
                }
                onPhaseFinished(phase);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void onPhaseFinished(AbstractPhase phase){

        eagleStatusEntity.setFinishedPhase(phase.getPhaseId());
        switch (phase.getPHASE_NAME()){
            case RECORD:
                recordsDao.updateFinishedPhase(phase.getPhaseId());
                break;
            case UPLOAD:
                uploadDao.updateFinishedPhase(phase.getPhaseId());
                break;
            default:
                break;

        }
        statusDao.updateFinishedPhase(eagleStatusEntity.getId(),phase.getPhaseId());

    }

    public EagleStatusEntity getEagleStatusEntity() {
        return eagleStatusEntity;
    }

    public void setEagleStatusEntity(EagleStatusEntity eagleStatusEntity) {
        this.eagleStatusEntity = eagleStatusEntity;
    }
}
