package com.example.yjopsoperator.reconciler;

import com.example.yjopsoperator.customresources.YjOps;
import com.example.yjopsoperator.dependentresources.YjOpsNamespaceResource;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@ControllerConfiguration(
        dependents = {
                @Dependent(type = YjOpsNamespaceResource.class)
        })
public class YjOpsReconciler implements Reconciler<YjOps>, ErrorStatusHandler<YjOps>, Cleaner<YjOps> {

    @Override
    public UpdateControl<YjOps> reconcile(YjOps yjOps, Context<YjOps> context) throws IOException, InterruptedException {
        deployHelmChart(yjOps, context);
        return UpdateControl.updateResourceAndPatchStatus(yjOps);
    }

    @Override
    public DeleteControl cleanup(YjOps yjOps, Context<YjOps> context) {
        return DeleteControl.defaultDelete();
    }

    @Override
    public ErrorStatusUpdateControl<YjOps> updateErrorStatus(YjOps yjOps, Context<YjOps> context, Exception e) {
        return ErrorStatusUpdateControl.patchStatus(yjOps);
    }

    private void deployHelmChart(YjOps yjOps, Context<YjOps> context) throws IOException, InterruptedException {
        String namespace = yjOps.getSpec().getNamespace();

        String jenkins = getValueSafely(() -> yjOps.getSpec().getJenkins().getPath());
        String argocd = getValueSafely(() -> yjOps.getSpec().getArgocd().getPath());
        String gitlab = getValueSafely(() -> yjOps.getSpec().getGitlab().getPath());

        String jenkins_valuse = getValueSafely(() -> yjOps.getSpec().getJenkins().getValuePath());
        String argocd_valuse = getValueSafely(() -> yjOps.getSpec().getArgocd().getValuePath());
        String gitlab_valuse = getValueSafely(() -> yjOps.getSpec().getGitlab().getValuePath());

        if(jenkins_valuse != null) {
            executeHelmCommand("helm", "install", jenkins, "-f", jenkins_valuse, "-n", namespace);
        }

        if(argocd_valuse != null) {
            executeHelmCommand("helm", "install", argocd, "-f", argocd_valuse, "-n", namespace);
        }

        if(gitlab_valuse != null) {
            executeHelmCommand("helm", "install", gitlab, "-f", gitlab_valuse, "-n", namespace);
        }
    }

    private <T> T getValueSafely(Supplier<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get()).orElse(null);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private void executeHelmCommand(String... command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
            }
            while ((line = errorReader.readLine()) != null) {
            }

            boolean finished = process.waitFor(10, TimeUnit.MINUTES);
            if (!finished) {
                process.destroy();
                throw new RuntimeException("Helm command timed out");
            }

            if (process.exitValue() != 0) {
                throw new RuntimeException("Helm command failed with exit code " + process.exitValue());
            }
        }
    }
}