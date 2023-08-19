package com.bhanna.oddsapi.repository;

import com.bhanna.oddsapi.model.Outcome;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BetsRepository extends ReactiveCrudRepository<Outcome, Long> {
}
