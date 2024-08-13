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
        final String mdName = yjops.getSpec().getMd();

        Deployment deployment = null;

        if (mdName.equals("jenkins")){

        } else if (mdName.equals("gitlab")) {
            deployment = new DeploymentBuilder()
                    .editMetadata()
                    .withName(mdName + "-deployment")
                    .withNamespace(yjopsMetadata.getNamespace())
                    .addToLabels("app", mdName)
                    .endMetadata()
                    .editSpec()
                    .withReplicas(1)
                    .withSelector(new LabelSelectorBuilder()
                            .addToMatchLabels("app", mdName)
                            .build())
                    .withTemplate(new PodTemplateSpecBuilder()
                            .editMetadata()
                            .addToLabels("app", mdName)
                            .endMetadata()
                            .editSpec()
                            .withContainers(new ContainerBuilder()
                                    .withName(mdName + "-container")
                                    .withImage(yjops.getSpec().getImage())
                                    .addToPorts(
                                            new ContainerPortBuilder()
                                                    .withContainerPort(yjops.getSpec().getPort())
                                                    .build(),
                                            new ContainerPortBuilder()
                                                    .withContainerPort(yjops.getSpec().getPort())
                                                    .build(),
                                            new ContainerPortBuilder()
                                                    .withContainerPort(yjops.getSpec().getPort())
                                                    .build()
                                            )
                                    .addNewVolumeMount()
                                    .withName("gitlab-config-volume")
                                    .withMountPath("/etc/gitlab")
                                    .endVolumeMount()
                                    .addNewVolumeMount()
                                    .withName("gitlab-data-volume")
                                    .withMountPath("/var/opt/gitlab")
                                    .endVolumeMount()
                                    .build())
                            .withVolumes(new VolumeBuilder()
                                            .withName("gitlab-config-volume")
                                            .withNewPersistentVolumeClaim()
                                            .withClaimName("gitlab-config")
                                            .endPersistentVolumeClaim()
                                            .build(),
                                    new VolumeBuilder()
                                            .withName("gitlab-data-volume")
                                            .withNewPersistentVolumeClaim()
                                            .withClaimName("gitlab-data")
                                            .endPersistentVolumeClaim()
                                            .build()
                            )
                            .endSpec()
                            .build())
                    .endSpec()
                    .build();
        }

        return deployment;
    }
}