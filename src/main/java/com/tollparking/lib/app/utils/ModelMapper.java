package com.tollparking.lib.app.utils;

import com.tollparking.lib.app.model.ParkingSlot;
import com.tollparking.lib.app.model.PricingPolicy;
import com.tollparking.lib.app.payload.CreateParkingSlot;
import com.tollparking.lib.app.payload.CreatePricing;
import com.tollparking.lib.app.payload.UpdatePricing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {
    private static final Logger logger = LoggerFactory.getLogger(ModelMapper.class);

    public PricingPolicy mapToPricingPolicyUpdateModel(UpdatePricing updatePricing, PricingPolicy pricingPolicy) {
        pricingPolicy.setHourly(updatePricing.isHourly());
        pricingPolicy.setCleaningServiceCharge(updatePricing.getCleaningServiceCharge());
        pricingPolicy.setFixedPrice(updatePricing.getFixedPrice());
        pricingPolicy.setHourPrice(updatePricing.getHourPrice());
        pricingPolicy.setOtherExtraCharges(updatePricing.getOtherExtraCharges());
        return pricingPolicy;
    }

    public PricingPolicy mapToPricingPolicyCreateModel(CreatePricing createPricing) {
        PricingPolicy pricingPolicy = new PricingPolicy();
        pricingPolicy.setHourly(createPricing.isHourly());
        pricingPolicy.setCleaningServiceCharge(createPricing.getCleaningServiceCharge());
        pricingPolicy.setFixedPrice(createPricing.getFixedPrice());
        pricingPolicy.setHourPrice(createPricing.getHourPrice());
        pricingPolicy.setOtherExtraCharges(createPricing.getOtherExtraCharges());
        pricingPolicy.setPricingPolicyName(createPricing.getPricingPolicyName());
        return pricingPolicy;
    }

    public ParkingSlot mapToParkingSlotCreateModel(CreateParkingSlot createParkingSlot, PricingPolicy policy) {
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setParkingType(createParkingSlot.getParkingType());
        parkingSlot.setAvailable(createParkingSlot.isAvailable());
        parkingSlot.setPricingPolicy(policy);
        return parkingSlot;
    }
}
