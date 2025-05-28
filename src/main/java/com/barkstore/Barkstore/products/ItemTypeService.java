package com.barkstore.Barkstore.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemTypeService {
    @Autowired
    private ItemTypeRepo itemTypeRepo;

    public void createItemType (ItemTypeRequest itemTypeRequest) {
        ItemType itemType = new ItemType();

        itemType.setName(itemTypeRequest.getName());
        System.out.println(itemTypeRequest.getName());
        itemTypeRepo.save(itemType);
    }
}
