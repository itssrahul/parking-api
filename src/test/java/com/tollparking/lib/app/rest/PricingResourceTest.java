package com.tollparking.lib.app.rest;

import com.tollparking.lib.app.Application;
import com.tollparking.lib.app.exception.InvalidInput;
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

import java.util.concurrent.CompletableFuture;

import static com.tollparking.lib.app.mock.TestHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingResourceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected WebApplicationContext wac;

    @MockBean
    private TollParkingService mockTollParkingService;

    @Test
    public void testGetAllPricingPolicy() throws Exception {
        when(mockTollParkingService.getAllPricingPolicy()).thenReturn(CompletableFuture.completedFuture(dummyPricingPolicyList()));
        final MvcResult mvcResult = mvc
                .perform(get("/pricing/api/v1/getAllPricingPolicy")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetPricingPolicyByPolicyId() throws Exception {
        when(mockTollParkingService.getPricingPolicyByPolicyId(anyLong())).thenReturn(CompletableFuture.completedFuture(dummyPricingPolicy("spme",4L)));
        final MvcResult mvcResult = mvc
                .perform(get("/pricing/api/v1/getPricingPolicyByPolicyId")
                        .accept(MediaType.APPLICATION_JSON)
                .param("PolicyId", "1"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdatePricingPolicyByPolicyId() throws Exception {
        when(mockTollParkingService.updatePricingPolicyByPolicyId(any())).thenReturn(CompletableFuture.completedFuture(dummyPricingPolicy("spme",4L)));
        final MvcResult mvcResult = mvc
                .perform(post("/pricing/api/v1/updatePricingPolicyByPolicyId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dummyUpdatePricing())))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }

    @Test
    public void testCreatePricingPolicy() throws Exception {
        when(mockTollParkingService.createPricingPolicy(any())).thenReturn(CompletableFuture.completedFuture(dummyPricingPolicy("spme",4L)));
        final MvcResult mvcResult = mvc
                .perform(post("/pricing/api/v1/createPricingPolicy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dummyUpdatePricing())))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }

    @Test
    public void testDeletePricingPolicyByPolicyId() throws Exception {
        when(mockTollParkingService.deletePricingPolicyByPolicyId(anyLong())).thenReturn(CompletableFuture.completedFuture(null));
        final MvcResult mvcResult = mvc
                .perform(delete("/pricing/api/v1/deletePricingPolicyByPolicyId")
                        .param("PolicyId","1"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdatePricingPolicyByPolicyId_resultsInInvalidInputException() throws Exception {
        when(mockTollParkingService.updatePricingPolicyByPolicyId(any())).thenReturn(CompletableFuture.failedFuture(new InvalidInput("")));
        final MvcResult mvcResult = mvc
                .perform(post("/pricing/api/v1/updatePricingPolicyByPolicyId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dummyUpdatePricing())))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is5xxServerError());

    }

}
