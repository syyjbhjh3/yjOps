package com.example.yjopsoperator.dependentresources.multiplePvc;

import java.util.Optional;

import com.example.yjopsoperator.customresources.Yjops;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ResourceDiscriminator;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import io.javaoperatorsdk.operator.processing.event.source.informer.InformerEventSource;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;

import static com.example.yjopsoperator.dependentresources.YjopsPvcResource.NAME_SUFFIX;

public class PersistentVolumeClaim1Discriminator implements ResourceDiscriminator<PersistentVolumeClaim, Yjops>{
    @Override
    public Optional<PersistentVolumeClaim> distinguish(Class<PersistentVolumeClaim> resource, Yjops yjops, Context<Yjops> context) {
        InformerEventSource<PersistentVolumeClaim, Yjops> ies =
                (InformerEventSource<PersistentVolumeClaim, Yjops>) context
                        .eventSourceRetriever().getResourceEventSourceFor(PersistentVolumeClaim.class);

        return ies.get(new ResourceID(yjops.getMetadata().getName() + NAME_SUFFIX,
                yjops.getMetadata().getNamespace()));
    }
}