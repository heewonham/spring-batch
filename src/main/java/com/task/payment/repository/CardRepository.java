package com.task.payment.repository;

import com.task.payment.domain.entity.CardData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardData, String> {

}
