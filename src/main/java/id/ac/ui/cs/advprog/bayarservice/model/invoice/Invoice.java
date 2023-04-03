package id.ac.ui.cs.advprog.bayarservice.model.invoice;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices", indexes = {@Index(name = "invoice_session_id_idx", columnList = "sessionId")})
public class Invoice {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false, columnDefinition = "VARCHAR(5)")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP")
    private Date createdAt;
    @Column(nullable = false)
    private Integer totalAmount;
    @Column(nullable = false)
    private Integer adminFee;
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer discount;
    @JsonManagedReference
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Bill> bills;
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID sessionId;
}
