package com.ilya.orderservice.repositories;

import com.ilya.orderservice.models.Bag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BagsRepository extends JpaRepository<Bag, Long> {

    List<Bag> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
