package com.example.yjopsoperator;

import com.example.yjopsoperator.customresources.Petclinic;
import com.example.yjopsoperator.dependentresources.PetclinicDeploymentResource;
import com.example.yjopsoperator.dependentresources.PetclinicServiceResource;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;

@ControllerConfiguration(
        dependents = {
                @Dependent(type = PetclinicDeploymentResource.class),
                @Dependent(type = PetclinicServiceResource.class)
        })
public class PetclinicReconciler implements Reconciler<Petclinic>, ErrorStatusHandler<Petclinic>, Cleaner<Petclinic> {

    @Override
    public UpdateControl<Petclinic> reconcile(Petclinic petclinic, Context<Petclinic> context) {
        return UpdateControl.updateResourceAndPatchStatus(petclinic);
    }

    @Override
    public DeleteControl cleanup(Petclinic petclinic, Context<Petclinic> context) {
        return DeleteControl.defaultDelete();
    }

    @Override
    public ErrorStatusUpdateControl<Petclinic> updateErrorStatus(Petclinic petclinic, Context<Petclinic> context, Exception e) {
        return ErrorStatusUpdateControl.patchStatus(petclinic);
    }

}