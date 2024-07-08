package com.example.yjopsoperator.dependentresources;

import com.example.yjopsoperator.customresources.Petclinic;
import io.fabric8.kubernetes.api.model.*;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent
public class PetclinicServiceResource extends CRUDKubernetesDependentResource<Service, Petclinic> {

    public PetclinicServiceResource() {
        super(Service.class);
    }

    @Override
    protected Service desired(Petclinic petclinic, Context<Petclinic> context) {
        final ObjectMeta petclinicMetadata = petclinic.getMetadata();
        final String petclinicName = petclinicMetadata.getName();

        return new ServiceBuilder()
                .editMetadata()
                .withName(petclinicName)
                .withNamespace(petclinicMetadata.getNamespace())
                .addToLabels("app", petclinicName)
                .endMetadata()
                .editSpec()
                .withType("NodePort")
                .addToSelector("app", petclinicName)
                .addToPorts(new ServicePortBuilder().withName("http").withPort(petclinic.getSpec().getPort()).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(petclinic.getSpec().getPort()).build()).build())
                .endSpec()
                .build();
    }
}