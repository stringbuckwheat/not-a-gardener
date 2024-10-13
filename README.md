


![logo](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/f10d393b-13cd-4b9e-9294-47fef4af4dfd)

## 목차
1. not-a-gardener🌿?
   * 프로젝트 소개
   * 개발 스택 
   * ERD
2. 주요 기능
   * 물주기 계산 핵심 로직🚰
     * Watering Code
     * After Watering Code
   * 커뮤니티 
   * Gradle, Jenkins를 통한 AWS EC2 자동 빌드/배포
3. 트러블 슈팅
  - `Notifiable` 인터페이스를 사용한 알림 시스템 설계
  - QueryDsl, DTO Projection 도입으로 개발 생산성 및 응답 속도 개선
4. 구현 상세

<br>

# 1. not-a-gardener🌿?

> '진정한 가드너'가 되지 못한 이들을 위한 실내 식물 돌보기 서비스

기존의 식물 돌보기 서비스는 <u>사용자가 직접 관수 주기를 입력하게 하거나 평균 관수 주기를 계산</u>해줍니다.       
**이러한 접근은 식물을 기르는 데에 적합하지 않습니다!** 

계절/환경의 변화, 식물의 성장등을 고려했을 때, **가장 최근의 관수 주기**가 제일 중요합니다.       
not-a-gardener🌿는, 동적인 관수 주기를 자동으로 계산하여 최적의 관수 알림을 제공합니다.

> 90+개의 API 엔드포인트, 280+개의 테스트 코드 규모의 프로젝트로 종합적인 식물 관리 기능과 커뮤니티 기능을 포함합니다.     

배포중인 서비스는 [이곳](http://not-a-gardener.xyz/)에서, API 명세서는 [이곳](http://not-a-gardener.xyz/swagger-ui/index.html)에서 보실 수 있습니다.

<br>

## ✔ 개발 스택
* `Spring Boot(3.1)`
  * Spring Security
  * JWT
  * OAuth2
  * WebSocket
  * Swagger
* `JPA/Hibernate`, `QueryDsl`
* `MariaDB(11.5)`, `Redis`
* `React(18.2)`, `Redux`
* `Jenkins`, `AWS EC2`

<br>

## ✔ ERD
![garden](https://github.com/user-attachments/assets/98b7761b-ae5c-4445-bf37-fdd380dc580e)

<br>

# 2. 주요 기능
## 1) 물주기 계산 핵심 로직🚰
### - `Watering Code`
* 각 식물의 실제 관수일과 최근 관수 간격을 사용해, 마지막으로 물을 준 날로부터 얼마나 시간이 지났는지 계산
  * 이 정보를 통해 다음 물을 줘야 할 날짜까지 남은 일수를 계산 
* 위에서 계산한 정보를 바탕으로 물주기 상태 코드 결정

```java
public enum WateringCode {
    LATE_WATERING("LATE_WATERING"), // 관수 날짜 놓침
    NOT_ENOUGH_RECORD("NOT_ENOUGH_RECORD"), // 관수 기록 부족
    THIRSTY("THIRSTY"), // 오늘 물 줄 날짜
    CHECK("CHECK"), // 내일 관수 예정일(오늘 상태 체크 필요)
    LEAVE_HER_ALONE("LEAVE_HER_ALONE"), // 할 일 없음
    WATERED_TODAY("WATERED_TODAY"), // 오늘 이미 물 줌
    YOU_ARE_LAZY("YOU_ARE_LAZY"); // 오늘 물주기를 미룸
}
```

* 오늘 물을 줄 가능성이 있는 식물이라면, 최근 비료/살충제 사용 기록을 조회하여 적절한 약품 추천

![스크린샷 2024-10-10 20 07 20](https://github.com/user-attachments/assets/fbfcd771-2792-49a3-853c-a4bd28f8d878)

* `안 말랐어요`, `물 주기를 미룰래요` 등의 기능으로 물주기 간격 변동 가능성을 세심히 포착 

<br>


### - `After Watering Code`
![aftwtr](https://github.com/user-attachments/assets/a387c97d-d63f-4822-b931-eeb8e72913c6)

* 사용자의 관수 기록 시 관수 주기를 비교하여 `AfterWateringCode`를 계산, 식물의 성장/건강 상태 보고

<br>

## 2) 커뮤니티
`팔로우`, `게시물`, `댓글`, `좋아요`와 같은 커뮤니티 핵심 기능을 구현하고, `WebSocket`을 사용하여 실시간 알림을 제공

<br>

## 3) Gradle, Jenkins를 통한 AWS EC2 자동 빌드/배포
### - 통합 빌드 프로세스
* React 애플리케이션 빌드 후 빌드된 정적 리소스를 Spring Boot의 `src/main/resources/static` 디렉토리로 복사하여 백엔드와 통합
* 이후 Spring Boot 애플리케이션을 빌드하여 React 애플리케이션의 정적 리소스가 통합된 최종 프로덕션용 JAR 파일 생성

### - Jenkins 파이프라인을 통한 자동화된 빌드, 배포
* 최종 JAR 파일을 원격 서버에 배포
* `pgrep` 명령어를 사용해 애플리케이션 PID 검색, `kill -15`를 통한 애플리케이션 종료
* `nohup` 명령어를 통해 백그라운드에서 애플리케이션 재실행

<br>

# 3. 트러블슈팅

## 1) `Notifiable` 인터페이스를 사용한 알림 시스템 설계
### - 문제 상황
* `팔로우`, `게시글`, `댓글`, `좋아요` 등 여러 엔티티 신규 저장 시 알림 처리 로직 중복
* 새로운 타입의 알림 기능이 추가될 때마다 코드를 수정/추가 해야 하는 상황 발생

### - 해결 방안
![alarm_uml](https://github.com/user-attachments/assets/647653c8-62e2-424c-bf06-2ddc8e3403b9)

* 생성 시 알림을 보내야 할 엔티티에 `Notifiable` 인터페이스를 implements 시켜 **알림 관련 기능 추상화**
  * `NotificationService`에서 구체적인 엔티티의 타입에 상관없이 Notifiable 타입으로 웹소켓 알림 통합 처리가 가능해짐

### - 결과
* **유연성**: 다양한 엔티티(Comment, Follow, Post, Like)를 동일한 방식으로 처리할 수 있게 됨
  * 알림 대상 엔티티가 `Notifiable` 인터페이스를 구현하기만 하면, 기존 로직을 수정할 필요 없이 쉽게 확장
* **코드 중복 감소**: 여러 엔티티에 공통된 로직을 Notifiable 인터페이스로 추상화함으로써 중복 코드 감소
  * ex) NotificationService에서 엔티티에 따라 알림을 보내는 유사한 코드들을 작성할 필요가 없어짐

<br>

## 2) `QueryDsl`, `DTO Projection` 도입으로 개발 생산성 및 응답 속도 개선
### - 배경
* 개발 초기 느린 응답 속도에 대한 고민
  * 기존 코드는 주로 쿼리 메소드에 의존하여 전체적인 성능 저하가 발생했다고 판단
* 길고 복잡한 쿼리 메소드로 인해 가독성이 떨어져 각 메소드의 역할과 동작을 파악하기 어려움 

### - 해결
* `QueryDsl`과 `DTO Projection`을 도입하여 쿼리를 더 명확하고 효율적으로 작성하도록 리팩토링
  * `QueryDsl`을 사용하여 동적 쿼리 작성이 용이해졌으며, 메소드 이름을 간결하게 하여 재사용성 높임
  * `DTO Projection`을 통해 엔티티 전체를 가져오는 대신 필요한 데이터만 조회할 수 있도록 개선 
* 프론트엔드 영역에서는 `Redux`를 도입하여 상태 관리 개선
  * 이전에는 `useState`만 사용했으나, Redux를 통해 **데이터 흐름을 개선**하고 컴포넌트 간의 **상태 공유를 용이**하게 개선

<br>

# 4. 구현 상세
## ✔ 로그인: 기본/소셜로그인, 최근 로그인 알림
![로그인_최근 로그인](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/d6d883fd-612c-4f5e-bad0-3aa987cd513c)
- **기본 로그인과 소셜 로그인**을 모두 지원합니다.   
- 소셜 로그인 이용 시 **가장 최근 로그인한 서비스**를 알려줍니다.

<br>

## ✔ 메인 페이지: 물주기 알림
![메인페이지_후버, 알림](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/0825bbeb-1801-4df3-93d6-f07549621344)

- **오늘 할 일이 있는 식물들, 유저가 등록한 루틴, 물주기 정보가 입력되지 않은 식물들**을 모여 보여줍니다. 
- 물을 줘야할 식물에 대해서 <u>맹물을 줘야하는지, 비료/약품을 줘야하는지</u>를 알려줍니다.
  - 물주기를 놓쳤을 시 비료를 주지 말라고 경고하여 식물의 손상을 방지합니다.
- 물주기 알림에 커서를 올리면 **'안 말랐어요, 물을 줬어요, 미룰래요'** 등의 현상을 기록할 수 있고,     
- '물을 줬어요'를 클릭 시 맹물/약품을 줬는지 선택할 수 있습니다.

<br>

## ✔ 관수 후 간격 변동 알림
![afterWateringMsg](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/8368d4bf-298e-4071-9f16-c4087a9b9487)
- **물주기 기록 시 <u>관수 간격에 대한 알림 메시지와 예상되는 이유</u>를 리턴**합니다.

<br>

## ✔ 식물 리스트: 전체 식물의 물주기 상태
![전체 식물 리스트](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/a7a8dd55-47c9-43da-bf31-5df1ec4c5a8e)

- (할일이 있는 식물들만 보여주는 메인 페이지와 달리) **전체 식물의 물주기 상태**를 확인할 수 있습니다.
  - 상태 아이콘에 커서를 올릴 시, <u>'이 식물은 지금 행복해요. 가만히 두세요'</u>와 같은 안내 메시지를 보여줍니다.
- 이름, 종 같은 기본 정보 외에도 마지막 관수일 등을 확인할 수 있습니다.

<br>

## ✔ 식물 상세 정보: 그간의 물주기 기록
![식물 디테일](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/a91a0f12-4f8a-42de-b4b8-0cb8192a7146)

- 기본 정보 외에 **최근 물주기 간격, 마지막 관수일, 상태 메시지, 물 준 횟수** 등을 보여줍니다.
- <u>**지금껏 기록한 관수 일자와 약품 시비 여부, 과거의 물주기 간격**</u>을 확인할 수 있습니다.
- 물주기 기록 우측의 버튼을 통해 **과거 물주기 기록을 수정/삭제**할 수 있습니다.

<br>

## ✔ 식물 상세 정보: 물주기 추가

![식물 페이지에서 물주기](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/7c15a281-1e1b-479f-a278-adff263a3ec7)
- 식물 페이지에서 물주기 추가가 가능하도록 구현하여 편의성을 높였습니다.

<br>

## ✔ 비료/살충제/살균제: 시비 일자 알림

![약품 정보와 준 내역](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/1b8faee0-7395-4122-86ea-bba412fc1230) | ![약품리스트](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/9f590366-4c44-4d82-90d3-1ec74d30b05e)
--- | ---

- 약품 정보와 권장 주기를 입력하면 **식물 별 시비 일자**를 알려줍니다.
- 상세 정보 페이지에서 **해당 약품을 준 기록**을 확인할 수 있습니다.
- <u>논리 삭제</u>를 적용하여 **약품을 삭제해도 물주기를 보존**합니다.

<br>

## ✔ 물주기 캘린더: 전체 물 준 기록 날짜별 모아보기
![물주기 캘린더](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/fa9bfb8a-d73a-445a-bd2d-851d6591ccd2)
- 물을 준 기록을 **달력** 형태로 모아 확인할 수 있습니다.
- 상단 세부 정보에서 해당 식물 페이지로 이동하거나 물주기를 삭제할 수 있습니다.
- <u>해당 날짜에 물주기 기록</u>을 추가할 수 있습니다.

<br>

## ✔ 장소: 실내 장소 구분 기록

![장소 디테일](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/3a3c605f-de0a-495a-a48f-df182cdd5806) | ![장소 리스트](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/a88854f2-de8b-447f-ae28-8ed8fff4187b)
--- | ---

- 장소 정보(이름, 야외/실내/베란다, 식물등 사용 여부)를 입력하여 **장소별로 식물을 관리**할 수 있습니다.
- 해당 장소에 속한 <u>식물의 개수와 목록</u> 등을 확인할 수 있습니다.

<br>

![장소 페이지에서 식물 추가](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/b424f5cc-5e9b-4f33-a914-1701409d5c8d) | ![장소페이지_이 장소로 식물 옮기기](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/faf3fb1b-47d9-475b-b1ab-aa4bca427d40)
--- | ---

- **장소 페이지 내에서 '새 식물 추가, 다른 장소의 식물 이동' 기능을 지원**합니다.

<br>

## ✔ 루틴, 목표 기능
![루틴, 목표](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/acf1f1ba-f84d-4b99-a14a-ae7289f431d5)
- n일 마다 반복해야할 일인 루틴, 가드너로서 꿈꾸고 있는 목표 등을 설정할 수 있습니다.
- 루틴은 해당 날짜가 오면 메인 페이지에서도 확인할 수 있습니다.

<br>

## ✔ 커뮤니티 기능

* `Follow`
  * 사용자가 다른 사용자를 팔로우할 수 있는 기능
  * 자기 자신을 팔로우할 수 없도록 제한 
* `Post`
  * 게시글 기능, 이미지 첨부 가능
* `Comment`
  * 댓글 기능, self join을 통해 대댓글 작성을 구현하여 대화형 소통 가능 
* `Like`
  * 그냥 좋아요
* `Notification`
  * 사용자가 Follow, Post, Comment, Like와 같은 활동을 할 때 알림 제공
    * `WebSocket` 알림: 로그인한 사용자에게 웹소켓을 통해 실시간으로 알림 전송
    * 알림 정보는 `MariaDB`에 저장하여 사용자가 나중에 다시 확인할 수 있도록 관리