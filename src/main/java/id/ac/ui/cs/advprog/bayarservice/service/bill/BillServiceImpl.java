package id.ac.ui.cs.advprog.bayarservice.service.bill;

import id.ac.ui.cs.advprog.bayarservice.dto.Bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.exceptions.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.bayarservice.service.invoice.InvoiceService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final InvoiceService invoiceService;

    @Override
    public Bill findById(Integer id) {
        Optional<Bill> bill = billRepository.findBillById(id);
        if (bill.isEmpty()) {
            throw new BillDoesNotExistException(id);
        }
        return bill.get();
    }

    @Override
    public Bill create(BillRequest request) {
        Bill bill = Bill.builder()
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .subTotal(request.getSubTotal())
                .invoice(invoiceService.findById(request.getInvoiceId()))
                .build();
        return billRepository.save(bill);
    }

    @Override
    public void delete(Integer id) {
        if (isBillDoesNotExist(id)) {
            throw new BillDoesNotExistException(id);
        } else {
            Invoice parent = invoiceService.findById(billRepository.findBillById(id).get().getInvoice().getId());
            parent.getBills().remove(billRepository.findBillById(id).get());
            billRepository.deleteById(id);
        }
    }

    private boolean isBillDoesNotExist(Integer id) {
        return billRepository.findBillById(id).isEmpty();
    }
}
