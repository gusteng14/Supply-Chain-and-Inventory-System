package com.barkstore.Barkstore.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public void createSupplier(SupplierRequest supplierRequest) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierRequest.getName());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setContactNo(supplierRequest.getContactNo());
        supplier.setAddressLine1(supplierRequest.getAddressLine1());
        supplier.setAddressLine2(supplierRequest.getAddressLine2());
        supplier.setAddressLine3(supplierRequest.getAddressLine3());
        supplier.setAgentName(supplierRequest.getAgentName());
        supplier.setAgentContactNo(supplierRequest.getAgentContactNo());
        supplierRepository.save(supplier);
    }

    public void editSupplier(Supplier supplier, SupplierRequest supplierRequest) {
        supplier.setName(supplierRequest.getName());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setContactNo(supplierRequest.getContactNo());
        supplier.setAddressLine1(supplierRequest.getAddressLine1());
        supplier.setAddressLine2(supplierRequest.getAddressLine2());
        supplier.setAddressLine3(supplierRequest.getAddressLine3());
        supplier.setAgentName(supplierRequest.getAgentName());
        supplier.setAgentContactNo(supplierRequest.getAgentContactNo());
        supplierRepository.save(supplier);
    }

}
