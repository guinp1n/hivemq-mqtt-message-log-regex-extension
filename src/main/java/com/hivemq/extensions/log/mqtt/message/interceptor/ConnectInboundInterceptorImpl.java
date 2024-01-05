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
import com.hivemq.extension.sdk.api.interceptor.connect.ConnectInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundInput;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundOutput;
import com.hivemq.extension.sdk.api.packets.connect.ConnectPacket;
import com.hivemq.extensions.log.mqtt.message.util.MessageLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Florian Limpöck
 * @since 1.0.0
 */
public class ConnectInboundInterceptorImpl implements ConnectInboundInterceptor {

    private static final @NotNull Logger LOG = LoggerFactory.getLogger(ConnectInboundInterceptorImpl.class);
    private final boolean verbose;
    private final String regex;

    public ConnectInboundInterceptorImpl(final boolean verbose, final String regex) {
        this.verbose = verbose;
        this.regex = regex;
    }

    @Override
    public void onConnect(
            final @NotNull ConnectInboundInput connectInboundInput,
            final @NotNull ConnectInboundOutput connectInboundOutput) {
        try {
            final ConnectPacket connectPacket = connectInboundInput.getConnectPacket();
            final String clientId = connectPacket.getClientId();
            if (regex.isEmpty() || clientId.matches(regex)) {
                MessageLogUtil.logConnect(connectPacket, verbose);
            }
        } catch (final Exception e) {
            LOG.debug("Exception thrown at inbound connect logging: ", e);
        }
    }
}