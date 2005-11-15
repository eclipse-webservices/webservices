echo off
setlocal

REM *********** Local envars ***************************

REM The JRE java.exe to be used
set JAVAEXE="C:\j2sdk1.4.2_07\jre\bin\java.exe"

REM The Eclipse startup.jar - target workspace/wtp workspace
set STARTUPJAR="D:\wtp0929\eclipse\startup.jar"

REM The location of your workspace
set WORKSPACE=D:\workspaces\ant_task

REM ****************************************************

if not exist %JAVAEXE% echo ERROR: incorrect java.exe=%JAVAEXE%, edit this file and correct the JAVAEXE envar
if not exist %JAVAEXE% goto done

if not exist %STARTUPJAR% echo ERROR: incorrect startup.jar=%STARTUPJAR%, edit this file and correct the STARTUPJAR envar
if not exist %STARTUPJAR% goto done

:run
@echo on
%JAVAEXE% -cp %STARTUPJAR% org.eclipse.core.launcher.Main -noupdate -application org.eclipse.ant.core.antRunner -data %WORKSPACE% -file wsgen.xml %* >wsgen.txt 2>&1

:done
pause
