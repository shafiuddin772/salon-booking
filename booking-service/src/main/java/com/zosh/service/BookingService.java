package com.zosh.service;

import com.zosh.domain.BookingStatus;
import com.zosh.dto.BookingRequest;
import com.zosh.dto.SalonDTO;
import com.zosh.dto.ServiceDTO;
import com.zosh.dto.UserDTO;
import com.zosh.modal.Booking;
import com.zosh.modal.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking createBooking(BookingRequest booking,
                          UserDTO userDTO,
                          SalonDTO salon,
                          Set<ServiceDTO>serviceDTOSet) throws Exception;
    List<Booking>getBookingsByCustomer(Long customerId);
    List<Booking>getBookingsBySalon(Long salonId);
    Booking getBookingById(Long id);
    Booking updateBooking(Long bookingId, BookingStatus status);
    List<Booking>getBookingByDate(LocalDate date,Long salonId);
    SalonReport getSalonReport(Long salonId);
}
