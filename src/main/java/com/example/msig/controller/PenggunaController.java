package com.example.msig.controller;

import com.example.msig.model.ApiResponse;
import com.example.msig.model.PenggunaDto;
import com.example.msig.service.PenggunaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.msig.model.PenggunaEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pengguna")

public class PenggunaController {
    @Autowired
    private PenggunaRepository penggunaRepository;

    @GetMapping("/penggunaAll")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<PenggunaEntity>>> getSeluruhPengguna() {
        try {
            List<PenggunaEntity> pengguna = penggunaRepository.findAll();
            ApiResponse<List<PenggunaEntity>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "sukses",
                    pengguna
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<PenggunaEntity>> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "error",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{namaPengguna}")
    public ResponseEntity<ApiResponse<List<PenggunaDto>>> getPengguna(@PathVariable String namaPengguna) {
        try {
            List<Object[]> pengguna = penggunaRepository.findByNama("%" + namaPengguna + "%");

            List<PenggunaDto> dtos = new ArrayList<>();
            for (Object[] obj : pengguna) {
                PenggunaDto dto = new PenggunaDto(
                        (Integer) obj[0],
                        (String) obj[1],
                        (String) obj[2]
                );
                dtos.add(dto);
            }
            ApiResponse<List<PenggunaDto>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "sukses",
                    dtos
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<PenggunaDto>> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Terjadi kesalahan saat mengambil data pengguna: " + e.getMessage(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addPengguna")
    public ResponseEntity<ApiResponse<PenggunaEntity>> addPengguna (@RequestBody PenggunaEntity pengguna) {
        try {

            Integer findnotelp = penggunaRepository.findNoTelp(pengguna.getNomorTelepon());
            System.out.println(pengguna.getNomorTelepon() + "notelp");
            System.out.println(findnotelp +"findnotelp");
            if (findnotelp > 0) {
                ApiResponse<PenggunaEntity> response = new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Nomor Telepon sudah terdaftar",
                        null
                );
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            pengguna.setActive(true);
            PenggunaEntity savedPengguna = penggunaRepository.save(pengguna);
            ApiResponse<PenggunaEntity> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Pengguna berhasil ditambahkan",
                    savedPengguna
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<PenggunaEntity> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Terjadi kesalahan saat menambahkan pengguna",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updatePengguna")
    public ResponseEntity<ApiResponse<PenggunaEntity>> updatePengguna(@RequestBody PenggunaEntity updatedPengguna) {
        try {
            Optional<PenggunaEntity> optionalPengguna= penggunaRepository.findById(updatedPengguna.getIdPengguna());
            if (!optionalPengguna.isPresent()) {
                ApiResponse<PenggunaEntity> response = new ApiResponse<>(
                        HttpStatus.NOT_FOUND.value(),
                        "Pengguna tidak ada di database",
                        null
                );
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            PenggunaEntity pengguna = optionalPengguna.get();

            pengguna.setNamaPengguna(updatedPengguna.getNamaPengguna());
            pengguna.setNomorTelepon(updatedPengguna.getNomorTelepon());
            pengguna.setUpdatedDate(new Date());

            PenggunaEntity savedPengguna = penggunaRepository.save(pengguna);

            ApiResponse<PenggunaEntity> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Pengguna berhasil diperbaharui",
                    savedPengguna
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<PenggunaEntity> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Terjadi kesalahan saat memperbarui pengguna",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/softDeletePengguna")
    public ResponseEntity<ApiResponse<PenggunaEntity>> softDeletePengguna(@RequestBody PenggunaEntity updatedPengguna) {
        try {
            Optional<PenggunaEntity> optionalPengguna= penggunaRepository.findById(updatedPengguna.getIdPengguna());
            if (!optionalPengguna.isPresent()) {
                ApiResponse<PenggunaEntity> response = new ApiResponse<>(
                        HttpStatus.NOT_FOUND.value(),
                        "Pengguna tidak ada di database",
                        null
                );
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            PenggunaEntity pengguna = optionalPengguna.get();

            pengguna.setActive(false);
            pengguna.setUpdatedDate(new Date());

            PenggunaEntity savedPengguna = penggunaRepository.save(pengguna);

            ApiResponse<PenggunaEntity> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Pengguna berhasil dihapus",
                    savedPengguna
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<PenggunaEntity> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Terjadi kesalahan saat menghapus pengguna",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/hardDeletePengguna")
    public ResponseEntity<ApiResponse<Void>> deletePengguna(@RequestBody PenggunaEntity pengguna) {
        try {
            Optional<PenggunaEntity> optionalPengguna = penggunaRepository.findById(pengguna.getIdPengguna());
            if (!optionalPengguna.isPresent()) {
                ApiResponse<Void> response = new ApiResponse<>(
                        HttpStatus.NOT_FOUND.value(),
                        "Pengguna tidak ditemukan",
                        null
                );
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            penggunaRepository.deleteById(pengguna.getIdPengguna());

            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Pengguna berhasil dihapus",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Terjadi kesalahan saat menghapus pengguna",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
