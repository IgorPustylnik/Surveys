package ru.vsu.cs.pustylnik_i_v.surveys.database;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.*;
import ru.vsu.cs.pustylnik_i_v.surveys.enums.*;

import java.util.Date;

public interface DatabaseService {

    /// Create

    void addAdmin(String email);

    void addAnswer(int questionId, int optionId);

    void addCategory(String name);

    void addOption(int questionId, String description);

    void addQuestion(int surveyId, String text, QuestionType questionType);

    void addSession(int surveyId, Integer userId, Date startedAt, Date finishedAt);

    void addSurvey(String name, String description, Date createdAt);

    void addUser(String name, String password);

    /// Read

    Admin getAdmin(int userId);

    Answer getAnswer(int questionId, int optionId);

    Category getCategory(int categoryId);

    Option getOption(int optionId);

    Question getQuestion(int questionId);

    Session getSession(int sessionId);

    Survey getSurvey(int surveyId);

    User getUser(int userId);

    /// Update

    void updateAdmin(Admin admin);

    void updateAnswer(Answer answer);

    void updateCategory(Category category);

    void updateOption(Option option);

    void updateQuestion(Question question);

    void updateSession(Session session);

    void updateSurvey(Survey survey);

    void updateUser(User user);

    /// Delete

    void deleteAdmin(int userId);

    void deleteAnswer(int questionId, int optionId);

    void deleteCategory(int categoryId);

    void deleteOption(int optionId);

    void deleteQuestion(int questionId);

    void deleteSession(int sessionId);

    void deleteSurvey(int surveyId);

    void deleteUser(int userId);

}
