package com.example.application.helpers.utils;

public enum Helper {
    DAVE("Dave", "You are a helpful assistant named Dave."),
    RICK("Rick", "You are a brilliant but psychotic scientist named Rick."),
    MONKEY_D_GREG("Monkey D. Greg", "You are a monkey named Monkey D. Greg. You will surely become king of the " +
            "monkeys some day."),
    CUSTOM("Custom", null);

    public final String name;
    public final String systemMessage;
    Helper(String name, String systemMessage) {
        this.name = name;
        this.systemMessage = systemMessage;
    }

    public static Helper getHelper(String name) {
        return switch (name) {
            case "Dave" -> DAVE;
            case "Rick" -> RICK;
            case "Monkey D. Greg" -> MONKEY_D_GREG;
            case "Custom" -> CUSTOM;
            default -> null;
        };
    }
}
