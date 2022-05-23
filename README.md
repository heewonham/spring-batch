# karrotpay-249-50578171003-62308593003
<br>
<br>
## 프로젝트 명 : 결제 시스템과 카드 시스테 사이의 정합성 검증 프로그램
<br><br>
### 주요 내용:<br>
1. 배치 시스템 - 구현완료<br>
2. rest API - 구현완료<br>
3. 테스트 코드 - 미구현<br>
4. docker-compose - 미완성<br>
<br>
<br>
### 배치 시스템 :<br>
1개의 job과 3개의 step으로 구성<br>
ㄴpaymentSystemJob<br><br>
...ㄴcardCsvFileReaderStep : 카드시스템 csv 파일을 읽어 DB에 저장한다<br>
......ㄴcardCsvReader.cardCsvFileItemReader() : 읽기<br>
......ㄴjpaItemWriters.jpaCardItemWriter() : 쓰기<br><br>
...ㄴpaymentCsvFileReaderStep : 결제시스템 csv 파일을 읽어 DB에 저장한다.<br>
......ㄴpaymentCsvReader.paymentCsvFileItemReader() : 읽기<br>
......ㄴjpaItemWriters.jpaPaymentItemWriter() : 쓰기<br><br>
...ㄴsystemIntegrityStep : DB에 저장된 카드시스템의 정보와 결제 시스템 사이의 정합성 검증 배치 프로그램을 돌리고 DB에 저장한다.<br>
......ㄴjdbcCursorItemReader() : 읽기 - jdbc cursor reader 이용<br>
......ㄴconverterProcessor.processor() : 프로세서 (데이터 정제 - 밑에서 자세히 설명)<br>
......ㄴjpaItemWriters.writerList() : 쓰기 DB 저장<br>
<br><br>
### 프로세서 설명 : <br>
카드 시스템 데이터를 기준으로 결제 시스템의 누락 여부를 판단했습니다.<br>
카드 시스템의 paykey를 통해 결제 시스템의 정보를 불러오고 PayStatus 순서에 따라 정렬하여 진행했습니다.<br>
하나의 결제 key의 경우에도 여러개의 부적합 케이스가 있을 수 있기 때문에 List로 반환했습니다.<br><br>
1. 데이터 누락, 유실 :<br> 
- 저는 데이터 누락을 과정의 유실로 보고 판단했습니다.
- 결제시스템의 데이터가 1개인 경우 누락이 발생한 상황입니다. 왜냐하면 카드 시스템에 기록이 있다는 것은 approve 이상이 진행되었다는 것을 의미하기 때문에 ready상태만 가지고 있다는 것은 누락되었음을 보여주기 때문입니다.<br>
- 결제시스템 데이터의 2번째가 approve가 아님은 누락되었음을 의미합니다. 왜냐하면 앞서말씀드렸듯이 카드시스템 기록이 있다는 것은 approve를 전제로 깔고 가기 때문에 approve 가 없다는 것은 누락되었음을 보여줍니다.<br>
- 결제시스템 데이터가 3개이면서 cancel이 아닌 경우 누락 되었음을 의미합니다. error도 가능하지만 세번째 데이터가 error라는 것은 승인이 되지 않았음을 의미하기 때문에 cancel이 누락되었음을 암시하기 때문입니다.<br>
- 결제시스템 데이터가 4개이면, 3번째는 cancel, 4번째는 error가 와야합니다.<br> 
2. 결제, 취소 요청 금액 일치 : <br>
- 결제, 취소 요청 금액 일치여부이기 때문에 취소 후 error에 상관없이 approve 상태와 cancel 상태의 금액을 확인하고 판단내렸습니다.<br>
3. 결제 상태 일치 : <br>
- 정렬한 결제시스템 데이터의 마지막이 approve이면 카드시스템은 승인 상태이여하고,<br>
- 정렬한 결제시스템 데이터의 마지막이 cancel이면 카드시스템은 취소 상태이여하고,<br>
- 정렬한 결제시스템 데이터의 마지막이 error인 경우 2단계 전의 상태를 보고 카드 시스템과 상태를 맞춰야합니다. 만약 2단계 전이 approve라면 카드 시스템은 승인 상태이어야 합니다. 위의 3가지 조건을 벗어나는 경우 상태 불일치이며, 2단계 전이 ready라면 카드 시스템은 승인이 되지 않아야하기 때문에 상태 불일치로 판단했습니다. <br>
4. 결제 수단 일치 : <br>
- CREDIT_CARD와 CARD를 비교했습니다. contain을 통해 구현했습니다. <br>
5. 결제, 취소 일시 일치 :<br>
- 결제시스템 상태가 approve를 가지고 있다면 카드 시스템의 승인 날짜와 비교하고, 결제시스템 사이즈가 4개보다 작다. 즉 error가 없다는 전제하에 cancel을 가지고 있는 경우 카드 시스템의 취소 날짜와 비교했습니다. 
<br>
<br>
<br>
### RestAPI :<br>
* parameter name : <br>
	* verifiedDate(검증일자), type(부적합타입), date(결제,취소일자)<br>
* /integrity/search?verified=20220512&type=MISSING&date=20220513 으로 검색가능 <br>
* repository : querydsl로 검색 (동적쿼리)<br>
<br>
<br><br>
#### 후기 : 비록 테스트 코드 작성, 도커 컴포즈 실행에서 완성하지 못했지만 짧은 시간 동안 스프링 배치에 대해 공부하고, 결제 정합성 검증에 대해 생각해 볼 수 있어서 할 수 있어서 좋았습니다. 배치 프로그램은 테스트 코드가 필수라는 것을 알지만 시간이 부족해 진행하지 못한 점이 아쉽습니다. 또한 불필요한 코드들, 중복들이 많아 리팩토링절실했지만 시간이 부족해 수정하지 못했습니다. 소중한 기회 주셔서 감사합니다.
<br>
<br>
<br>
<br>
