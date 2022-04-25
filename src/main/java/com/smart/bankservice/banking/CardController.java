package com.smart.bankservice.banking;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
public class CardController {

    private static final String STATUS = "status";
    private static final String FAILED = "failed";
    private static final String ERROR_MESSAGE = "errorMessage";

    @Autowired
    CardService cardService;

    @PostMapping(value = "/cards",consumes = "application/json", produces = "application/json")
    public ResponseEntity createNewCard(@RequestBody Card card){
        card = cardService.createNewCard(card);
        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    @PostMapping(value = "/cards/validations",consumes = "application/json",produces = "application/json")
    public ResponseEntity validateCard(@RequestBody Card card)  {
        try{
            return new ResponseEntity(cardService.validateCard(card),HttpStatus.OK);
        }catch (Exception e){
            Map map = new HashMap();
            map.put(STATUS,FAILED);
            map.put(ERROR_MESSAGE,e.getMessage());
            return new ResponseEntity(map,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/cards/redemptions", produces = "application/json")
    public ResponseEntity redeemCard(HttpServletRequest request, @RequestBody Card card) throws ValidationException {
        try{
            long pointsToRedeem = Long.parseLong(request.getHeader("pointsToRedeem"));
            return new ResponseEntity(cardService.redeemCard(card,pointsToRedeem),HttpStatus.OK);
        }catch (Exception e){
            Map map = new HashMap();
            map.put(STATUS,FAILED);
            map.put(ERROR_MESSAGE,e.getMessage());
            return new ResponseEntity(map,HttpStatus.BAD_REQUEST);
        }
    }
}
