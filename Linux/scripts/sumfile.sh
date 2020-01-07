#!/bin/bash
ETC=/etc/
VAR=/var/
USR=/usr/
echo -e "${ETC}下文件夹数：${RED}`find ${ETC} -type d|wc -l` ${COLOREND}"
echo -e "${ETC}下文件数：${RED}`find ${ETC} -type f|wc -l` ${COLOREND}"
echo -e "${VAR}下文件夹数：${RED}`find ${VAR} -type d|wc -l` ${COLOREND}"
echo -e "${VAR}下文件数：${RED}`find ${VAR} -type f|wc -l` ${COLOREND}"
echo -e "${USR}下文件夹数：${RED}`find ${USR} -type d|wc -l` ${COLOREND}"
echo -e "${USR}下文件数：${RED}`find ${USR} -type f|wc -l` ${COLOREND}"
