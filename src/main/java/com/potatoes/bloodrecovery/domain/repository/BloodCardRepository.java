package com.potatoes.bloodrecovery.domain.repository;

import com.potatoes.bloodrecovery.domain.model.aggregates.BloodCard;

import java.util.List;
import java.util.Optional;

public interface BloodCardRepository {
    List<BloodCard> findByCid(String cid);
    Optional<BloodCard> findBloodCardByCidAndBloodCardId(String cid, Long bloodCardId);
    BloodCard save(BloodCard bloodCard);
    void deleteByBloodCardId(Long bloodCardId);
}
