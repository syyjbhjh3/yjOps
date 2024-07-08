package com.example.yjopsoperator.dependentresources;

import com.example.yjopsoperator.customresources.Petclinic;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

/*
 * KubernetesDependent 어노테이션을 통해 Petclinic CR의 변화에 대응하여 해당 k8s 리소스의 수명 주기를 관리합니다.
 * Deployment
 */
@KubernetesDependent
public class PetclinicDeploymentResource extends CRUDKubernetesDependentResource<Deployment, Petclinic> {

    public PetclinicDeploymentResource() {
        super(Deployment.class);
    }

    @Override
    protected Deployment desired(Petclinic petclinic, Context<Petclinic> context) {
        final ObjectMeta petclinicMetadata = petclinic.getMetadata();
        final String petclinicName = petclinicMetadata.getName();

        return new DeploymentBuilder()
                .editMetadata()
                .withName(petclinicName)
                .withNamespace(petclinicMetadata.getNamespace())
                .addToLabels("app", petclinicName)
                .endMetadata()
                .editSpec()
                .withSelector(new LabelSelectorBuilder()
                        .addToMatchLabels("app", petclinicName)
                        .build())
                .withReplicas(petclinic.getSpec().getSize())
                .withTemplate(new PodTemplateSpecBuilder()
                        .editMetadata()
                        .addToLabels("app", petclinicName)
                        .endMetadata()
                        .editSpec()
                        .withContainers(new ContainerBuilder()
                                .withName(petclinicName + "-container")
                                .withImage(petclinic.getSpec().getImage())
                                .addToPorts(new ContainerPortBuilder()
                                        .withContainerPort(petclinic.getSpec().getPort())
                                        .build())
                                .build())
                        .endSpec()
                        .build())
                .endSpec()
                .build();
    }
}