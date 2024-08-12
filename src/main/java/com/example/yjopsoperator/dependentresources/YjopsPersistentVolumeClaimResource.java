package com.example.yjopsoperator.dependentresources;

import com.example.yjopsoperator.customresources.Yjops;
import io.fabric8.kubernetes.api.model.*;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent
public class YjopsPersistentVolumeClaimResource extends CRUDKubernetesDependentResource<PersistentVolumeClaim, Yjops> {

    public YjopsPersistentVolumeClaimResource() {
        super(PersistentVolumeClaim.class);
    }

    @Override
    protected PersistentVolumeClaim desired(Yjops yjops, Context<Yjops> context) {
        final ObjectMeta yjopsMetadata = yjops.getMetadata();
        final String yjopsName = yjopsMetadata.getName();

        return new PersistentVolumeClaimBuilder()
                .editMetadata()
                .withName(yjopsName + "-pvc")
                .withNamespace(yjopsMetadata.getNamespace())
                .endMetadata()
                .withNewSpec()
                .withStorageClassName(yjops.getSpec().getStorageClassName())
                .withAccessModes("ReadWriteOnce")
                .withNewResources()
                .addToRequests("storage", new Quantity("50Gi"))
                .endResources()
                .endSpec()
                .build();
    }
}