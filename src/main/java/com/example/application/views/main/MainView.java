package com.example.application.views.main;


import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Hi GPT")
@Route(value = "")
public class MainView extends HorizontalLayout {

    public MainView() {
    	
    	VerticalLayout promptLayout = new VerticalLayout();
    	VerticalLayout chatBotLayout = new VerticalLayout();
    	
    	
    	add(promptLayout);
    	add(chatBotLayout); 
    	}
    }
