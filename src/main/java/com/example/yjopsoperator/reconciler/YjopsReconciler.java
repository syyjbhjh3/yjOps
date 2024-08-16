package com.example.yjopsoperator.reconciler;

import com.example.yjopsoperator.customresources.Yjops;
import com.example.yjopsoperator.dependentresources.YjopsDataPvcResource;
import com.example.yjopsoperator.dependentresources.YjopsDeploymentResource;
import com.example.yjopsoperator.dependentresources.YjopsPvcResource;
import com.example.yjopsoperator.dependentresources.YjopsServiceResource;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.javaoperatorsdk.operator.api.config.informer.InformerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import io.javaoperatorsdk.operator.processing.event.source.EventSource;
import io.javaoperatorsdk.operator.processing.event.source.informer.InformerEventSource;

import java.io.IOException;
import java.util.Map;

import static com.example.yjopsoperator.reconciler.YjopsReconciler.PVC_EVENT_SOURCE;


@ControllerConfiguration(
        dependents = {
                @Dependent(type = YjopsDeploymentResource.class),
                @Dependent(type = YjopsServiceResource.class),
                @Dependent(type = YjopsPvcResource.class, useEventSourceWithName = PVC_EVENT_SOURCE),
                @Dependent(type = YjopsDataPvcResource.class, useEventSourceWithName = PVC_EVENT_SOURCE)
        })
public class YjopsReconciler implements Reconciler<Yjops>, ErrorStatusHandler<Yjops>, Cleaner<Yjops>, EventSourceInitializer<Yjops> {

    public static final String PVC_EVENT_SOURCE = "PersistentVolumeClaimEventSource";
    public YjopsReconciler() {}

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

    @Override
    public Map<String, EventSource> prepareEventSources(
            EventSourceContext<Yjops> context) {
        InformerEventSource<PersistentVolumeClaim, Yjops> ies =
                new InformerEventSource<>(InformerConfiguration.from(PersistentVolumeClaim.class, context)
                        .build(), context);

        return Map.of(PVC_EVENT_SOURCE, ies);
    }
}