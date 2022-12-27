package dev.applaudostudios.applaudofinalproject.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private HttpStatus httpStatus;
    private String message;
}
