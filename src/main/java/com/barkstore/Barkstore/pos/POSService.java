package com.barkstore.Barkstore.pos;

import com.barkstore.Barkstore.requisition.RequestDetails;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class POSService {
    @Autowired
    private OrderDetailsRepository detailsRepository;
    @Autowired
    private OrderHeaderRepository headerRepository;

    public OrderHeader saveHeader() {
        OrderHeader header = new OrderHeader();
        header.setOrderNo("ORD" + UUID.randomUUID().toString().substring(0, 5));
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
            i++;
        }
    }
}
