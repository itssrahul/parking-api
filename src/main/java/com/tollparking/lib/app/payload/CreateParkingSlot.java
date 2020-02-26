package com.tollparking.lib.app.payload;

import com.tollparking.lib.app.model.ParkingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CreateParkingSlot {

    @ApiModelProperty(
            required = true,
            value = "50",
            notes = "Parking Type")
    private ParkingType parkingType;

    @ApiModelProperty(
            required = true,
            value = "1",
            notes = "Parking Availability")
    private boolean isAvailable;

    @ApiModelProperty(
            required = true,
            value = "",
            notes = "Pricing Policy Id")
    private Long pricingPolicyId;


}
