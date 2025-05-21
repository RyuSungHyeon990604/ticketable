package com.example.moduleticket.grpc;

import com.example.grpc.ticket.TicketServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GrpcServerRunner {

//    @Value("${grpc.port:9090}")
//    private int port;
//
//    private final TicketServiceGrpc.TicketServiceImplBase ticketService;
//
//    @PostConstruct
//    public void startGrpcServer() throws IOException {
//        Server server = ServerBuilder
//                .forPort(port)
//                .addService(ticketService)
//                .build()
//                .start();
//
//        System.out.println("✅ gRPC 서버 시작됨. 포트: " + port);
//
//        // JVM 종료 시 gRPC 서버도 종료되도록 후킹
//        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
//
//        // gRPC 서버는 블로킹이므로 별도 쓰레드로 실행
//        new Thread(() -> {
//            try {
//                server.awaitTermination();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt(); // 인터럽트 상태 복원 (권장)
//                System.err.println("gRPC 서버 대기 중 인터럽트 발생: " + e.getMessage());
//            }
//        }).start();
//    }
}
