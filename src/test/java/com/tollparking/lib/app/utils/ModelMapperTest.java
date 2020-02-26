package com.tollparking.lib.app.utils;

import com.tollparking.lib.app.model.ParkingSlot;
import com.tollparking.lib.app.model.PricingPolicy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static com.tollparking.lib.app.mock.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ModelMapperTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    public void TestMapToPricingPolicyUpdateModel(){
        PricingPolicy policy = modelMapper.mapToPricingPolicyUpdateModel(dummyUpdatePricing(),dummyPricingPolicy("some",4L));
        assertThat(policy.getPricingPolicyId()).isEqualTo(4L);
    }

    @Test
    public void TestMapToPricingPolicyCreateModel(){
        PricingPolicy policy = modelMapper.mapToPricingPolicyCreateModel(dummyCreatePricing());
        assertThat(policy.getPricingPolicyName()).isEqualTo("create");
    }

    @Test
    public void TestMapToParkingSlotCreateModel(){
        ParkingSlot slot = modelMapper.mapToParkingSlotCreateModel(dummyCreateParkingSlot(),dummyPricingPolicy("some",2L));
        assertThat(slot.getPricingPolicy().getPricingPolicyId()).isEqualTo(2L);
    }



}
