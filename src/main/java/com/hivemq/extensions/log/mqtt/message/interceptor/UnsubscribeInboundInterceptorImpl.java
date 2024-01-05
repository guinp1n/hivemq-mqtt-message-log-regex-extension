/*
 * Copyright 2019-present HiveMQ GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hivemq.extensions.log.mqtt.message.interceptor;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.unsubscribe.UnsubscribeInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.unsubscribe.parameter.UnsubscribeInboundInput;
import com.hivemq.extension.sdk.api.interceptor.unsubscribe.parameter.UnsubscribeInboundOutput;
import com.hivemq.extensions.log.mqtt.message.util.MessageLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michael Walter
 * @since 1.1.0
 */
class UnsubscribeInboundInterceptorImpl implements UnsubscribeInboundInterceptor {

    private static final @NotNull Logger LOG = LoggerFactory.getLogger(UnsubscribeInboundInterceptorImpl.class);
    private final boolean verbose;
    private final String regex;

    UnsubscribeInboundInterceptorImpl(final boolean verbose, final String regex) {
        this.verbose = verbose;
        this.regex = regex;
    }

    @Override
    public void onInboundUnsubscribe(
            final @NotNull UnsubscribeInboundInput unsubscribeInboundInput,
            final @NotNull UnsubscribeInboundOutput unsubscribeInboundOutput) {
        try {
            final String clientId = unsubscribeInboundInput.getClientInformation().getClientId();
            if (regex.isEmpty() || clientId.matches(regex)) {
                MessageLogUtil.logUnsubscribe(unsubscribeInboundInput, verbose);
            }
        } catch (final Exception e) {
            LOG.debug("Exception thrown at inbound unsubscribe logging: ", e);
        }
    }
}