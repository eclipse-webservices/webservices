#!/bin/sh
set +v

#Script to run WebService Ant tasks in headless Eclipse mode

echo "Setting environment variables"

# The JRE java.exe to be used
JAVAEXE=/home/tester/sunjdk/j2sdk1.4.2_06/bin/java
                                                                                
# The Eclipse startup.jar - the wtp workspace to launch headless
STARTUPJAR=/opt/wtp_install/wtp_1116/eclipse/startup.jar

# The location of your workspace
WORKSPACE=/home/tester/workspace_1116b

run() {
  set -v
  $JAVAEXE -cp $STARTUPJAR org.eclipse.core.launcher.Main -noupdate -application org.eclipse.ant.core.antRunner -data $WORKSPACE -file wsgen.xml $ls > wsgen.txt 2>&1
}

if [ ! -e $JAVAEXE ]; then 
  echo "ERROR: incorrect java.exe=$JAVAEXE, edit the script and correct the JAVAEXE environment variable";
  exit 1;
fi
                                                                           
if [ ! -e $STARTUPJAR ]; then
echo "ERROR: incorrect startup.jar=$STARTUPJAR, edit the script and correct the STARTUPJAR environment variable";
exit 1;
fi  

run
exit 0
