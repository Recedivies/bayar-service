package id.ac.ui.cs.advprog.bayarservice.service.bank;

import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.bayarservice.dto.Bank.BankRequest;

@Service
public interface BankService {
    Bank create(BankRequest request);
    Bank findById(Integer id);
    void deleteById(Integer id);
    Bank update(Integer id, BankRequest request);
}
