package com.barkstore.Barkstore.pos;

import com.barkstore.Barkstore.products.Product;
import com.barkstore.Barkstore.products.ProductRepo;
import com.barkstore.Barkstore.requisition.RequestDetails;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class POSService {
    @Autowired
    private OrderDetailsRepository detailsRepository;
    @Autowired
    private OrderHeaderRepository headerRepository;
    @Autowired
    private ProductRepo productRepo;

    public OrderHeader saveHeader(String total) {
        OrderHeader header = new OrderHeader();
        header.setOrderNo("O#" + UUID.randomUUID().toString().substring(0, 5));
        header.setTotal(Float.parseFloat(total));
        headerRepository.save(header);

        return header;
    }

    public void saveDetails(OrderHeader header, List<String> itemList, List<String> qtyList, List<String> totalList, List<String> unitPrice) {
        int i = 0;
        for (String str : itemList) {
            OrderDetail orderDetail = new OrderDetail();
            float qty = Float.parseFloat(qtyList.get(i));

            orderDetail.setItemName(str);
            orderDetail.setQuantity(Integer.parseInt(qtyList.get(i)));
            orderDetail.setTotal(Float.parseFloat(totalList.get(i)) * qty);
            orderDetail.setUnitPrice(Float.parseFloat(unitPrice.get(i)));
            orderDetail.setHeaderId(header);
            detailsRepository.save(orderDetail);

            // TODO: Should be find by id not by name
            Product product = productRepo.findByName(str).get();
            product.setStock(product.getStock() - Integer.parseInt(qtyList.get(i)));
            product.setTotalQuantitySold(product.getTotalQuantitySold() + Integer.parseInt(qtyList.get(i)));
            if(product.getStock() <= product.getReorderPoint()) {
                product.setIsLowStock(true);
            } else {
                product.setIsLowStock(false);
            }
            productRepo.save(product);
            i++;
        }
    }

    public float dailySales() {
        List<OrderHeader> ordersToday = headerRepository.findByCreatedOn(LocalDate.now());
        float sales = 0;

        for (OrderHeader ord : ordersToday) {
            sales += ord.getTotal();
        }

        return sales;
    }

    public float totalSales() {
        List<OrderHeader> orders = headerRepository.findAll();
        float totalSales = 0;

        for (OrderHeader ord : orders) {
            totalSales += ord.getTotal();
        }

        return totalSales;
    }

    public int totalOrders(String date) throws ParseException {
        LocalDate localDate = LocalDate.parse(date);
        List<OrderHeader> orders = headerRepository.findByCreatedOn(localDate);
        int count = orders.size();

        return count;
    }

    public Integer getTop5ProductsMonth(String itemName, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        return detailsRepository.totalQuantitySold(itemName, firstDayOfMonth, lastDayOfMonth);
    }

    public List<OrderHeader> getOrdersByMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        return headerRepository.findByCreatedOnBetween(firstDayOfMonth, lastDayOfMonth);
    }

    public List<OrderHeader> getOrdersByYear(int year) {
        LocalDate firstDayOfYear = LocalDate.ofYearDay(year, 1);
        LocalDate lastDayOfYear = LocalDate.ofYearDay(year, 365);
        return headerRepository.findByCreatedOnBetween(firstDayOfYear, lastDayOfYear);
    }


}
