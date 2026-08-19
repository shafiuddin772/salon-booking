package com.serviceOffering.controller;

import com.serviceOffering.dto.CategoryDTO;
import com.serviceOffering.dto.SalonDTO;
import com.serviceOffering.dto.ServiceDTO;
import com.serviceOffering.modal.ServiceOffering;
import com.serviceOffering.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {
    private final ServiceOfferingService serviceOfferingService;

    @PostMapping
    public ResponseEntity<ServiceOffering> createService(
            @RequestBody ServiceDTO serviceDTO
    ){
        SalonDTO salonDTO= new SalonDTO();
        salonDTO.setId(1L);
        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(serviceDTO.getCategory());

        ServiceOffering serviceOfferings= serviceOfferingService
                .createService(salonDTO,serviceDTO,categoryDTO);
        return ResponseEntity.ok(serviceOfferings);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ServiceOffering> updateService(
            @PathVariable Long serviceId,
            @RequestBody ServiceOffering serviceOffering
    ) throws Exception {
        ServiceOffering serviceOfferings= serviceOfferingService
                .updataService(serviceId,serviceOffering);
        return ResponseEntity.ok(serviceOfferings);
    }
}
