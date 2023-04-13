package id.ac.ui.cs.advprog.bayarservice.repository;

import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BankRepositoryTest {
    @Autowired
    private BankRepository bankRepository;

    @BeforeEach
    void setUp() {
        Bank bank = Bank.builder()
                .id(1)
                .name("Bank BCA")
                .adminFee(5000)
                .build();
        bankRepository.save(bank);
    }

    @AfterEach
    void tearDown() {
        bankRepository.deleteAll();
    }

    @Test
    void testFindByIdOrFindByName() {
        Optional<Bank> optionalBank = bankRepository.findById(1);
        Optional<Bank> optionalBank2 = bankRepository.findByName("Bank BCA");

        Assertions.assertTrue(optionalBank.isPresent());
        Assertions.assertTrue(optionalBank2.isPresent());
    }

    @Test
    void testFindByIdOrFindByNameNotFound() {
        Optional<Bank> optionalBank = bankRepository.findById(100);
        Optional<Bank> optionalBank2 = bankRepository.findByName("Bank BRI");

        Assertions.assertFalse(optionalBank.isPresent());
        Assertions.assertFalse(optionalBank2.isPresent());
    }
}
