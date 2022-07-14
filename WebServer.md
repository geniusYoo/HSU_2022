# WEB STUDY

강의 번호: WebServer

- 스크립트 태그
    - 스크립트 태그
        - 선언문 `<%! .. %>`
            - 자바 변수나 메소드 정의, 변수 정의하면 전역변수 됨
            - 서블릿으로 변환 시 메소드 외부에
        - 스크립틀릿 `<% .. %>`
            - 자바 로직 코드, 변수 정의하면 지역변수 됨
            - 서블릿으로 변환시 메소드 내부에
            - 각 행이 세미콜론으로 끝남
        - 표현문 `<%= .. %>`
            - 변수, 계산식 등 문자열 형태로 출력할 때
            - 연산된 결과를 출력
- 디렉티브 태그
    - 디렉티브 태그
        - page: 현재 JSP 페이지에 대한 정보를 설정하는 태그
            - language, contentType, pageEncoding, import
            - `**<%@page** contentType =”text/html:charset=UTF-8”%>`
            - `**<%@page** import=”java.util.Date”%>`
        - include : 현재 JSP 페이지의 특정 영역에 외부 파일의 내용을 포함하는 태그
            - 현재 JSP 페이지와 설정된 다른 외부 파일의 내용이 병합되어 출력
            - 페이지 내에 삽입
            - 페이지 내의 변수를 선언한 후 변수에 값을 저장, 정적 페이지에 사용
            - `**<%@include** file=”header.jsp” %>`
        - taglib
            - `**<%@taglib** .. %>`
- 액션 태그
    - 액션 태그
        - forward
            - 현재 JSP 페이지에서 다른 페이지로 이동, 프로그램의 제어가 이동 (흐름 제어)
            - forward 액션태그를 만나면 그 전까지 출력 버퍼에 저장되어있던 내용 모두 삭제
            - HTTP요청(request) → first.jsp 여기서 forward → second.jsp → HTTP응답(response)
            - `<jsp:forward page=”second.jsp” />`
            - `<jsp:forward page=”second.jsp”> ... </jsp:forward>`
            
        - include
            - 현재 JSP 페이지의 특정 영역에 외부 파일의 내용을 포함
            - 별도의 파일로 요청 처리 흐름을 이동, 동적 페이지에 사용
            - request 내장 객체나 param 액션태그를 이용해 파라미터 전달
            - `<jsp:include page=”header.jsp” flush=”false”>`
            
        
        **흐름, 요청 넘길 때 정보(param, attribute) 추가해서 넘김
        
        - `<jsp:param name=”” value=””>` → `getParameter();`
        - request객체 : 붙이기 :  `setProperty()` / 떼기 :  `getProperty()`
        
        - param
            - 단독으로 사용 불가능, `<jsp:forward>` OR `<jsp:include>`태그의 내부에 사용
            - `<jsp:param name=”date” value=”<%=new java.util.Date()%>” />` → `getParameter()`
        - useBean
            - `<jsp:useBean id=”beans” class=”java.util.Date” scope=”page” />`
            - setProperty
                - `<jsp:setProperty name=”member” property=”id” value=”admin” />`
            - getProperty
                - `<jsp:getProperty name=”member” property”id” />`
- 내장 객체
    - 내장 객체 : JSP페이지에서 사용할 수 있도록 JSP 컨테이너에 미리 정의된 객체, JSP→서블릿 시 자동으로 내장객체를 각종 객체, 멤버 변수로 포함해줌
    - request
        - 가장 많이 사용되는 기본 내장 객체
        - 요청 파라미터 관련 메소드
            - getParameter(String name) : return String
            - getParameterValues(String name) : return String[]
            - getParameterNames() : return java.util.Enumeration
        - 요청 HTTP 헤더 관련 메소드
            - getHeader(String name), getHeaders(String name)
            - getHeaderNames()
            - getIntHeader(String name)
            - getDateHeader(String name)
            - getCookies()
        - 웹 브라우저/서버 관련 메소드
            - getContentType()
            - getCharacterEncoding()
    - response
        - 페이지 이동 관련 메소드
            - sendRedirect(String url) : return void
        - 응답 HTTP 관련 메소드
            - addCookie(Cookie cookie)
            - add (없는 걸 새로 만드는 것)
                - addDateHeader(String name, long date)
                - addHeader(String name, String value)
                - addIntHeader(String name, int value)
            - set (기존 걸 변경, 수정)
                - setDateHeader(String name, long date)
                - setHeader(String name, String value)
                - setIntHeader(String name, int value)
            - getHeader(String name)
        - 응답 콘텐츠 관련 메소드
            - setContentType(String type), getContentType()
            - setCharacterEncoding(String charset), getCharacterEncoding()
            - sendError(int status_code, String message), setStatus(int statuscode)
    - out
        - print(String str), println(String str)
        - newLine()
    - session
        - 비연결형 프로토콜 HTTP의 한계점 극복
        - session 사용하는 경우
            - 사용자 로그인 후 일정 시간 지나거나 다른 페이지에서도 사용자가 여전히 로그인 되어있음을 판단할때
            - 장바구니, 트래킹, 개인화 홈페이지 구현
        - isNew() : 처음 생성되었다면 true 리턴
        - getMaxInactiveInterval() : session의 유지시간을 초로 반환
        - setMaxInactivaInterval(int t) : session의 유효시간을 t초로 설정
    - application
        - 서블릿이 실행되는 환경, 서버 자원과 관련한 정보를 얻거나 로그 파일을 기록할 때
        - log(String msg) : msg의 내용을 로그 파일에 기록
    
    - 내장 객체의 활성 범위 Scope
        
        
        | Scope | 연관 기본 객체 | 생성시기 | 소멸시기 |
        | --- | --- | --- | --- |
        | PAGE | pageContext | JSP페이지 처리시작 | JSP페이지 처리완료 |
        | REQUEST | request | 웹 브라우저부터의 요청 처리 시작 | 웹 브라우저로 응답 완료 |
        | SESSION | session | 웹 브라우저부터의 첫번째 요청 처리 시작 | 세션 타이머 만료, 세션을 명시적으로 소멸시킬 때 |
        | APPLICATION | application | Tomcat구동 + 웹 APP 첫 시작 | Tomcat 서버종료 |
- 참고 정리
    - 디렉티브 태그는 file, 액션 태그는 page