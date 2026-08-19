package com.salon.salon.service.service;

import com.salon.salon.service.model.Salon;
import com.salon.salon.service.payload.dto.SalonDTO;
import com.salon.salon.service.payload.dto.UserDTO;

import java.util.List;

public interface SalonService {
Salon createSalon(SalonDTO salon, UserDTO user);
Salon updateSalon(SalonDTO salon,UserDTO user,Long salonId) throws Exception;
List<Salon>getAllsalons();
Salon getSalonById(Long salonId) throws Exception;
Salon getSalonByOwnerId(Long ownerId);
List<Salon>searchSalonsByCity(String city);
}
