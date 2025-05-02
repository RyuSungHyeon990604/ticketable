package com.example.modulegame.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient(name = "module-ticket")
public interface TicketClient {

    @DeleteMapping("/api/tickets/games/{gameId}")
    void deleteAllTicketsByCanceledGame(@PathVariable("gameId") Long gameId);
}