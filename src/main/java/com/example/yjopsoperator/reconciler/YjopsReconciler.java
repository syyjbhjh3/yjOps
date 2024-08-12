package com.example.yjopsoperator.reconciler;

import com.example.yjopsoperator.customresources.Yjops;
import com.example.yjopsoperator.dependentresources.YjopsDeploymentResource;
import com.example.yjopsoperator.dependentresources.YjopsPersistentVolumeClaimResource;
import com.example.yjopsoperator.dependentresources.YjopsServiceResource;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;

import java.io.IOException;

@ControllerConfiguration(
        dependents = {
                @Dependent(type = YjopsDeploymentResource.class),
                @Dependent(type = YjopsServiceResource.class),
                @Dependent(type = YjopsPersistentVolumeClaimResource.class)
        })
public class YjopsReconciler implements Reconciler<Yjops>, ErrorStatusHandler<Yjops>, Cleaner<Yjops> {

    @Override
    public UpdateControl<Yjops> reconcile(Yjops yjOps, Context<Yjops> context) throws IOException, InterruptedException {
        return UpdateControl.updateResourceAndPatchStatus(yjOps);
    }

    @Override
    public DeleteControl cleanup(Yjops yjOps, Context<Yjops> context) {
        return DeleteControl.defaultDelete();
    }

    @Override
    public ErrorStatusUpdateControl<Yjops> updateErrorStatus(Yjops yjOps, Context<Yjops> context, Exception e) {
        return ErrorStatusUpdateControl.patchStatus(yjOps);
    }
}