package id.ac.ui.cs.advprog.bayarservice.service.bill;

import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import org.springframework.stereotype.Service;

@Service
public interface BillService {
    Bill findById(Integer id);
}
