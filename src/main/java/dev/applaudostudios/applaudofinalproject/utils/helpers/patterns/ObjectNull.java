package dev.applaudostudios.applaudofinalproject.utils.helpers.patterns;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Data
@AllArgsConstructor
public class ObjectNull {
    private final List<Collections> objectNull = Collections.emptyList();

}
