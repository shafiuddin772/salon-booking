package com.serviceOffering.service;

import com.serviceOffering.dto.CategoryDTO;
import com.serviceOffering.dto.SalonDTO;
import com.serviceOffering.dto.ServiceDTO;
import com.serviceOffering.modal.ServiceOffering;

import java.util.List;
import java.util.Set;

public interface ServiceOfferingService {
    ServiceOffering createService(SalonDTO salonDto , ServiceDTO serviceDTO, CategoryDTO categoryDTO);

    ServiceOffering updataService(Long serviceId, ServiceOffering service) throws Exception;

    Set<ServiceOffering>getAllServiceBySalonId(Long salonId,Long categoryId);

    Set<ServiceOffering>getServicesByIds(Set<Long>ids);

    ServiceOffering getServiceById(Long id) throws Exception;
}
