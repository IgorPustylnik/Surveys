package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

public interface TokenValidationService {
    ResponseEntity<RoleType> getRoleFromToken(String token);
}
