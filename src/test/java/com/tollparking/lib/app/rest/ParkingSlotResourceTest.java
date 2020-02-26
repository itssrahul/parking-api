package com.tollparking.lib.app.rest;

import com.tollparking.lib.app.Application;
import com.tollparking.lib.app.exception.InvalidParkingSlot;
import com.tollparking.lib.app.exception.InvalidPricingPolicy;
import com.tollparking.lib.app.model.ParkingType;
import com.tollparking.lib.app.service.TollParkingService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static com.tollparking.lib.app.mock.TestHelper.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ParkingSlotResourceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected WebApplicationContext wac;

    @MockBean
    private TollParkingService mockTollParkingService;

    @Test
    public void testGetAllParkingSlots() throws Exception {
        when(mockTollParkingService.getAllParkingSlots()).thenReturn(CompletableFuture.completedFuture(dummyParkingSlotList()));

        final MvcResult mvcResult = mvc
                .perform(get("/parking/api/v1/getAllParkingSlots")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetAllParkingSlotsByType() throws Exception {
        when(mockTollParkingService.getAllParkingSlotsByType(any())).thenReturn(CompletableFuture.completedFuture(dummyParkingSlotList()));

        final MvcResult mvcResult = mvc
                .perform(get("/parking/api/v1/getAllParkingSlotsByType/{parkingType}", ParkingType.STANDARD)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetAllParkingSlotsByTypeAndAvailability() throws Exception {
        when(mockTollParkingService.getAllParkingSlotsByTypeAndAvailability(any(),anyBoolean())).thenReturn(CompletableFuture.completedFuture(dummyParkingSlotList()));

        final MvcResult mvcResult = mvc
                .perform(get("/parking/api/v1/getAllParkingSlotsByTypeAndAvailability/{parkingType}", ParkingType.STANDARD)
                        .accept(MediaType.APPLICATION_JSON)
                .param("available","true"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetAllParkingSlotsByAvailability() throws Exception {
        when(mockTollParkingService.getAllParkingSlotsByAvailability(anyBoolean())).thenReturn(CompletableFuture.completedFuture(dummyParkingSlotList()));

        final MvcResult mvcResult = mvc
                .perform(get("/parking/api/v1/getAllParkingSlotsByAvailability")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("available","true"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllParkingSlotsByPricingPolicyId() throws Exception {
        when(mockTollParkingService.getAllParkingSlotsByPricingPolicyId(anyLong())).thenReturn(CompletableFuture.completedFuture(dummyParkingSlotList()));

        final MvcResult mvcResult = mvc
                .perform(get("/parking/api/v1/getAllParkingSlotsByPricingPolicyId")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("PolicyId","1"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateParkingTypeByParkingSlotId() throws Exception {
        when(mockTollParkingService.updateParkingTypeByParkingSlotId(anyLong(),any())).thenReturn(CompletableFuture.completedFuture(dummyParkingSlot(1L,"some")));

        final MvcResult mvcResult = mvc
                .perform(post("/parking/api/v1/updateParkingTypeByParkingSlotId/{parkingType}",ParkingType.CAR_20KW_ELECTRIC)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("ParkingSlotId","1"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateAvailabilityByParkingSlotId() throws Exception {
        when(mockTollParkingService.updateAvailabilityByParkingSlotId(anyLong(),anyBoolean())).thenReturn(CompletableFuture.completedFuture(dummyParkingSlot(1L,"some")));

        final MvcResult mvcResult = mvc
                .perform(post("/parking/api/v1/updateAvailabilityByParkingSlotId")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("ParkingSlotId","1")
                        .param("availability","true"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePricingPolicyByParkingSlotId() throws Exception {
        when(mockTollParkingService.updatePricingPolicyByParkingSlotId(anyLong(),anyLong())).thenReturn(CompletableFuture.completedFuture(dummyParkingSlot(1L,"some")));

        final MvcResult mvcResult = mvc
                .perform(post("/parking/api/v1/updatePricingPolicyByParkingSlotId")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("ParkingSlotId","1")
                        .param("pricingPolicyId","2"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateParkingSlot() throws Exception {
        when(mockTollParkingService.createNewParkingSlot(any())).thenReturn(CompletableFuture.completedFuture(dummyParkingSlot(1L,"some")));

        final MvcResult mvcResult = mvc
                .perform(post("/parking/api/v1/createParkingSlot")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dummyCreateParkingSlot())))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteParkingSlotById() throws Exception {
        when(mockTollParkingService.deleteParkingSlot(anyLong())).thenReturn(CompletableFuture.completedFuture(null));
        final MvcResult mvcResult = mvc
                .perform(delete("/parking/api/v1/deleteParkingSlotById")
                        .param("id","1"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }

    @Ignore // todo : Need to fix this one
    public void testBulkCreateParkingSlot() throws Exception {
        when(mockTollParkingService.bulkCreateParkingSlot(any())).thenReturn(CompletableFuture.completedFuture(dummyParkingSlotList()));

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "testDataParkingSlots.csv",
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        "<<csv data>>".getBytes(StandardCharsets.UTF_8));

        final MvcResult mvcResult = mvc
                .perform(multipart("/parking/api/v1/bulkCreateParkingSlot")
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated());

    }

    @Test
    public void testCreateParkingSlot_resultsInInvalidPricingPolicyException() throws Exception {
        when(mockTollParkingService.createNewParkingSlot(any())).thenReturn(CompletableFuture.failedFuture(new InvalidPricingPolicy("")));

        final MvcResult mvcResult = mvc
                .perform(post("/parking/api/v1/createParkingSlot")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dummyCreateParkingSlot())))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testUpdateAvailabilityByParkingSlotId_resultsInInvalidParkingSlotException() throws Exception {
        when(mockTollParkingService.updateAvailabilityByParkingSlotId(anyLong(),anyBoolean())).thenReturn(CompletableFuture.failedFuture(new InvalidParkingSlot("")));

        final MvcResult mvcResult = mvc
                .perform(post("/parking/api/v1/updateAvailabilityByParkingSlotId")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("ParkingSlotId","1")
                        .param("availability","true"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is5xxServerError());
    }

}
