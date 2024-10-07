
![logo](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/f10d393b-13cd-4b9e-9294-47fef4af4dfd)

배포중인 서비스를 확인하고 싶으시다면, [이곳](http://not-a-gardener.xyz/)을 클릭하세요!     
API 명세서는 [이곳](http://not-a-gardener.xyz/swagger-ui/index.html)에서 보실 수 있습니다.


# 목차
- [1. not-a-gardener🌿?](#1-not-a-gardener)
  * [개발스택](#-개발스택)
  * [디렉토리 구조](#-디렉토리-구조)
- [2. 구현 목록 / 프로젝트 결과물](#2-구현-목록--프로젝트-결과물)
  * [로그인: 기본/소셜로그인, 최근 로그인 알림](#-로그인--기본소셜로그인-최근-로그인-알림)
  * [메인 페이지: 물주기 알림](#-메인-페이지--물주기-알림)
  * [관수 후 간격 변동 알림](#-관수-후-간격-변동-알림)
  * [validation에 따른 submit 버튼 비활성화](#-validation에-따른-submit-버튼-비활성화)
  * [식물 리스트: 전체 식물의 물주기 상태](#-식물-리스트--전체-식물의-물주기-상태)
  * [식물 상세 정보: 그간의 물주기 기록](#-식물-상세-정보--그간의-물주기-기록)
  * [식물 상세 정보: 물주기 추가](#-식물-상세-정보--물주기-추가)
  * [비료/살충제/살균제: 시비 일자 알림](#-비료살충제살균제--시비-일자-알림)
  * [물주기 캘린더: 전체 물 준 기록 날짜별 모아보기](#-물주기-캘린더--전체-물-준-기록-날짜별-모아보기)
  * [장소: 실내 장소 구분 기록](#-장소--실내-장소-구분-기록)
  * [루틴, 목표 기능](#-루틴-목표-기능)

<br>

# 1. not-a-gardener🌿?

> '진정한 가드너'가 되지 못한 이들을 위한 실내 식물 돌보기 서비스

실내 가드너를 위해 물을 줄 날짜를 알려줍니다!

기존의 식물 돌보기 서비스는 <u>사용자가 직접 관수 주기를 입력하게 하거나 평균 관수 주기를 계산</u>해줍니다.       
**이는 식물을 기르는 데에 적합하지 않습니다!** 

계절/환경의 변화, 식물의 성장등을 고려했을 때, **가장 최근의 관수 주기**가 제일 중요합니다.       

not-a-gardener🌿는, 관수 기록 시 직전 물 준 날짜와 비교해서 최근 관수 주기를 계산하고 기록합니다.      
물주기 예상 날짜의 하루 전에 알림을 주어 주기 단축 가능성을, ‘화분이 안 말랐어요’ 기능 등으로 주기가 늘어날 가능성을 캐치합니다.
식물에 물을 줄 때마다 **물주기 변동 여부와 (변동 시) 예상되는 이유**를 알려주어 체계적인 실내 식물 관찰, 기록이 가능합니다!

<br>

## ✔ 개발스택
<div align="left">
  <img src="https://img.shields.io/badge/Spring%20Boot%20(3.1)-6DB33F?style=flat-square&logo=Spring%20Boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/JPA-yellow?style=flat-square"/>
  <img src="https://img.shields.io/badge/QueryDsl-purple?style=flat-square"/>
  <img src="https://img.shields.io/badge/React%20(18.2)-61DAFB?style=flat-square&logo=React&logoColor=black"/>
  <img src="https://img.shields.io/badge/MariaDB%20(11.5)-003545?style=flat-square&logo=MariaDB&logoColor=white"/>
  <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/>
  <img src="https://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white"/>
</div>

이후 <img src="https://img.shields.io/badge/AWS%20EC2-232F3E?style=flat-square&logo=Amazon%20AWS&logoColor=white"/>
에 배포하였습니다.

<br>

## ✔ ERD
![garden](https://github.com/user-attachments/assets/98b7761b-ae5c-4445-bf37-fdd380dc580e)

# 2. 구현 목록 / 프로젝트 결과물

API 명세서는 [이곳](http://not-a-gardener.xyz/swagger-ui/index.html)에서 보실 수 있습니다.

<br>

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

## ✔ validation에 따른 submit 버튼 비활성화
![validation_2](https://github.com/stringbuckwheat/not-a-gardener/assets/104717358/53e6699d-4c33-41df-a739-e08ffd5309ed)
- **필수 값이 입력되지 않았을 시 submit 버튼을 비활성화**합니다.

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
