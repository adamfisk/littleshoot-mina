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
package org.littleshoot.mina.common.support;

import java.io.IOException;
import java.net.SocketAddress;

import org.littleshoot.mina.common.IoAcceptor;
import org.littleshoot.mina.common.IoHandler;
import org.littleshoot.mina.common.IoSession;

/**
 * A base implementation of {@link IoAcceptor}.
 * 
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (Fri, 13 Jul 2007) $
 */
public abstract class BaseIoAcceptor extends BaseIoService implements
        IoAcceptor {
    protected BaseIoAcceptor() {
    }

    public void bind(SocketAddress address, IoHandler handler)
            throws IOException {
        this.bind(address, handler, getDefaultConfig());
    }

    public IoSession newSession(SocketAddress remoteAddress,
            SocketAddress localAddress) {
        throw new UnsupportedOperationException();
    }
}
