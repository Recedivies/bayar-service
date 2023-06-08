package id.ac.ui.cs.advprog.bayarservice.service.bill;

import id.ac.ui.cs.advprog.bayarservice.exception.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.dto.bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.bayarservice.service.invoice.InvoiceService;
import id.ac.ui.cs.advprog.bayarservice.exception.bill.BillSubtotalUnderZeroException;
import id.ac.ui.cs.advprog.bayarservice.exception.bill.BillQuantityUnderZeroException;
import id.ac.ui.cs.advprog.bayarservice.exception.bill.BillPriceUnderZeroException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    private final InvoiceService invoiceService;

    @Override
    public Bill findById(Integer id) {
        return this.billRepository.findById(id)
                .orElseThrow(() -> new BillDoesNotExistException(id));
    }

    @Override
    public Bill create(BillRequest request) {
        Invoice invoice = invoiceService.findBySessionId(request.getSessionId());
        Bill bill = Bill.builder()
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .subTotal(request.getSubTotal())
                .invoice(invoice)
                .build();
        Integer toBeAdded = bill.getSubTotal().intValue();
        invoice.setTotalAmount(invoice.getTotalAmount() + toBeAdded);
        return billRepository.save(bill);
    }

    @Override
    public void delete(Integer id) {
        if (isBillDoesNotExist(id)) {
            throw new BillDoesNotExistException(id);
        } else {
            billRepository.deleteById(id);
        }
    }

    @Override
    public Bill update(Integer id, BillRequest request) {


        Optional<Bill> optionalBill = this.billRepository.findById(id);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            try {
                if (request.getPrice() < 0) {
                    throw new BillPriceUnderZeroException(request.getPrice());
                } else if (request.getQuantity() < 0) {
                    throw new BillQuantityUnderZeroException(request.getQuantity());
                } else if (request.getSubTotal() < 0) {
                    throw new BillSubtotalUnderZeroException(request.getSubTotal());
                }
            } catch (BillPriceUnderZeroException e) {
                throw new BillPriceUnderZeroException(request.getPrice());
            } catch (BillQuantityUnderZeroException e) {
                throw new BillQuantityUnderZeroException(request.getQuantity());
            } catch (BillSubtotalUnderZeroException e) {
                throw new BillSubtotalUnderZeroException(request.getSubTotal());
            }
            bill.setName(request.getName());
            bill.setPrice(request.getPrice());
            bill.setQuantity(request.getQuantity());
            bill.setSubTotal(request.getSubTotal());

            Invoice invoice = this.invoiceService.findById(bill.getInvoice().getId());
            long toBeAdded = Math.max(0, request.getSubTotal() - bill.getSubTotal());
            invoice.setTotalAmount(invoice.getTotalAmount() + toBeAdded);
            this.billRepository.save(bill);
            return bill;
        } else {
            throw new BillDoesNotExistException(id);
        }
    }

    private boolean isBillDoesNotExist(Integer id) {
        return billRepository.findById(id).isEmpty();
    }
}
