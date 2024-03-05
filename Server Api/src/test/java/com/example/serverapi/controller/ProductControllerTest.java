package com.example.serverapi.controller;

import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.dto.product.ReqProductDto;
import com.example.serverapi.dto.product.ResProductDto;
import com.example.serverapi.dto.product.ResponseDto;
import com.example.serverapi.dto.token.UserTokenResponse;
import com.example.serverapi.enums.Authority;
import com.example.serverapi.enums.Role;
import com.example.serverapi.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private AuthAndRegRequest authRequest;
    private AuthAndRegRequest regRequest;
    private ReqProductDto reqProductDto;

    @BeforeEach
    public void setUp() {
        reqProductDto = new ReqProductDto();
        reqProductDto.setName("Apple");
        reqProductDto.setAmount(5);
        reqProductDto.setPrice(23.4);

        authRequest = new AuthAndRegRequest();
        authRequest.setUsername("Asadbek");
        authRequest.setPassword("Asadbek@2704");

        regRequest = new AuthAndRegRequest();
        regRequest.setUsername("Asadbek");
        regRequest.setPassword("Asadbek@2704");
        regRequest.setRoleList(List.of(Role.USER));
        regRequest.setAuthorityList(List.of(Authority.READ, Authority.CREATE, Authority.UPDATE, Authority.DELETE));
    }

    @Test
    void getById() {
        authService.register(regRequest);

        ResponseDto<UserTokenResponse> tokenResponseResponseDto = authService
                .authenticate(authRequest);

        UserTokenResponse userTokenResponse = tokenResponseResponseDto.getData();

        String accessToken = userTokenResponse.getAccessToken();

        try {
            MockHttpServletRequestBuilder createBuilder =
                    MockMvcRequestBuilders.post("http://localhost:8080/api/product/create")
                            .content(objectMapper.writeValueAsString(reqProductDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult = mockMvc.perform(createBuilder).andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            ResponseDto<?> responseDto = objectMapper.readValue(response.getContentAsString(), ResponseDto.class);

            assertThat(responseDto.getIsSuccess()).isTrue();

            Integer productId = (Integer) responseDto.getData();

            MockHttpServletRequestBuilder getByIdBuilder =
                    MockMvcRequestBuilders.get("http://localhost:8080/api/product//get-by-id/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult1 = mockMvc.perform(getByIdBuilder).andReturn();
            MockHttpServletResponse response1 = mvcResult1.getResponse();
            ResponseDto responseDto1 = objectMapper.readValue(response1.getContentAsString(), ResponseDto.class);

            assertThat(responseDto1.getIsSuccess()).isTrue();

            Map<String, Object> objectMap = (Map<String, Object>) responseDto1.getData();

            ResProductDto resProductDto1 = objectMapper.convertValue(objectMap, ResProductDto.class);

            ResProductDto resProductDto = new ResProductDto();
            resProductDto.setName("Apple");
            resProductDto.setAmount(5);
            resProductDto.setPrice(23.4);

            assertThat(resProductDto1).isEqualTo(resProductDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll() {
        authService.register(regRequest);

        ResponseDto<UserTokenResponse> tokenResponseResponseDto = authService
                .authenticate(authRequest);

        UserTokenResponse userTokenResponse = tokenResponseResponseDto.getData();

        String accessToken = userTokenResponse.getAccessToken();

        ReqProductDto reqProductDto1 = new ReqProductDto();
        reqProductDto1.setName("banana");
        reqProductDto1.setAmount(8);
        reqProductDto1.setPrice(78.56);

        try {
            MockHttpServletRequestBuilder createBuilder =
                    MockMvcRequestBuilders.post("http://localhost:8080/api/product/create")
                            .content(objectMapper.writeValueAsString(reqProductDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);


            MockHttpServletRequestBuilder createBuilder1 =
                    MockMvcRequestBuilders.post("http://localhost:8080/api/product/create")
                            .content(objectMapper.writeValueAsString(reqProductDto1))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);

            mockMvc.perform(createBuilder).andExpect(status().isOk());
            mockMvc.perform(createBuilder1).andExpect(status().isOk());

            MockHttpServletRequestBuilder getBuilder =
                    MockMvcRequestBuilders.get("http://localhost:8080/api/product/get-all")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult = mockMvc.perform(getBuilder).andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            ResponseDto<?> responseDto = objectMapper.readValue(response.getContentAsString(), ResponseDto.class);

            assertThat(responseDto.getIsSuccess()).isTrue();

            assertThat(responseDto.getData()).isNotNull();

            assertThat(((List<?>) responseDto.getData()).size()).isEqualTo(2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void create() {
        authService.register(regRequest);

        ResponseDto<UserTokenResponse> tokenResponseResponseDto = authService
                .authenticate(authRequest);

        UserTokenResponse userTokenResponse = tokenResponseResponseDto.getData();

        String accessToken = userTokenResponse.getAccessToken();

        try {
            MockHttpServletRequestBuilder createBuilder =
                    MockMvcRequestBuilders.post("http://localhost:8080/api/product/create")
                            .content(objectMapper.writeValueAsString(reqProductDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult = mockMvc.perform(createBuilder).andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            ResponseDto<?> responseDto = objectMapper.readValue(response.getContentAsString(), ResponseDto.class);
            assertThat(responseDto.getIsSuccess()).isTrue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        authService.register(regRequest);

        ResponseDto<UserTokenResponse> tokenResponseResponseDto = authService
                .authenticate(authRequest);

        UserTokenResponse userTokenResponse = tokenResponseResponseDto.getData();

        String accessToken = userTokenResponse.getAccessToken();

        try {
            MockHttpServletRequestBuilder createBuilder =
                    MockMvcRequestBuilders.post("http://localhost:8080/api/product/create")
                            .content(objectMapper.writeValueAsString(reqProductDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult = mockMvc.perform(createBuilder).andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            ResponseDto<?> responseDto = objectMapper.readValue(response.getContentAsString(), ResponseDto.class);

            assertThat(responseDto.getIsSuccess()).isTrue();
            assertThat(responseDto.getData() instanceof Integer).isTrue();

            Integer productId = (Integer) responseDto.getData();

            ReqProductDto updateProductDto = new ReqProductDto();
            updateProductDto.setName("Banana");
            updateProductDto.setAmount(7);
            updateProductDto.setPrice(17.5);

            MockHttpServletRequestBuilder updateBuilder =
                    MockMvcRequestBuilders.put("http://localhost:8080/api/product/update/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateProductDto))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);


            MvcResult mvcResult1 = mockMvc.perform(updateBuilder).andReturn();
            MockHttpServletResponse response1 = mvcResult1.getResponse();
            ResponseDto<?> responseDto1 = objectMapper.readValue(response1.getContentAsString(), ResponseDto.class);

            assertThat(responseDto1.getIsSuccess()).isTrue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void delete() {
        authService.register(regRequest);

        ResponseDto<UserTokenResponse> tokenResponseResponseDto = authService
                .authenticate(authRequest);

        UserTokenResponse userTokenResponse = tokenResponseResponseDto.getData();

        String accessToken = userTokenResponse.getAccessToken();

        try {
            MockHttpServletRequestBuilder createBuilder =
                    MockMvcRequestBuilders.post("http://localhost:8080/api/product/create")
                            .content(objectMapper.writeValueAsString(reqProductDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult = mockMvc.perform(createBuilder).andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            ResponseDto<?> responseDto = objectMapper.readValue(response.getContentAsString(), ResponseDto.class);
            assertThat(responseDto.getIsSuccess()).isTrue();

            Integer productId = (Integer) responseDto.getData();

            MockHttpServletRequestBuilder deleteBuilder =
                    MockMvcRequestBuilders.delete("http://localhost:8080/api/product/delete/{id}", productId)
                            .content(objectMapper.writeValueAsString(reqProductDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken))
                            .accept(MediaType.APPLICATION_JSON);

            MvcResult mvcResult1 = mockMvc.perform(deleteBuilder).andReturn();
            MockHttpServletResponse response1 = mvcResult1.getResponse();
            ResponseDto<?> responseDto1 = objectMapper.readValue(response1.getContentAsString(), ResponseDto.class);
            assertThat(responseDto1.getIsSuccess()).isTrue();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}