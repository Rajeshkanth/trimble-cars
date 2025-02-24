package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.enums.CarStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String brand;
    private boolean availableForLease;

    @Column(unique = true, nullable = false) // Ensuring unique registration numbers
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    private CarStatus status = CarStatus.IDLE; // Default to IDLE when registered

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false) // Every car must have an owner
    private User owner;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Lease> leaseHistory = new ArrayList<>();

    public boolean isAvailableForLease() {
        return leaseHistory.stream().noneMatch(lease -> lease.getStatus() == Lease.LeaseStatus.ACTIVE);
    }

    public void setAvailableForLease(boolean available) {
        this.availableForLease = available;
    }

}
