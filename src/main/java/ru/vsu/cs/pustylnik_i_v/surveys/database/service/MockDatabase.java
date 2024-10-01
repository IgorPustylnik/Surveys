package ru.vsu.cs.pustylnik_i_v.surveys.database.service;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.*;

import java.util.Date;
import java.util.List;

public class MockDatabase implements DatabaseService {

    private static DatabaseService INSTANCE;

    public static DatabaseService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDatabase();
        }
        return INSTANCE;
    }

    private final UserRepository userRepository = new UserRepositoryMock();
    private final AdminRepository adminRepository = new AdminRepositoryMock();
    private final CategoryRepository categoryRepository = new CategoryRepositoryMock();
    private final SurveyRepository surveyRepository = new SurveyRepositoryMock();
    private final QuestionRepository questionRepository = new QuestionRepositoryMock();
    private final OptionRepository optionRepository = new OptionRepositoryMock();
    private final SessionRepository sessionRepository = new SessionRepositoryMock();
    private final AnswerRepository answerRepository = new AnswerRepositoryMock();

    /// Create

    @Override
    public void addAdmin(int userId, String email) throws UserNotFoundException {
        if (!userRepository.exists(userId)) {
            throw new UserNotFoundException(userId);
        }
        adminRepository.addAdmin(userId, email);
    }

    @Override
    public void addAnswer(int sessionId, int optionId) throws SessionNotFoundException, OptionNotFoundException {
        if (!sessionRepository.exists(sessionId)) {
            throw new SessionNotFoundException(sessionId);
        }
        if (!optionRepository.exists(optionId)) {
            throw new OptionNotFoundException(optionId);
        }
        answerRepository.addAnswer(sessionId, optionId);
    }

    @Override
    public void addCategory(String name) {
        categoryRepository.addCategory(name);
    }

    @Override
    public void addOption(int questionId, String description) throws QuestionNotFoundException {
        if (!questionRepository.exists(questionId)) {
            throw new QuestionNotFoundException(questionId);
        }
        optionRepository.addOption(questionId, description);
    }

    @Override
    public void addQuestion(int surveyId, String text, QuestionType questionType) throws SurveyNotFoundException {
        if (!surveyRepository.exists(surveyId)) {
            throw new SurveyNotFoundException(surveyId);
        }
        questionRepository.addQuestion(surveyId, text, questionType);
    }

    @Override
    public void addSession(int surveyId, Integer userId, Date startedAt, Date finishedAt) throws SurveyNotFoundException, UserNotFoundException {
        if (!surveyRepository.exists(surveyId)) {
            throw new SurveyNotFoundException(surveyId);
        }
        if (userId != null && !userRepository.exists(userId)) {
            throw new UserNotFoundException(userId);
        }
        sessionRepository.addSession(surveyId, userId, startedAt, finishedAt);
    }

    @Override
    public void addSurvey(String name, String description, Integer categoryId,  Date createdAt) {
        surveyRepository.addSurvey(name, description, categoryId, createdAt);
    }

    @Override
    public void addUser(String name, String password) {
        userRepository.addUser(name, password);
    }

    /// Read

    @Override
    public Admin getAdmin(int userId) {
        return adminRepository.getAdminByUserId(userId);
    }

    @Override
    public Answer getAnswer(int questionId, int optionId) {
        return answerRepository.getAnswerBySelf(new Answer(questionId, optionId));
    }

    @Override
    public Category getCategoryById(int categoryId) {
        return categoryRepository.getCategoryById(categoryId);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.getCategoryByName(name);
    }

    @Override
    public Option getOption(int optionId) {
        return optionRepository.getOptionById(optionId);
    }

    @Override
    public Question getQuestion(int questionId) {
        return questionRepository.getQuestionById(questionId);
    }

    @Override
    public List<Question> getQuestions(Integer surveyId) {
        return questionRepository.getQuestions(surveyId);
    }

    @Override
    public Session getSession(int sessionId) {
        return sessionRepository.getSessionById(sessionId);
    }

    @Override
    public Survey getSurvey(int surveyId) {
        return surveyRepository.getSurveyById(surveyId);
    }

    @Override
    public List<Survey> getSurveys(Integer categoryId) {
        return surveyRepository.getSurveys(categoryId);
    }

    @Override
    public User getUser(String name) {
        return userRepository.getUser(name);
    }

    /// Update

    @Override
    public void updateAdmin(Admin admin) {
        adminRepository.updateAdmin(admin);
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.updateCategory(category);
    }

    @Override
    public void updateOption(Option option) {
        optionRepository.updateOption(option);
    }

    @Override
    public void updateQuestion(Question question) {
        questionRepository.updateQuestion(question);
    }

    @Override
    public void updateSession(Session session) {
        sessionRepository.updateSession(session);
    }

    @Override
    public void updateSurvey(Survey survey) {
        surveyRepository.updateSurvey(survey);
    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    /// Delete

    @Override
    public void deleteAdmin(int userId) {
        adminRepository.deleteAdmin(userId);
    }

    @Override
    public void deleteAnswer(int questionId, int optionId) {
        answerRepository.deleteAnswer(questionId, optionId);
    }

    @Override
    public void deleteCategory(int categoryId) {
        categoryRepository.deleteCategory(categoryId);
    }

    @Override
    public void deleteOption(int optionId) {
        optionRepository.deleteOption(optionId);
    }

    @Override
    public void deleteQuestion(int questionId) {
        questionRepository.deleteQuestion(questionId);
    }

    @Override
    public void deleteSession(int sessionId) {
        sessionRepository.deleteSession(sessionId);
    }

    @Override
    public void deleteSurvey(int surveyId) {
        surveyRepository.deleteSurvey(surveyId);
    }

    @Override
    public void deleteUser(String name) {
        userRepository.deleteUser(name);
    }
}
