package com.github.gaoqisen.webcenter.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.gaoqisen.webcenter.constant.ServerContextConstant;
import com.github.gaoqisen.webcenter.constant.SysPrompt;
import com.github.gaoqisen.webcenter.core.WebCenterClientBeanFactory;
import com.github.gaoqisen.webcenter.pojo.WebCenterConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class HttpUtil {


    public static <T> List<T> parseArray(String url, HashMap object, Class<T> clazz) {
        JSONObject jsonObject = getWebCenterConsole(url, object);
        return JSONObject.parseArray(jsonObject.getString("data"), clazz);
    }

    public static <T> T parseObject(String url, HashMap object, Class<T> clazz) {
        JSONObject jsonObject = getWebCenterConsole(url, object);
        return JSONObject.parseObject(jsonObject.getString("data"), clazz);
    }

    private static JSONObject getWebCenterConsole(String url, HashMap object) {
        WebCenterConsole webCenterConsole = WebCenterClientBeanFactory.getBean(WebCenterConsole.class);
        object.put("applicationName", webCenterConsole.getCurrentApplicationName());
        String returnStr = hostPortSendPost(webCenterConsole.getHost(), webCenterConsole.getPort(), url, JSON.toJSONString(object));
        if(returnStr == null) {
            throw new RuntimeException(SysPrompt.POST_REQUEST_ERROR);
        }
        JSONObject jsonObject = JSONObject.parseObject(returnStr);
        return jsonObject;
    }

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
