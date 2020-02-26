package com.tollparking.lib.app.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode
@Table(name = "parking_billing",
        indexes = @Index(name = "vehicleIndex",  columnList="vehicleNumber", unique = false)
)
public class ParkingBilling implements Serializable {

    @Id
    @Column(name = "pb_id")
    @SequenceGenerator(name = "parking_billing_seq", sequenceName = "parking_billing_seq",allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="parking_billing_seq")
    private Long parkingBillingId;

    @Column(length = 25, nullable = false)
    @NotEmpty
    private String vehicleNumber;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    @Column
    private boolean isBilled;

    @Column
    @PositiveOrZero
    private Double billAmount;

    @JsonIgnore
    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }

    @JsonIgnore
    public void setParkingSlot(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parking_slot_id", nullable = false)
    private ParkingSlot parkingSlot;

    public Long getParkingSlot_parkingSlotId(){
        return parkingSlot.getParkingSlotId();
    }


}
