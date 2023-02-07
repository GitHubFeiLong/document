#!/bin/bash
# 执行时间
startSeconds=`date +%s`

./stop-app.sh
./start-app.sh

# 启动结束时间
endSeconds=`date +%s`
totalSeconds=`echo "${endSeconds}-${startSeconds}" | bc`

echo -e "\n重启成功！！重启一共花费：${totalSeconds} 秒"
./send-qw.sh "重启成功！！重启一共花费：${totalSeconds} 秒"

exit 0

