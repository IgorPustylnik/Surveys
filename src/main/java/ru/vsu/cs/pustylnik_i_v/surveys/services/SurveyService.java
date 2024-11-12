package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.*;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.*;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SurveyService {

    private final UserDAO userDAO;
    private final SurveyDAO surveyDAO;
    private final QuestionDAO questionDAO;
    private final AnswerDAO answerDAO;
    private final CategoryDAO categoryDAO;
    private final SessionDAO sessionDAO;

    public SurveyService(UserDAO userDAO,
                         SurveyDAO surveyDAO,
                         QuestionDAO questionDAO,
                         AnswerDAO answerDAO,
                         CategoryDAO categoryDAO,
                         SessionDAO sessionDAO) {
        this.userDAO = userDAO;
        this.surveyDAO = surveyDAO;
        this.questionDAO = questionDAO;
        this.answerDAO = answerDAO;
        this.categoryDAO = categoryDAO;
        this.sessionDAO = sessionDAO;
    }

    public ServiceResponse<PagedEntity<List<Survey>>> getSurveysPagedList(String authorName, Integer categoryId, Date fromDate, Date toDate, Integer page, Integer perPageAmount) throws DatabaseAccessException {
        PagedEntity<List<Survey>> surveysPagedEntity;

        fromDate = fromDate == null ? null : DateUtil.setTimeToStartOfDay(fromDate);
        toDate = toDate == null ? null : DateUtil.setTimeToEndOfDay(toDate);

        surveysPagedEntity = surveyDAO.getSurveysPagedEntity(authorName, categoryId, fromDate, toDate, page, perPageAmount);

        if (surveysPagedEntity.page().isEmpty()) {
            return new ServiceResponse<>(false, "Nothing found", surveysPagedEntity);
        }
        return new ServiceResponse<>(true, "Surveys successfully found", surveysPagedEntity);
    }

    public ServiceResponse<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer index) throws DatabaseAccessException {
        try {
            List<Question> questions = questionDAO.getQuestions(surveyId);

            if (index >= questions.size()) {
                return new ServiceResponse<>(false, "Survey completed successfully", null);
            }
            Question question = questions.get(index);

            return new ServiceResponse<>(true, "Question successfully found", new PagedEntity<>(index, questions.size(), question));
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey not found", null);
        }
    }

    public ServiceResponse<List<Question>> getQuestions(Integer surveyId) throws DatabaseAccessException {
        List<Question> questions;
        try {
            questions = questionDAO.getQuestions(surveyId);
            return new ServiceResponse<>(true, "Questions successfully found", questions);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey not found", null);
        }
    }

    public ServiceResponse<PagedEntity<List<Category>>> getCategoriesPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException {
        List<Category> sliced;
        List<Category> categories = categoryDAO.getAllCategories();

        int totalPages = (int) Math.ceil((double) categories.size() / perPageAmount);

        int fromIndex = perPageAmount * page;
        int toIndex = Math.min(fromIndex + perPageAmount, categories.size());

        sliced = categories.subList(fromIndex, toIndex);

        if (totalPages < 1) totalPages = 1;

        if (sliced.isEmpty()) {
            return new ServiceResponse<>(false, "No categories found", new PagedEntity<>(page, totalPages, sliced));
        }

        return new ServiceResponse<>(true, "Categories successfully found", new PagedEntity<>(page, totalPages, sliced));
    }

    public ServiceResponse<?> addQuestionToSurvey(Integer surveyId, String description, List<String> options, QuestionType questionType) throws DatabaseAccessException {
        try {
            questionDAO.addQuestion(surveyId, description, questionType, options);

            List<Question> questions = questionDAO.getQuestions(surveyId);
            if (questions.isEmpty()) {
                return new ServiceResponse<>(false, "Error (couldn't create a question)", null);
            }

        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(true, "Survey doesn't exist", null);
        }

        return new ServiceResponse<>(true, "Question created successfully", null);
    }

    public ServiceResponse<?> deleteCategory(Integer categoryId) throws DatabaseAccessException {
        try {
            categoryDAO.deleteCategory(categoryId);
        } catch (CategoryNotFoundException e) {
            return new ServiceResponse<>(false, "Category to delete was not found", null);
        }
        return new ServiceResponse<>(true, "Category deleted successfully", null);
    }

    public ServiceResponse<Survey> getSurvey(Integer surveyId) throws DatabaseAccessException {
        try {
            Survey survey = surveyDAO.getSurveyById(surveyId);
            return new ServiceResponse<>(true, "Successfully found survey", survey);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey not found", null);
        }
    }

    public ServiceResponse<?> deleteSurvey(Integer surveyId) throws DatabaseAccessException {
        surveyDAO.deleteSurvey(surveyId);
        return new ServiceResponse<>(true, "Survey deleted successfully", null);
    }

    public ServiceResponse<Survey> addSurveyAndGetSelf(String name, String description, String categoryName, Integer authorId) throws DatabaseAccessException {
        if (categoryName != null && !categoryDAO.exists(categoryName)) {
            categoryDAO.addCategory(categoryName);
        }

        Integer categoryId = null;
        if (categoryName != null) {
            categoryId = categoryDAO.getCategoryByName(categoryName).getId();
        }

        String authorName;
        try {
            authorName = userDAO.getUser(authorId).getName();
        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "User doesn't exist", null);
        }
        Survey newSurvey = surveyDAO.addSurvey(name, description, categoryId, authorName, Calendar.getInstance().getTime());

        if (newSurvey == null) {
            return new ServiceResponse<>(false, "Couldn't create new survey", null);
        }

        return new ServiceResponse<>(true, "Survey created successfully", newSurvey);
    }

    public ServiceResponse<?> setSurveyCategory(Integer surveyId, String categoryName) throws DatabaseAccessException {
        if (!categoryDAO.exists(categoryName)) {
            categoryDAO.addCategory(categoryName);
        }

        Integer categoryId = categoryDAO.getCategoryByName(categoryName).getId();
        try {
            surveyDAO.updateSurveyCategory(surveyId, categoryId);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey doesn't exist", null);
        }

        return new ServiceResponse<>(true, "Category set successfully", null);
    }
}
