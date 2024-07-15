package com.example.yjopsoperator.customresources;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

/*
 * CustomResource 추상클래스를 확장할 때 PetclinicSpec과 PetclinicStatus 클래스를 참조합니다.
 */
@Group("yjOps-operator.io")
@Version("v1")
public class YjOps extends CustomResource<YjOpsSpec, YjOpsStatus> implements Namespaced {

    @Override
    public String toString() {
        return "YjOps{spec=" + spec + ", status=" + status + "}";
    }
}
