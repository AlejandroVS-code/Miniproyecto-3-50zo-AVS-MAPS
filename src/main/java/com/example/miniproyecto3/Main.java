package com.example.miniproyecto3;

import com.example.miniproyecto3.view.HomeStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public void start(Stage primaryStage) throws IOException {

        new HomeStage();
    }
}
