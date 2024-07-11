package com.example.yjopsoperator.dependentresources;

import com.example.yjopsoperator.customresources.YjOps;
import io.fabric8.kubernetes.api.model.*;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent
public class YjOpsNamespaceResource extends CRUDKubernetesDependentResource<Namespace, YjOps> {

    public YjOpsNamespaceResource() {
        super(Namespace.class);
    }

    @Override
    protected Namespace desired(YjOps yjOps, Context<YjOps> context) {
        final String nsName = yjOps.getSpec().getNamespace();
        final String istio_injection = yjOps.getSpec().getIstio();

        return new NamespaceBuilder()
                .withNewMetadata()
                .withName(nsName)
                .addToLabels("istio-injection", istio_injection)
                .endMetadata()
                .build();
    }
}