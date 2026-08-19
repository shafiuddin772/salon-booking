package com.serviceOffering.service.impl;

import com.serviceOffering.dto.CategoryDTO;
import com.serviceOffering.dto.SalonDTO;
import com.serviceOffering.dto.ServiceDTO;
import com.serviceOffering.modal.ServiceOffering;
import com.serviceOffering.repository.ServiceOfferingRepository;
import com.serviceOffering.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {


    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createService(SalonDTO salonDto, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {
        ServiceOffering serviceOffering=new ServiceOffering();
        serviceOffering.setImage(serviceDTO.getImage());
        serviceOffering.setSalonId(salonDto.getId());
        serviceOffering.setName(serviceDTO.getName());
        serviceOffering.setDescription(serviceDTO.getDescription());
        serviceOffering.setCategoryId(categoryDTO.getId());
        serviceOffering.setPrice(serviceDTO.getPrice());
        serviceOffering.setDuration(serviceDTO.getDuration());
    return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updataService(Long serviceId, ServiceOffering service) throws Exception {
        ServiceOffering serviceOffering=serviceOfferingRepository
                .findById(serviceId).orElse(null);
        if(serviceOffering==null){
            throw new Exception("service not exist"+serviceId);
        }
        serviceOffering.setImage(service.getImage());
        serviceOffering.setName(service.getName());
        serviceOffering.setDescription(service.getDescription());
        serviceOffering.setPrice(service.getPrice());

       return   serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public Set<ServiceOffering> getAllServiceBySalonId(Long salonId, Long categoryId) {
        Set<ServiceOffering>services= serviceOfferingRepository.findBySalonId(salonId);
    if(categoryId!=null){
        services=services.stream().filter((service)->service.getCategoryId()!=null
        && service.getCategoryId()==categoryId).collect(Collectors.toSet());
    }
    return services;
    }

    @Override
    public Set<ServiceOffering> getServicesByIds(Set<Long> ids) {
        List<ServiceOffering>services= serviceOfferingRepository.findAllById(ids);
    return new HashSet<>(services);
    }

    @Override
    public ServiceOffering getServiceById(Long id) throws Exception {
        ServiceOffering serviceOffering=serviceOfferingRepository
                .findById(id).orElse(null);
        if(serviceOffering==null){
            throw new Exception("service not exist"+id);
        }
        return serviceOffering;
    }
}
