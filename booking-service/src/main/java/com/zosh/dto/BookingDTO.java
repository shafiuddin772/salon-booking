package com.zosh.dto;

import com.zosh.domain.BookingStatus;
import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {
    private Long id;

    private Long  salonId;

    private Long customId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    private Set<Long> serviceIds;

    public BookingStatus status=BookingStatus.PENDING;


}
