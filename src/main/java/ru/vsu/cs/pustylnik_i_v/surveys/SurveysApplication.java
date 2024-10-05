package ru.vsu.cs.pustylnik_i_v.surveys;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleView;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.mock.MockServiceProvider;

@ApplicationPath("/api")
public class SurveysApplication extends Application {
}