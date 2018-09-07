# eureka-server

Linux部署服务：
1、因为读取配置文件采用的是  读取环境变量的方式   所以首先要建环境变量
mkdir /home/config/eureka-server/config
mkdir /home/config/eureka-server/log
vi /etc/profile
文件最下面添加一条命令：export EUREKA_SERVER="/home/config/eureka-server/"
source /etc/profile
echo $EUREKA_SERVER

2、启动项目
mkdir /home/eureka-server
rz eureka-server.jar
rz startup.sh
rz nohup.out
./startup.sh
这里要给startup.sh赋予权限：chmod a+x startup.sh
或者直接通过 bash ./startup.sh 进行启动服务

服务BOOT启动类：
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
	public static void main(String[] args) {
		String envPath = System.getenv(EurekaConstants.ENV_CONF_PATH.toUpperCase());
		if (StringUtils.isEmpty(envPath)) {
			throw new IllegalStateException(EurekaConstants.ENV_CONF_PATH.toUpperCase() + " is blank");
		}
		if (!envPath.endsWith(File.separator))
			envPath = envPath + File.separator;	
		String configPath = envPath + "config" + File.separator;
		String logPath = envPath + "log" + File.separator;
		System.setProperty("spring.config.location", configPath);
		System.setProperty("log.base", logPath);
		System.setProperty("log.path", configPath);
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}

startup.sh命令：
#!/bin/sh

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# -----------------------------------------------------------------------------
# Start Script for the CATALINA Server
# -----------------------------------------------------------------------------
# Better OS/400 detection: see Bugzilla 31132
## java env
#export JAVA_HOME=/usr/java/jdk1.7.0_79
#export JRE_HOME=${JAVA_HOME}/jre

J_HOME="/usr/java/jdk1.8.0_91/bin/java"
SRC_JAR_HOME="/data/www"
SRC_JAR_NAME="eureka-server"
JAR_HOME="/home/eureka-server"
JAR_NAME="eureka-server"

## step 1 stop

echo "step 1 stop "

echo "kill -15 ${JAR_NAME}"

ps -ef | grep java | grep ${JAR_HOME} | awk '{print $2}' | xargs kill -15

sleep 5s

echo "top success"
## step remove old jar 2
## echo "step 2 remove old jar"
## rm -rf ${JAR_HOME}/${JAR_NAME}.jar

## step 3 copy new jar
## echo "step 3 copy new jar "
## cp ${SRC_JAR_HOME}/${SRC_JAR_NAME}.jar  ${JAR_HOME}/${JAR_NAME}.jar
## step 4 start jar
echo "step 4 start jar"
nohup ${J_HOME} -jar ${JAR_HOME}/${JAR_NAME}.jar --server.port=8761 > ${JAR_HOME}/nohup.out 2>&1 &

注：step 2 和 step 3是用于Jenkins部署用的命令，Jenkins生成的jar会放入SRC_JAR_HOME的文件夹，启动目录每次都是从这个目录拉最新的 删除老的jar
