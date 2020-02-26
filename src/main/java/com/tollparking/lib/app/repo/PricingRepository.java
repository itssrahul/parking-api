package com.tollparking.lib.app.repo;

import com.tollparking.lib.app.model.PricingPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PricingRepository extends JpaRepository<PricingPolicy, Long> {

    @Override
    List<PricingPolicy> findAll();

    @Override
    Optional<PricingPolicy> findById(Long aLong);

    @Override
    <S extends PricingPolicy> S save(S s);

    @Override
    void deleteById(Long aLong);
}