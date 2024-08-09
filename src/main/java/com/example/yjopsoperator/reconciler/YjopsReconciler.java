package com.example.yjopsoperator.reconciler;

import com.example.yjopsoperator.customresources.Yjops;
import com.example.yjopsoperator.dependentresources.YjopsNamespaceResource;
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
                @Dependent(type = YjopsNamespaceResource.class)
        })
public class YjopsReconciler implements Reconciler<Yjops>, ErrorStatusHandler<Yjops>, Cleaner<Yjops> {

    @Override
    public UpdateControl<Yjops> reconcile(Yjops yjOps, Context<Yjops> context) throws IOException, InterruptedException {
        String repository = yjOps.getSpec().getRepository();
        executeHelmCommand("helm", "repo", "add", repository);

        deployHelmChart(yjOps, context);
        return UpdateControl.updateResourceAndPatchStatus(yjOps);
    }

    @Override
    public DeleteControl cleanup(Yjops yjOps, Context<Yjops> context) {
        return DeleteControl.defaultDelete();
    }

    @Override
    public ErrorStatusUpdateControl<Yjops> updateErrorStatus(Yjops yjOps, Context<Yjops> context, Exception e) {
        return ErrorStatusUpdateControl.patchStatus(yjOps);
    }

    private void deployHelmChart(Yjops yjOps, Context<Yjops> context) throws IOException, InterruptedException {
        String namespace = yjOps.getSpec().getNamespace();

        String jenkins = getValueSafely(() -> yjOps.getSpec().getJenkins().getPath());
        String argocd = getValueSafely(() -> yjOps.getSpec().getArgocd().getPath());
        String gitlab = getValueSafely(() -> yjOps.getSpec().getGitlab().getPath());

        String jenkins_valuse = getValueSafely(() -> yjOps.getSpec().getJenkins().getValuePath());
        String argocd_valuse = getValueSafely(() -> yjOps.getSpec().getArgocd().getValuePath());
        String gitlab_valuse = getValueSafely(() -> yjOps.getSpec().getGitlab().getValuePath());

        if(jenkins_valuse != null) {
            executeHelmCommand("helm", "install", jenkins, "-f", jenkins_valuse, "-n", namespace);
        }else{
            executeHelmCommand("helm", "install", jenkins, "-n", namespace);
        }

        if(argocd_valuse != null) {
            executeHelmCommand("helm", "install", argocd, "-f", argocd_valuse, "-n", namespace);
        }else {
            executeHelmCommand("helm", "install", argocd, "-n", namespace);
        }

        if(gitlab_valuse != null) {
            executeHelmCommand("helm", "install", gitlab, "-f", gitlab_valuse, "-n", namespace);
        }else{
            executeHelmCommand("helm", "install", gitlab, "-n", namespace);
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