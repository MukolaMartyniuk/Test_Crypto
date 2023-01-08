package com.example.test_crypto.service;

import com.example.test_crypto.model.CryptoBalanceTrade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CryptoServiceTest {

    @Autowired
    CryptoService cryptoService;

    @Test
    void getMinPrice() {

        List<CryptoBalanceTrade> listPages = cryptoService.switchCrypto("BTC");
        cryptoService.sortList(listPages);

        assertEquals("16826.1", Double.toString(listPages.get(0).getPrice()));
    }

    @Test
    void getMaxPrice() {

        List<CryptoBalanceTrade> listPages = cryptoService.switchCrypto("BTC");
        cryptoService.sortList(listPages);

        assertEquals("17077.9", Double.toString(listPages.get(listPages.size()-1).getPrice()));
    }

    @Test
    void sortListMinValue() {

        List<CryptoBalanceTrade> listPages = cryptoService.switchCrypto("ETH");
        cryptoService.sortList(listPages);

        assertEquals(cryptoService.getMinPrice("ETH"), listPages.get(0).getPrice());
    }

    @Test
    void sortListMaxValue() {

        List<CryptoBalanceTrade> listPages = cryptoService.switchCrypto("ETH");
        cryptoService.sortList(listPages);

        assertEquals(cryptoService.getMaxPrice("ETH"), listPages.get(listPages.size()-1).getPrice());
    }

}