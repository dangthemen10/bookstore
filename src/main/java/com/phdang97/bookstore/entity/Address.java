package com.phdang97.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.phdang97.bookstore.enums.Government;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "address")
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is required")
    @Column(name = "full_name")
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    @Column(name = "mobile_number")
    private String mobileNumber;

    @NotNull(message = "Government is required")
    @Enumerated(EnumType.STRING)
    private Government government;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Full address is required")
    @Column(name = "full_address")
    private String fullAddress;

    @NotNull(message = "Default status is required")
    @Column(name = "is_default")
    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getFullName(), address.getFullName()) && Objects.equals(getMobileNumber(), address.getMobileNumber()) && getGovernment() == address.getGovernment() && Objects.equals(getCity(), address.getCity()) && Objects.equals(getFullAddress(), address.getFullAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullName(), getMobileNumber(), getGovernment(), getCity(), getFullAddress());
    }

    public double getShippingPrice() {
        return this.government.getShippingPrice();
    }
}
