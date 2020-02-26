package com.tollparking.lib.app.rest;

import com.tollparking.lib.app.exception.InvalidParkingSlot;
import com.tollparking.lib.app.exception.InvalidPricingPolicy;
import com.tollparking.lib.app.model.ParkingSlot;
import com.tollparking.lib.app.model.ParkingType;
import com.tollparking.lib.app.payload.CreateParkingSlot;
import com.tollparking.lib.app.service.TollParkingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Api(
        value = "ParkingSlotResource",
        tags = {"Parking Slot Resource"})
@SwaggerDefinition(
        tags = {
                @Tag(name = "Parking Slot Resource", description = "The resource class for parking")
        })
@RestController
@RequestMapping("/parking/api/v1")
public class ParkingSlotResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingSlotResource.class);

    @Autowired
    private TollParkingService tollParkingService;

    @Async
    @RequestMapping(value ="/getAllParkingSlots" ,method = RequestMethod.GET)
    public @ResponseBody
    CompletableFuture<ResponseEntity> getAllParkingSlots() {
        LOGGER.info("Request to getAllParkingSlots...");
        return tollParkingService.getAllParkingSlots().<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    @Async
    @RequestMapping(value ="/getAllParkingSlotsByType/{parkingType}" ,method = RequestMethod.GET)
    public @ResponseBody
    CompletableFuture<ResponseEntity> getAllParkingSlotsByType(@PathVariable(name = "parkingType") ParkingType parkingType) {
        LOGGER.info("Request to  getAllParkingSlotsByType...{}:",parkingType);
        return tollParkingService.getAllParkingSlotsByType(parkingType).<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    @Async
    @RequestMapping(value ="/getAllParkingSlotsByTypeAndAvailability/{parkingType}" ,method = RequestMethod.GET)
    public @ResponseBody
    CompletableFuture<ResponseEntity> getAllParkingSlotsByTypeAndAvailability(@PathVariable(name = "parkingType", required = true) ParkingType parkingType, @RequestParam("available") boolean available) {
        LOGGER.info("Request to  getAllParkingSlotsByTypeAndAvailability...{},{}:",parkingType,available);
        return tollParkingService.getAllParkingSlotsByTypeAndAvailability(parkingType,available).<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    @Async
    @RequestMapping(value ="/getAllParkingSlotsByAvailability" ,method = RequestMethod.GET)
    public @ResponseBody
    CompletableFuture<ResponseEntity> getAllParkingSlotsByAvailability(@RequestParam("available") boolean available) {
        LOGGER.info("Request to  getAllParkingSlotsByAvailability...{}",available);
        return tollParkingService.getAllParkingSlotsByAvailability(available).<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    @Async
    @RequestMapping(value ="/getAllParkingSlotsByPricingPolicyId" ,method = RequestMethod.GET)
    public @ResponseBody
    CompletableFuture<ResponseEntity> getAllParkingSlotsByPricingPolicyId(@RequestParam("PolicyId") Long id) {
        LOGGER.info("Request to  getAllParkingSlotsByPricingPolicyId...{}:",id);
        return tollParkingService.getAllParkingSlotsByPricingPolicyId(id).<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    @Async
    @RequestMapping(value ="/updateParkingTypeByParkingSlotId/{parkingType}" ,method = RequestMethod.POST)
    public @ResponseBody
    CompletableFuture<ResponseEntity> updateParkingTypeByParkingSlotId(@RequestParam("ParkingSlotId") Long ParkingSlotId, @PathVariable(name = "parkingType", required = true) ParkingType parkingType) throws InvalidParkingSlot {
        LOGGER.info("Request to  updateParkingTypeByParkingSlotId...{},{}: ",parkingType,ParkingSlotId);
        return tollParkingService.updateParkingTypeByParkingSlotId(ParkingSlotId,parkingType).<ResponseEntity>thenApply(ResponseEntity::ok);
    }

    @Async
    @RequestMapping(value ="/updateAvailabilityByParkingSlotId" ,method = RequestMethod.POST)
    public @ResponseBody
    CompletableFuture<ResponseEntity> updateAvailabilityByParkingSlotId(@RequestParam("ParkingSlotId") Long ParkingSlotId,@RequestParam("availability") boolean availability) throws InvalidParkingSlot {
        LOGGER.info("Request to  updateAvailabilityByParkingSlotId...{},{}: ",availability,ParkingSlotId);
        return tollParkingService.updateAvailabilityByParkingSlotId(ParkingSlotId,availability).<ResponseEntity>thenApply(ResponseEntity::ok);
    }

    @Async
    @RequestMapping(value ="/updatePricingPolicyByParkingSlotId" ,method = RequestMethod.POST)
    public @ResponseBody
    CompletableFuture<ResponseEntity> updatePricingPolicyByParkingSlotId(@RequestParam("ParkingSlotId") Long ParkingSlotId,@RequestParam("pricingPolicyId") Long pricingPolicyId) throws InvalidParkingSlot {
        LOGGER.info("Request to  updatePricingPolicyByParkingSlotId...{},{}: ",pricingPolicyId,ParkingSlotId);
        return tollParkingService.updatePricingPolicyByParkingSlotId(ParkingSlotId,pricingPolicyId).<ResponseEntity>thenApply(ResponseEntity::ok);
    }

    private static Function<Throwable, ResponseEntity<? extends List<ParkingSlot>>> handleGetCarFailure = throwable -> {
        LOGGER.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

    @Async
    @RequestMapping(value ="/createParkingSlot" ,method = RequestMethod.POST)
    public @ResponseBody
    CompletableFuture<ResponseEntity> createParkingSlot(@ApiParam(name="ParkingType", value="Provide Parking Type like STANDARD, CAR_20KW_ELECTRIC, CAR_50KW_ELECTRIC") @RequestBody CreateParkingSlot newParkingSlot) throws InvalidPricingPolicy {
        LOGGER.info("Request to  createParkingSlot...{}: ",newParkingSlot);
        return tollParkingService.createNewParkingSlot(newParkingSlot).<ResponseEntity>thenApply(ResponseEntity::ok);
    }

    @Async
    @RequestMapping(value ="/deleteParkingSlotById" ,method = RequestMethod.DELETE)
    public @ResponseBody
    CompletableFuture<ResponseEntity> deleteParkingSlotById(@RequestParam Long id)  {
        LOGGER.info("Request to  deleteParkingSlotById...{}: ",id);
        return tollParkingService.deleteParkingSlot(id).<ResponseEntity>thenApply(ResponseEntity::ok);
    }


    @Async
    @RequestMapping(value ="/bulkCreateParkingSlot" ,method = RequestMethod.POST, consumes={MediaType.MULTIPART_FORM_DATA_VALUE},
            produces={MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity bulkCreateParkingSlot(@ApiParam(name="ParkingType", value="Provide Parking Type like STANDARD, CAR_20KW_ELECTRIC, CAR_50KW_ELECTRIC") @RequestParam (value = "file") MultipartFile file)  {
        LOGGER.info("Request to  bulkCreateParkingSlot...{}: ",file);
        try {
            tollParkingService.bulkCreateParkingSlot(file);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch(final Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
