package id.ac.ui.cs.advprog.bayarservice.service.bank;

import id.ac.ui.cs.advprog.bayarservice.exception.BankAlreadyExistsException;
import id.ac.ui.cs.advprog.bayarservice.exception.BankDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.dto.Bank.BankRequest;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    @Override
    public Bank create(BankRequest request) {
        if (isBankAlreadyExist(request.getName())) {
            throw new BankAlreadyExistsException(request.getName());
        }
        Bank newBank = Bank.builder()
                .name(request.getName())
                .adminFee(request.getAdminFee())
                .build();
        return bankRepository.save(newBank);
    }

    private boolean isBankAlreadyExist(String name) {
        return bankRepository.findByName(name).isPresent();
    }
}
