package com.bhanna.oddsapi.repository;

import com.bhanna.oddsapi.model.Bet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BetsRepository extends ReactiveCrudRepository<Bet, Long> {
}
