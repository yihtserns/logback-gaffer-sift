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

package com.github.yihtserns.logback.gaffer.sift

import ch.qos.logback.classic.gaffer.ConfigurationDelegate
import ch.qos.logback.classic.sift.SiftingAppender
import ch.qos.logback.core.Appender
import ch.qos.logback.core.Context
import ch.qos.logback.core.sift.AppenderFactory

/**
 * @author yihtserns
 */
class ClosureAppenderFactory implements AppenderFactory {

    private Closure buildAppenderPrototype;
	private SiftingAppender siftingAppender;

    ClosureAppenderFactory(Closure buildAppenderPrototype, SiftingAppender siftingAppender) {
        this.buildAppenderPrototype = buildAppenderPrototype
        this.siftingAppender = siftingAppender
    }

    @Override
    Appender buildAppender(Context context, String discriminatingValue) {
        ConfigurationDelegate config = new ConfigurationDelegate()
        config.context = context
        config.metaClass."${siftingAppender.discriminatorKey}" = discriminatingValue

        def buildAppender = buildAppenderPrototype.clone()
        buildAppender.delegate = config
        buildAppender.resolveStrategy = Closure.DELEGATE_FIRST

        buildAppender.call()
        return config.appenderList.get(0)
    }
}
