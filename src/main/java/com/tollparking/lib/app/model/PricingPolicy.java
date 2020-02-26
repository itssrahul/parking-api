package com.tollparking.lib.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode(exclude = "parkingSlots")
@Table(name = "pricing_policy")
public class PricingPolicy implements Serializable {
    @Id
    @Column(name = "pp_id")
    @SequenceGenerator(name = "pricing_policy_seq", sequenceName = "pricing_policy_seq",allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pricing_policy_seq")
    private Long pricingPolicyId;

    @Column(length = 50)
    @NotEmpty
    private String pricingPolicyName;

    @Column
    private boolean isHourly;

    @NotNull
    @Column(length = 5, precision = 2)
    @PositiveOrZero
    private Double fixedPrice;

    @NotNull
    @Column(length = 5, precision = 2)
    @PositiveOrZero
    private Double hourPrice;


    @NotNull
    @Column(length = 5, precision = 2)
    @PositiveOrZero
    private Double cleaningServiceCharge;

    @NotNull
    @Column(length = 5, precision = 2)
    @PositiveOrZero
    private Double otherExtraCharges;


    public PricingPolicy(@NotEmpty String pricingPolicyName, boolean isHourly, @PositiveOrZero Double fixedPrice, @NotNull @PositiveOrZero Double hourPrice, @PositiveOrZero Double cleaningServiceCharge, @PositiveOrZero Double otherExtraCharges) {
        this.pricingPolicyName = pricingPolicyName;
        this.isHourly = isHourly;
        this.fixedPrice = fixedPrice;
        this.hourPrice = hourPrice;
        this.cleaningServiceCharge = cleaningServiceCharge;
        this.otherExtraCharges = otherExtraCharges;
    }

    public PricingPolicy(){}
}
