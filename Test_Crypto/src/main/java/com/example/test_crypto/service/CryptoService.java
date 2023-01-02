package com.example.test_crypto.service;

import com.example.test_crypto.model.CryptoBalanceTrade;
import com.example.test_crypto.repository.BalanceRepository;
import jakarta.annotation.PostConstruct;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CryptoService {

    private final BalanceRepository repository;

    public CryptoService(BalanceRepository repository) {
        this.repository = repository;
    }

    //Reads data from the api
    public List<List<CryptoBalanceTrade>> urlCEXTrade() {
        List<List<CryptoBalanceTrade>> listOfListsJSON = new ArrayList<>();
        List<CryptoBalanceTrade> participantJsonList;
        try {
            URL[] urls = new URL[]{new URL("https://cex.io/api/trade_history/BTC/USD/"),new URL("https://cex.io/api/trade_history/ETH/USD/"), new URL("https://cex.io/api/trade_history/XRP/USD/")};
            for (int i = 0; i < 3; i++){
            HttpURLConnection con = (HttpURLConnection) urls[i].openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            ObjectMapper mapper = new ObjectMapper();
            participantJsonList = mapper.readValue(String.valueOf(response), new TypeReference<List<CryptoBalanceTrade>>(){});
            listOfListsJSON.add(participantJsonList);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return  listOfListsJSON;
    }

    //Writes data to the database when the program is started
    @PostConstruct
    public void init() {
        repository.remove("crypto_BTC");
        repository.remove("crypto_ETH");
        repository.remove("crypto_XRP");
        List<List<CryptoBalanceTrade>> lis1 = urlCEXTrade();
        repository.save(lis1.get(0),"crypto_BTC");
        repository.save(lis1.get(1),"crypto_ETH");
        repository.save(lis1.get(2),"crypto_XRP");

    }

    //Returns a sorted list of the given elements
    public List<String> getSelectPage(String currencyName, String pageNumber, String pageSize){
        List<String> page = new ArrayList<>();
        List<CryptoBalanceTrade> listPages;
        listPages = switchCrypto(currencyName);
        sortList(listPages);
        for (int i = Integer.parseInt(pageNumber)*Integer.parseInt(pageSize); i < (Integer.parseInt(pageNumber)+1)*Integer.parseInt(pageSize); i++){
            page.add(listPages.get(i).toString());
        }
        return page;
    }

    public void sortList(List<CryptoBalanceTrade> list){
        list.sort(Comparator.comparing(CryptoBalanceTrade::getPrice));
    }

    //Sorts the list
    public List<CryptoBalanceTrade> switchCrypto(String symbol1){
        List<CryptoBalanceTrade> listPages = new ArrayList<>();
        switch (symbol1) {
            case "BTC" -> listPages = getAllCrypto("crypto_BTC");
            case "ETH" -> listPages = getAllCrypto("crypto_ETH");
            case "XRP" -> listPages = getAllCrypto("crypto_XRP");
        }
        return listPages;
    }

    public List<CryptoBalanceTrade> getAllCrypto(String collectionName) {
        return repository.findAll(collectionName);
    }

    //Calculates the minimum price of cryptocurrency
    public double getMinPrice(String symbol1){
        List<CryptoBalanceTrade> listPages;
        listPages = switchCrypto(symbol1);
        sortList(listPages);
        CryptoBalanceTrade minPrice = listPages.get(0);
        return minPrice.getPrice();
    }

    //Calculates the maximum price of cryptocurrency
    public double getMaxPrice(String symbol1){
        List<CryptoBalanceTrade> listPages;
        listPages = switchCrypto(symbol1);
        sortList(listPages);
        CryptoBalanceTrade maxPrice = listPages.get(listPages.size()-1);
        return maxPrice.getPrice();
    }

    //Creates a CSV file
    public void getFileCSV(){
        StringBuilder stringBuilder = new StringBuilder();
        List<CryptoBalanceTrade> listPagesBTC;
        List<CryptoBalanceTrade> listPagesETH;
        List<CryptoBalanceTrade> listPagesXRP;
        listPagesBTC = getAllCrypto("crypto_BTC");
        listPagesETH = getAllCrypto("crypto_ETH");
        listPagesXRP = getAllCrypto("crypto_XRP");
        sortList(listPagesBTC);
        sortList(listPagesETH);
        sortList(listPagesXRP);
        stringBuilder.append("Name = ").append("BTC").append(", minPrice = ")
                .append(listPagesBTC.get(0).getPrice()).append(", maxPrice = ")
                .append(listPagesBTC.get(listPagesBTC.size()-1).getPrice()).append("\n");
        stringBuilder.append("Name = ").append("ETH").append(", minPrice = ")
                .append(listPagesETH.get(0).getPrice()).append(", maxPrice = ")
                .append(listPagesETH.get(listPagesETH.size()-1).getPrice()).append("\n");
        stringBuilder.append("Name = ").append("XRP").append(", minPrice = ")
                .append(listPagesXRP.get(0).getPrice()).append(", maxPrice = ")
                .append(listPagesXRP.get(listPagesXRP.size()-1).getPrice()).append("\n");
        try(FileWriter writer = new FileWriter("cryptoCSV.csv")){
            writer.write(stringBuilder.toString());
            System.out.println("СSV file created");
        }catch (Exception e){
            System.out.println("Error:СSV file not created");
        }

    }
}
