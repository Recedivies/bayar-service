package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.dto.Bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.BillRepository;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillServiceImpl;
import id.ac.ui.cs.advprog.bayarservice.service.invoice.InvoiceServiceImpl;
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

    @Mock
    private InvoiceServiceImpl invoiceService;

    Bill bill;

    Invoice invoice;

    @BeforeEach
    void setUp() {
        bill = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();

        invoice = Invoice.builder()
                .id(1)
                .totalAmount(50000)
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

    @Test
    void whenDeleteAndFoundByIdShouldDeleteBill() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.of(bill));

        billService.delete(1);

        verify(billRepository, atLeastOnce()).findById(any(Integer.class));
        verify(billRepository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteAndNotFoundByIdShouldThrowException() {
        when(billRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(BillDoesNotExistException.class, () -> billService.delete(1));
    }

    @Test
    void whenCreateBillShouldReturnBill() {
        when(invoiceService.findById(any(Integer.class))).thenReturn(invoice);
        when(billRepository.save(any(Bill.class))).thenReturn(bill);
        BillRequest billRequest = new BillRequest("Coffee", 5, 10000, 50000L, 1);
        Bill result = billService.create(billRequest);

        verify(billRepository, atLeastOnce()).save(any(Bill.class));
        Assertions.assertEquals(bill, result);
    }
}
