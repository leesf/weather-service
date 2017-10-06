#!/bin/sh

# export jdk env
export JAVA_HOME=/home/robbinli/program/java/jdk1.8.0_45
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

# get root path
base_path=`cd "$(dirname "$0")"; cd ..; pwd`

if [ ! -d "../log" ]; then
  mkdir ../log
fi


# auto format the jars in classpath
lib_jars=`ls $base_path/lib/ | grep jar | awk -v apppath=$base_path 'BEGIN{jars="";}{jars=sprintf("%s:%s/lib/%s", jars, apppath, $1);} END{print jars}'`
conf_path="$base_path/conf"

main_class="com.hust.grid.entry.WeatherServiceMain ${conf_path}"
jar_file="weather-service.jar"

run_cmd="java -Dlog.home=${base_path}/log -Dlog4j.configuration=file:${base_path}/conf/log4j.properties -verbosegc -XX:+PrintGCDetails -cp ${conf_path}:${base_path}/bin/${jar_file}${lib_jars} ${main_class} "

echo "start command: $run_cmd"
echo "start..."
       $run_cmd > $base_path/log/jvm.log 2>&1 &

