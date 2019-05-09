# 개요
Protocol buffer를 이용한 Udp echo 샘플


[udp echo 샘플](src/main/java/akka/example/echo/README.md)과 같이 akka remote를 사용하지 않는
UDP 통신을 한다.
송수신은 protocol buffer을 이용한다.

Message.proto 파일은 protoc로 빌드하면 java 소스가 생성된다.
- Content
- ContentOrBuilder
- Message
- MessageOrBuilder
- MessageOuterClass

# 참고
같은 시스템에 client와 server가 존재하고 레거시 연동이 아니라면
akka remote로 개발하는 것이 좋다. 보다 단순하게 구현이 가능할 것이다.