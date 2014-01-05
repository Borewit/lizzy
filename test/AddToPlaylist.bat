@echo off
setlocal
set PATH=%PATH%;%~dp0lib
java -cp "%~dp0lizzy.jar;%~dp0lib\args4j.jar;%~dp0classes" christophedelory.lizzy.AddToPlaylist %*
