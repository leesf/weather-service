package com.hust.grid.cache;


import com.hust.grid.conf.ConfigConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConstantCacheCenter {
    private static volatile ConstantCacheCenter constantCacheCenter;
    private String crawlUrl;
    private String username;
    private String authorizationCode;
    private String senderEmail;
    private String senderName;
    private String host;
    private String port;
    private String protocol;
    private List<String> receivers;
    private final static String CONSTANT_FILE_NAME = "constant.property";
    private static String constant_file_path;
    private int retry_times;
    private List<String> weixinReceiverKeysList;

    private ConstantCacheCenter() {
    }

    public static ConstantCacheCenter getInstance() {
        if (null == constantCacheCenter) {
            synchronized (ConstantCacheCenter.class) {
                if (null == constantCacheCenter) {
                    constantCacheCenter = new ConstantCacheCenter();
                }
            }
        }
        return constantCacheCenter;
    }

    public void init(String conf) {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        constant_file_path = conf + File.separator + CONSTANT_FILE_NAME;
        try {
            fileInputStream = new FileInputStream(new File(constant_file_path));
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    //
                }
        }

        crawlUrl = properties.getProperty(ConfigConstants.SZ_URL);
        username = properties.getProperty(ConfigConstants.USERNAME);
        authorizationCode = properties.getProperty(ConfigConstants.AUTHORIZATION_CODE);
        senderEmail = properties.getProperty(ConfigConstants.SENDER_EMAIL);
        senderName = properties.getProperty(ConfigConstants.SENDER_NAME);
        host = properties.getProperty(ConfigConstants.HOST);
        protocol = properties.getProperty(ConfigConstants.PROTOCOL);
        port = properties.getProperty(ConfigConstants.PORT);
        initReceivers(properties.getProperty(ConfigConstants.RECEIVERS));
        initWeixinReceiverKeys(properties.getProperty(ConfigConstants.WEIXIN_RECEIVER_KEYS));
        retry_times = Integer.parseInt(properties.getProperty(ConfigConstants.RETRY_TIMES));

        /*Thread monitorThread = new MonitorThread(conf);
        monitorThread.start();*/

    }

    private void initReceivers(String receiverString) {
        receivers = new ArrayList<>();
        String[] _receivers = receiverString.split(",");
        for (String _receiver : _receivers) {
            receivers.add(_receiver.trim());
        }
    }

    private void initWeixinReceiverKeys(String weixinReceiverKeys) {
        weixinReceiverKeysList = new ArrayList<>();
        String[] _receivers = weixinReceiverKeys.split(",");
        for (String _receiver : _receivers) {
            weixinReceiverKeysList.add(_receiver.trim());
        }
    }

    public String getCrawlUrl() {
        return crawlUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public int getRetry_times() {
        return retry_times;
    }

    public List<String> getWeixinReceiverKeysList() {
        return weixinReceiverKeysList;
    }

    /**
     * monitor the changes in constant.property
     */
    private class MonitorThread extends Thread {
        private boolean stop = false;
        private long lastModify = 0;
        private String conf;

        public MonitorThread(String conf) {
            this.conf = conf;
        }

        public void shutDown() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                try {
                    File file = new File(constant_file_path);
                    if (lastModify < file.lastModified()) {
                        lastModify = file.lastModified();
                        init(conf);
                    }
                    Thread.sleep(2 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
