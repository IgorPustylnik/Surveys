package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support;

public enum CommandType {
    MAIN_MENU,

    ANONYMOUS_MENU,
    USER_MENU,
    ADMIN_MENU,

    LOGIN,
    REGISTER,
    CHANGE_PASSWORD,
    LOGOUT,

    LIST_SURVEYS,
    OPEN_SURVEY,
    START_SURVEY,
    PREVIOUS_PAGE,
    NEXT_PAGE,

    CREATE_SURVEY,
    EDIT_SURVEY,
    DELETE_SURVEY,
    ASSIGN_ADMIN,

    UNKNOWN
}
