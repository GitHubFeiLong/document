#/bin/bash
readonly APP_BAK_DIR="app-bak";
readonly BEFORE_APP_NAME="caicang-service-consumer.jar";
readonly AFTER_APP_NAME="caicang-service-consumer-prod.jar";
readonly REMOTE_JVM_DEBNUG_PARAMTER=" ";
readonly APP_PORT="9566";
readonly APP_PROFILES="prod";

if [ ! -d "./${APP_BAK_DIR}" ]; then
  mkdir "./${APP_BAK_DIR}"
	echo "${APP_BAK_DIR}创建成功";
fi

if [ -f "./${AFTER_APP_NAME}" ];then
	cp "${AFTER_APP_NAME}" "./${APP_BAK_DIR}/${AFTER_APP_NAME}`date +%Y-%m-%d_%H:%M:%S`.jar"
	echo "备份${AFTER_APP_NAME}成功"
fi

kill -9 $(ps -ef| grep "${AFTER_APP_NAME}" |grep -v grep|awk '{ print $2 }')
echo "结束进程成功"

echo > nohup.out

if [ -f "./${BEFORE_APP_NAME}" ];then
	mv "${BEFORE_APP_NAME}" "${AFTER_APP_NAME}"
	echo "修改文件名称成功"
fi

echo "开始启动项目"
# start app
nohup java -jar \
 ${REMOTE_JVM_DEBNUG_PARAMTER} "${AFTER_APP_NAME}" \
--server.port="${APP_PORT}" \
--spring.profiles.active="${APP_PROFILES}" \
-XX:SurvivorRatio=8 \
-XX:MaxTenuringThreshold=10 \
-XX:+UseConcMarkSweepGC \
-XX:CMSInitiatingOccupancyFraction=70 \
-XX:+UseCMSInitiatingOccupancyOnly \
-XX:+AlwaysPreTouch \
-XX:+HeapDumpOnOutOfMemoryError \
-verbose:gc \
-XX:+PrintGCDetails \
-XX:+PrintGCDateStamps \
-XX:+PrintGCTimeStamps \
-Xloggc:gc.log &

# 手动回车
sleep 1
tail -f -n 100 nohup.out
exit 0
