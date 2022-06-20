package com.geolocation.exception;

import com.geolocation.domain.dto.ResponseDto;
import io.dropwizard.jersey.validation.ConstraintMessage;
import io.dropwizard.jersey.validation.JerseyViolationException;
import org.glassfish.jersey.server.model.Invocable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Set;

public class CustomValidationExceptionMapper implements ExceptionMapper<JerseyViolationException> {
    private static Logger logger = LoggerFactory.getLogger(CustomValidationExceptionMapper.class);
    @Override
    public Response toResponse(final JerseyViolationException exception) {
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        final Invocable invocable = exception.getInvocable();
        final int status = ConstraintMessage.determineStatus(violations, invocable);
        logger.warn(exception.getMessage());
        ResponseDto response = ResponseDto.builder()
                .success(false)
                .message(exception.getMessage())
                .build();
        return Response.status(status)
                .entity(response)
                .build();
    }
}
