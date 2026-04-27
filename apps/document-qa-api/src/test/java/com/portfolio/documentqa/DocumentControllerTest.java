package com.portfolio.documentqa;

import com.portfolio.documentqa.service.DocumentService;
import com.portfolio.documentqa.service.QaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private QaService qaService;

    @Test
    void ask_returnsAnswerForValidQuestion() throws Exception {
        when(qaService.answer(anyString())).thenReturn("The answer is 42.");

        mockMvc.perform(post("/api/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"question": "What is the meaning of life?"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("The answer is 42."))
                .andExpect(jsonPath("$.question").value("What is the meaning of life?"));
    }

    @Test
    void ask_returns400ForBlankQuestion() throws Exception {
        mockMvc.perform(post("/api/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"question": ""}
                                """))
                .andExpect(status().isBadRequest());
    }
}
