package ru.vsu.cs.pustylnik_i_v.surveys.services;

public interface TokenService {
    public String encrypt(Object object) throws Exception;
    public Object decrypt(String token) throws Exception;
}
