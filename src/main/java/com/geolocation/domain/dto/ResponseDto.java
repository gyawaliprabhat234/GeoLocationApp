package com.geolocation.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:50 AM
 * @project GeoLocationApp
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private boolean success; // indicate weather the call is successful or not
    private Object message;  // if the call is successful message can be null otherwise it contains the error message.
    private Object responseData; // response contains the actual data if call is successful
}

