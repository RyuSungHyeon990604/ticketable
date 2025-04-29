package com.example.moduleauction.domain.auction.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionSearchCondition {

	@Nullable
	private String home;

	@Nullable
	private String away;

	@Nullable
	private Integer seatCount;

	@Nullable
	private Boolean isTogether = false;

	@Nullable
	private LocalDateTime startTime;
}
