package com.example.application.helpers;

import com.example.application.helpers.utils.Helper;

public class CustomHelper extends AbstractHelper {
    public CustomHelper(String customSystemMessage) {
        super(Helper.CUSTOM, customSystemMessage);
    }
}
