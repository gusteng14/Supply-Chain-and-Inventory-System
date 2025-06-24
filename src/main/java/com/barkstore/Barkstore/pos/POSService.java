package com.barkstore.Barkstore.pos;

import com.barkstore.Barkstore.products.Product;
import com.barkstore.Barkstore.products.ProductRepo;
import com.barkstore.Barkstore.requisition.RequestDetails;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        header.setOrderNo("ORD" + UUID.randomUUID().toString().substring(0, 5));
        header.setTotal(Float.parseFloat(total));
        headerRepository.save(header);

        return header;
    }

    public void saveDetails(OrderHeader header, List<String> itemList, List<String> qtyList, List<String> totalList) {
        int i = 0;
        for (String str : itemList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setItemName(str);
            orderDetail.setQuantity(Integer.parseInt(qtyList.get(i)));
            orderDetail.setTotal(Float.parseFloat(totalList.get(i)));
            orderDetail.setHeaderId(header);
            detailsRepository.save(orderDetail);

            Product product = productRepo.findByName(str).get();
            product.setStock(product.getStock() - Integer.parseInt(qtyList.get(i)));
            product.setTotalQuantitySold(product.getTotalQuantitySold() + Integer.parseInt(qtyList.get(i)));
            productRepo.save(product);
            i++;
        }
    }

    public float dailySale() {
        List<OrderHeader> orders = headerRepository.findByCreatedDate(LocalDate.now());
        float totalSaleForToday = 0;

        for (OrderHeader order : orders) {
            totalSaleForToday += order.getTotal();
        }

        return totalSaleForToday;
    }

    public float totalSales() {
        List<OrderHeader> orders = headerRepository.findAll();
        float totalSales = 0;

        for (OrderHeader ord : orders) {
            totalSales += ord.getTotal();
        }

        return totalSales;
    }

    public long totalOrdersWithDate(LocalDateTime date) {
        List<OrderHeader> orders = headerRepository.findByCreatedOn(date);
        long totalOrders = 0;

        for (OrderHeader ord : orders) {
            totalOrders ++;
        }

        return totalOrders;
    }

    public float totalSalesWithDate(LocalDateTime date) {
        List<OrderHeader> orders = headerRepository.findByCreatedOn(date);
        float totalSales = 0;

        for (OrderHeader ord : orders) {
            totalSales += ord.getTotal();
        }

        return totalSales;
    }

    public float averageSalesWithDate(LocalDateTime date) {
        List<OrderHeader> orders = headerRepository.findByCreatedOn(date);
        float averageSales = 0;
        float totalSales = 0;
        float orderCount = 0;

        for (OrderHeader ord : orders) {
            totalSales += ord.getTotal();
            orderCount++;
        }

        averageSales = totalSales / orderCount;
        System.out.println("Ave: " + averageSales);
        return averageSales;
    }



}
