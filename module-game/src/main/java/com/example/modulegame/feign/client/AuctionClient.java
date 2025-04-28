package com.example.modulegame.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auction", url = "http://localhost:8082")
public interface AuctionClient {

    @DeleteMapping("/api/auctions/game/{gameId}")
    void deleteAllAuctionsByCanceledGame(@PathVariable("gameId") Long gameId);
}