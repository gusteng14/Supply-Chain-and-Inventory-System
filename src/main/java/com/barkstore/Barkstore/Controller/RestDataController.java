package com.barkstore.Barkstore.Controller;


import com.barkstore.Barkstore.pos.OrderHeader;
import com.barkstore.Barkstore.pos.POSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestDataController {
    @Autowired
    private POSService posService;

    @GetMapping("/getSalesToday")
    public float getSalesToday() {
        float sales = posService.dailySales();

        return sales;
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
}
