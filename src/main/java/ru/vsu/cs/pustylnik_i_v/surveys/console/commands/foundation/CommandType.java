package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation;

public enum CommandType {
    MAIN_MENU,

    LOGIN,
    REGISTER,
    CHANGE_PASSWORD,
    LOGOUT,

    LIST_SURVEYS,
    OPEN_SURVEY,
    OPEN_QUESTION,
    SUBMIT_ANSWER,
    PREVIOUS_SURVEYS_PAGE,
    NEXT_SURVEYS_PAGE,

    CREATE_SURVEY,
    ADD_QUESTION,
    EDIT_SURVEY,
    SET_SURVEY_CATEGORY,
    DELETE_SURVEY,

    LIST_CATEGORIES,
    PREVIOUS_CATEGORIES_PAGE,
    NEXT_CATEGORIES_PAGE,
    OPEN_CATEGORY,
    CHOOSE_CATEGORY,
    UNCHOOSE_CATEGORY,
    DELETE_CATEGORY,

    LIST_USERS,
    OPEN_USER,
    PREVIOUS_USERS_PAGE,
    NEXT_USERS_PAGE,
    UPDATE_USER_ROLE,
    DELETE_USER,

    UNKNOWN
}
