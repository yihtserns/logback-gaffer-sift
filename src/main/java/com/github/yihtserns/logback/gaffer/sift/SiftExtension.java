/*
 * Copyright 2015 yihtserns.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.yihtserns.logback.gaffer.sift;

import ch.qos.logback.classic.gaffer.AppenderDelegate;
import ch.qos.logback.classic.gaffer.ComponentDelegate;
import ch.qos.logback.classic.sift.SiftingAppender;
import groovy.lang.Closure;
import groovy.lang.MissingPropertyException;

/**
 * Extension module to enable sifting functionality.
 *
 * @author yihtserns
 */
public class SiftExtension {

    /**
     * Adds a {@code sift(Closure)} method into {@link AppenderDelegate}.
     */
    public static void sift(AppenderDelegate self, Closure buildAppender) {
        SiftingAppender siftingAppender = (SiftingAppender) self.getComponent();

        siftingAppender.setAppenderFactory(new ClosureAppenderFactory(buildAppender, siftingAppender));
    }

    /**
     * Adds a {@code propertyMissing(String)} method into {@link ComponentDelegate} that throws
     * {@link MissingPropertyException}, to workaround LOGBACK-488 since https://github.com/qos-ch/logback/pull/249
     * is not accepted yet.
     * <p/>
     * Without this, closure that builds the appender cannot "see" discriminating key that was injected as property by
     * {@link ClosureAppenderFactory}.
     */
    public static Object propertyMissing(ComponentDelegate self, String propertyName) {
        throw new MissingPropertyException(propertyName, self.getClass());
    }
}
