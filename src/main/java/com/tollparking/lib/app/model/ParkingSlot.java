package com.tollparking.lib.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode
@Table(name = "parking_slot")
public class ParkingSlot implements Serializable {

    @Id
    @Column(name = "ps_id")
    @SequenceGenerator(name = "parking_slot_seq", sequenceName = "parking_slot_seq",allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="parking_slot_seq")
    private Long parkingSlotId;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private ParkingType parkingType;

    @Column
    private boolean isAvailable;

    @JsonIgnore
    public PricingPolicy getPricingPolicy() {
        return pricingPolicy;
    }

    @JsonIgnore
    public void setPricingPolicy(PricingPolicy pricingPolicy) {
        this.pricingPolicy = pricingPolicy;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pricing_policy_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PricingPolicy pricingPolicy;


    public Long getPricingPolicy_pricingPolicyId(){
        return pricingPolicy.getPricingPolicyId();
    }

    public void setPricingPolicy_pricingPolicyId(Long id){
        pricingPolicy.setPricingPolicyId(id);
    }

}
