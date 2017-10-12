package com.hust.grid.weixin;

import com.hust.grid.bean.WeatherInfo;
import com.hust.grid.cache.ConstantCacheCenter;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WeiXinSender {
    private Logger logger = LoggerFactory.getLogger(WeiXinSender.class);

    private static final String PREFIX = "https://sc.ftqq.com/";
    private ConstantCacheCenter constantCacheCenter = ConstantCacheCenter.getInstance();

    private WeatherInfo weatherInfo;

    public WeiXinSender(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public void sendToAll() {
        List<String> receiverKeys = constantCacheCenter.getWeixinReceiverKeysList();
        logger.info("receiverKeys " + receiverKeys);
        for (String key : receiverKeys) {
            send(key);
        }
    }

    private void send(String key) {
        CloseableHttpClient client = null;
        HttpPost post;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(PREFIX);
        stringBuffer.append(key);
        stringBuffer.append(".send");

        try {
            client = HttpClients.custom().setRetryHandler(DefaultHttpRequestRetryHandler.INSTANCE).build();
            RequestConfig config = RequestConfig
                    .custom()
                    .setConnectionRequestTimeout(3000)
                    .setConnectTimeout(3000)
                    .setSocketTimeout(30 * 60 * 1000)
                    .build();


            String text = "明日天气情况";
            String desp = weatherInfo.getWeixinFormatString();

            List<BasicNameValuePair> postDatas = new ArrayList<>();
            postDatas.add(new BasicNameValuePair("text", text));
            postDatas.add(new BasicNameValuePair("desp", desp));

            logger.info("url is " + stringBuffer.toString());
            post = new HttpPost(stringBuffer.toString());
            post.setConfig(config);
            post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            post.setHeader("Accept-Encoding", "gzip, deflate, br");
            post.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            post.setHeader("Cache-Control", "max-age=0");
            post.setHeader("Connection", "keep-alive");
            post.setHeader("Host", "sc.ftqq.com");
            post.setHeader("Upgrade-Insecure-Requests", "1");
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postDatas , Consts.UTF_8) ;
            post.setEntity(formEntity);
            CloseableHttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                logger.info("call the cgi success");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    //logger.error("close client error " + e.getMessage());
                }
            }
        }
    }
}
