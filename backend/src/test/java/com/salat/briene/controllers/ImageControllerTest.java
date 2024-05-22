// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salat.briene.BrieneApplication;
import com.salat.briene.payload.response.ImageDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.jbrofuzz.core.Database;
import org.owasp.jbrofuzz.core.Fuzzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BrieneApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ImageControllerTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Database database = new Database();

    @Autowired
    private MockMvc mvc;

    @Before
    public void beforeAll() {
        log.debug("Available fuzzers: {}", () -> Arrays.toString(database.getAllFuzzerIDs()));
    }

    @Test
    public void uploadImage() throws Exception {
        Fuzzer fuzzer = database.createFuzzer("031-B16-HEX", 10);

        for (int i = 0; i < 100 && fuzzer.hasNext(); i++) {
            String imageContent = fuzzer.next();

            ImageDTO image = new ImageDTO();
            image.setContent(imageContent);

            log.trace("uploadImage(). Testing with content: {}", () -> image);

            mvc.perform(
                            post("/api/images/upload")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(mapper.writeValueAsBytes(image))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isString())
                    .andExpect(jsonPath("$.content", is(imageContent)));
        }
    }
}