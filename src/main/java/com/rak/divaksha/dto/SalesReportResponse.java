package com.rak.divaksha.dto;

import java.util.List;

public class SalesReportResponse {
    private Summary summary;
    private List<SaleRow> data;

    public static class Summary {
        private double totalSales;
        private long totalOrders;

        // getters and setters
        public double getTotalSales() { return totalSales; }
        public void setTotalSales(double totalSales) { this.totalSales = totalSales; }
        public long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    }

    public static class SaleRow {
        private String orderId;
        private double amount;
        private String userId;
        private String date;

        // getters & setters
        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }

    // getters & setters
    public Summary getSummary() { return summary; }
    public void setSummary(Summary summary) { this.summary = summary; }

    public List<SaleRow> getData() { return data; }
    public void setData(List<SaleRow> data) { this.data = data; }
}
