package com.potatoes.bloodrecovery.domain.model.aggregates;

import com.potatoes.bloodrecovery.domain.model.commands.ModifyBloodRequestCommand;
import com.potatoes.bloodrecovery.domain.model.commands.RegisterBloodRequestCommand;
import com.potatoes.bloodrecovery.domain.model.valueobjects.DirectedDonation;
import com.potatoes.bloodrecovery.domain.model.valueobjects.Post;
import com.potatoes.constants.RequestStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.potatoes.constants.RequestStatus.*;
import static com.potatoes.constants.RequestStatus.ONGOING;

@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "blood_request")
@ToString
public class BloodRequest {

    @Id
    @GeneratedValue
    private Long requestId;
    private String cid;
    private RequestStatus requestStatus;

    private String requestType;
    private Integer bloodReqCnt;
    private Integer bloodDonationCnt;

    private Post post;

    private DirectedDonation directedDonation;

    private boolean editable;

    public BloodRequest(RegisterBloodRequestCommand registerBloodRequestCommand) {
        this.cid = registerBloodRequestCommand.getCid();
        this.requestType = registerBloodRequestCommand.getRequestType();
        this.bloodReqCnt = registerBloodRequestCommand.getBloodReqCnt();
        this.requestStatus = REGISTER;
        this.post = new Post(registerBloodRequestCommand);
        if (!registerBloodRequestCommand.getDirectInfo().isEmpty()){
            new DirectedDonation(registerBloodRequestCommand);
        }
    }

    public void modifyBloodRequest(ModifyBloodRequestCommand modifyBloodRequestCommand) {
        this.cid = modifyBloodRequestCommand.getCid();
        this.requestType = modifyBloodRequestCommand.getRequestType();
        this.bloodReqCnt = modifyBloodRequestCommand.getBloodReqCnt();
        this.post = new Post(modifyBloodRequestCommand);
        if (!modifyBloodRequestCommand.getDirectInfo().isEmpty()){
            new DirectedDonation(modifyBloodRequestCommand);
        }
    }

    public boolean deletableBloodRequest(){
        return this.bloodDonationCnt == 0;
    }
    public void deleteBloodRequest(){
        this.requestStatus = DELETE;
    }

    public boolean isModifiable(){
        return !this.requestStatus.equals(DIRECTED_DONATION_ONGOING) && !this.requestStatus.equals(ONGOING);
    }
}
