package com.smart.bankservice.banking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String cardNo;
    private String cardName;
    private long expMonth;
    private long expYear;
    private long cvv;
    private long availablePoints;

}
