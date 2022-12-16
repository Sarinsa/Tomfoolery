package com.sarinsa.tomfoolery.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a marker annotation used by Tomfoolery to
 * detect mod plugins. Your Tomfoolery mod plugin class
 * must be annotated with this to be loaded.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TomfooleryPlugin {

    /**
     * @return Your mod's modid or an empty String
     *         if this plugin does not depend on
     *         a mod being loaded.
     *         <br><br>
     *         This is used to make sure plugins doesn't
     *         get loaded if the mod that adds them failed to load
     *         itself.
     */
    String modid() default "";
}
