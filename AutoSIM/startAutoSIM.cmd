set curdir=%cd%
cd /d %~dp0
java -version
java -classpath ./lib/felix.jar org.apache.felix.main.Main
cd /d %curdir%