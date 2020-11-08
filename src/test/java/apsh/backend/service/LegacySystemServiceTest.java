package apsh.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LegacySystemServiceTest {

    @Autowired
    LegacySystemService legacySystemService = null;

    @Test
    void getAllOrders() {
        System.out.println(legacySystemService.getAllOrders());
    }

    @Test
    void getAllHumans() {
        System.out.println(legacySystemService.getAllHumans());
    }

    @Test
    void getAllEquipments() {
        System.out.println(legacySystemService.getAllEquipments());
    }

    @Test
    void getAllCrafts() {
        System.out.println(legacySystemService.getAllCrafts());
    }
}