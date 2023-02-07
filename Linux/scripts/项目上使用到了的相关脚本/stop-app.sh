#/bin/bash
# 执行时间
startSeconds=`date +%s`
source ~/.bashrc
progress=`ps -ef | grep 'app.jar' | grep -v 'grep' | head -1 | awk '{print $2}'`
if [[ "$progress" -gt 0  ]]; then 
	echo ”执行命令：kill -15 $progress“; 
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
./send-qw.sh ${content}

exit 0

