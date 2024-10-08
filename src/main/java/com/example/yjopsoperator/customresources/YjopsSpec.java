package com.example.yjopsoperator.customresources;

// Lombok @Data로도 적용 가능
public class YjopsSpec {
    private String name;
    private String namespace;
    private String storageClassName;
    private String repository;
    private String image;
    private Integer port;
    private Integer nodeport;
    private String md; // Jenkins / Gitlab

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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getMd() {
        return md;
    }
    public void setMd(String md) {
        this.md = md;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public Integer getNodeport() {
        return nodeport;
    }
    public void setNodeport(Integer nodeport) {
        this.nodeport = nodeport;
    }
    public String getStorageClassName() {
        return storageClassName;
    }
    public void setStorageClassName(String storageClassName) {
        this.storageClassName = storageClassName;
    }
}
