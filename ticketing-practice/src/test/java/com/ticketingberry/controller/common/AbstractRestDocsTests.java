package com.ticketingberry.controller.common;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketingberry.config.RestDocsConfiguration;

// RestDocs에 대한 설정을 모든 테스트 클래스의 setUp으로 동일하게 작성해 줄 필요는 없으니
// abstract 클래스로 만들어 각 테스트 클래스들이 상속받아 사용하도록 만들어준다.
@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
public class AbstractRestDocsTests {
	@Autowired
	protected RestDocumentationResultHandler restDocs;
	
	@Autowired
	protected MockMvc mockMvc;	// HTTP 요청을 시뮬레이션하여 로직 테스트
	
	@Autowired
	protected ObjectMapper objectMapper;
	
	@BeforeEach
	public void setUp(
			final WebApplicationContext context,
			final RestDocumentationContextProvider restDocumentation) {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(documentationConfiguration(restDocumentation))
				.alwaysDo(MockMvcResultHandlers.print())
				.alwaysDo(restDocs)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.build();
	}
}
