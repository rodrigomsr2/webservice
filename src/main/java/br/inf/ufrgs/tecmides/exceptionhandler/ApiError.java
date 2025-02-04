package br.inf.ufrgs.tecmides.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

/** Follows RFC 7807 (Problem Details for HTTP APIs) */
@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class ApiError {

	private Integer status;
	
	private String type;
	
	private String title;
	
	private String detail;
	
}
