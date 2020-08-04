## 6.1 REST 컨트롤러 작성하기
  - REST API
    - Spring MVC 사용
    - ??) JPA도 외부에서 사용할 수 있게 자동으로 노출

  <br>
  
  - SPA
    - Single-Page Application 단일 페이지 애플리케이션
    - 기존 서버 생성 페이지 대체 (MPA, Multi-Page Application)
    - 프론트엔드와 백엔드 분리
    - 백엔드 기능은 같은 것을 사용하면서 UI만 다르게 개발하는 경우 유용 ex. 모바일 앱
    - 같은 API를 사용하는 다른 애플리케이션과 통합 가능
    - 책에서는 앵귤러 사용
    
  <br>
  
  - REST 컨트롤러에서 사용할 HTTP 요청 어노테이션
    - 스프링 MVC 제공
    - @GetMapping
    -  @PostMapping
    -  @PutMapping
    -  @PatchMapping
    -  @DeleteMappin
    -  @RequestMapping : HTTP 메소드를 method 속성에 지정
    
  <br>
  
  - GET, POST, PUT, PATCH, DELETE 사용
    - @RestController
      - REST 컨트롤러
      - 스프링의 스테레오 타입 어노테이션이므로 지정된 클래스는 스프링 컴포넌트 검색 가능
      - 컨트롤러의 모든 HTTP 요청 처리 메소드에서 HTTP 응답 시 프론트단에서 필요한 값 반환
      - @Controller 사용 시 요청 처리 메소드에 @ResponseBody 지정하는 것과 같은 결과
        ```
        @Controller
        public class ExController {
            . . .
            @ResponseBody
            @RequestMapping(. . .)
            public Response exMethod(@RequestBody ExVO exVO) {
                . . .
            }
        }
        ```
    <br>
      
    - ResponseEntity 객체를 반환하는 방법도 가능
      - 추후 자세히
      
    <br>
    
    - @RequestMapping
      - path 속성
      - produces 속성
        - 요청의 Accept 헤더에 “application/json”이 포함된 요청만 해당 컨트롤러에서 처리하는 것을 의미
        - 속성의 값은 String 배열로 저장되므로 여러 ContentType 작성 가능 ex. {“application/json”, “text/html"}
     
    <br>
    
    - @CrossOrigin
      - 크로스 도메인
        - 호출한 URL의 도메인이 다른 경우 웹 브라우저가 API 사용 제한
      - 서버 응답에 CORS(Cross-Origin Resource Sharing) 헤더 포함하여 제한 해제 가능
      - 해당 어노테이션 사용하여 CORS 적용 가능
      - 다른 도메인의 클라이언트에서 해당 REST API를 사용 가능하게 하는 어노테이션
      
    <br>
    
    - @PathVariable
      - HTTP 요청 어노테이션의 URL 값에 플레이스 홀더’{}’ 사용
        ```
        @GetMapping(“/{id}”)
        public Taco tacoById(@PathVariable(“id”) Long id) {
            . . .
        }
        ```
        
    <br>
    
    - Optional<Object>
      - Object가 없을 수도 있을 때 사용 (Swift optional과 비슷)
      - Optional.isPresent() / Optional.get() 등
    - ResponseEntity<>(응답 객체, HttpStatus.OK);
    - 개발 중 화면 구현이 덜 된 API 테스트 시 사용 방법
      ```
      $ curl localhost:8080/design/recent
      OR
      $ http :8080/design/recent
      ```
      
    <br>
    
    - 어노테이션 consumes속성
      - @XxxMapping 어노테이션의 속성 (배열 가능)
      - Http 헤더의 ContentType 지정하는 속성
    - @RequestBody
      - 요청의 JSON 데이터가 자바 객체로 변환되어 객체 인스턴스와 바인딩
      - 미지정 시, 폼데이터 전송할 때 처럼 바로 자바 객체에 바인딩
    - @ResponseStatus(HttpStatus.CREATED) or @ResponseStatus(code = HttpStatus.NO_CONTENT)
      - 메소드 지정 어노테이션
      - 해당 요청이 성공했을 때 HttpStatus.OK 대신 사용자에게 더 상세한 설명 가능(정확한 상태 코드 전달)
    - @PutMapping/@PatchMapping
      - PUT은 데이터 전체를 교체, 전달하지 않은 항목은 null 전달
      - PATCH는 전달한 데이터만 교체
    - @DeleteMapping
