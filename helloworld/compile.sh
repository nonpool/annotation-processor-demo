cd src/main/java || exit
mkdir classes
javac com/nonpool/processor/helloworld/AnnoProc.java -d classes
javac -cp classes -d classes -processor com.nonpool.processor.helloworld.AnnoProc com/nonpool/processor/helloworld/*.java