#/bin/bash
# 执行时间
startSeconds=`date +%s`
startTime=`date "+%Y-%m-%d %H:%M:%S"`
source ~/.bashrc

# 备份文件夹
readonly APP_BAK_DIR="app-bak";
readonly NOHUP_BAK_DIR="nohup-bak";
if [ ! -d "./${APP_BAK_DIR}" ]; then
       	mkdir "./${APP_BAK_DIR}"
	echo "${APP_BAK_DIR}创建成功";
fi
if [ ! -d "./${NOHUP_BAK_DIR}" ]; then 
	mkdir "./${NOHUP_BAK_DIR}"
	echo "${NOHUP_BAK_DIR}创建成功";
fi

# 将原app.jar备份，方便回滚
cp app.jar "./${APP_BAK_DIR}/app-`date +%Y-%m-%d_%H:%M:%S`.jar"
# 将原nohup.out 备份
#cat nohup.out >> "./${NOHUP_BAK_DIR}/nohup-`date +%Y-%m-%d`.out"
echo > nohup.out

# 将jar包修改名字再启动，上传jar包到服务器不会替换正在运行的程序
if [ -f "./lottery-api.jar" ];then
	mv lottery-api.jar app.jar
fi

# start app
nohup java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5125  ./app.jar --server.port=12566 --spring.profiles.active=prod \
-Xmx3584m -Xms3584m -Xmn1344m \
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
#echo -e "\n"

# 判断启动成功的条件(因为启动时，nohup.out会被我备份后清理)
echo -n "启动中(启动时间：`date +%Y-%m-%d_%H:%M:%S`)"
started=""
while [[ -z "$started" ]]; do
	echo -n "."
	sleep 1
	started=`grep -w "LogApplicationStartup" ./nohup.out`
done

# 启动结束时间
endSeconds=`date +%s`
totalSeconds=`echo "${endSeconds}-${startSeconds}" | bc`
endMsg="启动成功(${startTime})！启动花费：${totalSeconds} 秒"

echo -e "\n${endMsg}"
./send-qw.sh "${endMsg}"

exit 0
