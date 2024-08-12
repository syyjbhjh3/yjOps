package com.example.yjopsoperator.dependentresources;

import com.example.yjopsoperator.customresources.Yjops;
import io.fabric8.kubernetes.api.model.*;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent
public class YjopsServiceResource extends CRUDKubernetesDependentResource<Service, Yjops> {

    public YjopsServiceResource() {
        super(Service.class);
    }

    @Override
    protected Service desired(Yjops yjops, Context<Yjops> context) {
        final ObjectMeta yjopsMetadata = yjops.getMetadata();
        final String yjopsName = yjopsMetadata.getName();

        return new ServiceBuilder()
                .editMetadata()
                .withName(yjopsName)
                .withNamespace(yjopsMetadata.getNamespace())
                .addToLabels("app", yjopsName)
                .endMetadata()
                .editSpec()
                .withType("NodePort")
                .addToSelector("app", yjopsName)
                .addToPorts(new ServicePortBuilder().withName("http").withPort(yjops.getSpec().getPort()).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(yjops.getSpec().getPort()).build()).build())
                .endSpec()
                .build();
    }
}
