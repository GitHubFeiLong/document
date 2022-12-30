#/bin/bash
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
	echo -e "\n停止成功!"
	exit 0
fi
echo "停止成功!"
exit 0

