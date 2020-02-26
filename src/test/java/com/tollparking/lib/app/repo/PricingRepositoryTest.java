package com.tollparking.lib.app.repo;

import com.tollparking.lib.app.model.PricingPolicy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PricingRepositoryTest {

    @Autowired
    private PricingRepository pricingRepository;


    @Test
    public void whenFindAll_thenPricingPolicy() {
        List<PricingPolicy> listFound = pricingRepository.findAll();
        assertThat(listFound.size()).isEqualTo(6);
    }

    @Test
    public void whenFindById_thenPricingPolicy() {
        Optional<PricingPolicy> pricingPolicy = pricingRepository.findById(1L);
        assertThat(pricingPolicy.isPresent()).isEqualTo(true);
    }

    @Test
    public void whenSave_thenPricingPolicy() {
        PricingPolicy policy = new PricingPolicy("dummy",true,0.0,0.0,0.0,0.0);
        PricingPolicy savedPolicy = pricingRepository.save(policy);
        assertThat(savedPolicy.getPricingPolicyName()).isEqualTo(policy.getPricingPolicyName());
    }

    @Test
    public void whenDeleteById_thenPricingPolicy() {
        pricingRepository.deleteById((long) 2);
        Optional<PricingPolicy> pricingPolicy = pricingRepository.findById(2L);
        assertThat(pricingPolicy.isPresent()).isEqualTo(false);
    }
}
