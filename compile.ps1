# Setup
mkdir -force .\obj
del -recurse .\obj\*

# Compile
javac -encoding UTF-8 -Xlint:unchecked -Xlint:deprecation -cp '.\src\lib\*' -d .\obj\ (dir -s .\src\main\*.java)
