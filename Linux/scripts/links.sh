#!/bin/bash
set -u
set -e
echo -e "$RED`netstat -tan|grep 'ESTAB'|tr -s ' ' ':'|cut -d: -f6|sort|uniq -c|sort -nr`$COLOREND"
