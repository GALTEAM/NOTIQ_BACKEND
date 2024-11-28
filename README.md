몽고DB- Atlas

https://velog.io/@myong/MongoDB-Atlas-%EC%8D%A8%EB%B3%B4%EA%B8%B0

코틀린 조건식

https://androidtest.tistory.com/96

코틀린에서 엑셀 다루기

https://loko1124.tistory.com/126

git actions yml 파일 생성

https://sum-mit45.tistory.com/entry/SPRING-BOOT-AWS-EC2-RDS-github-actions-docker-%EC%82%AC%EC%9A%A9%ED%95%B4%EC%84%9C-%EC%9E%90%EB%8F%99%EB%B0%B0%ED%8F%AC-%ED%95%98%EA%B8%B05-%EA%B9%83%ED%97%88%EB%B8%8C-%ED%8C%8C%EC%9D%BC-%EC%84%A4%EC%A0%95

에러 발생 : 배포 후에 다음과 같은 에러 발생

내용

Exception in monitor thread while connecting to server localhost:27017

com.mongodb.MongoSocketOpenException: Exception opening socket
        at com.mongodb.internal.connection.SocketStream.lambda$open$0(SocketStream.java:86) ~[mongodb-driver-core-5.0.1.jar!/:na]
        at java.base/java.util.Optional.orElseThrow(Optional.java:403) ~[na:na]
        at com.mongodb.internal.connection.SocketStream.open(SocketStream.java:86) ~[mongodb-driver-core-5.0.1.jar!/:na]
        at com.mongodb.internal.connection.InternalStreamConnection.open(InternalStreamConnection.java:201) ~[mongodb-driver-core-5.0.1.jar!/:na]
        at com.mongodb.internal.connection.DefaultServerMonitor$ServerMonitorRunnable.lookupServerDescription(DefaultServerMonitor.java:193) ~[mongodb-driver-core-5.0.1.jar!/:na]
        at com.mongodb.internal.connection.DefaultServerMonitor$ServerMonitorRunnable.run(DefaultServerMonitor.java:153) ~[mongodb-driver-core-5.0.1.jar!/:na]
        at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]
Caused by: java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.pollConnect(Native Method) ~[na:na]
        at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:672) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:539) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:594) ~[na:na]
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327) ~[na:na]
        at java.base/java.net.Socket.connect(Socket.java:633) ~[na:na]
        at com.mongodb.internal.connection.SocketStreamHelper.initialize(SocketStreamHelper.java:76) ~[mongodb-driver-core-5.0.1.jar!/:na]
        at com.mongodb.internal.connection.SocketStream.initializeSocket(SocketStream.java:105) ~[mongodb-driver-core-5.0.1.jar!/:na]
        at com.mongodb.internal.connection.SocketStream.open(SocketStream.java:80) ~[mongodb-driver-core-5.0.1.jar!/:na]

해결 

@SpringBootApplication(exclude = [MongoAutoConfiguration::class]) // AutoConfiguration 설정
@EnableJpaAuditing  // Auditing 활성화
class NotiQApplication

자료 : https://stackoverflow.com/questions/35415308/exception-in-monitor-thread-while-connecting-to-server-localhost27017-while-acc

에러 : 평가, 가채졈표 저장안됨 -> 가채점표 저장, 평가 저장 분리

FCM으로 알람구현

https://velog.io/@haden/spring-fcm-server-%EC%84%A4%EC%A0%95

