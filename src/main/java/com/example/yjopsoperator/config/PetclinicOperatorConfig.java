package com.example.yjopsoperator.config;

import com.example.yjopsoperator.PetclinicReconciler;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PetclinicOperatorConfig {

    @Bean
    public PetclinicReconciler petclinicReconciler() {
        return new PetclinicReconciler();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Operator operator(List<Reconciler<?>> controllers) {
        Operator operator = new Operator();
        controllers.forEach(operator::register);
        return operator;
    }
}