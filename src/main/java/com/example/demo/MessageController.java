package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable String id) {
        return messageRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No message found with ID: " + id));
    }

    @PostMapping("/message")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        return messageRepository.findById(message.getId())
                .<ResponseEntity<Message>>map(existingMessage -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message with ID: " + message.getId() + " already exists.");
                })
                .orElseGet(() -> ResponseEntity.ok(messageRepository.save(message)));
    }

    @PutMapping("/message")
    public ResponseEntity<Message> updateMessage(@RequestBody Message message) {
        return messageRepository.findById(message.getId())
                .map(existingMessage -> {
                    existingMessage.setContent(message.getContent());
                    return ResponseEntity.ok(messageRepository.save(existingMessage));
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No message found with ID: " + message.getId()));
    }
}
