package email.crappy.ssao.ruoka.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author Santeri Elo
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFragment {
}
