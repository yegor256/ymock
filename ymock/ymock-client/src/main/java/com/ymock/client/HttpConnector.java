/**
 * Copyright (c) 2011, yMock.com
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

// commons
import com.ymock.commons.PortDetector;

// apache httpcomponents:httpclient
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * HTTP connector between YMockClient and remote YMockServer.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class HttpConnector implements Connector {

    /**
     * HTTP client.
     */
    private final HttpClient client;

    /**
     * Public ctor.
     */
    public HttpConnector() {
        this(new DefaultHttpClient());
    }

    /**
     * Protected ctor, only for unit testing.
     */
    protected HttpConnector(final HttpClient clt) {
        this.client = clt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String call(final String request) throws YMockException {
        final HttpPost http = new HttpPost(this.url());
        final ResponseHandler<String> handler = new BasicResponseHandler();
        String body;
        try {
            body = this.client.execute(http, handler);
        } catch (java.io.IOException ex) {
            throw new YMockException(ex);
        } finally {
            this.client.getConnectionManager().shutdown();
        }
        return body;
    }

    /**
     * Build URL to connect to.
     * @return The URL
     */
    private String url() {
        return "http://localhost:"
            + new PortDetector().port()
            + "/ymock/mock";
    }

}
