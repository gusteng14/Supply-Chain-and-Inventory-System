package com.barkstore.Barkstore.Controller;


import com.barkstore.Barkstore.pos.POSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DateController {
    @Autowired
    private POSService posService;

    @GetMapping(value = "/getDate")
    public List<String> getDateSalesSummary(@RequestParam("date") String date) {
        String chosenDate = date + " 00:00:00";
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formattedDate = LocalDateTime.parse(chosenDate, formatter2);

        List<String> data = new ArrayList<>();

        String totalOrders = String.valueOf(posService.totalOrdersWithDate(formattedDate));
        String totalSales = String.valueOf(String.format("%.02f", posService.totalSalesWithDate(formattedDate)));
        String averageSales = String.valueOf(String.format("%.02f", posService.averageSalesWithDate(formattedDate)));

        data.add(totalOrders);
        data.add(totalSales);
        data.add(averageSales);

        return data;
    }
}
