package ru.vsu.cs.pustylnik_i_v.surveys.services;

public interface CryptoService {
    String encrypt(Object object) throws Exception;
    Object decrypt(String token) throws Exception;
}
