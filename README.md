# yjOps

Notion - https://syyjbhjh7.notion.site/yjOps-Operator-9a6937eeb8a2470da403c14c162f8e4a?pvs=4  

```
오퍼레이터(Operator)는 사용자 정의 리소스를 사용하여 애플리케이션 및 해당 컴포넌트를 관리하는 쿠버네티스의 소프트웨어 익스텐션(어렵다….)

 쿠버네티스는 자동화를 위해 설계되었다. 기본적으로 쿠버네티스의 중추를 통해 많은 빌트인 자동화 기능을 사용할 수 있다. 쿠버네티스를 사용하여 워크로드 배포 및 실행을 자동화할 수 있고, 또한 쿠버네티스가 수행하는 방식을 자동화할 수 있다.
 쿠버네티스의 오퍼레이터 패턴 개념을 통해 쿠버네티스 코드 자체를 수정하지 않고도 컨트롤러를 하나 이상의 사용자 정의 리소스(custom resource)에 연결하여 클러스터의 동작을 확장할 수 있다. 오퍼레이터는 사용자 정의 리소스의 컨트롤러 역할을 하는 쿠버네티스 API의 클라이언트다.
```

## 제작동기

- Java로도 Kubernetes Operator를 만들 수 있다는 영상을 보고 시작
    - 11번가 Tech 영상이 알고리즘에 떳다!
- Operator에 대한 개념확립
    - Operator 뭔지는 알지만… Istio 설치할때 쓰는.. Jaeger Operator가 깔리던데…
- 참조할 수 있는 이미 그 길을 걸으신 분이 계신다!
    - 참조 - https://dev.gmarket.com/112

## 개발환경 세팅

- Java용 Kubernetes Client API
    - fabric8
    - Official Java Client
        - 함수에 argument 개수가 많고, argument가 `null`인 경우도 많아서 코드 가독성 하락
     


## Helm Repo
helm repo add yjops https://syyjbhjh3.github.io/md-helm/
