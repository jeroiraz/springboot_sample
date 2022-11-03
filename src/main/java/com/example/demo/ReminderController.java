package com.example.demo;

import com.example.demo.model.Reminder;
import com.example.demo.repository.ImmudbRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.codenotary.immudb4j.exceptions.VerificationException;

@RestController
@RequestMapping("/api/immutable/reminder")
public class ReminderController {
    
    @Autowired
    ImmudbRepository<String, Reminder> repository;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reminder> save(@RequestBody Reminder reminder) {
        repository.save(reminder.getTitle(), reminder);
        return ResponseEntity.ok(reminder);
    }

    @PostMapping(value = "/verified", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reminder> verifiedSave(@RequestBody Reminder reminder) throws VerificationException{
        repository.verifiedSave(reminder.getTitle(), reminder);
        return ResponseEntity.ok(reminder);
    }

    @GetMapping(value = "/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reminder> find(@PathVariable("title") String title) {
        return ResponseEntity.ok(repository.find(title));
    }

    @GetMapping(value = "/{title}/{txId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reminder> find(@PathVariable("title") String title, @PathVariable("txId") long txId) {
        return ResponseEntity.ok(repository.findAt(title, txId));
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Long> delete(@PathVariable("title") String title) {
        return ResponseEntity.ok(repository.delete(title));
    }

    @ExceptionHandler(VerificationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleVerificationException(
        VerificationException exception
    ) {
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementFoundException(
        RuntimeException exception
    ) {
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(exception.getMessage());
    }
}
