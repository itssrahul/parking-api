package com.tollparking.lib.app.repo;

import com.tollparking.lib.app.model.ParkingSlot;
import com.tollparking.lib.app.model.ParkingType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParkingSlotRepositoryTest {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Test
    public void whenFindAll_thenParkingSlot() {
        List<ParkingSlot> listFound = parkingSlotRepository.findAll();
        assertThat(listFound.size()).isEqualTo(22);
    }

    @Test
    public void whenDeleteById_thenParkingSlot() {
        parkingSlotRepository.deleteById((long) 2);
        Optional<ParkingSlot> pricingPolicy = parkingSlotRepository.findById(2L);
        assertThat(pricingPolicy.isPresent()).isEqualTo(false);
    }

    @Test
    public void whenFindAllByParkingType_thenParkingSlot() {
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAllByParkingType(ParkingType.valueOf("STANDARD"));
        assertThat(parkingSlots.isEmpty()).isFalse();
    }

    @Test
    public void whenFindAllByParkingTypeAndIsAvailable_thenParkingSlot() {
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAllByParkingTypeAndIsAvailable(ParkingType.valueOf("STANDARD"), true);
        assertThat(parkingSlots.isEmpty()).isFalse();
    }

    @Test
    public void whenFindAllByIsAvailable_thenParkingSlot() {
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAllByIsAvailable(true);
        assertThat(parkingSlots.isEmpty()).isFalse();
    }

    @Test
    public void whenFindAllByPricingPolicy_pricingPolicyId_thenParkingSlot() {
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAllByPricingPolicy_pricingPolicyId(2L);
        assertThat(parkingSlots.isEmpty()).isFalse();
    }


}
