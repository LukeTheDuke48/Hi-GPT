package com.example.application.views.main;


import java.util.ArrayList;
import com.example.application.ChatGPTHelper;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.concurrent.atomic.AtomicBoolean;

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

		VerticalLayout selectHelperLayout = new VerticalLayout();
		Select<String> selectHelper = new Select<>();
		TextField customHelper = new TextField();
		customHelper.setPlaceholder("Enter custom prompt...");
		selectHelper.setLabel("Helper Bot");
		selectHelper.setItems("Dave", "Rick", "Monkey D. Greg", "Custom");
		selectHelper.setValue("Dave");
		selectHelper.addValueChangeListener(
				(HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Select<String>, String>>)
						selectStringComponentValueChangeEvent -> {
			if (selectHelper.getValue().equals("Custom")) {
				selectHelperLayout.add(customHelper);
			} else {
				selectHelperLayout.remove(customHelper);
			}
		});
		selectHelperLayout.setSpacing(false);
		selectHelperLayout.setPadding(false);
		selectHelperLayout.add(selectHelper);
		promptLayout.add(selectHelperLayout);

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

    	// MessageInput input = new MessageInput();
    	// input.addSubmitListener(submitEvent -> {

    	// 	MessageListItem message1 = new MessageListItem(
    	// 			submitEvent.getValue(),
        // 	        null , "User");
        // 	message1.setUserColorIndex(1);

        // 	messages.add(message1);

    	// 	MessageListItem message2 = new MessageListItem(
        //             helper.chatGPT(submitEvent.getValue(), apiKeyField.getValue(), selectApiVersion.getValue(), tokensField.getValue().intValue(), temperatureField.getValue()),
        //             null , "Dave");
        //     message1.setUserColorIndex(2);

        // 	messages.add(message2);

    	//     VerticalLayout messageLayout = new VerticalLayout();
    	//     messageLayout.setJustifyContentMode(JustifyContentMode.CENTER);

    	//     list.setItems(messages);
    	//     tokenCountField.setValue(String.valueOf(helper.getTotalTokens()));
    	// });

    	// input.setWidthFull();
    	// chatBotLayout.add(input);

    	// HorizontalLayout searchBarLayout = new HorizontalLayout();
    	// searchBarLayout.setJustifyContentMode(JustifyContentMode.END);

    	// chatBotLayout.add(searchBarLayout);

		TextField input = new TextField();
        input.setPlaceholder("Enter message here...");
        input.setWidthFull();
        input.addKeyPressListener(Key.ENTER, event -> {
            submitMessage(input.getValue(), apiKeyField, selectApiVersion, tokensField, temperatureField, tokenCountField, messages, list);
            input.clear();
        });

        Button micButton = new Button(new Icon(VaadinIcon.MICROPHONE));
        micButton.getElement().getStyle().set("color", "red"); // Color when off
        micButton.getElement().getStyle().set("border-radius", "50%"); // Make it circular

        Button sendButton = new Button(new Icon(VaadinIcon.ARROW_RIGHT));
        sendButton.addClickListener(e -> {
            submitMessage(input.getValue(), apiKeyField, selectApiVersion, tokensField, temperatureField, tokenCountField, messages, list);
            input.clear();
        });

        AtomicBoolean isListening = new AtomicBoolean(false); // State of the microphone

        micButton.addClickListener(e -> {
            if (isListening.get()) {
                // Stop listening
                UI.getCurrent().getPage().executeJs("recognition.stop();");
                micButton.getElement().getStyle().set("color", "red"); // Color when off
                // Manually submit the current text in the input field
                submitMessage(input.getValue(), apiKeyField, selectApiVersion, tokensField, temperatureField, tokenCountField, messages, list);
                input.clear(); // Clear the input field after submission
            } else {
                // Start listening
                UI.getCurrent().getPage().executeJs(
                    "var recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition || window.mozSpeechRecognition || window.msSpeechRecognition)();" +
                    "recognition.lang = 'en-US';" +
                    "recognition.interimResults = false;" +
                    "recognition.maxAlternatives = 1;" +
                    "recognition.onresult = function(event) {" +
                    "  var speechText = event.results[0][0].transcript;" +
                    "  $0.value = speechText;" + // Set the input field text
                    "};" +
                    "recognition.start();",
                    input.getElement() // Pass the input element to the JavaScript code
                );
                micButton.getElement().getStyle().set("color", "green"); // Color when on
            }
            isListening.set(!isListening.get()); // Toggle the state
        });

		HorizontalLayout inputLayout = new HorizontalLayout(micButton, input, sendButton);
        inputLayout.setWidthFull();
        inputLayout.setFlexGrow(1.0, input); // make the input field expand
        chatBotLayout.add(inputLayout);

        add(promptLayout);
        add(chatBotLayout);
        setHeightFull();

    	}

		private void submitMessage(String message, PasswordField apiKeyField, Select<String> selectApiVersion, NumberField tokensField, NumberField temperatureField, TextField tokenCountField, ArrayList<MessageListItem> messages, MessageList list) {
			MessageListItem message1 = new MessageListItem(
				message,
				null , "User");
			message1.setUserColorIndex(1);

			messages.add(message1);

			MessageListItem message2 = new MessageListItem(
				helper.chatGPT(message, apiKeyField.getValue(), selectApiVersion.getValue(), tokensField.getValue().intValue(), temperatureField.getValue()),
				null , "Dave");
			message1.setUserColorIndex(2);

			messages.add(message2);
			list.setItems(messages);
			tokenCountField.setValue(String.valueOf(helper.getTotalTokens()));
		}

    }


