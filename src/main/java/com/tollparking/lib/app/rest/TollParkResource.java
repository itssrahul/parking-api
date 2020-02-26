package com.tollparking.lib.app.rest;

import com.tollparking.lib.app.exception.BookingAlreadyExists;
import com.tollparking.lib.app.exception.NoParkingFound;
import com.tollparking.lib.app.exception.NoParkingSlot;
import com.tollparking.lib.app.model.ParkingBilling;
import com.tollparking.lib.app.model.ParkingType;
import com.tollparking.lib.app.service.TollParkingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Api(
        value = "TollParkResource",
        tags = {"TollPark Resource"})
@SwaggerDefinition(
        tags = {
                @Tag(name = "TollPark Resource", description = "The resource class for Toll Parking")
        })
@RestController
@RequestMapping("/toll/api/v1")
public class TollParkResource {

        private static final Logger LOGGER = LoggerFactory.getLogger(TollParkResource.class);

        @Autowired
        private TollParkingService tollParkingService;

        @Async
        @RequestMapping(value ="/getAllParkedVehicles" ,method = RequestMethod.GET)
        public @ResponseBody
        CompletableFuture<ResponseEntity> getAllParkedVehicles() {
                LOGGER.info("Request to  getAllParkedVehicles... ");
                return tollParkingService.getAllParkedVehicles().<ResponseEntity>thenApply(ResponseEntity::ok)
                        .exceptionally(handleGetCarFailure);
        }

        @Async
        @RequestMapping(value ="/getParkedVehicleById" ,method = RequestMethod.GET)
        public @ResponseBody
        CompletableFuture<ResponseEntity> getParkedVehicleById(@RequestParam(name = "ParkedVehicleId", required = true) Long ParkedVehicleId) throws NoParkingFound {
                LOGGER.info("Request to  getParkedVehicleById...{}: ",ParkedVehicleId);
                return tollParkingService.getParkedVehicleById(ParkedVehicleId).<ResponseEntity>thenApply(ResponseEntity::ok)
                        .exceptionally(handleGetCarFailure);
        }

        private static Function<Throwable, ResponseEntity<? extends List<ParkingBilling>>> handleGetCarFailure = throwable -> {
                LOGGER.error("Failed to read records: {}", throwable);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };

        @Async
        @RequestMapping(value ="/makeParkingBooking/{parkingType}" ,method = RequestMethod.POST)
        public @ResponseBody
        CompletableFuture<ParkingBilling> makeParkingBooking(@PathVariable(name = "parkingType", required = true) ParkingType parkingType ,
                                                             @RequestParam(name = "vehicleNumber", required = true) String vehicleNumber ,
                                                             @RequestParam(name = "isHourly", required = true)boolean isHourly) throws NoParkingSlot, BookingAlreadyExists {
                LOGGER.info("Request to  makeParkingBooking...vehicleNumber:{},parkingType:{},isHourly:{}: ",vehicleNumber,parkingType,isHourly);
                return tollParkingService.makeParkingBooking(vehicleNumber, parkingType,isHourly);
        }

        @Async
        @RequestMapping(value ="/exitParking/{vehicleNumber}" ,method = RequestMethod.POST)
        public @ResponseBody
        CompletableFuture<ParkingBilling> exitParking(@PathVariable(name = "vehicleNumber", required = true) String vehicleNumber ) throws NoParkingFound {
                LOGGER.info("Request to  exitParking...vehicleNumber:{}: ",vehicleNumber);
                return tollParkingService.exitParking(vehicleNumber);
        }



}
