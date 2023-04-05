package id.ac.ui.cs.advprog.bayarservice.repository;

import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @NonNull
    List<Invoice> findAll();
    @NonNull
    Optional<Invoice> findById(@NonNull UUID sessionId);
    void deleteById(@NonNull UUID sessionId);
}
