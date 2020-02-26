package com.tollparking.lib.app.service;

import com.tollparking.lib.app.exception.*;
import com.tollparking.lib.app.model.ParkingBilling;
import com.tollparking.lib.app.model.ParkingSlot;
import com.tollparking.lib.app.model.ParkingType;
import com.tollparking.lib.app.model.PricingPolicy;
import com.tollparking.lib.app.payload.CreateParkingSlot;
import com.tollparking.lib.app.payload.CreatePricing;
import com.tollparking.lib.app.payload.UpdatePricing;
import com.tollparking.lib.app.repo.ParkingBillingRepository;
import com.tollparking.lib.app.repo.ParkingSlotRepository;
import com.tollparking.lib.app.repo.PricingRepository;
import com.tollparking.lib.app.utils.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class TollParkingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TollParkingService.class);
    @Autowired
    private PricingRepository pricingRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    @Autowired
    private ParkingBillingRepository parkingBillingRepository;


    public CompletableFuture<List<PricingPolicy>> getAllPricingPolicy() {
        LOGGER.info("Request to get a list of PricingPolicy");
        final List<PricingPolicy> pricingPolicies = pricingRepository.findAll();
        return CompletableFuture.completedFuture(pricingPolicies);
    }


    public CompletableFuture<PricingPolicy> getPricingPolicyByPolicyId(Long id) throws InvalidPricingPolicy {
        LOGGER.info("Request to get a PricingPolicy by Id - {}",id);
        final Optional<PricingPolicy> pricingPolicies = pricingRepository.findById(id);
        return CompletableFuture.completedFuture(pricingPolicies.orElseThrow(() -> new InvalidPricingPolicy("No Pricing Policy found for Id: "+id)));
    }


    public synchronized CompletableFuture<PricingPolicy> updatePricingPolicyByPolicyId(UpdatePricing pricingPolicy) throws InvalidInput, InvalidPricingPolicy {
        LOGGER.info("Updating PricingPolicy");
        validateInput(pricingPolicy.isHourly(),pricingPolicy.getHourPrice());
        PricingPolicy policy = findExistingPricingPolicy(pricingPolicy);
        pricingRepository.save(policy);
        return CompletableFuture.completedFuture(policy);
    }

    private void validateInput(boolean isHourly, Double hourPrice ) throws InvalidInput {
        if(isHourly && hourPrice<=0){
            throw new InvalidInput("Hour Price can not be zero when Pricing is Hourly");
        }
    }

    private PricingPolicy findExistingPricingPolicy(UpdatePricing pricingPolicy) throws InvalidPricingPolicy {
        Optional<PricingPolicy> policy= pricingRepository.findById(pricingPolicy.getPricingPolicyId());
        if(policy.isPresent()){
            return modelMapper.mapToPricingPolicyUpdateModel(pricingPolicy,policy.get());
        }else {
            throw new InvalidPricingPolicy("Trying to Update Invalid pricing policy with ID..."+pricingPolicy.getPricingPolicyId());
        }
    }


    public synchronized CompletableFuture<PricingPolicy> createPricingPolicy(CreatePricing pricingPolicy) throws InvalidInput {
        LOGGER.info("Creating PricingPolicy");
        validateInput(pricingPolicy.isHourly(),pricingPolicy.getHourPrice());
        PricingPolicy policy = modelMapper.mapToPricingPolicyCreateModel(pricingPolicy);
        pricingRepository.save(policy);
        return CompletableFuture.completedFuture(policy);
    }


    public CompletableFuture<Void> deletePricingPolicyByPolicyId(Long id) {
        LOGGER.info("Deleting a PricingPolicy by Id - {}",id);
        pricingRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }


    public CompletableFuture<List<ParkingSlot>> getAllParkingSlots() {
        LOGGER.info("Request to get a list of all ParkingSlots");
        return CompletableFuture.completedFuture(parkingSlotRepository.findAll());
    }


    public CompletableFuture<List<ParkingSlot>> getAllParkingSlotsByType(ParkingType parkingType) {
        LOGGER.info("Request to get a list of all ParkingSlots by Parking Type");
        return CompletableFuture.completedFuture(parkingSlotRepository.findAllByParkingType(parkingType));
    }


    public CompletableFuture<List<ParkingSlot>> getAllParkingSlotsByTypeAndAvailability(ParkingType parkingType, boolean available) {
        LOGGER.info("Request to get a list of all ParkingSlots by Parking Type and Availability");
        return CompletableFuture.completedFuture(parkingSlotRepository.findAllByParkingTypeAndIsAvailable(parkingType, available));
    }


    public CompletableFuture<List<ParkingSlot>> getAllParkingSlotsByAvailability( boolean available) {
        LOGGER.info("Request to get a list of all ParkingSlots by Availability");
        return CompletableFuture.completedFuture(parkingSlotRepository.findAllByIsAvailable(available));
    }


    public CompletableFuture<List<ParkingSlot>> getAllParkingSlotsByPricingPolicyId(Long id) {
        LOGGER.info("Request to get a list of all ParkingSlots by Pricing Policy Id - {}",id);
        return CompletableFuture.completedFuture(parkingSlotRepository.findAllByPricingPolicy_pricingPolicyId(id));
    }

    private ParkingSlot findExistingParkingSlot(Long id) throws InvalidParkingSlot {
        Optional<ParkingSlot> parkingSlot= parkingSlotRepository.findById(id);
        if(parkingSlot.isPresent()){
            return parkingSlot.get();
        }else {
            throw new InvalidParkingSlot("Invalid Parking Slot Id provided...");
        }
    }


    public synchronized CompletableFuture<ParkingSlot> updateParkingTypeByParkingSlotId(Long id,ParkingType parkingType) throws  InvalidParkingSlot {
        LOGGER.info("Updating ParkingSlot for id {}" ,id);
        ParkingSlot parkingSlot = findExistingParkingSlot(id);
        parkingSlot.setParkingType(parkingType);
        parkingSlotRepository.save(parkingSlot);
        return CompletableFuture.completedFuture(parkingSlot);
    }


    public synchronized CompletableFuture<ParkingSlot> updateAvailabilityByParkingSlotId(Long id,boolean availability) throws  InvalidParkingSlot {
        LOGGER.info("Updating Availability for id {}" ,id);
        ParkingSlot parkingSlot = findExistingParkingSlot(id);
        parkingSlot.setAvailable(availability);
        parkingSlotRepository.save(parkingSlot);
        return CompletableFuture.completedFuture(parkingSlot);
    }


    public synchronized CompletableFuture<ParkingSlot> updatePricingPolicyByParkingSlotId(Long id,Long pricingPolicyId) throws  InvalidParkingSlot {
        LOGGER.info("Updating Pricing Policy for id {}" ,id);
        ParkingSlot parkingSlot = findExistingParkingSlot(id);
        parkingSlot.setPricingPolicy_pricingPolicyId(pricingPolicyId);
        parkingSlotRepository.save(parkingSlot);
        return CompletableFuture.completedFuture(parkingSlot);
    }


    public synchronized CompletableFuture<ParkingSlot> createNewParkingSlot(CreateParkingSlot parkingSlot) throws InvalidPricingPolicy {
        LOGGER.info("Creating ParkingSlot {}", parkingSlot);
        return CompletableFuture.completedFuture(pricingRepository.findById(parkingSlot.getPricingPolicyId()).map(ps -> {
            ParkingSlot slot = modelMapper.mapToParkingSlotCreateModel(parkingSlot, ps);
            parkingSlotRepository.save(slot);
            return slot;
        }).orElseThrow(() -> new InvalidPricingPolicy("Pricing Policy Id " + parkingSlot.getPricingPolicyId() + " not found")));
    }


    public CompletableFuture<ParkingSlot> deleteParkingSlot(Long id) {
        LOGGER.info("Deleting ParkingSlot for id - {}", id);
        parkingSlotRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }


    public synchronized CompletableFuture<List<ParkingSlot>> bulkCreateParkingSlot (final MultipartFile file) throws Exception {
        final long start = System.currentTimeMillis();
        List<ParkingSlot> parkingSlots = parseCSVFile(file);
        LOGGER.info("Saving a list of parking Slots of size {} records", parkingSlots.size());
        parkingSlots = parkingSlotRepository.saveAll(parkingSlots);
        LOGGER.info("Elapsed time: {}", (System.currentTimeMillis() - start));
        return CompletableFuture.completedFuture(parkingSlots);
    }

    public List<ParkingSlot> parseCSVFile(final MultipartFile file) throws Exception {
        final List<ParkingSlot> parkingSlots=new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line=br.readLine()) != null) {
                    final String[] data=line.split(",");
                    pricingRepository.findById(Long.valueOf(data[2])).map(ps -> {
                        final ParkingSlot parkingSlot = new ParkingSlot();
                        parkingSlot.setParkingType(ParkingType.valueOf(data[0]));
                        parkingSlot.setAvailable(Boolean.parseBoolean(data[1]));
                        parkingSlot.setPricingPolicy(ps);
                        return parkingSlots.add(parkingSlot);
                    });
                }
                return parkingSlots;
            }
        } catch(final IOException e) {
            LOGGER.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }


    public CompletableFuture<List<ParkingBilling>> getAllParkedVehicles() {
        LOGGER.info("Request to get a list of all Parked Vehicless");
        return CompletableFuture.completedFuture(parkingBillingRepository.findAll());
    }


    public CompletableFuture<ParkingBilling> getParkedVehicleById(Long id) throws NoParkingFound {
        LOGGER.info("Request to get a ParkingBilling by Id - {}",id);
        final Optional<ParkingBilling> parkingBilling = parkingBillingRepository.findById(id);
        return CompletableFuture.completedFuture(parkingBilling.orElseThrow(() -> new NoParkingFound("No Parking found for given vehicle number..."+id)));
    }


    public synchronized CompletableFuture<ParkingBilling> makeParkingBooking (String vehicleNumber,
                                                                              ParkingType parkingType, boolean isHourly)
            throws NoParkingSlot, BookingAlreadyExists {
        LOGGER.info("Request to book a parking slot vehicleNumber and parkingType - {}, {}",vehicleNumber,parkingType);

        validateBookingRequest(vehicleNumber);
        Optional<ParkingSlot> parkingSlot =
                parkingSlotRepository.findAllByParkingType(parkingType).stream().filter(ParkingSlot::isAvailable).filter(g -> g.getPricingPolicy().isHourly()==isHourly).findFirst();
        if (parkingSlot.isPresent()) {
            ParkingSlot slotted = parkingSlot.get();
            ParkingBilling parkingBilling = new ParkingBilling();
            parkingBilling.setParkingSlot(slotted);
            parkingBilling.setBilled(false);
            parkingBilling.setStartTime(LocalDateTime.now());
            parkingBilling.setVehicleNumber(vehicleNumber);
            slotted.setAvailable(false);
            parkingSlotRepository.save(slotted);
            parkingBillingRepository.save(parkingBilling);
            return CompletableFuture.completedFuture(parkingBilling);
        } else {
            throw new NoParkingSlot("No Parking Slot Available for Required Slot Type and Pricing..."+parkingType+",isHourly:"+isHourly);
        }
    }

    private void validateBookingRequest(String vehicleNumber) throws BookingAlreadyExists{
        LOGGER.info("Validating the booking request for vehicleNumber - {}",vehicleNumber);
        Optional<ParkingBilling> optionalParkingBilling =
                parkingBillingRepository.findByVehicleNumberAndIsBilled(vehicleNumber,false);
        if(optionalParkingBilling.isPresent()){
            throw new BookingAlreadyExists("Mentioned Vehicle Number:" + vehicleNumber +" is already Parked...");
        }
    }


    public synchronized CompletableFuture<ParkingBilling> exitParking(String vehicleNumber) throws NoParkingFound {
        LOGGER.info("Request to exit parking for vehicleNumber  - {}",vehicleNumber);
        Optional<ParkingBilling> parkingBillingOption =
                parkingBillingRepository.findAllByVehicleNumber(vehicleNumber).stream().filter(b -> !b.isBilled()).findFirst();
        if (parkingBillingOption.isPresent()){
            ParkingBilling parkingBilling = parkingBillingOption.get();
            ParkingSlot unslotted =
                    parkingSlotRepository.getOne(parkingBilling.getParkingSlot().getParkingSlotId());
            unslotted.setAvailable(true);
            parkingBilling.setEndTime(LocalDateTime.now());
            parkingBilling.setBillAmount(calculateBill(parkingBilling));
            parkingBilling.setBilled(true);
            parkingBilling.setParkingSlot(unslotted);
            parkingSlotRepository.save(unslotted);
            parkingBillingRepository.save(parkingBilling);
            return CompletableFuture.completedFuture(parkingBilling);

        }else{
            throw new NoParkingFound("No Parking found for given vehicle number..."+vehicleNumber);
        }
    }

    private Double calculateBill(ParkingBilling parkingBilling){
        Double billAmount;
        PricingPolicy policy = parkingBilling.getParkingSlot().getPricingPolicy();
        Duration duration = Duration.between(parkingBilling.getEndTime().truncatedTo(ChronoUnit.HOURS),
                parkingBilling.getStartTime().truncatedTo(ChronoUnit.HOURS));
        long hours = Math.abs(duration.toHours());

        if(hours <= 1 ){
            billAmount =policy.getHourPrice()+policy.getCleaningServiceCharge()+policy.getOtherExtraCharges();
        }else{
            billAmount =policy.getHourPrice()*hours+policy.getCleaningServiceCharge()+policy.getOtherExtraCharges();
        }
        if(!policy.isHourly()){
            billAmount = billAmount + policy.getFixedPrice();

        }
        return billAmount;
    }
}

