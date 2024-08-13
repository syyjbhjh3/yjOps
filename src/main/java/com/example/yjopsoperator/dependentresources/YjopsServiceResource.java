package com.example.yjopsoperator.dependentresources;

import com.example.yjopsoperator.customresources.Yjops;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
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

        Service service = null;

        if (mdName.equals("jenkins")){

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
                    .addToPorts(new ServicePortBuilder().withName("http").withPort(80).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(80).build()).build(),
                                new ServicePortBuilder().withName("httpd").withPort(443).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(443).build()).build(),
                                new ServicePortBuilder().withName("ssh").withPort(22).withProtocol("TCP").withTargetPort(new IntOrStringBuilder().withValue(22).build()).build())
                    .endSpec()
                    .build();
        }

        return service;
    }
}
