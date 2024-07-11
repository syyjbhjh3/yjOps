package com.example.yjopsoperator;

import com.example.yjopsoperator.customresources.YjOps;
import com.example.yjopsoperator.dependentresources.YjOpsNamespaceResource;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;

@ControllerConfiguration(
        dependents = {
                @Dependent(type = YjOpsNamespaceResource.class)
        })
public class YjOpsReconciler implements Reconciler<YjOps>, ErrorStatusHandler<YjOps>, Cleaner<YjOps> {

    @Override
    public UpdateControl<YjOps> reconcile(YjOps yjOps, Context<YjOps> context) {
        return UpdateControl.updateResourceAndPatchStatus(yjOps);
    }

    @Override
    public DeleteControl cleanup(YjOps yjOps, Context<YjOps> context) {
        return DeleteControl.defaultDelete();
    }

    @Override
    public ErrorStatusUpdateControl<YjOps> updateErrorStatus(YjOps yjOps, Context<YjOps> context, Exception e) {
        return ErrorStatusUpdateControl.patchStatus(yjOps);
    }

}