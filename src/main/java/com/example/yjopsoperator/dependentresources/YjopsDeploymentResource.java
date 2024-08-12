package com.example.yjopsoperator.dependentresources;

import com.example.yjopsoperator.customresources.Yjops;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent
public class YjopsDeploymentResource extends CRUDKubernetesDependentResource<Deployment, Yjops> {

    public YjopsDeploymentResource() {
        super(Deployment.class);
    }

    @Override
    protected Deployment desired(Yjops yjops, Context<Yjops> context) {
        final ObjectMeta yjopsMetadata = yjops.getMetadata();
        final String yjopsName = yjopsMetadata.getName();

        return new DeploymentBuilder()
                .editMetadata()
                .withName(yjopsName)
                .withNamespace(yjopsMetadata.getNamespace())
                .addToLabels("app", yjopsName)
                .endMetadata()
                .editSpec()
                .withSelector(new LabelSelectorBuilder()
                        .addToMatchLabels("app", yjopsName)
                        .build())
                .withReplicas(1)
                .withTemplate(new PodTemplateSpecBuilder()
                        .editMetadata()
                        .addToLabels("app", yjopsName)
                        .endMetadata()
                        .editSpec()
                        .withContainers(new ContainerBuilder()
                                .withName(yjopsName + "-container")
                                .withImage(yjops.getSpec().getImage())
                                .addToPorts(new ContainerPortBuilder()
                                        .withContainerPort(yjops.getSpec().getPort())
                                        .build())
                                .build())
                        .endSpec()
                        .build())
                .endSpec()
                .build();
    }
}