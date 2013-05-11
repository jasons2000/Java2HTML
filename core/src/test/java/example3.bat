@echo off
set JAVA_HOME_J2H=C:\jdk1.5.0_11
set SRC_DIR="C:\Program Files\Java\jdk1.6.0\src"

java j2h -js %JAVA2HTML_HOME%\src\tests\testSource -d ..\..\..\JAVA_1_6_0_doc  -n Tests -m 4
rem java j2h -js %SRC_DIR% -d ..\..\..\JAVA_1_6_0_doc  -n Tests -m 4

