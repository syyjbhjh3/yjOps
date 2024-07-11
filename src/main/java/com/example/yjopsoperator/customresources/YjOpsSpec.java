package com.example.yjopsoperator.customresources;

import java.util.List;

// Lombok @Data로도 적용 가능
public class YjOpsSpec {
    private String repository;
    private String namespace;
    private String istio;
    private Chart jenkins;
    private Chart argocd;
    private Chart gitlab;

    public String getRepository() {
        return repository;
    }
    public void setRepository(String repository) {
        this.repository = repository;
    }
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public String getIstio() { return istio; }
    public void setIstio(String istio) {
        this.istio = istio;
    }
    public Chart getJenkins() {
        return jenkins;
    }
    public void setJenkins(Chart jenkins) {
        this.jenkins = jenkins;
    }
    public Chart getArgocd() {
        return argocd;
    }
    public void setArgocd(Chart argocd) {
        this.argocd = argocd;
    }
    public Chart getGitlab() {
        return gitlab;
    }
    public void setGitlab(Chart gitlab) {
        this.gitlab = gitlab;
    }


    public static class Chart {
        private String valuePath;
        private String path;

        public String getValuePath() {
            return valuePath;
        }
        public void setValuePath(String valuePath) {
            this.valuePath = valuePath;
        }
        public String getPath() {
            return path;
        }
        public void setPath(String path) {
            this.path = path;
        }
    }
}
