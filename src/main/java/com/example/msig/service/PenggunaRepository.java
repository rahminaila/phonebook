package com.example.msig.service;

import com.example.msig.model.PenggunaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PenggunaRepository extends JpaRepository<PenggunaEntity, Integer> {

    @Query(value = "select id_pengguna id, nama_pengguna Nama, nomor_telepon NomorTelepon from buku_telepon.pengguna where nama_pengguna like :nama_pengguna and is_active = true",nativeQuery = true)
    List<Object[]> findByNama(@Param("nama_pengguna") String nama_pengguna);

    boolean existsByNomorTelepon(String nomorTelepon);

    @Query(value = "select COUNT(*) from buku_telepon.pengguna where nomor_telepon = :nomor_telepon",nativeQuery = true)
    Integer findNoTelp(@Param("nomor_telepon") String nomorTelepon);

}
