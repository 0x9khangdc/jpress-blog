#!/bin/bash
# ----------------------------------------------------------------------
# name:         jpress.sh
# version:      1.0
# description:  JPress Control script
# author:       yangfuhai
# email:        fuhai999@gmail.com
# use : ./jpress.sh {start, stop, restart}
# ----------------------------------------------------------------------

MAIN_CLASS=io.jpress.Starter
COMMAND="$1"

if [[ "$COMMAND" != "start" ]] && [[ "$COMMAND" != "stop" ]] && [[ "$COMMAND" != "restart" ]]; then
	echo "Usage:  ./jpress.sh {start, stop, restart}"
	exit 0
fi



# Java The command line parameters, open the following configuration as need
# JAVA_OPTS="-Xms256m -Xmx1024m -Dundertow.port=80 -Dundertow.host=0.0.0.0"
# JAVA_OPTS="-Dundertow.port=80 -Dundertow.host=0.0.0.0 -Dundertow.devMode=false"

# generate class path value
APP_BASE_PATH=$(cd `dirname $0`; pwd)
CP=${APP_BASE_PATH}/config:${APP_BASE_PATH}/lib/*

function start()
{
    # Run as the background process, and output information on the console
    java -Djava.awt.headless=true -Xverify:none ${JAVA_OPTS} -cp ${CP} ${MAIN_CLASS} &


    # Run as the background process, and does not output information in the console
    # nohup java -Djava.awt.headless=true -Xverify:none ${JAVA_OPTS} -cp ${CP} ${MAIN_CLASS} >/dev/null 2>&1 &

    # Run as the background process, and output the information to output.log document
    #nohup java -Djava.awt.headless=true -Xverify:none ${JAVA_OPTS} -cp ${CP} ${MAIN_CLASS} > output.log &
    #tail -f /dev/null

    # Running is a non-background process, which is mostly used in the development stage. The shortcut key CTRL + C can stop the service
    # When starting in Docker in this way, due to the background process and the process of no front platform, the Docker container will exit immediately after starting.
    # You need to add commands tail -f /dev/null，You can keep your container running in the front desk
    # Or use the following non -background process to run
    #java -Djava.awt.headless=true -Xverify:none ${JAVA_OPTS} -cp ${CP} ${MAIN_CLASS}
}

function stop()
{
    kill `pgrep -f ${APP_BASE_PATH}` 2> /dev/null
}

if [[ "$COMMAND" == "start" ]]; then
	start
elif [[ "$COMMAND" == "stop" ]]; then
    stop
else
    stop
    start
fi
