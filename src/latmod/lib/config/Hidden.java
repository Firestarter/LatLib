package latmod.lib.config;

import java.lang.annotation.*;

/**
 * Created by LatvianModder on 14.02.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Hidden
{
	boolean DEF_VALUE = false;
	boolean value() default !DEF_VALUE;
}