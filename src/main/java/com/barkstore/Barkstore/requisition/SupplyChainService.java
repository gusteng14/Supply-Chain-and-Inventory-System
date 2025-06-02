package com.barkstore.Barkstore.requisition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplyChainService {
    @Autowired
    private HeaderRepository headerRepository;
    @Autowired
    private DetailsRepository detailsRepository;

    public RequestHeader saveHeader(HeaderDTO headerDTO) {
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setName(headerDTO.getRequestName());
        requestHeader.setDescription(headerDTO.getRequestDescription());
        headerRepository.save(requestHeader);

        return requestHeader;
    }

    public void saveDetails(RequestHeader requestHeader, List<String> itemList, List<String> qtyList, List<String> totalList ) {
        int i = 0;
        for (String str : itemList) {
            RequestDetails requestDetails = new RequestDetails();
//            System.out.println("Product " + i + ": " + str);
            requestDetails.setProduct(str);
//            System.out.println("Qty " + i + ": " + Integer.parseInt(qtyList.get(i)));
            requestDetails.setQuantity(Integer.parseInt(qtyList.get(i)));
//            System.out.println("Total " + i + ": " + Integer.parseInt(totalList.get(i)));
            requestDetails.setTotal(Integer.parseInt(totalList.get(i)));
            requestDetails.setHeaderId(requestHeader);
            detailsRepository.save(requestDetails);
            i++;
        }
    }

    public void deleteHeader() {

    }
}
