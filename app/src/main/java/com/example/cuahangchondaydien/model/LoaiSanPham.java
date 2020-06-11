package com.example.cuahangchondaydien.model;

public class LoaiSanPham {
    public int id;
    public String tenloaisanpham;
    public String hinhloaisanpham;

    public LoaiSanPham(int id, String tenloaisanpham, String hinhloaisanpham) {
        this.id = id;
        this.tenloaisanpham = tenloaisanpham;
        this.hinhloaisanpham = hinhloaisanpham;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenloaisanpham() {
        return tenloaisanpham;
    }

    public void setTenloaisanpham(String tenloaisanpham) {
        this.tenloaisanpham = tenloaisanpham;
    }

    public String getHinhloaisanpham() {
        return hinhloaisanpham;
    }

    public void setHinhloaisanpham(String hinhloaisanpham) {
        this.hinhloaisanpham = hinhloaisanpham;
    }
}
