package com.example.test_crypto.repository;


import com.example.test_crypto.model.CryptoBalanceTrade;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class BalanceRepository {

    final MongoTemplate mongoTemplate;

    Query query = new Query();

    public BalanceRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public void save(List<CryptoBalanceTrade> cryptoBalanceTrade, String nameCollection) {
        for (CryptoBalanceTrade i:
             cryptoBalanceTrade) {
            mongoTemplate.save(i, nameCollection);
        }
    }

    public void remove(String nameCollection) {
        mongoTemplate.remove(query, nameCollection);
    }

    public List<CryptoBalanceTrade> findAll(String collectionName) {
        return mongoTemplate.findAll(CryptoBalanceTrade.class, collectionName);
    }



}
