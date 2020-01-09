#!/bin/bash
CURRENT_LOGIN=`whoami`
FILENAME=$1
VUID=`id ${CURRENT_LOGIN}|cut -d" " -f1|grep -o "\<[[:digit:]]\+\>"`
GIDS=`id ${CURRENT_LOGIN}|cut -d" " -f3|grep -oE "\<[[:digit:]]+\>"`

FUID=`ls -n ${FILENAME}|cut -d" " -f3`
FGID=`ls -n ${FILENAME}|cut -d" " -f4`
USER=`ls -l ${FILENAME}|cut -d" " -f 1|grep -oE "."|sed -n "1,4p" |tr -d "\n"`
GROUP=`ls -l ${FILENAME}|cut -d" " -f 1|grep -oE "."|sed -n "5,7p" |tr -d "\n"`
OTHER=`ls -l ${FILENAME}|cut -d" " -f 1|grep -oE "."|sed -n "8,10p" |tr -d "\n"`

if [ ${VUID} -eq ${FUID} ]
	then echo "${VUID}用户是文件${FILENAME}的所属主，具有${USER}权限"
elif [[ ${GIDS} =~ ${FGID} ]]
	then "${GIDS}是文件${FILENAME}的所属组，具有${GROUP}权限"
else
	echo "文件${FILENAME}其它权限，具有${OTHER}权限"
fi
echo "CURRENT_LOGIN :${CURRENT_LOGIN}"
echo "FILENAME:${FILENAME}"
echo "VUID :${VUID}"
echo "GIDS :${GIDS}"
echo "USER :${USER}"
echo "GROUP :${GROUP}"
echo "OTHER :${OTHER}"

