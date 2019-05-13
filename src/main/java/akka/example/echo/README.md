전형적인 Client Server Echo 샘플

Client에서 텍스트를 보내면 Server는 대문자로 변경하여 응답한다. 

Akka가 아닌 타 시스템과 통신시 참고하면 되며
만약 Akka로 된 시스템끼리 통신한다면 Akka remote 기능을 사용하는 것이 좋다.