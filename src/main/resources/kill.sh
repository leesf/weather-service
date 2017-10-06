#!/bin/sh

weatherServicePid=`ps aux | grep WeatherServiceMain | grep java | grep -v grep | awk '{print $2}'`

if [ ! -n "weatherServicePid" ];then
    echo "WeatherService is not run!"
else
    echo "$weatherServicePid is running!"
    kill $weatherServicePid
    sleep 10

    cnt=5
    weatherServicePid=`ps aux | grep WeatherServiceMain | grep java | grep -v grep | awk '{print $2}'`
    while [ 0 -lt $cnt -a  -n "$weatherServicePid" ];do
        echo "$weatherServicePid still running!"
        ((cnt--));
        kill $weatherServicePid
        sleep 10
	weatherServicePid=`ps aux | grep WeatherServiceMain | grep java | grep -v grep | awk '{print $2}'`
    done;
    if [  -n "$weatherServicePid" ];then
        kill -9 $weatherServicePid
        echo "$weatherServicePid is force killed!"
    fi
fi