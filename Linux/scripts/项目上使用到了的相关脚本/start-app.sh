#/bin/bash
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
cat nohup.out >> "./${NOHUP_BAK_DIR}/nohup-`date +%Y-%m-%d`.out"
echo > nohup.out

# 将jar包修改名字再启动，上传jar包到服务器不会替换正在运行的程序
if [ -f "./lottery-api.jar" ];then
	mv lottery-api.jar app.jar
fi

# start app
nohup java -jar -Dspring.config.location=./spring-extend.yml -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5125  ./app.jar --server.port=12566 & 
# 手动回车
sleep 1
#echo -e "\n"

# 判断启动成功的条件(因为启动时，nohup.out会被我备份后清理)
echo -n "启动中"
started=""
while [[ -z "$started" ]]; do
	echo -n "."
	sleep 1
	started=`grep -w "LogApplicationStartup" ./nohup.out`
done
echo -e "\n启动成功！"

