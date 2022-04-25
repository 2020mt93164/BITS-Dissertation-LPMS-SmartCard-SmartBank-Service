package com.smart.bankservice.banking;

import com.fasterxml.jackson.databind.JsonNode;
import com.smart.bankservice.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CardService {

    private static final String STATUS = "status";
    private static final String SUCCESSFUL = "successful";

    @Autowired
    CardRepository cardRepository;

    public Card createNewCard(Card card){
        card.setCardNo(Utils.getRandomCardNo());
        card.setCvv(Utils.getRandomCvv());
        card.setExpMonth(Utils.getRandomExpMonth());
        card.setExpYear(Utils.getRandomExpYear());
        return cardRepository.save(card);
    }

    public Map validateCard(Card card) throws ValidationException {

        Optional<Card> fetchedCard = cardRepository.findByCardNo(card.getCardNo());
        if (fetchedCard.isPresent()) {
            if (fetchedCard.get().getCvv()==card.getCvv() &&
                    fetchedCard.get().getExpMonth()==card.getExpMonth() &&
                    fetchedCard.get().getExpYear()==card.getExpYear()){
                Map map = new HashMap();
                map.put(STATUS,SUCCESSFUL);
                map.put("availablePoints",fetchedCard.get().getAvailablePoints());
                return map;
            }else {
                throw new ValidationException("Card details do not match", String.valueOf(HttpStatus.BAD_REQUEST));
            }
        }else{
            throw new ValidationException("Card not found", String.valueOf(HttpStatus.NOT_FOUND));
        }
    }

    public Map redeemCard(Card card,long pointsToRedeem) throws ValidationException {

        Optional<Card> fetchedCard = cardRepository.findByCardNo(card.getCardNo());
        if (fetchedCard.isPresent()){
            validateCard(card);
            long availablePoints = fetchedCard.get().getAvailablePoints();

            if (availablePoints>=pointsToRedeem){
                fetchedCard.get().setAvailablePoints(availablePoints-pointsToRedeem);
                cardRepository.save(fetchedCard.get());
                Map map = new HashMap();
                map.put(STATUS,SUCCESSFUL);
                return map;
            }else {
                throw new ValidationException("Insufficient funds", String.valueOf(HttpStatus.UNAUTHORIZED));
            }
        }else{
            throw new ValidationException("Card not found", String.valueOf(HttpStatus.NOT_FOUND));
        }
    }
}