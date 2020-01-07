#!/bin/bash
echo $1
echo $2
if [ -z "$1" ]
	then echo "参数1不能为空";	 
elif [ -z "$2" ]
	then echo "参数2不能为空";
else 
	echo `cat $1|grep "^$"|wc -l; cat $2|grep "^$"|wc -l` |tr -s " " "+"|bc;
fi
