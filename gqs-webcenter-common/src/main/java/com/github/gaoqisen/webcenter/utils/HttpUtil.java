package com.github.gaoqisen.webcenter.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    public static String hostPortSendPost(String host, Integer port, String context, String params) {
        return sendPost(urlJoint(host, port, context), params);
    }

    public static String urlJoint(String host, Integer port, String context) {
        return "http://".concat(host).concat(":").concat(String.valueOf(port)).concat(context);
    }

    public static String sendPost(String url, String params) {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response = "";
        try {
            //创建URL
            URL httpUrl = new URL(url);
            //建立连接\
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("connection", "keep-alive");
            //设置不要缓存
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            //POST请求
            out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(params);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response += lines;
            }
            reader.close();
            // 断开连接
            httpURLConnection.disconnect();
        } catch (Exception e) {
            return null;
        } finally{
            try{
                if(out != null){
                    out.close();
                }
                if(reader != null){
                    reader.close();
                }
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return response;
    }

}
