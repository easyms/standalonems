package com.standalonems.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.standalonems.dto.Client;
import com.standalonems.service.ClientService;
import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RestController
@RequestMapping("/api")
@Validated
@NoArgsConstructor
public class ClientResource {

    @Inject

    ClientService clientService;


    @Inject
    private ObjectMapper objectMapper;



    @ApiOperation("returns all details of a client")
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/clients/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id)  {
        log.info("get client by id {}", id);
        Optional<Client> client = clientService.getById(id);

        return client.map(clientDto -> ResponseEntity.ok().body(clientDto)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation("returns client by name and lastName")
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/clients")
    public ResponseEntity<Client> findByNameAndLastName(@RequestParam String name, @RequestParam(required = false) String lastName)  {
        log.info("get client by name {} and lastName {}", name, lastName);
        Optional<Client> client = clientService.getByNameAndLastNameJava8StreamToFirst(name, lastName);

        return client.map(clientDto -> ResponseEntity.ok().body(clientDto)).orElseGet(() -> ResponseEntity.notFound().build());
    }



}
