
rem was used to check the quality of the HTML output

@echo off
set TIDY_BIN=C:\Work\Desktop\Downloads\tidy_win32\TidyDbg.exe
set JAVA2HTML_HOME=C:\Work\Dev\Java2HTML

%TIDY_BIN% %1

rem %TIDY_BIN% %JAVA2HTML_HOME%\release\java2html.html
rem %TIDY_BIN% %JAVA2HTML_HOME%\release\download.html
rem %TIDY_BIN% %JAVA2HTML_HOME%\release\index.html
rem %TIDY_BIN% %JAVA2HTML_HOME%\release\examples\Java2D_demo\front.html
rem %TIDY_BIN% %JAVA2HTML_HOME%\release\examples\Java2D_demo\index.html
rem %TIDY_BIN% %JAVA2HTML_HOME%\release\examples\Java2D_demo\.index.html
rem %TIDY_BIN% %JAVA2HTML_HOME%\release\examples\Java2D_demo\AllClasses.html
rem %TIDY_BIN% %JAVA2HTML_HOME%\release\examples\Java2D_demo\packages.html
rem %TIDY_BIN% %JAVA2HTML_HOME%\release\examples\Java2D_demo\stylesheet.css



