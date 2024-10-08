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
        final String mdName = yjops.getSpec().getMd();

        Deployment deployment = null;

        if (mdName.equals("jenkins")){
            deployment = new DeploymentBuilder()
                    .editMetadata()
                    .withName(mdName + "-deployment")
                    .withNamespace(yjopsMetadata.getNamespace())
                    .addToLabels("app", mdName)
                    .endMetadata()
                    .editSpec()
                    .withReplicas(1)
                    .withProgressDeadlineSeconds(600)
                    .withRevisionHistoryLimit(10)
                    .withSelector(new LabelSelectorBuilder()
                            .addToMatchLabels("app", mdName)
                            .build())
                    .withTemplate(new PodTemplateSpecBuilder()
                            .editMetadata()
                            .addToLabels("app", mdName)
                            .endMetadata()
                            .editSpec()
                            .withContainers(
                                    new ContainerBuilder()
                                        .withName(mdName + "-container")
                                        .withImage("jenkins/jenkins:lts")
                                        .withImagePullPolicy("IfNotPresent")
                                        .addToPorts(
                                                new ContainerPortBuilder()
                                                        .withName("http")
                                                        .withContainerPort(8080)
                                                        .withProtocol("TCP")
                                                        .build(),
                                                new ContainerPortBuilder()
                                                        .withName("jnlp")
                                                        .withContainerPort(50000)
                                                        .withProtocol("TCP")
                                                        .build()
                                        )
                                        .withTerminationMessagePath("/dev/termination-log")
                                        .withTerminationMessagePolicy("File")
                                        .addNewVolumeMount()
                                            .withName("jenkins-volume")
                                            .withMountPath("/var/jenkins_home")
                                        .endVolumeMount()
                                        .addNewVolumeMount()
                                            .withName("jenkins-log")
                                            .withMountPath("/var/logs")
                                        .endVolumeMount()
                                        .addNewVolumeMount()
                                            .withName("docker-socket")
                                            .withMountPath("/var/run")
                                        .endVolumeMount()
                                        .build(),
                                    new ContainerBuilder()
                                        .withName(mdName + "-docker")
                                        .withImage("docker:dind")
                                        .withImagePullPolicy("IfNotPresent")
                                        .addNewVolumeMount()
                                            .withName("docker-socket")
                                            .withMountPath("/var/run")
                                        .endVolumeMount()
                                        .build())
                            .withVolumes(
                                    new VolumeBuilder()
                                            .withName("jenkins-vol")
                                            .withNewPersistentVolumeClaim()
                                                .withClaimName("jenkins-data-pvc")
                                            .endPersistentVolumeClaim()
                                            .build(),
                                    new VolumeBuilder()
                                            .withName("jenkins-log")
                                            .withNewPersistentVolumeClaim()
                                                .withClaimName("jenkins-log-pvc")
                                            .endPersistentVolumeClaim()
                                            .build(),
                                    new VolumeBuilder()
                                            .withName("docker-socket")
                                            .withNewEmptyDir()
                                            .endEmptyDir()
                                            .build(),
                                    new VolumeBuilder()
                                            .withName("timezone-seoul")
                                            .withNewHostPath()
                                            .withPath("/usr/share/zoneinfo/Asia/Seoul")
                                            .withType("")
                                            .endHostPath()
                                            .build()
                            )
                            .withDnsPolicy("ClusterFirst")
                            .withRestartPolicy("Always")
                            .withSchedulerName("default-scheduler")
                            .withNewSecurityContext()
                                .withRunAsGroup(0L)
                            .endSecurityContext()
                            .withTerminationGracePeriodSeconds(30L)
                            .endSpec()
                            .build())
                    .endSpec()
                    .build();
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
                                    .withImage("gitlab/gitlab-ce")
                                    .addToPorts(
                                            new ContainerPortBuilder()
                                                    .withContainerPort(80)
                                                    .build(),
                                            new ContainerPortBuilder()
                                                    .withContainerPort(443)
                                                    .build(),
                                            new ContainerPortBuilder()
                                                    .withContainerPort(22)
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
                                                .withClaimName("gitlab-config-pvc")
                                            .endPersistentVolumeClaim()
                                            .build(),
                                    new VolumeBuilder()
                                            .withName("gitlab-data-volume")
                                                .withNewPersistentVolumeClaim()
                                            .withClaimName("gitlab-data-pvc")
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