#!/bin/sh
# Change this Script, to suit your needs
# (This script has not been tested)
# You will need to "chmod +x j2h" to enable it to be executed
if [ -z ${CLASSPATH:=""} ] ; then
        CLASSPATH=/opt/software/java2html/j2h.jar
else
        CLASSPATH=/opt/software/java2html/j2h.jar:${CLASSPATH}
fi
export CLASSPATH
exec java -Dinstall.root=/opt/software/java2html j2h "$@"
