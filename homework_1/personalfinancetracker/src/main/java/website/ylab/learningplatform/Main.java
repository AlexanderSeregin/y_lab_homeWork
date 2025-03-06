package website.ylab.learningplatform;


import website.ylab.learningplatform.datasource.*;
import website.ylab.learningplatform.service.*;
import website.ylab.learningplatform.controller.*;
import website.ylab.learningplatform.view.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

            MenuController menuController = new MenuController();
            menuController.runMenu();

    }
}