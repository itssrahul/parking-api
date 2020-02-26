package com.tollparking.lib.app.repo;

import com.tollparking.lib.app.model.ParkingSlot;
import com.tollparking.lib.app.model.ParkingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    @Override
    List<ParkingSlot> findAll();

    List<ParkingSlot> findAllByParkingType(ParkingType parkingType);


    List<ParkingSlot> findAllByParkingTypeAndIsAvailable(ParkingType parkingType, boolean available);

    List<ParkingSlot> findAllByIsAvailable(boolean available);

    List<ParkingSlot> findAllByPricingPolicy_pricingPolicyId(Long id);

    @Override
    void deleteById(Long aLong);


}
