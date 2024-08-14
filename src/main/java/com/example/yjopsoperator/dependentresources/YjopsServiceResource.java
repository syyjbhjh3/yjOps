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
        final String mdName = yjops.getSpec().getMd();

        /* Required Port */
        final int httpPort = 80;
        final int httpsPort = 443;
        final int targetPort = 8080;
        final int sshPort = 22;
        /* Custom NodePort */
        int jenkinsNodePort = 31010;
        int gitlabNodePort = 31020;

        Service service = null;

        if (mdName.equals("jenkins")){
            service = new ServiceBuilder()
                    .editMetadata()
                    .withName(mdName + "-service")
                    .withNamespace(yjopsMetadata.getNamespace())
                    .addToLabels("app", mdName)
                    .endMetadata()
                    .editSpec()
                    .withType("NodePort")
                    .addToSelector("app", mdName)
                    .addToPorts(new ServicePortBuilder().withName("http").withPort(httpPort).withNodePort(jenkinsNodePort).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(targetPort).build()).build(),
                                new ServicePortBuilder().withName("https").withPort(httpsPort).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(targetPort).build()).build())
                    .endSpec()
                    .build();
        } else if (mdName.equals("gitlab")) {
            service = new ServiceBuilder()
                    .editMetadata()
                    .withName(mdName + "-service")
                    .withNamespace(yjopsMetadata.getNamespace())
                    .addToLabels("app", mdName)
                    .endMetadata()
                    .editSpec()
                    .withType("NodePort")
                    .addToSelector("app", mdName)
                    .addToPorts(new ServicePortBuilder().withName("http").withPort(httpPort).withNodePort(gitlabNodePort).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(httpPort).build()).build(),
                                new ServicePortBuilder().withName("httpd").withPort(httpsPort).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(httpsPort).build()).build(),
                                new ServicePortBuilder().withName("ssh").withPort(sshPort).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(sshPort).build()).build())
                    .endSpec()
                    .build();
        }

        return service;
    }
}
