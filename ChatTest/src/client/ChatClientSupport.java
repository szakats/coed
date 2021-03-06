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
package client;

import java.net.SocketAddress;

import javax.net.ssl.SSLContext;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import ssl.BogusSSLContextFactory;
import org.apache.mina.filter.SSLFilter;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

/**
 * A simple chat client for a given user.
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev$, $Date$
 */
public class ChatClientSupport {
    private final IoHandler handler;

    private final String name;

    private IoSession session;

    public ChatClientSupport(String name, IoHandler handler) {
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null");
        }
        this.name = name;
        this.handler = handler;
    }

    public boolean connect(SocketConnector connector, SocketAddress address,
            boolean useSsl) {
        if (session != null && session.isConnected()) {
            throw new IllegalStateException(
                    "Already connected. Disconnect first.");
        }

        try {

            SocketConnectorConfig config = new SocketConnectorConfig();
            if (useSsl) {
                SSLContext sslContext = BogusSSLContextFactory
                        .getInstance(false);
                SSLFilter sslFilter = new SSLFilter(sslContext);
                sslFilter.setUseClientMode(true);
                config.getFilterChain().addLast("sslFilter", sslFilter);
            }

            ConnectFuture future1 = connector.connect(address, handler, config);
            future1.join();
            if (!future1.isConnected()) {
                return false;
            }
            session = future1.getSession();
            session.write("LOGIN " + name);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void broadcast(String message) {
        session.write("BROADCAST " + message);
    }
    
    public void serverCommand(String cmd) {
    	session.write(cmd);
    }

    public void quit() {
        if (session != null) {
            if (session.isConnected()) {
                session.write("QUIT");
                // Wait until the chat ends.
                session.getCloseFuture().join();
            }
            session.close();
        }
    }

}
