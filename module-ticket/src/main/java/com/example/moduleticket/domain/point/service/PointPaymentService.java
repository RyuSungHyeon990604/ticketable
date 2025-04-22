package com.example.moduleticket.domain.point.service;


import com.example.modulecommon.exception.ErrorCode;
import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.domain.member.entity.Member;
import com.example.moduleticket.domain.point.entity.PointPayment;
import com.example.moduleticket.domain.point.enums.PointHistoryType;
import com.example.moduleticket.domain.point.repository.PointPaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointPaymentService {
	
	private final IamportClient iamportClient;
	private final PointService pointService;
	private final PointPaymentRepository pointPaymentRepository;
	
	@Transactional
	public IamportResponse<Payment> iamPortPayment(Auth auth, String imp_uid) {
		try {
			IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
			Payment payment = response.getResponse();
			
			int price = payment.getAmount().intValue();
			String status = payment.getStatus();
			
			if (!status.equals("paid")) {
				throw new ServerException(ErrorCode.PAYMENT_NOT_SUCCESS);
			}
			
			Member member = Member.fromAuth(auth.getId());
			pointService.increasePoint(auth.getId(), price, PointHistoryType.FILL);
			
			PointPayment pointPayment = PointPayment.builder()
				.price(price)
				.member(member)
				.build();
			pointPaymentRepository.save(pointPayment);
			
			return response;
		} catch(Exception e) {
			throw new ServerException(ErrorCode.PAYMENT_VALID_ERROR);
		}
	}
}
