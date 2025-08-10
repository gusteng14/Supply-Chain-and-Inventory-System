package com.barkstore.Barkstore.Controller;


import com.barkstore.Barkstore.pos.*;
import com.barkstore.Barkstore.products.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class RestDataController {
    @Autowired
    private POSService posService;
    @Autowired
    private OrderDetailsRepository orderDetailRepository;

    @GetMapping("/getSalesToday")
    public float getSalesToday() {
        float sales = posService.dailySales();

        return sales;
    }

    @GetMapping("/getTop5ProductsMonth")
    public ResponseEntity<Map<String, Integer>> getTop5ProductsMonth(@RequestParam int year, @RequestParam int month) {
        List<OrderDetail> allDtl = orderDetailRepository.findAll();
        HashMap<String, Integer> top5 = new HashMap<>();

        for (OrderDetail dtl : allDtl) {
            try {
                int qty = posService.getTop5ProductsMonth(dtl.getItemName(), year, month);
                top5.put(dtl.getItemName(), qty);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }

        Map<String, Integer> result = top5.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getOrdersToday")
    public int getOrdersToday(@RequestParam String date) throws ParseException {
        int orders = posService.totalOrders(date);

        return orders;
    }

    @GetMapping("/getOrdersMonth")
    public int getOrdersMonth(@RequestParam int year, @RequestParam int month) {
        List<OrderHeader> orders = posService.getOrdersByMonth(year, month);
        int count = orders.size();

        return count;
    }

    @GetMapping("/getOrdersYear")
    public int getOrdersYear(@RequestParam int year) {
        List<OrderHeader> orders = posService.getOrdersByYear(year);
        int count = orders.size();

        return count;
    }

    @GetMapping("/getAverageSalesToday")
    public float getAverageSalesToday() {
        float sales = posService.dailySales();
        float avgSales = sales / 10;

        return avgSales;
    }
}
