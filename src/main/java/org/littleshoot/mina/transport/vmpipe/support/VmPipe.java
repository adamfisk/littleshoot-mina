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
package org.littleshoot.mina.transport.vmpipe.support;

import org.littleshoot.mina.common.IoHandler;
import org.littleshoot.mina.common.IoServiceConfig;
import org.littleshoot.mina.common.support.IoServiceListenerSupport;
import org.littleshoot.mina.transport.vmpipe.VmPipeAcceptor;
import org.littleshoot.mina.transport.vmpipe.VmPipeAddress;

/**
 * @todo Document me!
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 585081 $, $Date: 2007-10-16 17:45:14 +0900 (Tue, 16 Oct 2007) $
 */
public class VmPipe {
    private final VmPipeAcceptor acceptor;

    private final VmPipeAddress address;

    private final IoHandler handler;

    private final IoServiceConfig config;

    private final IoServiceListenerSupport listeners;

    public VmPipe(VmPipeAcceptor acceptor, VmPipeAddress address,
            IoHandler handler, IoServiceConfig config,
            IoServiceListenerSupport listeners) {
        this.acceptor = acceptor;
        this.address = address;
        this.handler = handler;
        this.config = config;
        this.listeners = listeners;
    }

    public VmPipeAcceptor getAcceptor() {
        return acceptor;
    }

    public VmPipeAddress getAddress() {
        return address;
    }

    public IoHandler getHandler() {
        return handler;
    }

    public IoServiceConfig getConfig() {
        return config;
    }

    public IoServiceListenerSupport getListeners() {
        return listeners;
    }
}