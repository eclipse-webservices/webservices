rem *******************************************************************************
rem  Copyright (c) 2005, 2007 IBM Corporation and others.
rem  All rights reserved. This program and the accompanying materials
rem  are made available under the terms of the Eclipse Public License v1.0
rem  which accompanies this distribution, and is available at
rem  http://www.eclipse.org/legal/epl-v10.html
rem 
rem  Contributors:
rem      IBM Corporation - initial API and implementation
rem *******************************************************************************
echo off
setlocal

REM *********** Local envars ***************************

REM The JRE java.exe to be used
set JAVAEXE="C:\j2sdk1.4.2_07\jre\bin\java.exe"

REM The Eclipse Equinox Launcher jar.  Usually in eclipse\plugins\org.eclipse.equinox.launcher*.jar
set LAUNCHER_JAR="D:\wtp\eclipse\plugins\org.eclipse.equinox.launcher_1.0.0.v20070208a.jar"

REM The location of your workspace
set WORKSPACE=D:\workspaces\ant_task

REM ****************************************************

if not exist %JAVAEXE% echo ERROR: incorrect java.exe=%JAVAEXE%, edit this file and correct the JAVAEXE envar
if not exist %JAVAEXE% goto done

if not exist %LAUNCHER_JAR% echo ERROR: incorrect launcher jar=%LAUNCHER_JAR%, edit this file and correct the LAUNCHER_JAR envar
if not exist %LAUNCHER_JAR% goto done

:run
@echo on
%JAVAEXE% -jar %LAUNCHER_JAR% -application org.eclipse.ant.core.antRunner -data %WORKSPACE% -file wsgen.xml %* >wsgen.txt 2>&1

:done
pause
