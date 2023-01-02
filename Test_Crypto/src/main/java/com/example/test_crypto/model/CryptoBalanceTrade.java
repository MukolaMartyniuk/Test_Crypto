package com.example.test_crypto.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
public class CryptoBalanceTrade {
    private String type;
    private String date;
    private String amount;
    @Field(name = "price")
    @Indexed(unique = true)
    private String price;
    private String tid;

    @Override
    public String toString(){
        return type + " " + date + " " + amount + " " + price + " " + tid;
    }

    public double getPrice(){
        return Double.parseDouble(price);
    }

}
