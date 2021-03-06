package com.tollparking.lib.app.repo;

import com.tollparking.lib.app.model.ParkingBilling;
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
public class ParkingBillingRepositoryTest {

    @Autowired
    private ParkingBillingRepository parkingBillingRepository;

    @Test
    public void whenFindAllByVehicleNumber_noParkingBilling(){

        List<ParkingBilling> parkingBillings = parkingBillingRepository.findAllByVehicleNumber("abc123");
        assertThat(parkingBillings.isEmpty()).isTrue();
    }

    @Test
    public void whenFindByVehicleNumberAndIsBilled_noParkingBilling(){
        Optional<ParkingBilling> parkingBillings = parkingBillingRepository.findByVehicleNumberAndIsBilled("abc123", true);
        assertThat(parkingBillings.isPresent()).isFalse();
    }



}
