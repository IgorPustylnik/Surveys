package ru.vsu.cs.pustylnik_i_v.surveys.services.entities;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

public record AuthBody(RoleType roleType, String token) {
}
