package com.example.accountingservice.models;

import com.example.accountingservice.enums.OperationTypes;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 20)
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Accounts account;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationTypes operationType;

    @Column(name = "amount", nullable = false, columnDefinition = "DECIMAL(16,2)")
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
    @PrePersist
    public void prePersist() {
        if (this.currency == null) {
            this.currency = "INR";  // Set default value to INR
        }
    }
    @CreationTimestamp
    @Column(name = "create_timestamp", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createTimestamp;

    @UpdateTimestamp
    @Column(name = "update_timestamp", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updateTimestamp;
}
