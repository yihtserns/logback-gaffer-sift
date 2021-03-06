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
package com.github.yihtserns.logback.gaffer.sift.testutil;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author yihtserns
 */
public class MockAppender extends AppenderBase<ILoggingEvent> {

    private List<String> messageList = new ArrayList<String>();
    private Layout<ILoggingEvent> layout;

    @Override
    protected void append(ILoggingEvent event) {
        messageList.add(layout.doLayout(event));
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    public void assertLogged(String... expectedMessages) {
        assertThat(messageList, contains(expectedMessages));
    }
}
