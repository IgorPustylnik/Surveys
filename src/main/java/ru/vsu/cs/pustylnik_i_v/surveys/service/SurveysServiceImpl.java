package ru.vsu.cs.pustylnik_i_v.surveys.service;

import ru.vsu.cs.pustylnik_i_v.surveys.console.roles.Role;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.service.DatabaseService;
import ru.vsu.cs.pustylnik_i_v.surveys.database.service.MockDatabase;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.ResponseEntity;

import java.util.Calendar;
import java.util.List;

public class SurveysServiceImpl implements SurveysService {

    private static final int maxPageElementsAmount = 7;

    private static SurveysService INSTANCE;
    private static final DatabaseService database = MockDatabase.getInstance();

    @Override
    public ResponseEntity<AuthBody> login(String name, String password) {
        User user = database.getUser(name);
        Role role = isUserAdmin(name).isSuccess() ? Role.ADMIN : Role.USER;

        if (user == null) {
            return new ResponseEntity<>(false, "No such user", new AuthBody(role, null));
        }
        if (!user.getPassword().equals(password)) {
            return new ResponseEntity<>(false, "Wrong password", new AuthBody(role, null));
        }

        // TODO: Generate a real token
        String token = String.valueOf(database.getUser(name).getId());
        return new ResponseEntity<>(true, "Login successful", new AuthBody(role, token));
    }

    @Override
    public ResponseEntity<AuthBody> register(String name, String password) {
        User user = database.getUser(name);
        Role role = isUserAdmin(name).isSuccess() ? Role.ADMIN : Role.USER;

        if (user != null) {
            return new ResponseEntity<>(false, "Username exists", new AuthBody(role, null));
        }
        database.addUser(name, password);

        // TODO: Generate a real token
        String token = String.valueOf(database.getUser(name).getId());
        return new ResponseEntity<>(true, "Registration successful", new AuthBody(role, token));
    }

    @Override
    public ResponseEntity<?> checkIfPasswordIsCorrect(String name, String password) {
        User user = database.getUser(name);
        if (user == null) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
        if (user.getPassword().equals(password)) {
            return new ResponseEntity<>(true, "Password is correct", null);
        }
        return new ResponseEntity<>(false, "Password is incorrect", null);
    }

    @Override
    public ResponseEntity<?> updatePassword(String name, String newPassword) {
        User user = database.getUser(name);
        if (user == null) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
        user.setPassword(newPassword);
        database.updateUser(user);
        return new ResponseEntity<>(true, "Password changed", null);
    }

    @Override
    public ResponseEntity<?> addAdmin(String userName, String email) {
        User user = database.getUser(userName);
        if (user == null) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
        int id = user.getId();
        try {
            database.addAdmin(id, email);
            return new ResponseEntity<>(true, "Admin added successfully", null);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
    }

    @Override
    public ResponseEntity<PagedEntity<List<Survey>>> getSurveysPagedList(Integer categoryId, Integer page) {
        List<Survey> sliced;

        List<Survey> surveys = database.getSurveys(categoryId);
        int totalPages = (int) Math.ceil((double) surveys.size() / 7);

        int fromIndex = maxPageElementsAmount * (page - 1);
        int toIndex = Math.min(fromIndex + maxPageElementsAmount, surveys.size());

        sliced = surveys.subList(fromIndex, toIndex);

        if (totalPages < 1) totalPages = 1;

        return new ResponseEntity<>(true, "Surveys successfully found", new PagedEntity<>(page, totalPages, sliced));
    }

    @Override
    public ResponseEntity<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer index) {
        List<Question> questions = database.getQuestions(surveyId);
        if (index >= questions.size()) {
            return new ResponseEntity<>(true, "Survey completed successfully", null);
        }
        Question question = questions.get(index);
        return new ResponseEntity<>(true, "Question successfully found", new PagedEntity<>(index, questions.size(), question));
    }

    @Override
    public ResponseEntity<List<Option>> getQuestionOptionList(Integer questionId) {
        List<Option> options = database.getOptions(questionId);
        if (options.isEmpty()) {
            return new ResponseEntity<>(true, "No questions found", null);
        }
        return new ResponseEntity<>(true, "Questions found successfully",options);
    }

    @Override
    public ResponseEntity<?> submitAnswer(Integer sessionId, Integer optionId) {
        try {
            database.addAnswer(sessionId, optionId);
        } catch (SessionNotFoundException e) {
            return new ResponseEntity<>(false, "Session doesn't exist", null);
        } catch (OptionNotFoundException e) {
            return new ResponseEntity<>(false, "Option doesn't exist", null);
        }
        return new ResponseEntity<>(true, "Answer submitted successfully", null);
    }

    @Override
    public ResponseEntity<?> deleteSurvey(Integer surveyId) {
        database.deleteSurvey(surveyId);
        return new ResponseEntity<>(true, "Survey deleted successfully", null);
    }

    @Override
    public ResponseEntity<Integer> addSurveyAndGetId(String name, String description, String categoryName) {
        if (database.getCategoryByName(categoryName) != null) {
            database.addCategory(categoryName);
        }
        Integer id = database.getCategoryByName(name) == null ? null : database.getCategoryByName(name).getId();
        database.addSurvey(name, description, id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(true, "Survey created successfully", null);
    }

    @Override
    public ResponseEntity<Integer> startSessionAndGetId(String userName, Integer surveyId) {
        ResponseEntity<Integer> response = getUserId(userName);
        if (!response.isSuccess()) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
        Integer userId = response.getBody();
        try {
            Integer sessionId = database.addSessionAndGetId(surveyId, userId, Calendar.getInstance().getTime(), null);
            return new ResponseEntity<>(true, "Successfully created a session", sessionId);
        } catch (SurveyNotFoundException e) {
            return new ResponseEntity<>(false, "Survey doesn't exist", null);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
    }

    public void createTestSurvey() {
        database.addCategory("category test");

        database.addSurvey("Test", "description test", 0, Calendar.getInstance().getTime());

    }


    /// Internal convenience methods

    private ResponseEntity<?> isUserAdmin(String name) {
        User user = database.getUser(name);
        if (user == null) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
        boolean isAdmin = database.getAdmin(user.getId()) != null;
        if (isAdmin) {
            return new ResponseEntity<>(true, "User is admin", null);
        } else {
            return new ResponseEntity<>(false, "User is not admin", null);
        }
    }

    private ResponseEntity<Integer> getUserId(String userName) {
        User user = database.getUser(userName);
        if (user == null) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
        return new ResponseEntity<>(true, "Successfully found userId", user.getId());
    }

    public static SurveysService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SurveysServiceImpl();
        }
        return INSTANCE;
    }
}
