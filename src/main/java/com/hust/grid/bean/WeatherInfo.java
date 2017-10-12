package com.hust.grid.bean;

public class WeatherInfo {
    private String weather;
    private String temp;
    private String wind;

    public WeatherInfo(String weather, String temp, String wind) {
        this.weather = weather;
        this.temp = temp;
        this.wind = wind;
    }

    public boolean isValid() {
        return weather != null && temp != null && wind != null;
    }

    public String getWeather() {
        return weather;
    }

    public String getTemp() {
        return temp;
    }

    public String getWind() {
        return wind;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("天气：");
        stringBuffer.append(weather);
        stringBuffer.append(", 温度：");
        stringBuffer.append(temp);
        stringBuffer.append(", 风力：");
        stringBuffer.append(wind);
        stringBuffer.append("\n\n");
        if (weather.contains("雨")) {
            stringBuffer.append("主人记得带伞回家哦~");
        } else {
            stringBuffer.append("今天可以不用带伞回家哦~");
        }
        return stringBuffer.toString();
    }

    public String getWeixinFormatString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (weather.contains("雨")) {
            stringBuffer.append("主人今天记得带伞回家哦~");
        } else {
            stringBuffer.append("今天可以不用带伞回家哦~");
        }
        return stringBuffer.toString();
    }
}
