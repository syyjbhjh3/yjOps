package com.example.yjopsoperator.customresources;

import java.util.List;

// Lombok @Data로도 적용 가능
public class YjOpsSpec {
    private String repository;
    private String namespace;
    private String istio;
    private List<Chart> chart;

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
    public List<Chart> getChart() {
        return chart;
    }
    public void setChart(List<Chart> chart) {
        this.chart = chart;
    }

    public static class Chart{
        private String name;
        private String chartName;
        private Integer nodePort;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getChartName() {
            return chartName;
        }
        public void setChartName(String chartName) {
            this.chartName = chartName;
        }
        public Integer getNodePort() {
            return nodePort;
        }
        public void setNodePort(Integer nodePort) {
            this.nodePort = nodePort;
        }
    }


}
