package com.example.test_crypto.controller;


import com.example.test_crypto.service.CryptoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController("/cryptocurrencies")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    //Returns the sorted selected page with the selected number of items
    @GetMapping("/cryptocurrencies")
    public List<String> selectPage(@RequestParam String currencyName, @RequestParam(defaultValue = "0")  String pageNumber, @RequestParam(defaultValue = "10")  String pageSize) {
        return cryptoService.getSelectPage(currencyName, pageNumber, pageSize);
    }

    //Returns the minimum price of a cryptocurrency
    @GetMapping("/cryptocurrencies/minPrice")
    public double minPrice(@RequestParam String currencyName){
        return cryptoService.getMinPrice(currencyName);
    }

    //Returns the maximum price of a cryptocurrency
    @GetMapping("/cryptocurrencies/maxPrice")
    public double maxPrice(@RequestParam String currencyName) {
        return cryptoService.getMaxPrice(currencyName);
    }

    //Creates a CSV file with three cryptocurrencies BTC, ETH, XRP
    @GetMapping("/cryptocurrencies/csv")
    public void fileCSV(){
        cryptoService.getFileCSV();
    }

}
