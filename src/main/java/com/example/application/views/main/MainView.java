package com.example.application.views.main;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import com.example.application.ChatGPTHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Hi GPT")
@Route(value = "")
public class MainView extends HorizontalLayout {

	private ChatGPTHelper helper;

    public MainView() {
    	
    	String primaryColor  = "#303030";
    	String secondaryColor  = "#c6cccc";
    	String tertiaryColor  = "#6ec2c2";
    	ArrayList<MessageListItem> messages = new ArrayList<MessageListItem>();
    	
		this.helper = new ChatGPTHelper();

    	VerticalLayout promptLayout = new VerticalLayout();
       	promptLayout.setWidth("20em");
    	promptLayout.getStyle().set("background-color", secondaryColor);
    	
    	
    	PasswordField apiKeyField = new PasswordField();
    	apiKeyField.setLabel("API Key");
    	promptLayout.add(apiKeyField);
    	
    	Select<String> selectApiVersion = new Select<>();
    	selectApiVersion.setLabel("API Version");
    	selectApiVersion.setItems("gpt-3.5-turbo", "gpt-4");
    	selectApiVersion.setValue("gpt-3.5-turbo");
    	
    	promptLayout.add(selectApiVersion);

    	NumberField temperatureField = new NumberField();
    	temperatureField.setLabel("Temperature");
    	temperatureField.setStep(0.05);
    	temperatureField.setValue(0.5);
    	temperatureField.setMax(1.0);
    	temperatureField.setMin(0.0);
    	temperatureField.setStepButtonsVisible(true);
    	promptLayout.add(temperatureField);
    	
    	NumberField tokensField = new NumberField();
    	tokensField.setLabel("Tokens");
    	tokensField.setStep(1);
    	tokensField.setValue(500.0);
    	tokensField.setMin(10);
    	tokensField.setStepButtonsVisible(true);
    	promptLayout.add(tokensField);
    	
    	
    	TextField tokenCountField;
    	  
    	tokenCountField = new TextField("Total Tokens");
    	        tokenCountField.setReadOnly(true);
    	        promptLayout.add(tokenCountField);
    	
    	VerticalLayout chatBotLayout = new VerticalLayout();
    	chatBotLayout.setJustifyContentMode(JustifyContentMode.END);
    	
    	MessageList list = new MessageList();
    	chatBotLayout.add(list);
    	
    	MessageInput input = new MessageInput();
    	input.addSubmitListener(submitEvent -> {
    		
    		MessageListItem message1 = new MessageListItem(
    				submitEvent.getValue(),
        	        null , "User");
        	message1.setUserColorIndex(1);
        	
        	messages.add(message1);
        
    		MessageListItem message2 = new MessageListItem(
                    helper.chatGPT(submitEvent.getValue(), apiKeyField.getValue(), selectApiVersion.getValue(), tokensField.getValue().intValue(), temperatureField.getValue()),
                    null , "Dave");
            message1.setUserColorIndex(2);
     
        	messages.add(message2);
    	    
    	    VerticalLayout messageLayout = new VerticalLayout();
    	    messageLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    	    
    	    list.setItems(messages);
    	    tokenCountField.setValue(String.valueOf(helper.getTotalTokens()));
    	});
    	
    	input.setWidthFull();
    	chatBotLayout.add(input);
    	
    	HorizontalLayout searchBarLayout = new HorizontalLayout();
    	searchBarLayout.setJustifyContentMode(JustifyContentMode.END);
    	
    	chatBotLayout.add(searchBarLayout);
    	
    	add(promptLayout);
    	add(chatBotLayout);
    	setHeightFull();
    	
    	}
    
    
    }


