package com.example.moduleticket.grpc;

import com.example.grpc.game.GameServiceGrpc;
import com.example.grpc.game.SeatDetailDtoList;
import com.example.grpc.game.SeatInfo;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GameGrpcClient {

	@GrpcClient("game")
	private GameServiceGrpc.GameServiceBlockingStub blockingStub;

	public SeatDetailDtoList getSeatDetail(Long gameId, Long sectionId, List<Long> seatIds) {

		SeatInfo seatInfo = SeatInfo.newBuilder()
			.setGameId(gameId)
			.setSectionId(sectionId)
			.addAllSeatIds(seatIds)
			.build();

		SeatDetailDtoList seatsByGameAndSection = blockingStub.getSeatsByGameAndSection(seatInfo);

		return seatsByGameAndSection;
	}
}
