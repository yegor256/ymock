/**
 * Copyright (c) 2011-2012, yMock.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the yMock.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ymock.client;

import com.ymock.commons.YMockException;

/**
 * RESTful client.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class YMockClient {

    /**
     * The connector to use.
     */
    private transient Connector connector;

    /**
     * Public ctor.
     * @param name The unique name of the client
     */
    public YMockClient(final String name) {
        this(name, new HttpConnector());
    }

    /**
     * Public ctor.
     * @param name The unique name of the client
     * @param conn The connector to use
     * @todo #1 The name is just ingored now, an it's not correct. We should
     *  use it in order to build the URL of the server. The server will
     *  filter out requests not related to the specified name.
     */
    public YMockClient(final String name, final Connector conn) {
        assert name != null;
        assert conn != null;
        this.connector = conn;
    }

    /**
     * Make a call to server and return a response.
     * @param request The request string
     * @return The response
     * @throws YMockException If something was wrong with the server
     *  and the operation failed.
     * @checkstyle RedundantThrows (2 lines)
     */
    public String call(final String request) throws YMockException {
        return this.connector.call(request);
    }

}
