FrontendActor와 BackendActor는 서로 다른 JVM에서 동작한다.
이 예제는 BackedActor가 중지/시작중/정상과 상관없이 RemoteLookupProxy를 통해서 안전하게 통신할 수 있는 것을 보여준다.
FrontendActor는 Backed 상태를 걱정할 필요없이 작업을 수행할 수 있다.

다만 예제는 완전하지 않은데 Backend가 중지 혹은 실행중인 상태이면 메세지가 전달되지 못한다.
하지만 전달되지 않은 메시지를 다른 곳에 보관하고 있다 재전송하도록 추가 로직을 구현할 수 있을 것이다.
