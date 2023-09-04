# Setup
mkdir -force .\bin
del -Recurse .\bin\*

# Copy libraries
mkdir -force .\bin\lib
copy .\src\lib\* .\bin\lib

# Make jar
jar cmf 'manifest.mf' .\bin\ByteStrike.jar -C .\obj\ . -C .\src\ .\assets\
