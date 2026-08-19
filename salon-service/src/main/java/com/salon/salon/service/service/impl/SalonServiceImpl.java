package com.salon.salon.service.service.impl;

import com.salon.salon.service.model.Salon;
import com.salon.salon.service.payload.dto.SalonDTO;
import com.salon.salon.service.payload.dto.UserDTO;
import com.salon.salon.service.repository.SalonRepository;
import com.salon.salon.service.service.SalonService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    @Override
    public Salon createSalon(SalonDTO req, UserDTO user) {
        Salon salon=new Salon();
        salon.setName(req.getName());
        salon.setAddress(req.getAddress());
        salon.setEmail(req.getEmail());
        salon.setCity(req.getCity());
        salon.setImages(req.getImages());
        salon.setOwnerId(user.getId());
        salon.setOpenTime(req.getOpenTime());
        salon.setCloseTime(req.getCloseTime());
        salon.setPhoneNumber(req.getPhoneNumber());

        return salonRepository.save(salon);
    }

    @Override
    public Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) throws Exception {
        Salon existingSalon = salonRepository.findById(salonId).orElse(null);

        if (!salon.getOwnerId().equals(user.getId())) {
            throw new Exception("you dont have permission to update the salon");
        }
        if (existingSalon != null ) {
existingSalon.setCity(salon.getCity());
existingSalon.setName(salon.getName());
existingSalon.setAddress(salon.getAddress());
existingSalon.setEmail(salon.getEmail());
existingSalon.setImages(salon.getImages());
existingSalon.setOpenTime(salon.getOpenTime());
existingSalon.setCloseTime(salon.getCloseTime());
existingSalon.setOwnerId(user.getId());
existingSalon.setPhoneNumber(salon.getPhoneNumber());
      return salonRepository.save(existingSalon);
        }
        throw new Exception("salon not found");
    }

    @Override
    public List<Salon> getAllsalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long salonId) throws Exception {
        Salon salon= salonRepository.findById(salonId).orElse(null);
    if(salon==null){
      throw new Exception("Salon not found");
    }
    return salon;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
       return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonsByCity(String city) {
       return salonRepository.searchSalons(city);
    }
}
