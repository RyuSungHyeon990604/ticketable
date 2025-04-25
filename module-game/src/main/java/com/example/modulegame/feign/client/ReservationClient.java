package com.example.modulegame.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient(name = "reservation", url = "http://localhost:8082")
public interface ReservationClient {

    @GetMapping("/api/v1/reservations/games/{gameId}")
    Set<Long> getBookedSeatsId(@PathVariable("gameId") Long gameId);
}