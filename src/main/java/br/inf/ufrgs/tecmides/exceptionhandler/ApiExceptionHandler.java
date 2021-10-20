package br.inf.ufrgs.tecmides.exceptionhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.inf.ufrgs.tecmides.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBusinessException(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ErrorType errorType = ErrorType.BUSINESS;
		String detail = ex.getMessage();
		
		ApiError error = createApiErrorBuilder(status, errorType, detail).build();
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (body == null) {
			body = ApiError.builder()
					.title(body == null ? status.getReasonPhrase() : (String) body)
					.status(status.value())
					.build();
		} else if (body instanceof String) {
			body = ApiError.builder()
					.title((String) body)
					.status(status.value())
					.build();
		}
		
		log.error( ((ApiError) body).getDetail() , ex );
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private ApiError.ApiErrorBuilder createApiErrorBuilder(HttpStatus status, ErrorType errorType, String detail) {
		return ApiError.builder()
				.status(status.value())
				.type(errorType.getUri())
				.title(errorType.getTitle())
				.detail(detail);
	}
	
}
