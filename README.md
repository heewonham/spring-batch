# karrotpay-249-50578171003-62308593003
<br>
<br>
프로젝트 명 : 결제 시스템과 카드 시스테 사이의 정합성 검증 프로그램
<br><br>
주요 내용:<br>
1. 배치 시스템 - 구현완료<br>
2. rest API - 구현완료<br>
3. 테스트 코드 - 미구현<br>
4. docker-compose - 미완성<br>
<br>
<br>
배치 시스템 :<br>
1개의 job과 3개의 step으로 구성<br>
ㄴpaymentSystemJob<br>
...ㄴcardCsvFileReaderStep : 카드시스템 csv 파일을 읽어 DB에 저장한다<br>
......ㄴcardCsvReader.cardCsvFileItemReader() : 읽기<br>
......ㄴjpaItemWriters.jpaCardItemWriter() : 쓰기<br>
...ㄴpaymentCsvFileReaderStep : 결제시스템 csv 파일을 읽어 DB에 저장한다.<br>
......ㄴpaymentCsvReader.paymentCsvFileItemReader() : 읽기<br>
......ㄴjpaItemWriters.jpaPaymentItemWriter() : 쓰기<br>
...ㄴsystemIntegrityStep : DB에 저장된 카드시스템의 정보와 결제 시스템 사이의 정합성 검증 배치 프로그램을 돌리고 DB에 저장한다.<br>
......ㄴjdbcCursorItemReader() : 읽기 - jdbc cursor reader 이용<br>
......ㄴconverterProcessor.processor() : 프로세서 (데이터 정제 - 밑에서 자세히 설명)<br>
......ㄴjpaItemWriters.writerList() : 쓰기 DB 저장<br>
<br><br>
프로세서 설명 : <br>


<br>
<br>
restApi :<br>
* parameter name : <br>
	* verifiedDate(검증일자), type(부적합타입), date(결제,취소일자)<br>
* /integrity/search?verified=20220512&type=MISSING&date=20220513 으로 검색가능 <br>
* repository : querydsl로 검색 (동적쿼리)<br>
<br>
<br><br>
후기 : 비록 테스트 코드 작성, 도커 컴포즈 실행에서 완성하지 못했지만 짧은 시간 동안 스프링 배치에 대해 공부하고, 결제 정합성 검증에 대해 생각해 볼 수 있어서 할 수 있어서 좋았습니다. 배치 프로그램은 테스트 코드가 필수라는 것을 알지만 시간이 부족해 진행하지 못한 점이 아쉽습니다. 또한 불필요한 코드들, 중복들이 많아 리팩토링절실했지만 시간이 부족해 수정하지 못했습니다. 소중한 기회 주셔서 감사합니다.
<br>
<br>
<br>
<br>
