package com.tollparking.lib.app.rest;

import com.tollparking.lib.app.exception.InvalidInput;
import com.tollparking.lib.app.exception.InvalidPricingPolicy;
import com.tollparking.lib.app.model.PricingPolicy;
import com.tollparking.lib.app.payload.CreatePricing;
import com.tollparking.lib.app.payload.UpdatePricing;
import com.tollparking.lib.app.service.TollParkingService;
import io.swagger.annotations.*;
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
        value = "PricingResource",
        tags = {"Pricing Resource"})
@SwaggerDefinition(
        tags = {
                @Tag(name = "Pricing Resource", description = "The resource class for pricing")
        })
@RestController
@RequestMapping("/pricing/api/v1")
public class PricingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingResource.class);

    @Autowired
    private TollParkingService tollParkingService;


    @Async
    @RequestMapping (value ="/getAllPricingPolicy" ,method = RequestMethod.GET)
    public @ResponseBody CompletableFuture<ResponseEntity> getAllPricingPolicy() {
        LOGGER.info("Request to  getAllPricingPolicy...: ");
        return tollParkingService.getAllPricingPolicy().<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    @Async
    @RequestMapping (value ="/getPricingPolicyByPolicyId" ,method = RequestMethod.GET)
    public @ResponseBody CompletableFuture<ResponseEntity> getPricingPolicyByPolicyId(@RequestParam("PolicyId") Long id) throws InvalidPricingPolicy {
        LOGGER.info("Request to  getPricingPolicyByPolicyId...{}: ",id);
        return tollParkingService.getPricingPolicyByPolicyId(id).<ResponseEntity>thenApply(ResponseEntity::ok);
    }

    @Async
    @RequestMapping (value ="/updatePricingPolicyByPolicyId" ,method = RequestMethod.POST)
    public @ResponseBody CompletableFuture<ResponseEntity> updatePricingPolicyByPolicyId(@RequestBody UpdatePricing pricingPolicy) throws InvalidPricingPolicy, InvalidInput {
        LOGGER.info("Request to  updatePricingPolicyByPolicyId...{}: ",pricingPolicy);
        return tollParkingService.updatePricingPolicyByPolicyId(pricingPolicy).thenApply(ResponseEntity::ok);

    }

    @Async
    @RequestMapping (value ="/createPricingPolicy" ,method = RequestMethod.POST)
    public @ResponseBody CompletableFuture<ResponseEntity> createPricingPolicy(@RequestBody CreatePricing pricingPolicy) throws InvalidInput {
        LOGGER.info("Request to  createPricingPolicy...{}: ",pricingPolicy);
        return tollParkingService.createPricingPolicy(pricingPolicy).thenApply(ResponseEntity::ok);
    }

    @Async
    @RequestMapping (value ="/deletePricingPolicyByPolicyId" ,method = RequestMethod.DELETE)
    public CompletableFuture<ResponseEntity<Void>> deletePricingPolicyByPolicyId(@RequestParam("PolicyId") Long id) {
        LOGGER.info("Request to  deletePricingPolicyByPolicyId...{}: ",id);
        return tollParkingService.deletePricingPolicyByPolicyId(id).thenApply(ResponseEntity::ok);
    }


    private static Function<Throwable, ResponseEntity<? extends List<PricingPolicy>>> handleGetCarFailure = throwable -> {
        LOGGER.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

}
