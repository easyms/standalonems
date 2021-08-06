package com.standalonems.service;

import com.standalonems.dto.Client;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClientService {

    private Map<Long, Client> clients = new HashMap<>();


    @PostConstruct
    private void initClient() {
        clients.put(1L, Client.builder().email("anis@gmail.com").name("anis").lastName("bessa").build());
        clients.put(2L, Client.builder().email("wael@gmail.com").name("wael").lastName("ben").build());
        clients.put(3L, Client.builder().email("lionel@gmail.com").name("lionel").lastName("name2").build());
        clients.put(4L, Client.builder().email("aziz@gmail.com").name("aziz").lastName("name3").build());
    }


    public Optional<Client> getById(Long id) {
        return Optional.ofNullable(clients.get(id));
    }

    public Optional<Client> getByNameAndLastName(String name, String lastName) {
        Iterator<Long> iterator = clients.keySet().iterator();
        Client foundClient = null;
        while (iterator.hasNext()) {
            var currentKey = iterator.next();
            var currentClient = clients.get(currentKey);
            boolean found;
            found = name.equals(currentClient.getName());
            if(lastName != null) {
                found = found & lastName.equals(currentClient.getLastName());
            }
            if(found) {
                foundClient = currentClient;
                break;
            }
        }

        return Optional.ofNullable(foundClient);

    }

    public Optional<Client> getByNameAndLastNameJava8StreamToList(String name, String lastName) {

        var matchedClients = clients.values().stream().filter((cl) -> isClientMatch(cl, name, lastName)).collect(Collectors.toList());
        if(! matchedClients.isEmpty()) {
            return Optional.of(matchedClients.get(0));
        } else {
            return Optional.empty();
        }

    }

    public Optional<Client> getByNameAndLastNameJava8StreamToFirst(String name, String lastName) {

        return  clients.values().stream().filter((cl) -> isClientMatch(cl, name, lastName)).findFirst();

    }

    public Optional<Client> getByNameAndLastNameJava8ForEach(String name, String lastName) {
        AtomicReference<Client> foundClient = new AtomicReference<>();

        clients.keySet().forEach((currentKey) -> this.checkMatch(currentKey, foundClient, name, lastName));

        return Optional.ofNullable(foundClient.get());

    }

    private void checkMatch(Long id, AtomicReference<Client> foundClient, String name, String lastName) {

        var currentClient = clients.get(id);
        checkMatchClient(foundClient, name, lastName, currentClient);
    }

    private void checkMatchClient(AtomicReference<Client> foundClient, String name, String lastName, Client currentClient) {
        boolean found;
        found = name.equals(currentClient.getName());
        if(lastName != null) {
            found = found & lastName.equals(currentClient.getLastName());
        }
        if(found) {
            foundClient.set(currentClient);
        }
    }

    private boolean isClientMatch(Client client, String name, String lastName) {
        boolean found;
        found = name.equals(client.getName());
        if(lastName != null) {
            found = found & lastName.equals(client.getLastName());
        }
        return found;
    }


}
