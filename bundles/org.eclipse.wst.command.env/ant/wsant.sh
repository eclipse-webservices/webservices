#!/bin/sh
set +v

#Script to run WebService Ant tasks in headless Eclipse mode

echo "Setting environment variables"

# The JRE java.exe to be used
JAVAEXE=/home/tester/sunjdk/j2sdk1.4.2_06/bin/java
                                                                                
# The Eclipse The Eclipse Equinox Launcher jar.  Usually in eclipse/plugins/org.eclipse.equinox.launcher*.jar
LAUNCHER_JAR=/root/wtp/eclipse/plugins/org.eclipse.equinox.launcher_1.0.0.v20070208a.jar

# The location of your workspace
WORKSPACE=/home/tester/workspace_1116b

run() {
  set -v
  $JAVAEXE -jar $LAUNCHER_JAR -application org.eclipse.ant.core.antRunner -data $WORKSPACE -file wsgen.xml $ls > wsgen.txt 2>&1
}

if [ ! -e $JAVAEXE ]; then 
  echo "ERROR: incorrect java.exe=$JAVAEXE, edit the script and correct the JAVAEXE environment variable";
  exit 1;
fi
                                                                           
if [ ! -e $LAUNCHER_JAR ]; then
echo "ERROR: incorrect launcher=$LAUNCHER_JAR, edit the script and correct the LAUNCHER_JAR environment variable";
exit 1;
fi  

run
exit 0
