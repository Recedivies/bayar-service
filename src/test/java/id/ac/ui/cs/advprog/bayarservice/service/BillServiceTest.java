package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.exception.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.repository.BillRepository;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillServiceImpl;
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
public class BillServiceTest {

    @InjectMocks
    private BillServiceImpl billService;

    @Mock
    private BillRepository billRepository;

    Bill bill;

    @BeforeEach
    void setUp() {
        bill = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();
    }

    @Test
    void whenFindByIdAndFoundShouldReturnBill() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.of(bill));

        Bill result = billService.findById(1);

        verify(billRepository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(bill, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(BillDoesNotExistException.class, () -> billService.findById(1));
    }
}
