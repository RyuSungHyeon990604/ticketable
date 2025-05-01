package com.example.modulegame.domain.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;


//@Service
//@RequiredArgsConstructor
//public class GameRSocketClientService {
//
//    private final RSocketRequester rSocketRequester;
//    public Mono<Set<Long>> getBookedSeatIds(Long gameId) {
//        return rSocketRequester
//                .route("ticket.bookedSeats")           // 서버의 @MessageMapping과 일치해야 함
//                .data(gameId)                          // 보내는 데이터
//                .retrieveMono(new ParameterizedTypeReference<Set<Long>>() {});
//    }
//}
