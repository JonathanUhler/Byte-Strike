: Setup
mkdir .\obj
del -Recurse .\obj\*

: Compile
javac -Xlint:unchecked -Xlint:deprecation -cp '.\src\lib\*' -d .\obj\ $(dir -s .\src\main\*.java)
