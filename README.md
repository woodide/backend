# 주제
클라우드 IDE 기반 과제 제출/채점 시스템     
##  메인 DB 접속
```sh
> docker-compose up -d
> docker ps # mariaDB 컨테이너 아이디 확인
> docker exec -it ${CONTAINER_ID} /bin/mariadb -u wood -p
```

## ERD diagram

1. Member     
2. Container     

더 추가할 예정     
                
![ERD diagram](https://user-images.githubusercontent.com/78399203/167126042-69d78ab3-2562-430e-9351-9f47fc9853ac.png)

## swagger ui

http://localhost:8080/swagger-ui.html#/     

## API

1. docker 컨테이너

    * 컨테이너 생성
    * 컨테이너 삭제

3. 회원가입/로그인



