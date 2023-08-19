package com.bhanna.oddsapi.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BetsRepository extends ReactiveCrudRepository<Outcome, Long> {
}
