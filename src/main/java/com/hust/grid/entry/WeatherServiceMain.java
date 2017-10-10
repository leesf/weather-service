package com.hust.grid.entry;

import com.hust.grid.cache.ConstantCacheCenter;
import com.hust.grid.email.MailSender;
import com.hust.grid.weather.WeatherCrawler;
import com.hust.grid.bean.WeatherInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherServiceMain {
    private static Logger logger = LoggerFactory.getLogger(WeatherServiceMain.class);

    private static void init(String constantFilePath) {
        ConstantCacheCenter constantCacheCenter = ConstantCacheCenter.getInstance();
        constantCacheCenter.init(constantFilePath);

        logger.info("authorizationCode => " + constantCacheCenter.getAuthorizationCode() + ", crawlUrl => " +
                constantCacheCenter.getCrawlUrl() + ", host => " + constantCacheCenter.getHost() + ", port => " +
                constantCacheCenter.getPort() + ", protocol => " + constantCacheCenter.getProtocol() + ", receivers => " +
                constantCacheCenter.getReceivers().toString() + ", senderEmail => " + constantCacheCenter.getSenderEmail() + ", senderName => " +
                constantCacheCenter.getSenderName() + ", username => " + constantCacheCenter.getUsername());
    }

    private static void doWork(ConstantCacheCenter constantCacheCenter) {
        for (int i = 0; i < constantCacheCenter.getRetry_times(); i++) {
            WeatherCrawler weatherCrawler = new WeatherCrawler();
            WeatherInfo weatherInfo = weatherCrawler.crawlWeather(constantCacheCenter.getCrawlUrl());
            if (weatherInfo != null && weatherInfo.isValid()) { // normal
                MailSender mailSender = new MailSender(weatherInfo);
                mailSender.sendToAll();
                break;
            } else { // abnormal, sleep 10 minutes and visit again
				logger.info("visit abnormal, will visit again 10 minutes later");
                try {
                    Thread.sleep(10 * 1000 * 60);
                } catch (InterruptedException e) {
                    logger.error("sleep interrupted " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        String confPath = null;
        if (args.length == 1) {
            confPath = args[0];
        } else {
            logger.error("invalid args number");
        }
        init(confPath);
        doWork(ConstantCacheCenter.getInstance());
    }
}
