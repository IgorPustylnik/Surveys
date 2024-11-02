package ru.vsu.cs.pustylnik_i_v.surveys.services.impl;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.*;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

import java.util.Calendar;
import java.util.List;

public class SurveysServiceImpl implements SurveysService {

    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final SessionRepository sessionRepository;

    public SurveysServiceImpl(UserRepository userRepository,
                              SurveyRepository surveyRepository,
                              QuestionRepository questionRepository,
                              OptionRepository optionRepository,
                              AnswerRepository answerRepository,
                              CategoryRepository categoryRepository,
                              SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.answerRepository = answerRepository;
        this.categoryRepository = categoryRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public ServiceResponse<PagedEntity<List<Survey>>> getSurveysPagedList(Integer categoryId, Integer page, Integer perPageAmount) throws DatabaseAccessException {
        PagedEntity<List<Survey>> surveysPagedEntity;
        surveysPagedEntity = surveyRepository.getSurveysPagedEntity(categoryId, page, perPageAmount);

        if (surveysPagedEntity.page().isEmpty()) {
            return new ServiceResponse<>(false, "Nothing found", surveysPagedEntity);
        }
        return new ServiceResponse<>(true, "Surveys successfully found", surveysPagedEntity);
    }

    @Override
    public ServiceResponse<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer index) throws DatabaseAccessException {
        List<Question> questions = questionRepository.getQuestions(surveyId);

        if (index >= questions.size()) {
            return new ServiceResponse<>(false, "Survey completed successfully", null);
        }
        Question question = questions.get(index);
        return new ServiceResponse<>(true, "Question successfully found", new PagedEntity<>(index, questions.size(), question));
    }

    @Override
    public ServiceResponse<List<Option>> getQuestionOptionList(Integer questionId) throws DatabaseAccessException {
        List<Option> options = optionRepository.getOptions(questionId);

        if (options.isEmpty()) {
            return new ServiceResponse<>(true, "No options found", null);
        }
        return new ServiceResponse<>(true, "Options found successfully", options);
    }

    @Override
    public ServiceResponse<PagedEntity<List<Category>>> getCategoriesPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException {
        List<Category> sliced;
        List<Category> surveys = categoryRepository.getAllCategories();

        int totalPages = (int) Math.ceil((double) surveys.size() / perPageAmount);

        int fromIndex = perPageAmount * page;
        int toIndex = Math.min(fromIndex + perPageAmount, surveys.size());

        sliced = surveys.subList(fromIndex, toIndex);

        if (totalPages < 1) totalPages = 1;

        if (sliced.isEmpty()) {
            return new ServiceResponse<>(false, "No categories found", new PagedEntity<>(page, totalPages, sliced));
        }

        return new ServiceResponse<>(true, "Categories successfully found", new PagedEntity<>(page, totalPages, sliced));
    }

    @Override
    public ServiceResponse<?> submitAnswer(Integer sessionId, Integer optionId) throws DatabaseAccessException {
        try {
            answerRepository.addAnswer(sessionId, optionId);
        } catch (SessionNotFoundException e) {
            return new ServiceResponse<>(false, "Session doesn't exist", null);
        } catch (OptionNotFoundException e) {
            return new ServiceResponse<>(false, "Option doesn't exist", null);
        }
        return new ServiceResponse<>(true, "Answer submitted successfully", null);
    }

    @Override
    public ServiceResponse<?> addQuestionToSurvey(Integer surveyId, String description, List<String> options, QuestionType questionType) throws DatabaseAccessException {
        questionRepository.addQuestion(surveyId, description, questionType);

        List<Question> questions = questionRepository.getQuestions(surveyId);
        if (questions.isEmpty()) {
            return new ServiceResponse<>(false, "Error (couldn't create a question)", null);
        }
        Question question = questions.get(questions.size() - 1);

        for (String option : options) {
            optionRepository.addOption(question.getId(), option);
        }

        return new ServiceResponse<>(true, "Question created successfully", null);
    }

    @Override
    public ServiceResponse<?> deleteCategory(Integer categoryId) throws DatabaseAccessException {
        try {
            categoryRepository.deleteCategory(categoryId);
        } catch (CategoryNotFoundException e) {
            return new ServiceResponse<>(false, "Category to delete was not found", null);
        }
        return new ServiceResponse<>(true, "Category deleted successfully", null);
    }

    @Override
    public ServiceResponse<Survey> getSurvey(Integer surveyId) throws DatabaseAccessException {
        try {
            Survey survey = surveyRepository.getSurveyById(surveyId);
            return new ServiceResponse<>(true, "Successfully found survey", survey);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey not found", null);
        }
    }

    @Override
    public ServiceResponse<?> deleteSurvey(Integer surveyId) throws DatabaseAccessException {
        try {
            surveyRepository.deleteSurvey(surveyId);
            return new ServiceResponse<>(true, "Survey deleted successfully", null);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey doesn't exist", null);
        }
    }

    @Override
    public ServiceResponse<Survey> addSurveyAndGetSelf(String name, String description, String categoryName, Integer authorId) throws DatabaseAccessException {
        if (!categoryRepository.exists(categoryName)) {
            categoryRepository.addCategory(categoryName);
        }

        Integer categoryId = categoryRepository.getCategoryByName(categoryName).getId();

        String authorName;
        try {
            authorName = userRepository.getUser(authorId).getName();
        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "User doesn't exist", null);
        }
        Survey newSurvey = surveyRepository.addSurvey(name, description, categoryId, authorName, Calendar.getInstance().getTime());

        return new ServiceResponse<>(true, "Survey created successfully", newSurvey);
    }

    @Override
    public ServiceResponse<?> setSurveyCategory(Integer surveyId, String categoryName) throws DatabaseAccessException {
        if (!categoryRepository.exists(categoryName)) {
            categoryRepository.addCategory(categoryName);
        }

        Integer categoryId = categoryRepository.getCategoryByName(categoryName).getId();
        try {
            surveyRepository.updateSurveyCategoryName(surveyId, categoryId);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey doesn't exist", null);
        }

        return new ServiceResponse<>(true, "Category set successfully", null);
    }

    @Override
    public ServiceResponse<Integer> startSessionAndGetId(String userName, Integer surveyId) throws DatabaseAccessException {
        Integer userId = null;

        try {
            userId = userRepository.getUser(userName).getId();
        } catch (UserNotFoundException ignored) {
        }

        try {
            surveyRepository.getSurveyById(surveyId);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey doesn't exist", null);
        }

        Integer sessionId = sessionRepository.addSessionAndGetId(surveyId, userId, Calendar.getInstance().getTime(), null);
        return new ServiceResponse<>(true, "Successfully created a session", sessionId);
    }

    @Override
    public ServiceResponse<?> finishSession(Integer sessionId) throws DatabaseAccessException {
        try {
            Session session = sessionRepository.getSessionById(sessionId);
            session.setFinishedAt(Calendar.getInstance().getTime());
            sessionRepository.updateSession(session);
            return new ServiceResponse<>(true, "Session finished successfully", null);
        } catch (SessionNotFoundException e) {
            return new ServiceResponse<>(false, "Session doesn't exist", null);
        }
    }
}
