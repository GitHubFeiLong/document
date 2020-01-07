#!/bin/bash
set -u
set -e
COLOREND='\e[0m'
RED='\e[1;31m'
echo -e "$RED`df -h|grep .*sd|tr -s " "|cut -d" " -f5|tr -d '%'|sort -rn|head -n 1`$COLOREND"
