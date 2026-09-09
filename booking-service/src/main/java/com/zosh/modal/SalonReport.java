package com.zosh.modal;

import lombok.Data;

@Data
public class SalonReport {
private Long SalonId;
private String salonName;
private int totalEarnings;
private Integer totalBookings;
private Integer cancelledBooking;
private Double totalRefund;
}
