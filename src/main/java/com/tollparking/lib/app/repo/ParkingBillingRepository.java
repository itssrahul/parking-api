package com.tollparking.lib.app.repo;

import com.tollparking.lib.app.model.ParkingBilling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParkingBillingRepository extends JpaRepository<ParkingBilling, Long> {

    List<ParkingBilling> findAllByVehicleNumber(String vehicleNumber);

    Optional<ParkingBilling> findByVehicleNumberAndIsBilled(String vehicleNumber, boolean billed);
}