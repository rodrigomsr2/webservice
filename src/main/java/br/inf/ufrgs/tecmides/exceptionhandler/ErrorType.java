package br.inf.ufrgs.tecmides.exceptionhandler;

import lombok.Getter;

@Getter
public enum ErrorType {

	BUSINESS("/business", "Business rule error");
	
	private String title;
	private String uri;

	ErrorType(String path, String title) {
		this.uri = "https://git.com" + path;
		this.title = title;
	}
	
}
