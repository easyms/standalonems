package com.standalonems.api;

import com.standalonems.dto.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;


public class ClientResourceTest {

    private static Map<Long, Client> clients = new HashMap<>();

    private static Map<ClientKeyFunctionallyUnique, Client> clientsCache = new HashMap<>();


    @BeforeAll
    private static void initClient() {

        clients.put(1L, Client.builder().email("anis@gmail.com").name("anis").lastName("bessa").cat("A").build());
        clients.put(2L, Client.builder().email("wael@gmail.com").name("wael").lastName("ben").cat("A").build());
        clients.put(25L, Client.builder().email("wael@gmail.com").name("wael").lastName("ben").cat("B").build());
        clients.put(3L, Client.builder().email("lionel@gmail.com").name("lionel").lastName("name2").cat(("A")).build());
        clients.put(4L, Client.builder().email("aziz@gmail.com").name("aziz").lastName("name3").cat("C").build());

        clientsCache =
                clients.values().stream().collect(Collectors.toMap((cl) -> new ClientKeyFunctionallyUnique(cl.getName(), cl.getLastName()),
                Function.identity(), (cl1, cl2) -> cl1));
    }


    @Test
    void should_set_duplicate_elements_when_equals_and_hashcode_are_not_overriden() {
        Set<ClientKey> clientKeys = new HashSet<>();
        ClientKey k1 = new ClientKey("anis", "bessa");

        clientKeys.add(k1);
        var k2 = new ClientKey("anis", "bessa");
        clientKeys.add(k2);
        assertThat(clientKeys.size()).isEqualTo(2);

    }


    @Test
    void should_set_avoid_duplication_elements_when_equals_and_hashcode_are_overriden() {
        Set<ClientKeyFunctionallyUnique> clientKeys = new HashSet<>();
        ClientKeyFunctionallyUnique k1 = new ClientKeyFunctionallyUnique("anis", "bessa");
        clientKeys.add(k1);
        var k2 = new ClientKeyFunctionallyUnique("anis", "bessa");
        clientKeys.add(k2);
        assertThat(clientKeys.size()).isEqualTo(1);

    }

    @Test
    void should_find_value_existing_in_cache_with_given_key_functionally_existant() {

        ClientKeyFunctionallyUnique key = new ClientKeyFunctionallyUnique("anis", "bessa");
        assertThat(clientsCache.get(key)).isNotNull();

    }

    @Test
    void count_clients_by_category() {
        var collect = clients.values().stream().collect(Collectors.groupingBy(Client::getCat, Collectors.summingInt((cl) -> 1)));
        assertThat(collect.size()).isEqualTo(3);
        assertThat(collect.get("A")).isEqualTo(3);

    }

    @Test
    void should_load_only_Clients_cat_A() {

        Long[] ids = {1L, 2L, 25L, 3L, 4L, 455L, 23L};

        List<Client> clientStream =
                Arrays.stream(ids).map((id) -> Optional.ofNullable(clients.get(id)))
                        .filter(Optional::isPresent).
                        map(Optional::get).
                        filter((cl) -> cl.getCat().equals("A")).collect(Collectors.toList());


        assertThat(clientStream.size()).isEqualTo(3);


    }



    @Getter
    @Setter
    @AllArgsConstructor

    static class ClientKey {
        private String name;
        private String lastName;

    }


    @Getter
    @Setter
    @AllArgsConstructor
    static class ClientKeyFunctionallyUnique {
        private String name;
        private String lastName;

        @Override
        public int hashCode() {
            return name.hashCode() + lastName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(! (obj instanceof ClientKeyFunctionallyUnique)) {
                return false;
            }
            ClientKeyFunctionallyUnique other = (ClientKeyFunctionallyUnique) obj;
            return equalsField(this.name, other.name) && equalsField(this.lastName, other.lastName);
        }

        private boolean equalsField(Object myField, Object otherField) {
            if(myField != null) {
                return myField.equals(otherField);
            } else {
                return otherField == null;
            }
        }

    }

}