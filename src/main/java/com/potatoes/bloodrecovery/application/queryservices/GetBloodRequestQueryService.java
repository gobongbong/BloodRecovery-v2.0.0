package com.potatoes.bloodrecovery.application.queryservices;

import com.potatoes.bloodrecovery.domain.model.aggregates.BloodRequest;
import com.potatoes.bloodrecovery.domain.model.view.BloodRequestView;
import com.potatoes.bloodrecovery.domain.model.view.UserInfoView;
import com.potatoes.bloodrecovery.domain.repository.BloodRequestRepository;
import com.potatoes.bloodrecovery.domain.repository.UserRepository;
import com.potatoes.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.potatoes.constants.ResponseCode.FAIL_GET_BLOOD_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetBloodRequestQueryService {

    private final BloodRequestRepository bloodRequestRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public BloodRequestView getBloodRequest(String cid, Long requestId) {
        boolean editable = false;
        BloodRequestView bloodRequestView;
        try {
            BloodRequest bloodRequest = bloodRequestRepository.findByRequestId(requestId);
            if (StringUtils.equals(bloodRequest.getCid(), cid)) {
                editable = true;
            }

            UserInfoView userInfoView = userRepository.getUserInfo(cid);
            bloodRequestView = new BloodRequestView(bloodRequest, userInfoView, editable);
        } catch (Exception e) {
            throw new ApiException(FAIL_GET_BLOOD_REQUEST);
        }
        return bloodRequestView;
    }
}
