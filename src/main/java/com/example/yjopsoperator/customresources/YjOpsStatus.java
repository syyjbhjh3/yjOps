package com.example.yjopsoperator.customresources;

import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;

/*
 * Custom Resource에 대한 변경사항을 추적할 수 있도록
 * Petclinic 오브젝트가 매번 변경될 때마다 Petclinic CR 내에 observedGeneration status 값을 증가시킵니다.
 */
public class YjOpsStatus extends ObservedGenerationAwareStatus {
}
