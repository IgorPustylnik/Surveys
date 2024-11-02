package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

public interface TokenValidationService {
    ServiceResponse<RoleType> getRoleFromToken(String token);
}
