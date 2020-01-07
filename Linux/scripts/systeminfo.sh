#!/bin/bash
COLOR="\e[1;31m"
COLOREND="\e[0m"
echo -e "操作系统 $COLOR `cat /etc/centos-release` $COLOREND"
echo -e "主机名：$COLOR `hostname`$COLOREND"
echo -e "Ipv4 地址：$COLOR`ifconfig eth0|grep netmask|tr -s " "|cut -d" " -f3`$COLOREND"
echo -e "操作系统版本：$COLOR`cat /etc/centos-release`$COLOREND"
echo -e "内核版本：$COLOR`uname -r`"$COLOREND
echo -e "CPU型号：$COLOR`lscpu|grep 'Model name'|grep -o '\<[^Model name:].*'`"$COLOREND
echo -e "内内存大小：$COLOR`free -h|grep Mem|tr -s " "|cut -d" " -f 2`"$COLOREND
echo -e "硬盘大小：$COLOR`df -h|grep .da1|tr -s ' '|cut -d' ' -f 2`"$COLOREND
