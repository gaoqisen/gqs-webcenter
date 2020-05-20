package com.github.gaoqisen.webcenter.pojo;


public class WebCenterConsole {

    private String host;
    private String port;
    private String clientId;
    private String secretKey;
    private String currentApplicationName;



    @Override
    public String toString() {
        return "WebCenterConsole{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", clientId='" + clientId + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", currentApplicationName='" + currentApplicationName + '\'' +
                '}';
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
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
