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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.gaffer.GafferConfigurator;
import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.core.sift.AppenderTracker;
import com.github.yihtserns.logback.gaffer.sift.testutil.MockAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

/**
 * @author yihtserns
 */
public class ClosureAppenderFactoryTest {

    private LoggerContext loggerContext = new LoggerContext();

    @After
    public void resetContext() {
        loggerContext.stop();
    }

    @Before
    @After
    public void clearMDC() {
        MDC.clear();
    }

    @Test
    public void canCreateIndividualAppenderBasedOnMdcValue() throws Exception {
        GafferConfigurator gafferConfigurator = new GafferConfigurator(loggerContext);
        gafferConfigurator.run(
                "import com.github.yihtserns.logback.gaffer.sift.testutil.MockAppender\n"
                + "import ch.qos.logback.classic.PatternLayout\n"
                + "\n"
                + "statusListener(OnConsoleStatusListener)\n"
                + "\n"
                + "appender('SIFT', ch.qos.logback.classic.sift.SiftingAppender) {\n"
                + "  discriminator(ch.qos.logback.classic.sift.MDCBasedDiscriminator) {\n"
                + "    key = 'userid'\n"
                + "    defaultValue = 'unknown'\n"
                + "  }\n"
                + "  sift {\n"
                + "    appender(\"MOCK-${userid}\", MockAppender) {\n"
                + "      layout(PatternLayout) {\n"
                + "        pattern = \"${userid}: %level - %msg\"\n"
                + "      }\n"
                + "    }\n"
                + "  }\n"
                + "}\n"
                + "\n"
                + "root(DEBUG, ['SIFT'])");

        final String key = "userid";
        Logger log = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        {
            log.info("No MDC set");
        }
        {
            MDC.put(key, "me");
            log.info("With MDC set");
        }
        {
            MDC.remove(key);
            log.debug("With MDC cleared");
        }

        SiftingAppender siftingAppender = (SiftingAppender) log.getAppender("SIFT");
        AppenderTracker siftingAppenderTracker = siftingAppender.getAppenderTracker();
        MockAppender withMdcMock = (MockAppender) siftingAppenderTracker.find("me");
        MockAppender withoutMdcMock = (MockAppender) siftingAppenderTracker.find("unknown");

        withMdcMock.assertLogged(
                "me: INFO - With MDC set");
        withoutMdcMock.assertLogged(
                "unknown: INFO - No MDC set",
                "unknown: DEBUG - With MDC cleared");
    }
}
