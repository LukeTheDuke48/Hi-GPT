package com.example.application.helpers.utils;

import com.example.application.helpers.*;

public class HelperFactory {
    public static IHelper createHelper(Helper helper, String customSystemMessage) {
        return switch (helper) {
            case DAVE -> new DaveHelper();
            case RICK -> new RickHelper();
            case MONKEY_D_GREG -> new MonkeyDGregHelper();
            case CUSTOM -> new CustomHelper(customSystemMessage);
            default -> null;
        };
    }
}
