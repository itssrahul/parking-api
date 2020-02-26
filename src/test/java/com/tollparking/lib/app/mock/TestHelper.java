package com.tollparking.lib.app.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tollparking.lib.app.model.ParkingBilling;
import com.tollparking.lib.app.model.ParkingSlot;
import com.tollparking.lib.app.model.ParkingType;
import com.tollparking.lib.app.model.PricingPolicy;
import com.tollparking.lib.app.payload.CreateParkingSlot;
import com.tollparking.lib.app.payload.CreatePricing;
import com.tollparking.lib.app.payload.UpdatePricing;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    public static PricingPolicy dummyPricingPolicy(String name, Long id){
        PricingPolicy pp1 = new PricingPolicy(name,true,4.0,6.4,0.0,0.0);
        pp1.setPricingPolicyId(id);
        return pp1;
    }

    public static List<PricingPolicy> dummyPricingPolicyList(){
        PricingPolicy pp1 = dummyPricingPolicy("name1",1L);
        PricingPolicy pp2 = dummyPricingPolicy("name2",2L);
        List<PricingPolicy> pricingPolicies = new ArrayList<>();
        pricingPolicies.add(pp1);
        pricingPolicies.add(pp2);
        return pricingPolicies;
    }

    public static ParkingSlot dummyParkingSlot( Long psId, String name){
        PricingPolicy pp1 = dummyPricingPolicy(name,1L);
        ParkingSlot ps = new ParkingSlot();
        ps.setAvailable(true);
        ps.setPricingPolicy(pp1);
        ps.setParkingType(ParkingType.CAR_50KW_ELECTRIC);
        ps.setParkingSlotId(psId);
        return ps;
    }

    public static List<ParkingSlot> dummyParkingSlotList(){
        ParkingSlot ps1 = dummyParkingSlot(1L,"nameP");
        ParkingSlot ps2 = dummyParkingSlot(2L,"nameQ");
        List<ParkingSlot> parkingSlots = new ArrayList<>();
        parkingSlots.add(ps1);
        parkingSlots.add(ps2);
        return parkingSlots;
    }

    public static ParkingBilling dummyParkingBilling(String vehicleNumber, Long id){
        ParkingBilling pb1 = new ParkingBilling();
        ParkingSlot parkingSlot = dummyParkingSlot(77L,"nameR");
        pb1.setParkingSlot(parkingSlot);
        pb1.setBillAmount(44.0);
        pb1.setBilled(true);
        pb1.setEndTime(null);
        pb1.setParkingBillingId(id);
        pb1.setStartTime(LocalDateTime.now());
        pb1.setVehicleNumber(vehicleNumber);
        return pb1;
    }

    public static List<ParkingBilling> dummyParkingBillingList(){
        ParkingBilling pb1 = dummyParkingBilling("v1",91L);
        ParkingBilling pb2 = dummyParkingBilling("v2",92L);
        ParkingBilling pb3 = dummyParkingBilling("some",93L);
        List<ParkingBilling> parkingBillings = new ArrayList<>();
        parkingBillings.add(pb1);
        parkingBillings.add(pb2);
        pb3.setBilled(false);
        parkingBillings.add(pb3);
        return parkingBillings;
    }

    public static UpdatePricing dummyUpdatePricing(){
        UpdatePricing up = new UpdatePricing();
        up.setCleaningServiceCharge(0.0);
        up.setFixedPrice(4.0);
        up.setHourly(true);
        up.setHourPrice(4.6);
        up.setOtherExtraCharges(0.3);
        up.setPricingPolicyId(1L);
        return up;
    }

    public static CreatePricing dummyCreatePricing(){
        CreatePricing cp = new CreatePricing();
        cp.setCleaningServiceCharge(0.0);
        cp.setFixedPrice(4.0);
        cp.setHourly(true);
        cp.setHourPrice(4.6);
        cp.setOtherExtraCharges(0.3);
        cp.setPricingPolicyName("create");
        return cp;
    }

    public static CreateParkingSlot dummyCreateParkingSlot(){
        CreateParkingSlot ps = new CreateParkingSlot();
        ps.setAvailable(true);
        ps.setParkingType(ParkingType.STANDARD);
        ps.setPricingPolicyId(2L);
        return ps;
    }

    public static String toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }

}
