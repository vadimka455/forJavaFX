package sample.Annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by vadim on 19.06.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    String tableName();
}