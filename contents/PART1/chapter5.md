## 5.1 자동 구성 세부 조정하기

- 스프링 부트는 구성 속성 사용 방법 제공
  - 스프링 애플리케이션 컨텍스트에서 구성 속성은 빈의 속성
  - JVM 시스템 속성, 명령행 인자, 환경 변수 설정을 활용해 스프링 애플리케이션 환경 구성 가능


### 자동 구성 세부 조정
- 스프링에는 두 가지 형태의 구성이 존재
  1. 빈 연결(Bean wiring)
      - 스프링 애플리케이션 컨텍스트에서 빈으로 생성되는 컴포넌트 및 상호 간 주입 방법을 선언하는 구성
  2. 속성 주입(Property injection)
      - 스프링 애플리케이션 컨텍스트에서 빈의 속성 값을 설정하는 구성
  - 예 : 자바 기반 구성에서 @Bean 어노테이션 사용
    - @Bean 어노테이션이 지정된 메소드는 사용하는 빈의 인스턴스를 생성하고 속성 값을 설정
    - 스프링 부트는 런타임 시 해당 빈을 자동으로 찾아 스프링 애플리케이션 컨텍스트에 생성
  - 스프링 구성 속성을 사용해서 스프링의 컴포넌트를 구성 설정하면 해당 컴포넌트의 속성 값을 쉽게 주입 가능하고 자동 구성의 세부 조정 가능
  - 개발자가 직접 생성한 빈에도 구성 속성 사용 가능
  
<br>
  
- 스프링 환경 추상화 이해하기
  - 구성 설정 방법 설명 전, 스프링 환경 추상화 이해 필요
  - 스프링 환경 추상화는 구성 가능한 모든 속성을 한 곳에서 관리하는 개념
    - 속성의 근원을 추상화하여 각 속성을 필요로 하는 빈이 스프링 자체에서 해당 속성 사용 가능
    - 속성의 근원
      - JVM 시스템 속성
      - 운영체제 환경 변수 설정
      - 명령행 인자
      - 애플리케이션 속성 구성 파일
    - 스프링 환경은 위 속성을 한 군데로 모은 후 각 속성이 주입되는 스프링 빈 사용 가능하게 함
    - 자동 구성 빈 및 구성 속성은 스프링 환경에서 가져온 근원 속성을 사용해서 설정 가능
    - 무수히 많은 구성 속성 종류가 있음
    - 예 : 서블릿 컨테이너의 포트 변경 시
      - 운영체제 환경 변수 설정
      ```
      $ export SERVER_PORT=9090
      ```
      - 명령행 인자
      ```
      $ java -jar taco cloud-0.0.5-SNAPSHOT.jar —server.port=9090
      ```
      - 애플리케이션 속성 구성 파일(application.properties)
      ```
      server.port=9090
      ```
      - 애플리케이션 속성 구성 파일(application.yml)
      ```
      server:
        port: 9090
      ```

<br>
                            
- Datasource 구성 방법
  - 원한다면 아래와 같이 DataSource 빈 직접 명시하여 구성 가능 (스프링 부트 내장 H2 Data Source 사용)
    ```
    @Bean
    public DataSource dataSource() {
      return new EmbeddedDatabaseBuilder()
                   .setType(EmbeddedDatabaseType.H2)
                   .addScript(“schema.sql”)
                   .addScripts(“user_data.sql”, “ingredient_data.sql”)
                   .build();
    }
    ```
  - 하지만 스프링 부트는 구성 속성을 통해 더 간단하게 구성 가능
    ```
    spring:
      datasource:
        url: jdbc:mysql://localhost/tacocloud
        username: tacodb
        password: tacopassword
        driver-class-name: com.mysql.jdbc.Driver    # 옵션 속성
    ```
    - 사용할 JDBC 드라이버 추가 후 구체적인 드라이버 클래스 지정할 필요는 없음
      - 스프링 부트가 url로부터 탐색 가능
      - 필요한 경우 driver-class-name 속성 지정
    - DataSource 빈 자동 구성 시, 스프링 부트는
      1. 위와 같은 속성 설정을 연결 데이터로 사용하는 방법
      2. 톰캣의 JDBC 커넥션 풀을 클래스패스에서 찾아 연결 데이터로 사용하는 방법
      3. HikariCP or Commons DBCP 2를 classpath에서 찾아 사용하는 방법
  - 데이터베이스 초기화를 위한 SQL 스크립트 실행 구성
    ```
    spring:
      datasource:
        schema:
          - order-schema.sql
          - ingredient-schema.sql
          - taco-schema.sql
          - user-schema.sql
        data:
          - ingredients.sql
    ```
  - JNDI(Java Naming and Directory Interface)에 구성하도록 작성 가능
    ```
    spring:
      datasource:
        jndi-name: java:/comp/env/jdbc/tacoCloudDS
    ```
    - 단, 해당 설정 시 기존에 설정된 다른 Data Source 구성 속성은 무시
    
<br>

- 내장 서버 구성 방법
  - 앞서 진행했던 포트 설정 시, 포트를 0으로 설정한다면
    - 현재 사용 가능한 포트를 무작위로 선택하여 시작
      - 자동화된 통합 테스트 실행 시 유용
      - 마이크로 서비스와 같이 애플리케이션 시작 포트가 중요하지 않을 때 유용
    - HTTPS 요청 처리 설정
      - JDK의 keytool 명령을 사용하여 키스토어 생성
      ```
      $ keytool -keystore mikes.jks -genkey -alias -tomcat -keyalg RSA
      ```
      - 키스토어 생성 시 입력한 비밀번호 보관 필요
    - 키스토어 생성 완료 후 HTTPS 활성화를 위한 속성 설정
      - 명령행 또는 애플리케이션 속성 구성 파일에 지정 가능
      ```
      server:
        port: 8443                               # 개발용 HTTPS 서버에 많이 사용
        ssl:
          key-store: file:///path/to/mykeys.jks  # 키스토어 파일 생성 경로
          key-store-password: letmein            # 키스토어 생성 시 지정한 비밀번호
          key-password: letmein                  # 키스토어 생성 시 지정한 비밀번호
      ```
      - 애플리케이션 JAR 파일에 키스토어 파일 넣은 경우 classpath: 로 지정
      
<br>      
      
- 로깅 구성 방법
  - 스프링 부트 기본 로깅 설정
    - 로거 : Logback
    - 로그 레벨 : INFO
    - 로깅 위치 : 콘솔
  - 구성 제어 시 /resources 경로에 logback-spring.xml 파일 생성
    - 파일명을 logback.xml로 사용 시 스프링 부트 설정 전에 Logback 설정이 되어 제어가 불가능
    - 파일명 변경 원할 경우 property 설정
  - 단, 스프링 부트의 구성 속성 사용 시 logback-spring.xml 파일 생성하지 않고 설정 변경 가능
    - application.yml 작성 예시
      ```
      logging:
        path: /var/logs            # logging.path : 로그 저장 경로
          file: TacoCloud.log      # logging.file : 로그 저장 파일명
        level:                     # logging.level : 로그 레벨 설정
          root: WARN
            org:
              springframework:
                security: DEBUG    #org.springframework.security로도 작성 가능
      ```
      - 로그 파일은 기본 10MB, 초과 시 새로운 로그 파일 생성
      - 스프링 2.0 부터는 날짜별로 로그 파일 생성, 지정 일 수 지나면 로그 파일 삭제 처리
      
<br>     
      
- 다른 구성 속성의 값 가져오기
  - 다른 구성 속성으로부터 값 가져오기 가능
    - ${} 활용
      ```
      greeting:
        welcome: ${spring.application.name}
      ```
                

## 5.2 우리의 구성 속성 생성하기
- 구성 속성은 빈의 속성
- 구성 속성은 스프링의 환경 추상화로부터 여러 가지 구성을 받기 위해 설계
- 개발자가 직접 작성한 Bean에 구성 속성을 주입하여 사용 가능

<br>

- @ConfigurationProperties
  - 구성 속성의 올바른 주입 지원
  - 어떤 스프링 빈이든 이 어노테이션이 지정되면 해당 빈의 속성은 스프링 환경 속성으로부터 주입

<br>

- @ConfigurationProperties 사용 방법
  - 페이징 처리를 위해 OrderController에 Pageable 객체 추가
  - 커스텀 구성 속성을 통해 페이지 크기 설정 가능
  
  <br>
  
  1. 어노테이션 의존성 추가 필요
	  - build.gradle의 dependencies에 추가
	    ```
        annotationProcessor('org.springframework.boot:spring-boot-configuration-processor:')
        ```
  2. 클래스 레벨에 @ConfigurationProperties 추가 및 접두어 지정
      ```
      @ConfigurationProperties(prefinx=“taco.orders”)
      public class OrderController {
          private int pageSize = 20;
          public void setPageSize(int pageSize) {
              this.pageSize = pageSize;
          }
          ...
      }
      ```
  3. application.yml 파일에 pageSize 구성 속성 값 설정
      ```
      taco:
        orders:
          pageSize: 10
      ```
        - 클래스 내부에는 20으로 지정되어있지만 구성 속성 값으로 덮어씌워짐
        - 운영 환경에서 급하게 변경이 필요할 경우 환경변수로 설정 변경 가능
            - 애플리케이션 재빌드 및 배포 불필요
              ```
              $ export TACO_ORDERS_PAGESIZE=10
              ```

<br>
                
- 구성 속성 홀더 정의 방법
  - @ConfigurationProperties는 컨트롤러나 특정 빈 보다 주로 구성 데이터의 홀더로 사용하는 빈에 지정하여 사용
	- 애플리케이션 클래스 외부에 구성 관련 정보 따로 유지 가능
	- 공통적인 구성 속성 공유 가능
  - 구성 속성 홀더는 스프링 환경으로부터 주입되는 속성을 갖는 빈
	- 해당 속성을 필요로 하는 다른 빈에 주입 가능
  - 구성 데이터의 홀더로 사용할 OrderProps 클래스 추가
    - @Component를 지정하여 스프링 애플리케이션 컨텍스트의 빈으로 생성
	  - 컨트롤러 클래스 등 다른 클래스에 주입하기 위함
	- 기존처럼 구성 속성 홀더 클래스 없이 필요로 하는 클래스에 일일이 작성 시 변동사항이 너무 많아짐
	- 변경 후에는 예를 들어 검사 어노테이션 추가 시 OrderProps 한 클래스만 수정하여 설정 가능
	- 필요한 다른 빈에서 OrderProps의 속성 재사용 가능
	
<br>

- 구성 속성 메타데이터 선언 방법
  - /resources/META-INF 경로에 additional-spring-configuration-metadata.json 추가
  - 메타데이터 추가 후, application.yml에 작성 시 자동완성 가능


## 5.3 프로파일 사용해서 구성하기
- 애플리케이션이 서로 다른 실행 환경에 배포, 설치될 경우 각 환경에 특화된 구성 설정 필요
- application.properties or application.yml에 설정하는 대신 운영체제 환경 변수 사용해서 구성 가능
- 단, 환경 변수 지정 과정이 번거롭고, 변경을 추적하거나 오류 발생 시 롤백 방법이 어려움
- 대안으로 스프링 프로파일 사용

<br>

- 프로파일 ?
  - 애플리케이션 런타임 시 활성화되는 프로파일에 따라 서로 다른 빈, 구성 클래스, 구성 속성들이 적용 또는 무시되도록 설정
  - 프로파일을 정의하지 않을 경우, 개발과 디버깅 목적으로 작성한 application.yml 파일을 수정하지 않고 운영에 배포하면 개발 시 사용한 설정 그대로 적용
- 프로파일 특정 속성 정의 방법
  1. 운영 환경의 속성들만 포함하는 또다른 .yml or .properties 파일 생성
      - 파일명 규칙은 application-{프로파일명}.yml/.properties
  2. YAML 파일이라면 하이픈 3개(---)를 활용하여 한 파일에 여러 프로파일 속성 설정 가능
      ```
      logging:
        level:
          tacos: DEBUG
      ---
      spring:
        profiles: prod
          datasource:
            url: jdbc:mysql://localhost/tacocloud
              username: tacouser
              password: tacopassword
      logging:
        level:
        tacos: WARN
      ```
      - 하이픈 3개를 기준으로 두 부분으로 구분
      - spring.profiles가 지정되지 않은 부분은 모든 프로파일에 공통 적용
      - spring.profiles가 지정된 부분은 지정한 프로파일이 활성화되면 적용
      - 공통 적용 속성을 특정 프로파일 속성에 작성하지 않은 경우, 공통 적용 속성이 그대로 적용(상속?)
<br>

- 프로파일 활성화 방법
  - 프로파일의 특정 속성 설정값은 해당 프로파일이 활성화되어야 유효
  - spring.profiles.active 속성에 지정하여 활성화
  - 비추 방법
    - yml 파일에 직접 작성
      ```
      spring:
        profiles:
          active:
            -prod
      ```
      - application.yml에 작성 시 prod가 기본 프로파일
      - 개발과 운영 속성을 분리하기 위해 사용하는 프로파일의 장점을 활용하지 못하는 방법
      - 여러 개 사용 시 하이픈(-)활용
  - 추천 방법
    - 환경 변수 사용
      ```
      $ export SPRING_PROFILES_ACTIVE=prod
      ```
      - 해당 컴퓨터에 배포되는 어떤 애플리케이션에서도 prod 프로파일이 활성화
      - 여러 개 사용 시 쉼표(,)사용
  - 참고 방법
    - 명령행 인자 사용
    ```
    $ java -jar taco-cloud.jar —spring.prifiles.active=prod
    ```
    - 실행 가능한 JAR 파일로 애플리케이션 실행 시 활용
  - 클라우드 파운드리에 배포 시, cloud라는 이름의 프로파일이 자동으로 활성화

<br>

- 프로파일을 사용해서 조건별로 빈 생성하는 방법
  - @Profile 활용
    - 특정 프로파일이 활성화될 때만 생성되어야 하는 빈 설정 가능
    - 메소드 레벨, 클래스 레벨 지정 가능
    - ex. TacoCloudApplication의 CommandLineRunner 빈은 개발환경에서만 사용할 빈
    ```
    @Profile(“dev”)
    public CommandLineRunner dataLoader(...) {
       ...
    } 
    ```
    - 단, dev라는 프로파일 설정 및 활성화 필요
    - @Profile({“dev”, “qa”}) : dev, qa가 활성화 된 환경
    - @Profile(“!prod”) : prod가 활성화되지 않은 모든 환경
