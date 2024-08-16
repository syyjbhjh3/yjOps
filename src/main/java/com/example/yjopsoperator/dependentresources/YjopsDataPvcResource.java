package com.example.yjopsoperator.dependentresources;

import com.example.yjopsoperator.customresources.Yjops;
import com.example.yjopsoperator.dependentresources.multiplePvc.PersistentVolumeClaim1Discriminator;
import com.example.yjopsoperator.dependentresources.multiplePvc.PersistentVolumeClaim2Discriminator;
import io.fabric8.kubernetes.api.model.*;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent(resourceDiscriminator = PersistentVolumeClaim2Discriminator.class)
public class YjopsDataPvcResource extends CRUDKubernetesDependentResource<PersistentVolumeClaim, Yjops> {
    public static final String NAME_SUFFIX = "-2";
    public YjopsDataPvcResource() {
        super(PersistentVolumeClaim.class);
    }

    @Override
    protected PersistentVolumeClaim desired(Yjops yjops, Context<Yjops> context) {
        final ObjectMeta yjopsMetadata = yjops.getMetadata();
        final String mdName = yjops.getSpec().getMd();

        return new PersistentVolumeClaimBuilder()
                .editMetadata()
                .withName(mdName + "-data-pvc")
                .withNamespace(yjopsMetadata.getNamespace())
                .endMetadata()
                .withNewSpec()
                .withStorageClassName(yjops.getSpec().getStorageClassName())
                .withAccessModes("ReadWriteMany")
                .withNewResources()
                .addToRequests("storage", new Quantity("30Gi"))
                .endResources()
                .endSpec()
                .build();
    }
}