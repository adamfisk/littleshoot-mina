/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.littleshoot.mina.transport.vmpipe;

import java.io.IOException;
import java.net.SocketAddress;

import org.littleshoot.mina.common.ConnectFuture;
import org.littleshoot.mina.common.ExceptionMonitor;
import org.littleshoot.mina.common.IoFilterChain;
import org.littleshoot.mina.common.IoHandler;
import org.littleshoot.mina.common.IoServiceConfig;
import org.littleshoot.mina.common.IoSessionConfig;
import org.littleshoot.mina.common.support.AbstractIoFilterChain;
import org.littleshoot.mina.common.support.BaseIoConnector;
import org.littleshoot.mina.common.support.BaseIoConnectorConfig;
import org.littleshoot.mina.common.support.BaseIoSessionConfig;
import org.littleshoot.mina.common.support.DefaultConnectFuture;
import org.littleshoot.mina.transport.vmpipe.support.VmPipe;
import org.littleshoot.mina.transport.vmpipe.support.VmPipeFilterChain;
import org.littleshoot.mina.transport.vmpipe.support.VmPipeIdleStatusChecker;
import org.littleshoot.mina.transport.vmpipe.support.VmPipeSessionImpl;
import org.littleshoot.mina.util.AnonymousSocketAddress;

/**
 * Connects to {@link IoHandler}s which is bound on the specified
 * {@link VmPipeAddress}.
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 587373 $, $Date: 2007-10-23 11:54:05 +0900 (Tue, 23 Oct 2007) $
 */
public class VmPipeConnector extends BaseIoConnector {
    private static final IoSessionConfig CONFIG = new BaseIoSessionConfig() {
    };

    private final IoServiceConfig defaultConfig = new BaseIoConnectorConfig() {
        public IoSessionConfig getSessionConfig() {
            return CONFIG;
        }
    };

    /**
     * Creates a new instance.
     */
    public VmPipeConnector() {
    }

    public ConnectFuture connect(SocketAddress address, IoHandler handler,
            IoServiceConfig config) {
        return connect(address, null, handler, config);
    }

    public ConnectFuture connect(SocketAddress address,
            SocketAddress localAddress, IoHandler handler,
            IoServiceConfig config) {
        if (address == null)
            throw new NullPointerException("address");
        if (handler == null)
            throw new NullPointerException("handler");
        if (!(address instanceof VmPipeAddress))
            throw new IllegalArgumentException("address must be VmPipeAddress.");

        if (config == null) {
            config = getDefaultConfig();
        }

        VmPipe entry = VmPipeAcceptor.boundHandlers.get(address);
        if (entry == null) {
            return DefaultConnectFuture.newFailedFuture(new IOException(
                    "Endpoint unavailable: " + address));
        }

        DefaultConnectFuture future = new DefaultConnectFuture();
        VmPipeSessionImpl localSession = new VmPipeSessionImpl(this,
                                                               config,getListeners(),
                                                               new AnonymousSocketAddress(),
                                                               handler,
                                                               entry);

        // initialize connector session
        try {
            IoFilterChain filterChain = localSession.getFilterChain();
            this.getFilterChainBuilder().buildFilterChain(filterChain);
            config.getFilterChainBuilder().buildFilterChain(filterChain);
            config.getThreadModel().buildFilterChain(filterChain);

            // The following sentences don't throw any exceptions.
            localSession.setAttribute(AbstractIoFilterChain.CONNECT_FUTURE, future);
            getListeners().fireSessionCreated(localSession);
            VmPipeIdleStatusChecker.getInstance().addSession(localSession);
        } catch (Throwable t) {
            future.setException(t);
            return future;
        }

        // initialize acceptor session
        VmPipeSessionImpl remoteSession = localSession.getRemoteSession();
        try {
            IoFilterChain filterChain = remoteSession.getFilterChain();
            entry.getAcceptor().getFilterChainBuilder().buildFilterChain(
                    filterChain);
            entry.getConfig().getFilterChainBuilder().buildFilterChain(
                    filterChain);
            entry.getConfig().getThreadModel().buildFilterChain(filterChain);

            // The following sentences don't throw any exceptions.
            entry.getListeners().fireSessionCreated(remoteSession);
            VmPipeIdleStatusChecker.getInstance().addSession(remoteSession);
        } catch (Throwable t) {
            ExceptionMonitor.getInstance().exceptionCaught(t);
            remoteSession.close();
        }


        // Start chains, and then allow and messages read/written to be processed. This is to ensure that
        // sessionOpened gets received before a messageReceived
        ((VmPipeFilterChain) localSession.getFilterChain()).start();
        ((VmPipeFilterChain) remoteSession.getFilterChain()).start();

        return future;
    }

    public IoServiceConfig getDefaultConfig() {
        return defaultConfig;
    }
}