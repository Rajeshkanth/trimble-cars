package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public void setActive(boolean b) {

    }

    @Enumerated(EnumType.STRING)
    private LeaseStatus status;

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }


    public enum LeaseStatus {
        ACTIVE, COMPLETED, ENDED, CANCELLED
    }
}
