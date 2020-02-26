package com.tollparking.lib.app.rest;

import com.tollparking.lib.app.Application;
import com.tollparking.lib.app.exception.BookingAlreadyExists;
import com.tollparking.lib.app.exception.NoParkingFound;
import com.tollparking.lib.app.exception.NoParkingSlot;
import com.tollparking.lib.app.model.ParkingType;
import com.tollparking.lib.app.service.TollParkingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.concurrent.CompletableFuture;

import static com.tollparking.lib.app.mock.TestHelper.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TollParkResourceTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    protected WebApplicationContext wac;

    @MockBean
    private TollParkingService mockTollParkingService;

    @Test
    public void testGetAllParkedVehicles() throws Exception {
        when(mockTollParkingService.getAllParkedVehicles()).thenReturn(CompletableFuture.completedFuture(dummyParkingBillingList()));
        final MvcResult mvcResult = mvc
                .perform(get("/toll/api/v1/getAllParkedVehicles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetParkedVehicleById() throws Exception {
        when(mockTollParkingService.getParkedVehicleById(anyLong())).thenReturn(CompletableFuture.completedFuture(dummyParkingBilling("abc 12 vb",4L)));
        final MvcResult mvcResult = mvc
                .perform(get("/toll/api/v1/getParkedVehicleById")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("ParkedVehicleId","4"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testMakeParkingBooking() throws Exception {
        when(mockTollParkingService.makeParkingBooking(anyString(),any(),anyBoolean())).thenReturn(CompletableFuture.completedFuture(dummyParkingBilling("abc 12 bn",4L)));
        final MvcResult mvcResult = mvc
                .perform(post("/toll/api/v1/makeParkingBooking/{parkingType}", ParkingType.CAR_50KW_ELECTRIC)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("vehicleNumber","abc 12 bn")
                        .param("isHourly","true"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testExitParking() throws Exception {
        when(mockTollParkingService.exitParking(anyString())).thenReturn(CompletableFuture.completedFuture(dummyParkingBilling("abc 12 bn",4L)));
        final MvcResult mvcResult = mvc
                .perform(post("/toll/api/v1/exitParking/{vehicleNumber}", "abc 12 bn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testMakeParkingBooking_resultsInBookingAlreadyExistsException() throws Exception {
        when(mockTollParkingService.makeParkingBooking(anyString(),any(),anyBoolean())).thenReturn(CompletableFuture.failedFuture(new BookingAlreadyExists("")));
        final MvcResult mvcResult = mvc
                .perform(post("/toll/api/v1/makeParkingBooking/{parkingType}", ParkingType.CAR_50KW_ELECTRIC)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("vehicleNumber","abc 12 bn")
                        .param("isHourly","true"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testMakeParkingBooking_resultsInNoParkingSlotException() throws Exception {
        when(mockTollParkingService.makeParkingBooking(anyString(),any(),anyBoolean())).thenReturn(CompletableFuture.failedFuture(new NoParkingSlot("")));
        final MvcResult mvcResult = mvc
                .perform(post("/toll/api/v1/makeParkingBooking/{parkingType}", ParkingType.CAR_50KW_ELECTRIC)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("vehicleNumber","abc 12 bn")
                        .param("isHourly","true"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testExitParking_resultsInNoParkingSlotException() throws Exception {
        when(mockTollParkingService.exitParking(anyString())).thenReturn(CompletableFuture.failedFuture(new NoParkingSlot("")));
        final MvcResult mvcResult = mvc
                .perform(post("/toll/api/v1/exitParking/{vehicleNumber}", "abc 12 bn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testExitParking_resultsInNoParkingFoundException() throws Exception {
        when(mockTollParkingService.exitParking(anyString())).thenReturn(CompletableFuture.failedFuture(new NoParkingFound("")));
        final MvcResult mvcResult = mvc
                .perform(post("/toll/api/v1/exitParking/{vehicleNumber}", "abc 12 bn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is5xxServerError());
    }

}
