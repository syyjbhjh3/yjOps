# yjOps

### __*Jenkinsm Gitlab을 설치하는 Operator 입니다.*__


> javaoperatorsdk을 써보기위해 제작하였습니다. 상세 내용은 Notion에 모두 담겨있습니다.
> #### Notion - https://syyjbhjh7.notion.site/yjOps-Operator-9a6937eeb8a2470da403c14c162f8e4a?pvs=4
>>
>> __*오퍼레이터(Operator)*__ 는 사용자 정의 리소스를 사용하여 애플리케이션 및 해당 컴포넌트를 관리하는 쿠버네티스의 소프트웨어 익스텐션(어렵다….)
>>
>> 쿠버네티스는 자동화를 위해 설계되었다. 기본적으로 쿠버네티스의 중추를 통해 많은 빌트인 자동화 기능을 사용할 수 있다. 쿠버네티스를 사용하여 워크로드 배포 및 실행을 자동화할 수 있고, 또한 쿠버네티스가 수행하는 방식을 자동화할 수 있다.
>>
>> 쿠버네티스의 오퍼레이터 패턴 개념을 통해 쿠버네티스 코드 자체를 수정하지 않고도 컨트롤러를 하나 이상의 사용자 정의 리소스(custom resource)에 연결하여 클러스터의 동작을 확장할 수 있다. 오퍼레이터는 사용자 정의 리소스의 컨트롤러 역할을 하는 쿠버네티스 API의 클라이언트다.


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
     
  
## 설치 MiddleWare 소개

- Jenkins
    - Docker in Docker Image를 Side Container로 사용하여 Docker Socket Volume으로 공유
    - Local Repository 설정을 위한 command 추가
    - Volume 설정이 불가한 /usr/local/bin 경로는 Container 생성시 Post-start를 통해 경로로 mv
- Gitlab
    - gitlab-ce:latest Image를 가지고, 개별 pvc만 2개를 생성하여 배포
    - gitlab 공식 차트는 NodePort 사용시, css를 읽지 못하고 사용하지 않는 컴포넌트이 많이 배포됨
 
## Operator 활용, 사용 순서
~~~
# Operator Source Build
./build-deploy.sh build

# docker Image save -> ssh send Image Registry Server
./build-deploy.sh save
./build-deploy.sh send

# Image Registry Server
docker load -i yjops.tar
docker push ${REGISTRY}/${PROJECT}/yjops:${VERSION}

# Kubernetes Master Server
kubectl apply -f yjopsCRD.yaml
kubectl apply -f yjops.yaml
kubectl apply -f sample.yaml
~~~
