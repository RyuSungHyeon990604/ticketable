package com.example.modulewaiting.queue.controller;


import com.example.modulewaiting.queue.QueueSystemConstants;
import com.example.modulewaiting.queue.dto.WaitingResponse;
import com.example.modulewaiting.queue.service.QueueManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WaitingQueueController {
	private final QueueManager queueManager;


	@GetMapping("/v1/waiting-queue/order")
	public ResponseEntity<WaitingResponse> handleWaiting(@RequestHeader(name = "waiting-token", required = false) String token) {

		if(token != null && queueManager.isAllowed(token)) {

			queueManager.deleteTokenFromWaitingAndProceedQueue(token);

			return ResponseEntity.ok(
				new WaitingResponse(
					0,
					"allow",
					token,
					0,
					QueueSystemConstants.MAX_POLLING_TIME
				)
			);
		}

		if(token == null || token.isEmpty()){
			token = queueManager.enterWaitingQueue();
		}

		long waitingOrder = queueManager.getWaitingOrder(token);
		long expectedWaitingSec = queueManager.getExpectedWaitingOrder(waitingOrder);
		return ResponseEntity.ok(
			new WaitingResponse(
				waitingOrder,
				"wait",
				token,
				expectedWaitingSec,
				Math.min(expectedWaitingSec, QueueSystemConstants.MAX_POLLING_TIME)
			)
		);
	}

	@DeleteMapping("/v1/waiting-queue")
	public ResponseEntity<Void> deleteToken(@RequestHeader(name = "waiting-token") String token) {
		queueManager.deleteTokenFromWaitingAndProceedQueue(token);

		return ResponseEntity.noContent().build();
	}

}
