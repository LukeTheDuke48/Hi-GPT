package com.example.application.views.main;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Hi GPT")
@Route(value = "")
public class MainView extends HorizontalLayout {

    public MainView() {
    	
    	String primaryColor  = "#303030";
    	String secondaryColor  = "#232424";
    	String tertiaryColor  = "#6ec2c2";

    	

    	getStyle().set("background-color", primaryColor);
    	
    	VerticalLayout promptLayout = new VerticalLayout();
       	promptLayout.setWidth("20em");
    	promptLayout.getStyle().set("background-color", secondaryColor);
    	
    	Button primaryButton = new Button(" +  New Chat ");
    	primaryButton.getStyle().set("background-color", tertiaryColor);
    	primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	primaryButton.setWidthFull();
    	promptLayout.add(primaryButton);

    	VerticalLayout chatBotLayout = new VerticalLayout();
    	
    	MessageInput input = new MessageInput();
    	input.addSubmitListener(submitEvent -> {
    	    Notification.show("Message received: " + submitEvent.getValue(),
    	            3000, Notification.Position.MIDDLE);
    	});
    	input.getStyle().set("background-color", tertiaryColor);
    	input.setWidthFull();
    	chatBotLayout.add(input);
 
    	
    	
    	add(promptLayout);
    	add(chatBotLayout);
    	setHeightFull();
    	
    	}
    }
