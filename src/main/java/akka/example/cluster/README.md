# 개요
이 Cluster 예제는 단순히 Seed Node만 만들고 다른 작업은 없다.

클러스터링에서 시드 노드는 필수인데 클러스터의 시작점으로 클러스터를 구성하고 다른 노드에 접근할 수 있도록 해준다.

# 초기화 순서

1. 첫번째 시드 노드를 사용해 클러스터를 초기화한다.
시느 노드는 자동으로 자신을 클러스터에 가입(JOIN)시킨다.
첫번째 시드 노드는 초기에 클러스터를 만드는 중요한 역할을 한다.
2. 더 많은 시드 노드가 연락처 역할을 위해 합류한다. (시드 노드2,3)
다른 시드 노드들은 첫번째 시드 노드에 의존한다.
3.  다른 작업 및 마스터 역할을 수행하는 노드들도 클러스터에 가입할 수 있다.
가입 처리는 시드 노드들이 각각 분담하여 처리한다.
4. 시드가 최소 하나만 있어도 계속 합류할 수 있다.

# 예제 실행 방법
JVM 옵션에 호스트/포트를 지정하여 각각 3개의 시드 노드를 실행하면 된다.
 
- 시드 노드1: java -DHOST=127.0.0.1 -DPORT=2551 ...
- 시드 노드2: java -DHOST=127.0.0.1 -DPORT=2552 ...
- 시드 노드3: java -DHOST=127.0.0.1 -DPORT=2553 ...

실행하면 로그를 통해 등록/합류/탈퇴 등의 내용을 확인할 수 있다. 

 
# 제약사항
- Akka 클러스터는 TCP 멀티캐스트, DNS 서비스 발견(DNS-SD)와 같은 무설정 발견 프로토콜을 지원하지 않는다.
합류하기 위해서는 시드 노드 목록을 지정 또는 합류 요청을 보낼 호스트/포트를 알고 있어야 한다.

# 팁
- 첫번째 시드 노드를 제외하고 IP나 도메인 주소를 모르는 경우 첫번째 시드 노드를 통해서 합류가 가능하다.
- Akka는 무설정 발견 프로토콜을 지원하지 않지만 ZooKeeper, HashiCorp-Consul, CoreOS/etcd 같은 것을 결합하여 가능하다.
방법은 모든 클러스터 노드가 시작시 자신을 이 서비스에 등록하고 연결시 기존 클러스터 노드를 이런 발견 서비스를 통해서 찾아내는 Adapter를 작성하면 된다.
다만 ZooKeeper의 경우 여전히 호스트/포트를 알아야 한다. 그러므로 각 서비스의 장단점을 충분히 파악하고 사용해야 한다. 