package org.codechallenge.social.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.codechallenge.social.model.Message;
import org.codechallenge.social.web.dto.PostMessageRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WallControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void getWall() throws Exception {
        Message message = Message.builder()
                .setAuthorId(41)
                .setPostedAt(Instant.now())
                .setContent("hello")
                .build();
        PostMessageRequest postMessageRequest = PostMessageRequest.builder()
                .setMessage(message)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/messages")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(postMessageRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(RestDocumentationRequestBuilders.get("/users/{userId}/wall", 41)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(document("users/{userId}/wall",
                        pathParameters(
                                parameterWithName("userId").description("User ID")),
                        responseFields(fieldWithPath("messages")
                                .description("Wall messages"))
                                .andWithPrefix("messages[].", FieldDescriptors.MESSAGE_FIELD_DESCRIPTORS)));
    }
}
