package org.evgenysav.infrastructure.dto.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FK {
    String tableName();

    String id();
}
