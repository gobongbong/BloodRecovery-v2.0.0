package com.potatoes.bloodrecovery.interfaces.rest.controller;

import com.potatoes.bloodrecovery.application.commandservices.BloodCardCommandService;
import com.potatoes.bloodrecovery.application.commandservices.DeleteBloodCardCommandService;
import com.potatoes.bloodrecovery.application.queryservices.GetBloodCardCountQueryService;
import com.potatoes.bloodrecovery.application.queryservices.GetBloodCardsQueryService;
import com.potatoes.bloodrecovery.domain.model.commands.DeleteBloodCardCommand;
import com.potatoes.bloodrecovery.domain.model.commands.RegisterBloodCardCommand;
import com.potatoes.bloodrecovery.interfaces.rest.dto.GetBloodCardCountRspDto;
import com.potatoes.bloodrecovery.interfaces.rest.dto.GetBloodCardsRspDto;
import com.potatoes.bloodrecovery.interfaces.rest.dto.RegisterBloodCardReqDto;
import com.potatoes.bloodrecovery.interfaces.rest.mapper.BloodCardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.potatoes.bloodrecovery.interfaces.rest.constants.apiurl.BloodApiUrl.*;
import static com.potatoes.constants.StaticValues.HEADER_CID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(BLOOD_BASE_URL)
public class BloodCardController extends BaseController{

    private final BloodCardMapper bloodCardMapper;
    private final BloodCardCommandService bloodCardCommandService;
    private final GetBloodCardsQueryService getBloodCardsQueryService;
    private final DeleteBloodCardCommandService deleteBloodCardCommandService;
    private final GetBloodCardCountQueryService getBloodCardCountQueryService;

    @PostMapping(BLOOD_CARD)
    public ResponseEntity<Object> registerBloodCard(@RequestHeader(value = HEADER_CID) @NotBlank String cid,
                                                    @RequestBody @Valid RegisterBloodCardReqDto registerBloodCardReqDto) {
        RegisterBloodCardCommand registerBloodCardCommand = bloodCardMapper.registerReqtoCommand(cid, registerBloodCardReqDto);
        bloodCardCommandService.registerBloodCard(registerBloodCardCommand);
        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    @GetMapping(BLOOD_CARD)
    public ResponseEntity<Object> getBloodCards(@RequestHeader(value = HEADER_CID) @NotBlank String cid) {
        GetBloodCardsRspDto getBloodCardsRspDto = GetBloodCardsRspDto.builder()
                .cards(getBloodCardsQueryService.getBloodCards(cid))
                .build();
        return new ResponseEntity<>(getBloodCardsRspDto, getSuccessHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(DELETE_BLOOD_CARD)
    public ResponseEntity<Object> deleteBloodCard(@RequestHeader(value = HEADER_CID) @NotBlank String cid,
                                                  @PathVariable String bloodCardId) {
        DeleteBloodCardCommand deleteBloodCardCommand = bloodCardMapper.deleteReqtoCommand(cid, bloodCardId);
        deleteBloodCardCommandService.deleteBloodCard(deleteBloodCardCommand);
        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    @GetMapping(GET_BLOOD_CARD_COUNT)
    public ResponseEntity<Object> getBloodCardCount(@RequestHeader(value = HEADER_CID) @NotBlank String cid) {
        GetBloodCardCountRspDto getBloodCardsRspDto = GetBloodCardCountRspDto.builder()
                .cardCnt(getBloodCardCountQueryService.getBloodCardCount(cid))
                .build();
        return new ResponseEntity<>(getBloodCardsRspDto, getSuccessHeaders(), HttpStatus.OK);
    }
}
