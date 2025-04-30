package com.example.modulegame.domain.game.service;

import com.example.grpc.ticket.BookedSeatsRequest;
import com.example.grpc.ticket.BookedSeatsResponse;
import com.example.grpc.ticket.TicketServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameGrpcClientService {
    private final TicketServiceGrpc.TicketServiceBlockingStub ticketServiceBlockingStub;
    public Set<Long> getBookedSeatIds(Long gameId) {
        BookedSeatsRequest request = BookedSeatsRequest.newBuilder()
                .setGameId(gameId)
                .build();

        BookedSeatsResponse response = ticketServiceBlockingStub.getBookedSeatIds(request);

        return response.getSeatIdsList().stream().collect(Collectors.toSet());
    }
}
