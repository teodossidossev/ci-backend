package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageRepository messageRepository;

    @Test
    void getMessage_whenMessageExists_returnsMessage() throws Exception {
        String id = "123";
        String content = "Test Content";
        Message message = new Message();
        message.setId(id);
        message.setContent(content);
        Mockito.when(this.messageRepository.findById(any())).thenReturn(Optional.of(message));

        MvcResult result = this.mockMvc.perform(get("/message/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = "{\"id\":\"123\",\"content\":\"Test Content\"}";
        assertThat(result.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(expectedJson);
    }

    @Test
    void getMessage_whenMessageDoesntExist_returnsNotFound() throws Exception {
        String id = "123";
        Mockito.when(this.messageRepository.findById(any())).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/message/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMessageCreation_noIdConflict_201Created() throws Exception {
        // Given
        Message newMessage = new Message();
        newMessage.setId("testId");
        newMessage.setContent("testContent");

        Mockito.when(messageRepository.findById("testId")).thenReturn(Optional.empty());
        Mockito.when(messageRepository.save(any(Message.class))).thenReturn(newMessage);

        // When and Then
        mockMvc.perform(post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMessage)))
                .andExpect(status().isOk());
    }

    @Test
    public void testMessageCreation_negativeIdExist_400BadRequest() throws Exception {
        // Given
        Message existingMessage = new Message();
        existingMessage.setId("existingId");
        existingMessage.setContent("Existing Content");

        Mockito.when(messageRepository.findById("existingId")).thenReturn(Optional.of(existingMessage));

        // When and Then
        mockMvc.perform(post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingMessage)))
                .andExpect(status().isBadRequest());
    }
}
