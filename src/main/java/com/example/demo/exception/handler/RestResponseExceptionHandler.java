package com.example.demo.exception.handler;

import com.example.demo.exception.*;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Customize the response for MethodArgumentNotValidException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),
                apiValidationErrorMapping(ex));

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    /**
     * Customize the response for BindException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),
                apiValidationErrorMapping(ex));

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    /**
     * Customize the response for TypeMismatchException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
                + ex.getRequiredType();

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
    }

    /**
     * Customize the response for MissingServletRequestPartException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final String error = ex.getRequestPartName() + " part is missing";

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
    }

    /**
     * Customize the response for MissingServletRequestParameterException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final String error = ex.getParameterName() + " parameter is missing";

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
    }

    /**
     * Customize the response for MissingPathVariableException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.2
     */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final String error = ex.getParameter().getParameterName() + " parameter is missing";

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
    }

    /**
     * Customize the response for NoHandlerFoundException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.0
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ApiError apiError = new ApiError(HttpStatus.NOT_IMPLEMENTED, ex.getLocalizedMessage(), error);

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
    }

    /**
     * Customize the response for HttpRequestMethodNotSupportedException.
     * <p>
     * This method logs a warning, sets the "Allow" header, and delegates to
     * {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final StringBuilder errorBuilder = new StringBuilder();

        errorBuilder.append(ex.getMethod());
        errorBuilder.append(" method is not supported for this request. Supported methods are ");

        ex.getSupportedHttpMethods().forEach(obj -> errorBuilder.append(obj + " "));

        final ApiError apiError = new ApiError(HttpStatus.NOT_IMPLEMENTED, ex.getLocalizedMessage(),
                errorBuilder.toString());

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
        }

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
    }

    /**
     * Customize the response for HttpMediaTypeNotSupportedException.
     * <p>
     * This method sets the "Accept" header and delegates to
     * {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final StringBuilder errorBuilder = new StringBuilder();

        errorBuilder.append(ex.getContentType());
        errorBuilder.append(" media type is not supported. Supported media types are ");

        ex.getSupportedMediaTypes().forEach(obj -> errorBuilder.append(obj + " "));

        final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(),
                errorBuilder.substring(0, errorBuilder.length() - 2));

        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
    }

    /**
     * Customize the response for HttpMessageNotReadableException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final ApiError apiError = new ApiError(HttpStatus.EXPECTATION_FAILED, ex.getLocalizedMessage());

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());

    }

    /**
     * Customize the response for HttpMessageNotWritableException.
     * <p>
     * This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final ApiError apiError = new ApiError(HttpStatus.EXPECTATION_FAILED, ex.getLocalizedMessage());

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());

    }

    /**
     * Customize the response for BadRequestException.
     * <p>
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());

        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }

    /**
     * Customize the response for DataBaseTransactionException.
     * <p>
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(DataBaseTransactionException.class)
    public final ResponseEntity<Object> handleDataBaseTransactionException(DataBaseTransactionException ex,
                                                                           WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage());

        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }

    /**
     * Customize the response for ResourceNotFoundException.
     * <p>
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                        WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());

        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }

    /**
     * Customize the response for ConstraintViolationException.
     * <p>
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                                                                           WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final List<String> errors = new ArrayList<>();

        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
                    + violation.getMessage());

        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }

    /**
     * Customize the response for DataIntegrityViolationException.
     * <p>
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> handleDataIntegrityViolationException(Exception ex, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            final ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage());
            return new ResponseEntity<Object>(apiError, apiError.getStatus());
        }

        if (ex.getCause() instanceof ConstraintViolationException) {

            ConstraintViolationException exc = (ConstraintViolationException) ex;
            final List<String> errors = new ArrayList<>();

            for (final ConstraintViolation<?> violation : exc.getConstraintViolations()) {
                errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
                        + violation.getMessage());

            }
            final ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), errors);

            return new ResponseEntity<Object>(apiError, apiError.getStatus());

        }

        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());

        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }

    /**
     * Customize the response for ApplicationException.
     * <p>
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(ApplicationException.class)
    public final ResponseEntity<Object> handleApplicationException(ApplicationException ex, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());

        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }

    /**
     * Customize the response for Exception.
     * <p>
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {

        logger.info(ex.getClass().getName());
        logger.error("error", ex);

        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());

        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }

    private static <T> List<String> apiValidationErrorMapping(T ex) {
        return ((BindException) ex).getBindingResult().getFieldErrors().stream()
                .map(RestResponseExceptionHandler::apiValidationErrorMapping).collect(Collectors.toList());
    }

    private static String apiValidationErrorMapping(FieldError fieldError) {
        return new ApiValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
                fieldError.getDefaultMessage()).toString();
    }

}
