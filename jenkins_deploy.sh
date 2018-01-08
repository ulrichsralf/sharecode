#!/usr/bin/env bash
ssh ralf@appserver1 ' fuser -k 8085/tcp '
scp ./target/sharecode-* ralf@appserver1:~/deploy/
ssh ralf@appserver1 ' nohup java -Dserver.port=8085 -jar ./deploy/sharecode-*.jar  >sharecode.log &'