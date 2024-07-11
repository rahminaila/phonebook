package com.example.msig.model;

public class PenggunaDto {
    private Integer idPengguna;
    private String namaPengguna;
    private String nomorTelepon;

    public PenggunaDto(Integer idPengguna, String namaPengguna, String nomorTelepon) {
        this.idPengguna = idPengguna;
        this.namaPengguna = namaPengguna;
        this.nomorTelepon = nomorTelepon;
    }

    public Integer getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(Integer idPengguna) {
        this.idPengguna = idPengguna;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }

    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }
}
