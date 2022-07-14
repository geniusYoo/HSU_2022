# SQL STUDY

강의 번호: Database

## SQL, Structed Query Language

- 데이터 정의 언어(DDL, Data Definition Language)
    - variables
        - 문자 : char(길이가 nbyte 고정길이), varchar(최대길이 nbyte 가변길이)
        - 숫자 : int, float, number (int는 메모리 공간을 많이 낭비하기 때문에 number 쓰기)
            - int = number(38), number는 한,영 상관없이 한 글자가 한 공간 차지함
        - 날짜, 시간 : date .. (년,월,일,시간,분,초)
    - 테이블 생성
        
        **`create table** department ( dept_id varchar(20) not null );`
        
    - 기본키/외래키 설정
    
    ```sql
    create table department (
    dept_id varchar(20), // 여기에 not null 안써도 기본키이기 때문에 상관없음 
    dept_name varchar(20) not null,
    office varchar(20)
    constraint pk_department primary key(dept_id) 
     // 제약조건, 에러 시 이 식을 알려줌, student 테이블의 기본키인 dept_name을 department 테이블의 외부키로 지정 (가정한 것)
     // 외래키로 참조하는 키는 반드시 다른 테이블의 기본키를 참조해야 함, 그리고 필드명은 달라도 상관X
    	constraint fk_department foreign key(dept_name) references student(dept_name)
    );
    ```
    
    - 테이블 삭제 : `drop table department`
    - 테이블 수정
        - 추가 : `alter table student add age int`
        - 삭제 : `alter table student drop column age`
        
- 데이터 조작 언어 (Data Manipulation Language, DML)
    - 레코드 삽입 : **`insert into** department **values** (’923’, ‘컴퓨터공학과', 유영재)`
    - 레코드 수정
        
        ```sql
        update professor
        set position='교수',dept_id='923'
        where name='고희석'
        ```
        
    - 레코드 삭제
        
        ```sql
        delete from professor
        where name='김태석'
        ```
        
    - **레코드 검색 *****
        - select 기본 구조
            
            ```sql
            select student.stu_id
            from student, department
            where student.dept_id = department.dept_id and student.year=3 and department.dept_name='컴퓨터공학과'
            // where 절에서 student 테이블의 dept_id는 departement 테이블의 dept_id를 외래키로 참조하고 있기 때문에 관련 있는 필드들임. 
            // 두 테이블 이상 카티션 프로덕트를 할 경우, 관련있는 필드끼리 묶어주는 게 99% 이기 때문에 기억하자!
            ```
            
        - 중복 제거 : `select **distinct** adress from student`
        - 레코드의 순서 지정
            
            ```sql
            select name, stu_id
            from student
            where year=3 or year=4
            order by name desc, stu_id
            // defalut는 오름차순, desc 쓰면 내림차순
            ```
            
        - LIKE 연산자
            - _ : 임의의 한개 문자
            - % : 임의의 여러개 문자
            
            “김 씨 성을 가진 학생들을 찾아라”
            
            ```sql
            select *
            from student
            where name like '김%'
            ```
            
        - 집합 연산
            - union, 합집합 : 중복되는 값은 한 번만 출력, 중복 제거 안하고싶으면 union all
                
                ```sql
                select name from student
                union 
                select name from professor
                ```
                
            - intersect, 교집합
                
                “컴퓨터공학과 학생들 중 교과목에 상관없이 학점을 A+ 받은 학생들의 학번을 찾아라”
                
                select s.stu_id
                
                from student s, department d, takes t
                
                where s.dept_id = d.dept_id and t.stu_id = s.stu_id
                
                and dept_name = ‘컴퓨터공학과' and grade=’A+’
                
                — 이거를 ... intersect를 이용하면 ?
                
                ```sql
                select stu_id
                from student s, department d
                where s.dept_id = d.dept_id and dept_name='컴퓨터공학과'
                intersect
                select stu_id
                from takes
                where grade='A+'
                ```
                
            - minus, 차집합
                
                “산업공학과 학생들 중에서 한번도 A+를 받지 못한 학생들의 학번을 찾아라”
                
                ```sql
                select stu_id
                from student s, department d
                where s.dept_id = d.dept_id and dept_name='산업공학과'
                minus
                select stu_id
                from takes
                where grade='A+'
                ```
                
        - 집계 함수 (count, sum, avg, max, min)
            - count : 데이터의 개수
                
                `select**count(*)** —> 레코드의 개수 리턴`
                
                `select**count(dept_id)** —> 필드에 값이 몇개 있는지 리턴`
                
                `from student`
                
                `where year=3`
                
            - sum : 필드에 저장된 값의 합
                
                `select sum(2022 - year_emp) from professor`
                
            - avg : 필드의 저장된 값들의 평균
                
                `select avg(2022 - year_emp) from professor`
                
            - max / min : 필드 값들 중 최대/최소값
                
                `select max/min(sal)`
                
                `from emp e, dept d`
                
                `where e.deptno = d.deptno and dname = ‘ACCOUNTING’`
                
            
            ***** select 절에 집계 함수와 다른 필드를 같이 사용할 수 없음. 그 때문에 나온 것이 ...**
            
            - group by
                
                ```sql
                select dname, count(*), avg(sal), max(sal), min(sal)
                from emp e, dept d
                where e.deptno = d.deptno
                group by dname
                // group by 절 안 쓰면 오류
                // 집계함수와 같이 쓰는 피드명은 group by 절의 그룹명과 같아야 함
                ```
                
            - having : 집계 함수에 대한 조건
                
                ```sql
                select dname, count(*), avg(sal), max(sal), min(sal)
                from emp e, dept d
                where e.deptno = d.deptno
                group by dname
                having count(*) >= 5 // 직원 숫자가 5명이 부서에 대해서만 부서별로 직원수, 급여 ,,, 출력
                ```
                
            - 중첩 질의
                - in, not in
                    
                    “301호 강의실에서 개설된 강좌의 과목명을 출력”
                    
                    ```sql
                    select title
                    from course
                    where course_id in
                    	(select distinct course_id
                    	 from class
                    	 where classroom='301')
                    ```
                    
                    “2012년 2학기에 개설되지 않은 강좌의 과목명을 출력”
                    
                    ```sql
                    select title 
                    from course
                    where course_id not in
                    	 (select distinct course_id
                    		from class
                    		where semester=2 and year=2012)
                    ```
                    
                - exist / no exists
                    
                    “301호 강의실에서 개설된 강좌의 과목명을 출력”
                    
                    ```sql
                    select title
                    from course
                    where exists
                    	(select *
                    	 from class
                    	 where classroom='301호' and course.course_id = class.course_id)
                    ```
                    
                    “2012년 2학기에 개설되지 않은 강좌의 과목명을 출력” → 이걸 not exists로 표현하면 ..
                    
                    ```sql
                    select title 
                    from course
                    where no exists
                    	(select *
                    	 from class
                    	 where semester=2 and year=2012 and course.course_id = class.course_id)
                    ```
                    
- 뷰
    - 뷰 : 기존의 테이블로부터 생기는 가상의 테이블
        
        → 테이블의 일부 내용을 숨겨서 보안의 효과 / 복잡한 질의를 간단하게
        
        - 뷰 생성 : `create or replace view table as ..`
            
            “takes 테이블에서 grade 필드를 제외한 나머지 필드들로만 구성된 뷰를 생성”
            
            ```sql
            create or replace view v_takes as 
            select stu_id, class_id
            from takes
            with read only // 읽기 옵션으로 설정, insert, update, delete 사용 불가능
            ```
            
            “student 테이블에서 컴퓨터공학과 학생들에 대한 레코드만 추출”
            
            ```sql
            create or replace view cs_student as
            select s.stu_id, s.resident_id, s.name, s.year, s.address, s.dept_id
            from student s, department d
            where s.dept_id = d.dept_id and d.dept_name='컴퓨터공학과'
            ```
            
        - 뷰 사용 : `insert into v_takes values (’1292502’, ‘C101-01’)`
        - 뷰 삭제 : `drop view v_takes`
        
- 무결성 제약
    - 무결성 제약 예시
        - 학생은 하나의 학과에 소속된다.
        - 하나의 교과목은 각 학기마다 두 강좌 이하만 개설할 수 있다 ... 등등
    - 기본적 무결성 제약
        - 기본키 무결성 제약
            
            기본키는 널 값 X, 기본키 값이 동일한 레코드가 하나의 테이블에 동시에 두 개 이상 X
            
            - 기본키 제약식 `constaint pk_student primary key (stu_id)`
            - 테이블 생성 당시에 안했으면 ..
                - 추가 `alter table student add constraint pk_student primary key (stu_id)`
                - 삭제 `alter table student drop constraint pk_student`
    - 참조 무결성 제약 (외래키 관련)
        - 외래키를 적절히 정의하는 것
        - 외래키 정의 `constraint fk_dept foreign key(dept_id) references department(dept_id)`
    - 테이블 무결성 제약
        - not null
        - UNIQUE : 기본키와 유사하게 해당 필드가 테이블 내에서 중복된 값을 갖지 않고 유일하게 식별되도록 함. 결국 후보키
            - `constraint uc_rid unique (resident_id)`
            - 테이블 생성 이후에 별도로 지정할 때
                - 추가 : `alter table student add constraint uc_rid unique (resident_id)`
                - 삭제 : `alter table student drop constraint uc_rid`
        - CHECK : 데이터 타입을 지정한 뒤 더욱 세부적으로 범위를 지정할 때
            - “위의 테이블에서는 year의 값이 1 이상, 4 이하의 정수 범위를 벗어날 수 없다”
                
                `constraint chk_year check (year>=1 and year <=4)`
                
            - 테이블 생성 이후에 ..
                - 추가 : `alter table student add constraint chk_year check (year>=1 and year<=4)`
                - 삭제 : `alter table student drop constraint chk_year`
        - DEFAULT : 레코드 삽입 시 필드에 대한 값이 정해지지 않았을 경우, 널 대신 디폴트 값을 넣어줄 때 사용
            - `default`
            - 테이블 생성 이후에 ..
                - 추가 : `alter table student alter column year set default 1`
                - 삭제 : `alter table student alter column year drop default`
    - 기타 무결성 제약
        - ASSERTION
            
            ```sql
            create assertion assert1 check
            	(not exists (select count(*)
            							 from class
            							 group by year, semester, prof_id) < 3 )
            	)
            ```
            
- 데이터베이스 보안
    - 권한 제어
        - GRANT : 권한을 부여하는 명령, 관리자/테이블 생성한 소유자만 할 수 있음
            - `grant select on student to kim` → kim 이라는 사용자에게 student 테이블에서 select 할 수 있는 권한을 줌
            - 모든 사용자에게 권한 부여 : `grant select on student to public`
            - 모든 종류의 권한을 한번에 부여 : `grant all privileges on student to lee`
        - WITH GRANT OPTION : 부여받은 권한을 다른 사용자에게 전파
            - `grant select on student to kim with grant option`
        - REVOKE : 다른 사용자에게 부여한 권한 회수
            - `revoke select on student to kim`
    - 롤 : 권한 별로 사용자 그룹을 만들어 그룹에 권한을 부여, 관리자만 부여 가능
        - `create employee` → employee 롤 만들기
        - `grant employee to lee, kim` → employee 롤에 lee, kim 추가
        - `grant select, insert on student to employee` → employee 롤에 select, insert 권한 부여
        - `revoke employee from lee` → lee를 롤에서 배제
        - `drop role employee` → employee 롤 삭제
    - 뷰에서 권한 제어
        
        ```sql
        create view junior 
        as select stu_id. name, year, dept_id
        	 from student
        	 where year=3
        
        ---select 권한 부여
        grant select on junior to kim
        ```