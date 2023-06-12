: Setup
mkdir .\bin
del -Recurse .\bin\*

: Copy libraries
mkdir .\bin\lib
copy .\src\lib\* .\bin\lib

: Make jar
jar cmf 'manifest.mf' .\bin\ByteStrike.jar -C .\obj\ . -C .\src\ .\assets\
