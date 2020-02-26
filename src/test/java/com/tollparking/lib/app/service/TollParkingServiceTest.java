package com.tollparking.lib.app.service;

import com.tollparking.lib.app.exception.*;
import com.tollparking.lib.app.model.ParkingBilling;
import com.tollparking.lib.app.model.ParkingSlot;
import com.tollparking.lib.app.model.ParkingType;
import com.tollparking.lib.app.model.PricingPolicy;
import com.tollparking.lib.app.repo.ParkingBillingRepository;
import com.tollparking.lib.app.repo.ParkingSlotRepository;
import com.tollparking.lib.app.repo.PricingRepository;
import com.tollparking.lib.app.utils.ModelMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static com.tollparking.lib.app.mock.TestHelper.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Matchers.anyLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class TollParkingServiceTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public TollParkingService tollParkingService() {
            return new TollParkingService();
        }
    }

    @Autowired
    private TollParkingService tollParkingService;

    @MockBean
    private PricingRepository mockPricingRepository;
    @MockBean
    private ModelMapper mockModelMapper;
    @MockBean
    private ParkingSlotRepository mockParkingSlotRepository;
    @MockBean
    private ParkingBillingRepository mockParkingBillingRepository;

    @Test
    public void whenGetAllPricingPolicy_thenAllPricingPolicy() throws ExecutionException, InterruptedException {
        Mockito.when(mockPricingRepository.findAll())
                .thenReturn(dummyPricingPolicyList());
        CompletableFuture<List<PricingPolicy>> future =  tollParkingService.getAllPricingPolicy();
        verify(mockPricingRepository).findAll();
        assertThat(future.get().size()).isNotEqualTo(0);
    }

    @Test
    public void whenGetPricingPolicyByPolicyId_thenPricingPolicy() throws ExecutionException, InterruptedException, InvalidPricingPolicy {
        Mockito.when(mockPricingRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(dummyPricingPolicy("anyString1",101L)));
        CompletableFuture<PricingPolicy> future =  tollParkingService.getPricingPolicyByPolicyId(anyLong());
        verify(mockPricingRepository).findById(anyLong());
        assertThat(future.get()).isNotNull();
    }

    @Test
    public void whenUpdatePricingPolicyByPolicyId_thenUpdatedPricingPolicy() throws ExecutionException, InterruptedException, InvalidPricingPolicy, InvalidInput {
        Mockito.when(mockPricingRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(dummyPricingPolicy("anyString1",101L)));
        Mockito.when(mockModelMapper.mapToPricingPolicyUpdateModel(dummyUpdatePricing(),dummyPricingPolicy("anyString1",101L)))
                .thenReturn(dummyPricingPolicy("anyString1",101L));
        Mockito.when(mockPricingRepository.save(dummyPricingPolicy("anyString1",101L)))
                .thenReturn(dummyPricingPolicy("anyString1",101L));
        CompletableFuture<PricingPolicy> future =  tollParkingService.updatePricingPolicyByPolicyId(dummyUpdatePricing());
        verify(mockPricingRepository).findById(anyLong());
        verify(mockModelMapper).mapToPricingPolicyUpdateModel(any(),any());
        verify(mockPricingRepository).save(any());
        assertThat(future.get()).isNotNull();
    }

    @Test
    public void whenCreatePricingPolicy_thenNewPricingPolicy() throws InvalidInput ,ExecutionException, InterruptedException {
        Mockito.when(mockModelMapper.mapToPricingPolicyCreateModel(dummyCreatePricing()))
                .thenReturn(dummyPricingPolicy("anyString1",101L));
        Mockito.when(mockPricingRepository.save(dummyPricingPolicy("anyString1",101L)))
                .thenReturn(dummyPricingPolicy("anyString1",101L));

        CompletableFuture<PricingPolicy> future =  tollParkingService.createPricingPolicy(dummyCreatePricing());
        verify(mockModelMapper).mapToPricingPolicyCreateModel(any());
        verify(mockPricingRepository).save(any());
        assertThat(future.get()).isNotNull();
    }

    @Test
    public void whenDeletePricingPolicyByPolicyId_thenDeletePricingPolicy() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future =  tollParkingService.deletePricingPolicyByPolicyId(anyLong());
        verify(mockPricingRepository).deleteById(any());
        assertThat(future.get()).isNull();
    }

    @Test
    public void whenGetAllParkingSlots_thenAllParkingSlots() throws ExecutionException, InterruptedException {
        Mockito.when(mockParkingSlotRepository.findAll())
                .thenReturn(dummyParkingSlotList());
        CompletableFuture<List<ParkingSlot>> future =  tollParkingService.getAllParkingSlots();
        verify(mockParkingSlotRepository).findAll();
        assertThat(future.get().size()).isNotEqualTo(0);
    }

    @Test
    public void whenGetAllParkingSlotsByType_thenAllParkingSlots() throws ExecutionException, InterruptedException {
        Mockito.when(mockParkingSlotRepository.findAllByParkingType(any()))
                .thenReturn(dummyParkingSlotList());
        CompletableFuture<List<ParkingSlot>> future =  tollParkingService.getAllParkingSlotsByType(ParkingType.STANDARD);
        verify(mockParkingSlotRepository).findAllByParkingType(any());
        assertThat(future.get().size()).isNotEqualTo(0);
    }

    @Test
    public void whenGetAllParkingSlotsByTypeAndAvailability_thenAllParkingSlots() throws ExecutionException, InterruptedException {
        Mockito.when(mockParkingSlotRepository.findAllByParkingTypeAndIsAvailable(any(),anyBoolean()))
                .thenReturn(dummyParkingSlotList());
        CompletableFuture<List<ParkingSlot>> future =  tollParkingService.getAllParkingSlotsByTypeAndAvailability(ParkingType.STANDARD,true);
        verify(mockParkingSlotRepository).findAllByParkingTypeAndIsAvailable(any(), anyBoolean());
        assertThat(future.get().size()).isNotEqualTo(0);
    }

    @Test
    public void whenGetAllParkingSlotsByAvailability_thenAllParkingSlots() throws ExecutionException, InterruptedException {
        Mockito.when(mockParkingSlotRepository.findAllByIsAvailable(anyBoolean()))
                .thenReturn(dummyParkingSlotList());
        CompletableFuture<List<ParkingSlot>> future =  tollParkingService.getAllParkingSlotsByAvailability(true);
        verify(mockParkingSlotRepository).findAllByIsAvailable(anyBoolean());
        assertThat(future.get().size()).isNotEqualTo(0);
    }

    @Test
    public void whenGetAllParkingSlotsByPricingPolicyId_thenAllParkingSlots() throws ExecutionException, InterruptedException {
        Mockito.when(mockParkingSlotRepository.findAllByPricingPolicy_pricingPolicyId(anyLong()))
                .thenReturn(dummyParkingSlotList());
        CompletableFuture<List<ParkingSlot>> future =  tollParkingService.getAllParkingSlotsByPricingPolicyId(1L);
        verify(mockParkingSlotRepository).findAllByPricingPolicy_pricingPolicyId(anyLong());
        assertThat(future.get().size()).isNotEqualTo(0);
    }

    @Test
    public void whenUpdateParkingTypeByParkingSlotId_thenParkingSlot() throws ExecutionException, InterruptedException, InvalidParkingSlot {
        Mockito.when(mockParkingSlotRepository.save(any()))
                .thenReturn(dummyParkingSlot(2L, "somename"));
        Mockito.when(mockParkingSlotRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(dummyParkingSlot(2L, "somename")));
        CompletableFuture<ParkingSlot> future =  tollParkingService.updateParkingTypeByParkingSlotId(1L,ParkingType.CAR_20KW_ELECTRIC);
        verify(mockParkingSlotRepository).save(any());
        verify(mockParkingSlotRepository).findById(anyLong());
        assertThat(future.get()).isNotNull();
    }

    @Test
    public void whenUpdateAvailabilityByParkingSlotId_thenParkingSlot() throws ExecutionException, InterruptedException, InvalidParkingSlot {
        Mockito.when(mockParkingSlotRepository.save(any()))
                .thenReturn(dummyParkingSlot(2L, "somename"));
        Mockito.when(mockParkingSlotRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(dummyParkingSlot(2L, "somename")));
        CompletableFuture<ParkingSlot> future =  tollParkingService.updateAvailabilityByParkingSlotId(1L,true);
        verify(mockParkingSlotRepository).save(any());
        verify(mockParkingSlotRepository).findById(anyLong());
        assertThat(future.get()).isNotNull();
    }

    @Test
    public void whenUpdatePricingPolicyByParkingSlotId_thenParkingSlot() throws ExecutionException, InterruptedException, InvalidParkingSlot {
        Mockito.when(mockParkingSlotRepository.save(any()))
                .thenReturn(dummyParkingSlot(2L, "somename"));
        Mockito.when(mockParkingSlotRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(dummyParkingSlot(2L, "somename")));
        CompletableFuture<ParkingSlot> future =  tollParkingService.updatePricingPolicyByParkingSlotId(1L,1L);
        verify(mockParkingSlotRepository).save(any());
        verify(mockParkingSlotRepository).findById(anyLong());
        assertThat(future.get()).isNotNull();
    }

    @Test
    public void whenCreateNewParkingSlot_thenParkingSlot() throws ExecutionException, InterruptedException, InvalidPricingPolicy {
        Mockito.when(mockParkingSlotRepository.save(any()))
                .thenReturn(dummyParkingSlot(2L, "somename"));
        Mockito.when(mockPricingRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(dummyPricingPolicy( "somename",2L)));
        Mockito.when(mockModelMapper.mapToParkingSlotCreateModel(any(),any()))
                .thenReturn(dummyParkingSlot(2L, "somename"));
        CompletableFuture<ParkingSlot> future =  tollParkingService.createNewParkingSlot(dummyCreateParkingSlot());
        verify(mockParkingSlotRepository).save(any());
        verify(mockPricingRepository).findById(anyLong());
        verify(mockModelMapper).mapToParkingSlotCreateModel(any(),any());
        assertThat(future.get()).isNotNull();
    }

    @Test
    public void whenDeleteParkingSlot_thenDeleteParkingSlot() throws ExecutionException, InterruptedException {
        CompletableFuture<ParkingSlot> future =  tollParkingService.deleteParkingSlot(anyLong());
        verify(mockParkingSlotRepository).deleteById(any());
        assertThat(future.get()).isNull();
    }

    @Test
    public void whenBulkCreateParkingSlot_thenBulkParkingSlotsCreated() throws Exception {
        Mockito.when(mockParkingSlotRepository.save(any()))
                .thenReturn(dummyParkingSlot(2L, "somename"));
        Mockito.when(mockPricingRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(dummyPricingPolicy( "somename",1L)));
        TollParkingService spyTollParkingService = Mockito.spy(tollParkingService);
        Mockito.doReturn(dummyParkingSlotList()).when(spyTollParkingService).parseCSVFile(any());

        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("testDataParkingSlots.csv");
        CompletableFuture<List<ParkingSlot>> future =  tollParkingService.bulkCreateParkingSlot(new MockMultipartFile("file", "test.png", "image/png", inputStream));
        verify(mockParkingSlotRepository).saveAll(any());
        assertThat(future.get()).isNotNull();
    }

    @Test
    public void whenGetAllParkedVehicles_thenAllPricingPolicy() throws ExecutionException, InterruptedException {
        Mockito.when(mockParkingBillingRepository.findAll())
                .thenReturn(dummyParkingBillingList());
        CompletableFuture<List<ParkingBilling>> future =  tollParkingService.getAllParkedVehicles();
        verify(mockParkingBillingRepository).findAll();
        assertThat(future.get().size()).isNotEqualTo(0);
    }

    @Test
    public void whenGetParkedVehicleById_thenPricingPolicy() throws ExecutionException, InterruptedException, NoParkingFound {
        Mockito.when(mockParkingBillingRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(dummyParkingBilling("uyt",3L)));
        CompletableFuture<ParkingBilling> future =  tollParkingService.getParkedVehicleById(anyLong());
        verify(mockParkingBillingRepository).findById(anyLong());
        assertThat(future.get()).isNotNull();
    }

    @Test
    public void whenMakeParkingBooking_thenParkedShouldBeDone() throws NoParkingSlot, BookingAlreadyExists, ExecutionException, InterruptedException {
        Mockito.when(mockParkingSlotRepository.findAllByParkingType(any()))
                .thenReturn(dummyParkingSlotList());
        Mockito.when(mockParkingSlotRepository.save(any()))
        .thenReturn(dummyParkingSlot(1L,"some"));
        Mockito.when(mockParkingBillingRepository.save(any()))
        .thenReturn(dummyParkingBilling("some",1L));
        Mockito.when(mockParkingBillingRepository.findByVehicleNumberAndIsBilled(anyString(),anyBoolean()))
                .thenReturn(java.util.Optional.empty());
        CompletableFuture<ParkingBilling> future =  tollParkingService.makeParkingBooking("some",ParkingType.CAR_50KW_ELECTRIC,true);
        verify(mockParkingSlotRepository).findAllByParkingType(any());
        verify(mockParkingSlotRepository).save(any());
        verify(mockParkingBillingRepository).save(any());
        verify(mockParkingBillingRepository).findByVehicleNumberAndIsBilled(anyString(),anyBoolean());
        assertThat(future.get()).isNotNull();

    }

    @Test
    public void whenExitParking_thenBillMustBePresented() throws NoParkingFound, ExecutionException, InterruptedException {
        Mockito.when(mockParkingBillingRepository.findAllByVehicleNumber(anyString()))
                .thenReturn(dummyParkingBillingList());
        Mockito.when(mockParkingSlotRepository.getOne(anyLong()))
                .thenReturn(dummyParkingSlot(93L,"some"));
        Mockito.when(mockParkingSlotRepository.save(any()))
                .thenReturn(dummyParkingSlot(93L,"some"));
        Mockito.when(mockParkingBillingRepository.save(any()))
                .thenReturn(dummyParkingBilling("some",2L));
        CompletableFuture<ParkingBilling> future =  tollParkingService.exitParking("some");
        verify(mockParkingBillingRepository).findAllByVehicleNumber(anyString());
        verify(mockParkingSlotRepository).getOne(anyLong());
        verify(mockParkingSlotRepository).save(any());
        verify(mockParkingBillingRepository).save(any());
        assertThat(future.get()).isNotNull();
        assertThat(future.get().getBillAmount()).isEqualTo(6.4);
    }


}
