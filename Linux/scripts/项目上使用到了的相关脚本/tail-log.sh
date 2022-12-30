#!/bin/bash
echo "开始查看"
n=$1
if [[ "$n" -gt 0 ]]; 
then
      n=$1;
else
	n=50
fi	
echo "n=$n"
cd /home/hqappt/work-online/lottery-api/log

filename=`ls -t | grep 'lottery-api.*.log' | head -1`
echo "本次监控的日志文件是 $filename"
tail -f -n "$n" `ls -t | grep 'lottery-api.*.log' | head -1`

