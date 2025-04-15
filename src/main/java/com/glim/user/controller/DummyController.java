package com.glim.user.controller;

import com.glim.user.dto.request.AddDummyRequest;
import com.glim.user.dto.request.UpdateDummyRequest;
import com.glim.user.dto.response.ViewDummyResponse;
import com.glim.user.service.DummyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/dummy")
@RequiredArgsConstructor
public class DummyController {

    private final DummyService dummyService;

    @GetMapping({"","/{id}"})
    public ResponseEntity<ViewDummyResponse> getDummy(@PathVariable(required = false) Long id) {
        ViewDummyResponse dummy = dummyService.findDummyById(id);
        return ResponseEntity.ok(dummy);
    }

    @PostMapping({"","/"})
    public ResponseEntity<HttpStatus> join(AddDummyRequest request) {
        dummyService.insert(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, UpdateDummyRequest request) {
        dummyService.update(id, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        dummyService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
