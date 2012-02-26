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
package com.ymock.server;

import com.ymock.client.Connector;
import com.ymock.client.YMockClient;
import com.ymock.commons.YMockException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

/**
 * Simple client.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
final class SimpleClient {

    /**
     * ID.
     */
    public static final String ID = "test";

    /**
     * Client.
     */
    private final transient YMockClient client;

    /**
     * Public ctor.
     */
    public SimpleClient() {
        this.client = new YMockClient(this.ID);
    }

    /**
     * Public ctor.
     * @param connector The connector to use
     */
    public SimpleClient(final Connector connector) {
        this.client = new YMockClient(this.ID, connector);
    }

    /**
     * Run.
     */
    public void run() {
        String response;
        try {
            response = this.client.call(YMockServerTest.REQUEST);
        } catch (YMockException ex) {
            throw new IllegalStateException(ex);
        }
        MatcherAssert.assertThat(
            response,
            Matchers.equalTo(YMockServerTest.RESPONSE)
        );
    }

}
