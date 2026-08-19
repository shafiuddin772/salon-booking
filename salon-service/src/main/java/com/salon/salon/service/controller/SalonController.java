package com.salon.salon.service.controller;

import com.salon.salon.service.mapper.SalonMapper;
import com.salon.salon.service.model.Salon;
import com.salon.salon.service.payload.dto.SalonDTO;
import com.salon.salon.service.payload.dto.UserDTO;
import com.salon.salon.service.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    //http://localhost:5002/api/salons
    @PostMapping
    public ResponseEntity<SalonDTO>createSalon(@RequestBody SalonDTO salonDTO){
        UserDTO userDTO=new UserDTO();
        userDTO.setId(1L);
        Salon salon=salonService.createSalon(salonDTO,userDTO);
        SalonDTO salonDTO1= SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO1);
    }

    //http://localhost:5002/api/salons/2
    @PutMapping("/{salonId}")
    public ResponseEntity<SalonDTO>updateSalon(
            @PathVariable("salonId") Long salonId,
            @RequestBody SalonDTO salonDTO) throws Exception {
        UserDTO userDTO=new UserDTO();
        userDTO.setId(1L);
        Salon salon=salonService.updateSalon(salonDTO,userDTO,salonId);
        SalonDTO salonDTO1= SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO1);
    }


    //http://localhost:5002/api/salons
    @GetMapping()
    public ResponseEntity<List<SalonDTO>>getSalon() throws Exception {
        List<Salon> salons=salonService.getAllsalons();

        List<SalonDTO>salonDTOS=salons.stream().map((salon)->{
            SalonDTO salonDTO=SalonMapper.mapToDTO(salon);
            return salonDTO;
        }).toList();
    return ResponseEntity.ok(salonDTOS);
    }

    //http://localhost:5002/api/salons/5
    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDTO>getSalonById(
            @PathVariable Long salonId
    ) throws Exception {
        List<Salon> salons=salonService.getAllsalons();

       Salon salon = salonService.getSalonById(salonId);
       SalonDTO salonDTO=SalonMapper.mapToDTO(salon);
       return ResponseEntity.ok(salonDTO);

    }

    //search salon
    //http://localhost:5002/api/salons/search?city=mumbai
    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>>searchSalons(
            @RequestParam("city") String city
    ) throws Exception {
        List<Salon> salons=salonService.searchSalonsByCity(city);

        List<SalonDTO>salonDTOS=salons.stream().map((salon)->{
            SalonDTO salonDTO=SalonMapper.mapToDTO(salon);
            return salonDTO;
        }).toList();
        return ResponseEntity.ok(salonDTOS);
    }


    //http://localhost:5002/api/salons/5
    @GetMapping("/owner")
    public ResponseEntity<SalonDTO>getSalonByOwnerId(
            @PathVariable Long salonId
    ) throws Exception {
        UserDTO userDTO=new UserDTO();
        userDTO.setId(1L);
        Salon salon = salonService.getSalonById(userDTO.getId());
        SalonDTO salonDTO=SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO);

    }


}
