package com.tollparking.lib.app.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CreatePricing {

    @ApiModelProperty(
            required = true,
            value = "50",
            notes = "Pricing PolicyName")
    private String pricingPolicyName;

    @ApiModelProperty(
            required = true,
            value = "1",
            notes = "Flag for indicating fixed or non-fixed pricing")
    private boolean isHourly;

    @ApiModelProperty(
            required = true,
            value = "50",
            notes = "Fixed Price Placeholder")
    private Double fixedPrice;

    @ApiModelProperty(
            required = true,
            value = "5",
            notes = "Hour Price Placeholder")
    private Double hourPrice;

    @ApiModelProperty(
            required = true,
            value = "5",
            notes = "Cleaning Service Charge Placeholder")
    private Double cleaningServiceCharge;

    @ApiModelProperty(
            required = true,
            value = "5",
            notes = "other Extra Charges Placeholder")
    private Double otherExtraCharges;

}
