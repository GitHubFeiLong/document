#!/bin/bash
echo -e `getent passwd|sed -n '10p;20p'|cut -d: -f3|tr -s '\n' '+'|head -c-1`|bc
