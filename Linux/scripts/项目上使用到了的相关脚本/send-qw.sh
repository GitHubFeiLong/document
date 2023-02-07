#!/bin/bash
# 发送企微机器人消息
readonly key="0ef3dd51-3e9b-44a3-a092-a0ab94c27878"
content=$@
if [[ -n "$content" ]];
then
	echo "发送信息${content}"
fi

curl -X POST https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key="${key}" \
	   -H 'Content-Type: application/json' \
	   -d "{\"msgtype\": \"text\",\"text\": {\"content\": \"$content\"}}"
echo -e "\n"
