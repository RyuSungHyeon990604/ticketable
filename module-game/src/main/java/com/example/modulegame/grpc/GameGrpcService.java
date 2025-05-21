package com.example.modulegame.grpc;

import static com.example.modulegame.global.exception.ErrorCode.SEAT_NOT_FOUND;

import com.example.grpc.game.GameServiceGrpc;
import com.example.grpc.game.SeatDetailDto;
import com.example.grpc.game.SeatDetailDtoList;
import com.example.grpc.game.SeatInfo;
import com.example.modulegame.domain.stadium.repository.SeatRepository;
import com.example.modulegame.global.exception.ServerException;
import io.grpc.stub.StreamObserver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GameGrpcService extends GameServiceGrpc.GameServiceImplBase {

	private final SeatRepository seatRepository;

	@Override
	public void getSeatsByGameAndSection(SeatInfo request, StreamObserver<SeatDetailDtoList> responseObserver) {
		List<com.example.modulegame.domain.game.dto.SeatDetailDto> seatDtosByGameAndSection = seatRepository.findSeatDtosByGameAndSection(
			request.getGameId(),
			request.getSectionId(),
			request.getSeatIdsList()
		);
		if(seatDtosByGameAndSection.size() != request.getSeatIdsList().size()){
			responseObserver.onError(new ServerException(SEAT_NOT_FOUND));
		}

		int gamePrice = seatDtosByGameAndSection.get(0).getGamePrice();
		int sectionPrice = seatDtosByGameAndSection.get(0).getSectionPrice();

		SeatDetailDtoList response = SeatDetailDtoList.newBuilder()
			.addAllSeatDetailDtoList(
				seatDtosByGameAndSection.stream()
					.map(seatDto -> SeatDetailDto.newBuilder()
						.setSeatId(seatDto.getSeatId())
						.setGameId(seatDto.getGameId())
						.setSectionId(seatDto.getSectionId())
						.setGamePrice(gamePrice)
						.setSectionPrice(sectionPrice)
						.build()
					).toList()
			).build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
