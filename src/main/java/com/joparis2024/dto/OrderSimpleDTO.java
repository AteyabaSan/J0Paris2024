package com.joparis2024.dto;

public class OrderSimpleDTO {
    private Long id;
    private String status;
    private Double totalAmount;

    // Constructeurs, getters et setters
    public OrderSimpleDTO() {}

    public OrderSimpleDTO(Long id, String status, Double totalAmount) {
        this.id = id;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
