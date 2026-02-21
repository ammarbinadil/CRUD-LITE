package com.banking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account number is required")
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @NotBlank(message = "Account type is required")
    @Column(name = "account_type", nullable = false)
    private String accountType;

    @NotNull(message = "Balance is required")
    @Column(name = "balance", nullable = false)
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", nullable = false)
    @JsonBackReference
    private Person person;

    // Convenience getter for frontend to have person info
    @Transient
    private Long personId;

    @Transient
    private String personName;

    @PostLoad
    private void postLoad() {
        if (person != null) {
            this.personId = person.getId();
            this.personName = person.getFirstName() + " " + person.getLastName();
        }
    }
}
