package bershika.route.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Messanger {
	public static void show(String header, String msg){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(header, msg));
	}
}
