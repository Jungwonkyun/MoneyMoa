230718

### ERD 회의

**member**

경험치(progress) 추가. rank 삭제



**follows**

to_member fk member랑 묶어있음

from_member fk



**feed**

작성일 추가



**feedComment**

작성일 추가

대댓글을 만들려면 고민을 해야한다.







**file**

feed의 이미지 url 관리

url의 path를 의존으로 묶을지? 아니면 join할지.

feed에 id가 있기 때문에 파일 피드 넘버를 피드 pk로 파일이랑 join시켜서 불러오기?

org_name과 new_name의 차이는 유저가 올린 이미지와 우리가 아마존에서 받는 이미지의 이름은 다르게 저장됨. 그래서 다르게 저장.



**challenge**

해시태그는 자유롭게 글 쓰고. 글 쓸때 챌린지를 선택.

챌린지에도 해시태그를 생성 할 수 있도록

전체 금액. 목표 금액. 생성 



피드 구성. 고려해야 할 게 많다. 이미지 업로드.

금융, 위키 구성 할 사람. 



**product**

예적금은 묶어서 같이 관리.

CMA는 따로 관리.

cma는 product_name, stock_name, min_rate, max_rate, memo

금액, 기간, 금리

히스토리까지만 저장하고 상세 페이지 가는 것. 금액, 기간, vue단에서  따로 관리. 프론트에서 리스트로 찜한거 보여줌. 상세 누르면 그 정보 사라질거임. 

피드보다 예적금이 더 어려울 것 같다.

likedproduct JOIN해서 예적금, 단리 복리까지



목욜 API 명세, 기능 명세 구체화 한 것 확인 받기. 피그마 디자인 금요일.

아키텍처 설계도 받아야함.


