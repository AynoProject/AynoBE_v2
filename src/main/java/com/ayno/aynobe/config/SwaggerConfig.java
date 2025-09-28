package com.ayno.aynobe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        String socialLoginGuide = """
                        ## 📌 소셜 로그인 테스트 방법
                        Swagger에서 직접 실행(`Try it out`)은 OAuth2 리다이렉트 흐름 특성상 동작하지 않습니다.
                        대신 **브라우저 주소창**에서 아래 URL로 이동하세요:
                        
                        - 로컬 Kakao 로그인: [http://localhost:8080/oauth2/authorization/kakao](http://localhost:8080/oauth2/authorization/kakao)
                        - 배포 Kakao 로그인: [https://api.ayno.co.kr/oauth2/authorization/kakao](https://api.ayno.co.kr/oauth2/authorization/kakao)
                        
                        로그인 성공 시 JWT Access/Refresh Token이 **쿠키**로 저장되고,
                        벡엔드 환경변수에 지정된 프론트 주소로 리다이렉트됩니다.(로컬에선 로컬 프론트로, 배포에선 배포 프론트로)
                        
                        ## 📌 예외 코드 처리
                        각 api를 열어보면 Responses Code마다 예시가 하나씩 들어있습니다(커스텀 예외를 놓쳤을시 뜨는 공통 예외처리들)
                        
                        ## 📌 스웨거 테스트시 유의점
                        바로 밑에 보이는 Servers에서 로컬환경이면 로컬주소를 선택해서, 배포환경이면 배포주소를 선택하고 테스트해주세요.
                        """;

        // 쿠키 스키마 생성
        SecurityScheme cookieScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("accessToken")
                .description("JWT Access Token (Cookie)");

        return new OpenAPI()
                .info(new Info()
                        .title("AYNO API")
                        .version("v1.0")
                        .description(socialLoginGuide))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server (HTTP)"),
                        new Server().url("https://api.ayno.co.kr").description("배포 Development Server (HTTPS)")
                ))
                .components(new Components()
                        .addSecuritySchemes("cookieAuth", cookieScheme)
                )
                // 전역으로 Bearer 필요하게(permitAll 엔드포인트는 무시됨)
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"));
    }

    @Bean
    public OpenApiCustomizer globalErrorResponses() {
        return openApi -> {
            if (openApi.getComponents() == null) openApi.setComponents(new Components());
            var components = openApi.getComponents();

            if (components.getSchemas() == null) components.setSchemas(new LinkedHashMap<>());
            var schemas = components.getSchemas();

            // --- 공통 스키마 (addProperty 로 수정) ---
            schemas.putIfAbsent("Response_Success",
                    new Schema<>().type("object")
                            .addProperty("status", new StringSchema().example("OK"))
                            .addProperty("serverDateTime", new StringSchema().example("2025-09-03T12:34:56.789"))
                            .addProperty("errorCode", new StringSchema().nullable(true).example(null))
                            .addProperty("errorMessage", new StringSchema().nullable(true).example(null))
                            .addProperty("data", new ObjectSchema().description("응답 데이터 페이로드"))
            );

            schemas.putIfAbsent("Response_Error",
                    new Schema<>().type("object")
                            .addProperty("status", new StringSchema().example("FAIL")) // 5xx는 예시에 ERROR로 내려줌
                            .addProperty("serverDateTime", new StringSchema().example("2025-09-03T12:34:56.789"))
                            .addProperty("errorCode", new StringSchema().example("REQ.VALIDATION"))
                            .addProperty("errorMessage", new StringSchema().example("입력값을 확인해주세요."))
                            .addProperty("data", new ObjectSchema().nullable(true).example(null))
            );

            if (openApi.getPaths() == null) return;

            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(op -> {
                        op.getResponses().addApiResponse("400",
                                error("잘못된 요청(Validation/Binding)", "REQ.VALIDATION", "입력값을 확인해주세요.", false));
                        op.getResponses().addApiResponse("401",
                                error("인증 필요", "AUTH.UNAUTHORIZED", "로그인이 필요합니다.", false));
                        op.getResponses().addApiResponse("403",
                                error("권한 없음", "AUTH.FORBIDDEN", "접근 권한이 없습니다.", false));
                        op.getResponses().addApiResponse("404",
                                error("리소스 없음", "DATA.NOT_FOUND", "요청한 리소스를 찾을 수 없습니다.", false));
                        op.getResponses().addApiResponse("405",
                                error("허용되지 않은 메서드", "REQ.METHOD_NOT_ALLOWED", "지원하지 않는 HTTP 메서드입니다.", false));
                        op.getResponses().addApiResponse("409",
                                error("중복/제약 위반", "DATA.DUPLICATE", "이미 존재하는 데이터입니다.", false));
                        op.getResponses().addApiResponse("413",
                                error("업로드 용량 초과", "UPLOAD.TOO_LARGE", "파일 용량 제한을 초과했습니다.", false));
                        op.getResponses().addApiResponse("415",
                                error("지원하지 않는 Content-Type", "REQ.UNSUPPORTED_MEDIA_TYPE", "Content-Type을 확인하세요.", false));
                        op.getResponses().addApiResponse("429",
                                error("요청 과다 (Rate Limit)", "REQ.RATE_LIMIT", "요청이 너무 많습니다. 잠시 후 다시 시도해주세요.", false));

                        // 선택: 422 비즈니스 규칙 위반 (DTO 바인딩 ok, 도메인 규칙 fail)
                        // op.getResponses().addApiResponse("422",
                        //         error("규칙 위반", "REQ.UNPROCESSABLE", "요청은 이해했지만 처리할 수 없습니다.", false));

                        op.getResponses().addApiResponse("500",
                                error("서버 내부 오류", "SYS.INTERNAL", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", true));
                    })
            );
        };
    }

    private ApiResponse error(String description, String code, String message, boolean is500) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", is500 ? "ERROR" : "FAIL");
        body.put("serverDateTime", "2025-09-03T12:34:56.789");
        body.put("errorCode", code);
        body.put("errorMessage", message);
        body.put("data", null);

        Example ex = new Example().summary(code).value(body);

        MediaType mt = new MediaType()
                .schema(new Schema<>().$ref("#/components/schemas/Response_Error"))
                .addExamples("default", ex);

        Content content = new Content().addMediaType("application/json", mt);
        return new ApiResponse().description(description).content(content);
    }
}

