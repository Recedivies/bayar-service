package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.dto.Bank.BankRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.BankAlreadyExistsException;
import id.ac.ui.cs.advprog.bayarservice.exception.BankDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.repository.BankRepository;
import id.ac.ui.cs.advprog.bayarservice.service.bank.BankServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {
    @InjectMocks
    private BankServiceImpl bankService;

    @Mock
    private BankRepository bankRepository;

    Bank bank;

    @Test
    void whenCreateBankShouldReturnBank() {
        BankRequest request = BankRequest.builder()
                .name("Bank BCA")
                .build();

        bank = Bank.builder()
                .name(request.getName())
                .build();
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);
        Bank result = bankService.create(request);
        Assertions.assertEquals(bank, result);
    }

    @Test
    void whenCreateBankAlreadyExistShouldThrowBankAlreadyExistException() {
        BankRequest request = BankRequest.builder()
                .name("Bank BCA")
                .build();

        bank = Bank.builder()
                .name(request.getName())
                .build();
        when(bankRepository.findByName(any(String.class))).thenReturn(Optional.of(bank));
        Assertions.assertThrows(BankAlreadyExistsException.class, () -> bankService.create(request));
    }

    @Test
    void whenDeleteAndFoundByIdShouldDeleteBank() {
        bank = Bank.builder()
                .name("BCA")
                .adminFee(3000)
                .build();
        bankRepository.save(bank);
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.of(bank));

        bankService.deleteById(1);

        verify(bankRepository, atLeastOnce()).findById(any(Integer.class));
        verify(bankRepository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteAndNotFoundByIdShouldThrowException() {
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(BankDoesNotExistException.class, () -> bankService.deleteById(1));
    }

    @Test
    void whenUpdateAndFoundByIdShouldUpdateBank() {
        BankRequest request = BankRequest.builder()
                .name("Bank BCA")
                .build();

        bank = Bank.builder()
                .name(request.getName())
                .build();
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.of(bank));
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);
        Bank result = bankService.update(1, request);
        Assertions.assertEquals(bank, result);
    }

    @Test
    void whenUpdateAndNotFoundByIdShouldThrowException() {
        BankRequest request = BankRequest.builder()
                .name("Bank BCA")
                .build();

        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(BankDoesNotExistException.class, () -> bankService.update(1, request));
    }

    @Test
    void whenUpdateAndFoundByIdButNameAlreadyExistShouldThrowException() {
        BankRequest request = BankRequest.builder()
                .name("Bank BCA")
                .build();

        bank = Bank.builder()
                .name(request.getName())
                .build();
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.of(bank));
        when(bankRepository.findByName(any(String.class))).thenReturn(Optional.of(bank));
        Assertions.assertThrows(BankAlreadyExistsException.class, () -> bankService.update(1, request));
    }

}
