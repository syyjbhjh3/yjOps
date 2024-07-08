package com.example.yjopsoperator.customresources;

// Lombok @Data로도 적용 가능
public class PetclinicSpec {
    private String image;
    private Integer size;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    private Integer port;
}
