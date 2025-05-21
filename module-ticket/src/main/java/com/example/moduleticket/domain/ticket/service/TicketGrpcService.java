package com.example.moduleticket.domain.ticket.service;

import com.example.grpc.ticket.BookedSeatsRequest;
import com.example.grpc.ticket.BookedSeatsResponse;
import com.example.grpc.ticket.TicketServiceGrpc;
import com.example.moduleticket.domain.reservation.service.ReservationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.util.Set;

@GrpcService
@RequiredArgsConstructor
public class TicketGrpcService extends TicketServiceGrpc.TicketServiceImplBase {
    private final ReservationService reservationService; // 기존 좌석 조회 서비스

    @Override
    public void getBookedSeatIds(BookedSeatsRequest request, StreamObserver<BookedSeatsResponse> responseObserver) {
        try {
            Long gameId = request.getGameId(); // 요청에서 gameId 추출
            Set<Long> seatIds = reservationService.getBookedSeatsId(gameId); // 예약된 좌석 ID 조회

            BookedSeatsResponse response = BookedSeatsResponse.newBuilder()
                    .addAllSeatIds(seatIds) // 조회 결과를 응답 메시지에 추가
                    .build();

            responseObserver.onNext(response); // 결과 반환
            responseObserver.onCompleted();    // 스트림 종료
        } catch (Exception e) {
            responseObserver.onError(e); // 에러가 발생하면 클라이언트에 에러 전달
        }
    }
}
