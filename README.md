<p align="center">
  <img width="250" height="250" src="https://user-images.githubusercontent.com/78399203/175772507-d67cf29c-75b7-4ca6-8033-981d552b1071.png">
</p>

# WOOD - 클라우드 IDE 기반 과제 제출/채점 시스템
#### "과제를 할때마다 환경 세팅하는 것이 너무 불편해요🤷‍♂️️
#### "과제를 손수 채점하는 시간이 너무 아까워요🤷‍♀"

> 학교 과제를 할 때, 윈도우 환경에서 소스코드가 컴파일되지 않는 경우가 있었습니다. 따라서 리눅스 가상환경을 세팅하고 코드를 작성하기까지
시간을 많이 잡아 먹었습니다. 반대로 과제를 채점하는 입장이 되었을 때도 학생이 업로드한 소스코드를 다운받아 하나씩 실행하는
것도 굉장히 귀찮은 작업이었습니다.
> 
> 따라서 학생에게는 환경 세팅 작업을 덜어주고, 조교에게는 채점하는데 드는 시간을 덜어주는 WOOD를 개발하였습니다.

<br/>

### 🧑‍🎓👩‍🎓학생들은 제공받은 웹 IDE 환경에서 과제에만 집중할 수 있습니다.
![스크린샷 2022-06-25 오후 8 38 30](https://user-images.githubusercontent.com/78399203/175771925-2f529837-3963-4100-8d8d-9b15affef92d.png)

### 🧑‍🏫 교수는 채점 시스템의 채점 기능으로 채점하는데 시간을 투자하지 않아도 됩니다.
![professor](https://user-images.githubusercontent.com/78399203/175769329-ac8bf667-6e22-41ff-b6c9-7b94d4e49382.png)

(참고: 이 레포지토리는 백엔드 레포지토리이며 프론트엔드 레포지토리는 [여기](https://github.com/woodide/client) 로 이동하시면 됩니다.)

<br/>

## 시스템 구성
![아키텍쳐 다이어그램-Page-3](https://user-images.githubusercontent.com/78399203/175772069-9ccfade9-4185-484d-9e48-eb4a8c842d76.png)

<br/>

##  메인 DB 생성 및 접속방법
```sh
> docker-compose up -d
> docker ps # mariaDB 컨테이너 아이디 확인
> docker exec -it ${CONTAINER_ID} /bin/mariadb -u wood -p
```

<br/>

## ERD diagram

![ERD diagram](https://user-images.githubusercontent.com/78399203/175501454-03b1a9b4-b9eb-47ea-a5c2-6a44c1c30072.png)

<br/>

## 파트 및 개발계획

* 강선규: 인프라, VM 담당   
* 신기철: 회원가입 및 로그인 API 구현    
* 최승환: 컨테이너 발급, 과제 제출 및 채점 API 구현



