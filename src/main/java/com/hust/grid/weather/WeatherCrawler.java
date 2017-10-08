package com.hust.grid.weather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.hust.grid.bean.WeatherInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherCrawler {
    private Logger logger = LoggerFactory.getLogger(WeatherCrawler.class);

    public WeatherInfo crawlWeather(String url) {
        CloseableHttpClient client = null;
        HttpGet get;
        WeatherInfo weatherInfo = null;
        try {
            client = HttpClients.custom().setRetryHandler(DefaultHttpRequestRetryHandler.INSTANCE).build();

            RequestConfig config = RequestConfig
                    .custom()
                    .setConnectionRequestTimeout(3000)
                    .setConnectTimeout(3000)
                    .setSocketTimeout(30 * 60 * 1000)
                    .build();

            get = new HttpGet(url);
            get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            get.setHeader("Accept-Encoding", "gzip, deflate");
            get.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            get.setHeader("Host", "www.weather.com.cn");
            get.setHeader("Proxy-Connection", "keep-alive");
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");

            get.setConfig(config);
            CloseableHttpResponse response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity, "utf8");
                logger.debug("content =====>" + content);
                if (content != null)
                    weatherInfo = parseResult(content);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                   logger.error("close client error " + e.getMessage());
                }
            }
        }


        return weatherInfo;
    }

    private WeatherInfo parseResult(String content) {
        Document document = Jsoup.parse(content);
        Element element = document.getElementById("7d");
        Elements elements = element.getElementsByTag("ul");
        Element clearFix = elements.get(0);
        Elements lis = clearFix.getElementsByTag("li");
        // 7 days weather info, we just take tomorrow weather info
        Element tomorrow = lis.get(1);
        logger.debug("tomorrow =====> " + tomorrow);
        return parseWeatherInfo(tomorrow);

    }

    private WeatherInfo parseWeatherInfo(Element element) {
        Elements weathers = element.getElementsByTag("p");
        String weather = weathers.get(0).text();
        String temp = weathers.get(1).text();
        String wind = weathers.get(2).text();

        WeatherInfo weatherInfo = new WeatherInfo(weather, temp, wind);

        logger.info("---------------------------------------------------------------------------------");
        logger.info("---------------------------------------------------------------------------------");
        logger.info("weather is " + weather);
        logger.info("temp is " + temp);
        logger.info("wind is " + wind);
        logger.info("---------------------------------------------------------------------------------");
        logger.info("---------------------------------------------------------------------------------");

        return weatherInfo;
    }

    public static void main(String[] args) {
        WeatherCrawler crawlWeatherInfo = new WeatherCrawler();
        crawlWeatherInfo.crawlWeather("http://www.weather.com.cn/weather/101280601.shtml");
    }
}
