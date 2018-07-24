package org.codechallenge.social.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codechallenge.social.web.dto.AddFollowerRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FollowerControllerTest {

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
    }

    @Test
    public void addFollower() throws Exception {
        AddFollowerRequest request = AddFollowerRequest.builder()
                .setFollowerId(2)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/users/{userId}/followers", 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("users/{userId}/followers",
                        pathParameters(
                                parameterWithName("userId").description("Followee user ID")),
                        requestFields(fieldWithPath("followerId")
                                .description("Follower user ID"))));
    }
}
