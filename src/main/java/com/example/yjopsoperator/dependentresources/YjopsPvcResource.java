package com.example.yjopsoperator.dependentresources;

import com.example.yjopsoperator.customresources.Yjops;
import com.example.yjopsoperator.dependentresources.multiplePvc.PersistentVolumeClaim1Discriminator;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent(resourceDiscriminator = PersistentVolumeClaim1Discriminator.class)
public class YjopsPvcResource extends CRUDKubernetesDependentResource<PersistentVolumeClaim, Yjops> {
    public static final String NAME_SUFFIX = "-1";
    public YjopsPvcResource() {
        super(PersistentVolumeClaim.class);
    }

    @Override
    protected PersistentVolumeClaim desired(Yjops yjops, Context<Yjops> context) {
        final ObjectMeta yjopsMetadata = yjops.getMetadata();
        final String mdName = yjops.getSpec().getMd();

        String pvcName = "", size = "";

        if(mdName.equals("jenkins")){
            pvcName = mdName + "-log-pvc";
            size = "1Gi";
        }else if(mdName.equals("gitlab")){
            pvcName = mdName + "-config-pvc";
            size = "10Gi";
        }

        return new PersistentVolumeClaimBuilder()
                .editMetadata()
                .withName(pvcName)
                .withNamespace(yjopsMetadata.getNamespace())
                .endMetadata()
                .withNewSpec()
                .withStorageClassName(yjops.getSpec().getStorageClassName())
                .withAccessModes("ReadWriteMany")
                .withNewResources()
                .addToRequests("storage", new Quantity(size))
                .endResources()
                .endSpec()
                .build();
    }
}