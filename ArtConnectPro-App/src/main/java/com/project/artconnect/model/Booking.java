package com.project.artconnect.model;

import java.time.LocalDateTime;

public class Booking {
    private int workshopId;
    private int memberId;
    private LocalDateTime bookingDate;
    private String paymentStatus; // PENDING, PAID, CANCELLED

    public Booking() {  // -> to not use ?
    }

    public Booking(int workshopId, int memberId) {
        this.workshopId = workshopId;
        this.memberId = memberId;
        this.bookingDate = LocalDateTime.now();
        this.paymentStatus = "PENDING";
    }

    public int getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(int workshopId) {
        this.workshopId = workshopId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
