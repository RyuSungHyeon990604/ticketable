package com.example.modulegame.config;

import com.example.grpc.ticket.TicketServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public TicketServiceGrpc.TicketServiceBlockingStub ticketServiceBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9092) // 티켓 서버 주소/포트
                .usePlaintext() // TLS 없이 통신
                .build();
        return TicketServiceGrpc.newBlockingStub(channel);
    }
}

