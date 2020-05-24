package com.github.gaoqisen.webcenter.pojo;


import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

public class WebCenterConsole {

    private String host;

    private Integer port;

    private String clientId;

    private String secretKey;

    private String currentApplicationName;

    private Boolean forestage;

    private String forestageHost;

    private Integer forestagePort;

    public WebCenterConsole() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties properties=yaml.getObject();
        this.host = properties.getProperty("webcenter.server.host");
        this.port = Integer.valueOf(properties.getProperty("webcenter.server.port"));
        this.clientId = properties.getProperty("webcenter.server.clientid");
        this.secretKey = properties.getProperty("webcenter.server.secretkey");
        this.currentApplicationName = properties.getProperty("spring.application.name");
        this.forestage = Boolean.valueOf(properties.getProperty("webcenter.client.forestage"));
        this.forestageHost = properties.getProperty("webcenter.client.host");
        this.forestagePort = Integer.valueOf(properties.getProperty("webcenter.client.port"));
    }

    @Override
    public String toString() {
        return "WebCenterConsole{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", clientId='" + clientId + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", currentApplicationName='" + currentApplicationName + '\'' +
                ", forestage=" + forestage +
                ", forestageHost='" + forestageHost + '\'' +
                ", forestagePort='" + forestagePort + '\'' +
                '}';
    }

    public Boolean getForestage() {
        return forestage;
    }

    public void setForestage(Boolean forestage) {
        this.forestage = forestage;
    }

    public String getForestageHost() {
        return forestageHost;
    }

    public void setForestageHost(String forestageHost) {
        this.forestageHost = forestageHost;
    }

    public Integer getForestagePort() {
        return forestagePort;
    }

    public void setForestagePort(Integer forestagePort) {
        this.forestagePort = forestagePort;
    }

    public String getCurrentApplicationName() {
        return currentApplicationName;
    }

    public void setCurrentApplicationName(String currentApplicationName) {
        this.currentApplicationName = currentApplicationName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
