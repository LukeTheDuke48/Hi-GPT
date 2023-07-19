package com.example.application.views.main;


import com.example.application.ChatGPTHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Hi GPT")
@Route(value = "")
public class MainView extends HorizontalLayout {

    public MainView() {
    	
    	String primaryColor  = "#303030";
    	String secondaryColor  = "#c6cccc";
    	String tertiaryColor  = "#6ec2c2";


    	//getStyle().set("background-color", primaryColor);
    	VerticalLayout promptLayout = new VerticalLayout();
       	promptLayout.setWidth("20em");
    	promptLayout.getStyle().set("background-color", secondaryColor);
    	
    	Button newChatButton = new Button(" +  New Chat ");
    	//newChatButton.getStyle().set("background-color", tertiaryColor);
    	newChatButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	newChatButton.setWidthFull();
    	promptLayout.add(newChatButton);
    	
    	PasswordField apiKeyField = new PasswordField();
    	apiKeyField.setLabel("API Key");
    	promptLayout.add(apiKeyField);

    	VerticalLayout chatBotLayout = new VerticalLayout();
    	chatBotLayout.setJustifyContentMode(JustifyContentMode.END);
    	
    	MessageInput input = new MessageInput();
    	input.addSubmitListener(submitEvent -> {
    		
    		ChatGPTHelper helper;
    	    Notification.show(ChatGPTHelper.chatGPT(submitEvent.getValue(), apiKeyField.getValue()),
    	            3000, Notification.Position.MIDDLE);
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
