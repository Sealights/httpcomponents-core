/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.nio.testserver;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.DefaultNHttpClientConnection;
import org.apache.http.nio.NHttpClientEventHandler;
import org.apache.http.nio.reactor.IOSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingNHttpClientConnection extends DefaultNHttpClientConnection {

    private static final AtomicLong COUNT = new AtomicLong();

    private final Logger log;
    private final Logger iolog;
    private final Logger headerLog;
    private final Logger wireLog;
    private final String id;

    public LoggingNHttpClientConnection(final IOSession session) {
        super(session, 8 * 1024);
        this.log = LoggerFactory.getLogger(getClass());
        this.iolog = LoggerFactory.getLogger(session.getClass());
        this.headerLog = LoggerFactory.getLogger("org.apache.http.headers");
        this.wireLog = LoggerFactory.getLogger("org.apache.http.wire");
        this.id = "http-outgoing-" + COUNT.incrementAndGet();
        if (this.iolog.isDebugEnabled() || this.wireLog.isDebugEnabled()) {
            this.session = new LoggingIOSession(session, this.id, this.iolog, this.wireLog);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.id + ": Close connection");
        }
        super.close();
    }

    @Override
    public void shutdown() throws IOException {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.id + ": Shutdown connection");
        }
        super.shutdown();
    }

    @Override
    public void submitRequest(final HttpRequest request) throws IOException, HttpException {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.id + ": "  + request.getRequestLine().toString());
        }
        super.submitRequest(request);
    }

    @Override
    public void consumeInput(final NHttpClientEventHandler handler) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.id + ": Consume input");
        }
        super.consumeInput(handler);
    }

    @Override
    public void produceOutput(final NHttpClientEventHandler handler) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.id + ": Produce output");
        }
        super.produceOutput(handler);
    }

    @Override
    protected void onResponseReceived(final HttpResponse response) {
        if (response != null && this.headerLog.isDebugEnabled()) {
            this.headerLog.debug(this.id + " << " + response.getStatusLine().toString());
            final Header[] headers = response.getAllHeaders();
            for (final Header header : headers) {
                this.headerLog.debug(this.id + " << " + header.toString());
            }
        }
    }

    @Override
    protected void onRequestSubmitted(final HttpRequest request) {
        if (request != null && this.headerLog.isDebugEnabled()) {
            this.headerLog.debug(id + " >> " + request.getRequestLine().toString());
            final Header[] headers = request.getAllHeaders();
            for (final Header header : headers) {
                this.headerLog.debug(this.id + " >> " + header.toString());
            }
        }
    }

    @Override
    public String toString() {
        return this.id;
    }

}
