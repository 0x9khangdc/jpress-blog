#!/bin/bash
# ----------------------------------------------------------------------
# name:         jpress.sh
# version:      1.0
# description:  JPress Control script
# author:       yangfuhai
# email:        fuhai999@gmail.com
# use : ./jpress.sh {start, stop, restart}
# ----------------------------------------------------------------------

# Start the entrance class, the script file is used for other items to be changed here
MAIN_CLASS=io.jboot.app.JbootApplication
COMMAND="$1"

if [[ "$COMMAND" != "start" ]] && [[ "$COMMAND" != "stop" ]] && [[ "$COMMAND" != "restart" ]]; then
	echo "./jpress.sh {start, stop, restart}"
	exit 0
fi

# Java The command line parameters, open the following configuration as need
# JAVA_OPTS="-Xms256m -Xmx1024m -Dundertow.port=80 -Dundertow.host=0.0.0.0"
# JAVA_OPTS="-Dundertow.port=8080 -Dundertow.host=0.0.0.0"

# generate class path value
APP_BASE_PATH=$(cd `dirname $0`; pwd)
CP=${APP_BASE_PATH}/config:${APP_BASE_PATH}/lib/*

function start()
{
    java -Djava.awt.headless=true -Xverify:none ${JAVA_OPTS} -cp ${CP} ${MAIN_CLASS}
}

function stop()
{
    kill `pgrep -f ${MAIN_CLASS}` 2>/dev/null

}

if [[ "$COMMAND" == "start" ]]; then
	start
elif [[ "$COMMAND" == "stop" ]]; then
    stop
else
    stop
    start
fi
