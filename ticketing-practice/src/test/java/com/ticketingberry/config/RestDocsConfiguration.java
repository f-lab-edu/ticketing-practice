package com.ticketingberry.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

// 테스트 코드를 작성한 {클래스명/테스트 메소드명} 으로 디렉토리를 지정해준다.
@TestConfiguration
public class RestDocsConfiguration {
    @Bean
    RestDocumentationResultHandler write() {
		return MockMvcRestDocumentation.document(
				"{class-name}/{method-name}",	// identifier
				Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
				Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));
	}
}
