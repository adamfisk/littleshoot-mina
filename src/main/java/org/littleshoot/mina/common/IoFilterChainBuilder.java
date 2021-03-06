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
package org.littleshoot.mina.common;

/**
 * An interface that builds {@link IoFilterChain} in predefined way
 * when {@link IoSession} is created.  You can extract common filter chain
 * modification logic to this interface.  For example, to add a filter
 * to the chain,
 * <pre>
 * public class MyFilterChainBuilder implements IoFilterChainBuilder {
 *     public void buildFilterChain( IoFilterChain chain ) throws Exception {
 *         chain.addLast( "myFilter", new MyFilter() );
 *     }
 * }
 * </pre>
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (Fri, 13 Jul 2007) $
 */
public interface IoFilterChainBuilder {
    /**
     * An implementation which does nothing.
     */
    IoFilterChainBuilder NOOP = new IoFilterChainBuilder() {
        public void buildFilterChain(IoFilterChain chain) throws Exception {
        }

        public String toString() {
            return "NOOP";
        }
    };

    /**
     * Modifies the specified <tt>chain</tt>.
     */
    void buildFilterChain(IoFilterChain chain) throws Exception;
}
