#!/bin/bash
source ~/.bashrc
# 自动部署服务
# 当目录下存在lottery-api.jar，说明还未进行部署
# 使用cron定时执行部署命令
# 当目录下不存在lottery-api.jar，说明不需要进行部署。

# 判断是否存在文件
PREFIX="/home/hqappt/work-online/lottery-api"
if [ -f "${PREFIX}/lottery-api.jar" ];then
	echo "${PREFIX}/lottery-api.jar 文件存在，即将执行自动部署"
	# 存在待部署项目，执行部署
	startShelltime=`date "+%Y-%m-%d %H:%M:%S"`
	source `${PREFIX}/send-qw.sh "定时任务（lottery-api自动部署）\n开始执行 ${startShelltime}"`

	# 停止
	################################################################################################################
	# 执行时间
	startShellSeconds=`date +%s`
	startSeconds=`date +%s`

	progress=`ps -ef | grep 'app.jar' | grep -v 'grep' | head -1 | awk '{print $2}'`
	if [[ "$progress" -gt 0  ]]; then 
		echo "执行命令：kill -15 $progress"; 
		kill -15 "$progress";
		echo -n "停止中"
		while [[ "$progress" -gt 0 ]]; do
			echo -n .;      
			progress=`ps -ef | grep 'app.jar' | grep -v 'grep' | head -1 | awk '{print $2}'`
			sleep 0.5
		done
		echo -e "\n"
	fi

	# 结束时间
	endSeconds=`date +%s`
	totalSeconds=`echo "${endSeconds}-${startSeconds}" | bc`

	content="停止成功！停止花费：${totalSeconds} 秒"
	echo "${content}"
	source `${PREFIX}/send-qw.sh ${content}`


	# 启动
	################################################################################################################
	startSeconds=`date +%s`
	
	# 备份文件夹
	readonly APP_BAK_DIR="${PREFIX}/app-bak";
	readonly NOHUP_BAK_DIR="${PREFIX}/nohup-bak";
	if [ ! -d "${APP_BAK_DIR}" ]; then
	        mkdir "${APP_BAK_DIR}"
	        echo "${APP_BAK_DIR}创建成功";
	fi
	if [ ! -d "${NOHUP_BAK_DIR}" ]; then 
	        mkdir "${NOHUP_BAK_DIR}"
	        echo "${NOHUP_BAK_DIR}创建成功";
	fi
	
	# 将原app.jar备份，方便回滚
	cp "${PREFIX}/app.jar" "${APP_BAK_DIR}/app-`date +%Y-%m-%d_%H:%M:%S`.jar"
	
	# 将jar包修改名字再启动，上传jar包到服务器不会替换正在运行的程序
	if [ -f "${PREFIX}/lottery-api.jar" ];then
	        mv "${PREFIX}/lottery-api.jar" "${PREFIX}/app.jar"
	fi
	
	# start app
	nohup java -jar -Dspring.config.location=${PREFIX}/spring-extend.yml -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5125  ${PREFIX}/app.jar --server.port=12566  > ${PREFIX}/nohup.out 2>&1 &
	# 手动回车
	sleep 1
	echo -e "\n"
	# 判断启动成功的条件(因为启动时，nohup.out会被我备份后清理)
	echo -n "启动中"
	started=""
	while [[ -z "$started" ]]; do
	        echo -n "."
	        sleep 1
	        started=`grep -w "LogApplicationStartup" ${PREFIX}/nohup.out`
	done
	
	# 启动结束时间
	endSeconds=`date +%s`
	totalSeconds=`echo "${endSeconds}-${startSeconds}" | bc`
	
	
	echo -e "\n启动成功！启动花费：${totalSeconds} 秒"
	source `${PREFIX}/send-qw.sh "启动成功！启动花费：${totalSeconds} 秒"`

	endShellSeconds=`echo "${endSeconds}-${startShellSeconds}" | bc`
	source `${PREFIX}/send-qw.sh "定时任务（lottery-api自动部署）\n执行成功，执行花费：${endShellSeconds}"`

	exit 0
fi
echo "不需要执行自动部署lottery-api"
exit 0


