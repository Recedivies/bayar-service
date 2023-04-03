package id.ac.ui.cs.advprog.bayarservice.service.bill;

import id.ac.ui.cs.advprog.bayarservice.exceptions.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    @Override
    public Bill findById(Integer id) {
        Optional<Bill> bill = billRepository.findBillById(id);
        if (bill.isEmpty()) {
            throw new BillDoesNotExistException(id);
        }
        return bill.get();
    }
}
