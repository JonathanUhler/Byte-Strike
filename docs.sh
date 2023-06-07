cd src
javadoc main/**/*.java -d ../documentation/javadoc -cp lib/jnet.jar
cd ..

cd level-builder
javadoc LevelBuilder.java -d ../documentation/level-builder
cd ..
