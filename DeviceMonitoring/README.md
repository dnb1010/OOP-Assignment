# Device Monitoring (Delegate & Event lab)

Java 17+, Maven, khong phu thuoc thu vien ngoai.

    mvn package
    java -jar target/device-monitoring-1.0.0.jar

Khong co Maven:

    mkdir out
    javac -d out $(find src/main/java -name "*.java")
    java -cp out com.lab.devicemonitoring.app.Main
