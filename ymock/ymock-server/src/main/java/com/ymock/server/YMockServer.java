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
package com.ymock.server;

// supplementary concrete classes
import com.ymock.server.matchers.RegexMatcher;
import com.ymock.server.responses.ErrorResponse;
import com.ymock.server.responses.TextResponse;

// logging
import com.ymock.util.Logger;

// maps
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// sting manipulations, from commons-lang:commons-lang
import org.apache.commons.lang.StringUtils;

/**
 * RESTful Server.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class YMockServer implements Catcher {

    /**
     * Associative array of matchers.
     */
    private final Map<Matcher, Response> matchers =
        new HashMap<Matcher, Response>();

    /**
     * Public ctor.
     * @param client ID of the client to listen to
     */
    public YMockServer(final String client) {
        this(client, RestfulServer.INSTANCE);
    }

    /**
     * Public ctor.
     * @param client ID of the client to listen to
     * @param provider Calls provider
     */
    public YMockServer(final String client, final CallsProvider provider) {
        provider.register(this);
        Logger.debug(
            this,
            "#YMockServer('%s', %s): listening",
            client,
            provider.getClass().getName()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response call(final String request) {
        Response response = null;
        final Collection<String> errors = new ArrayList<String>();
        for (Map.Entry<Matcher, Response> entry : this.matchers.entrySet()) {
            if (entry.getKey().matches(request)) {
                if (response != null) {
                    errors.add(
                        String.format(
                            "Duplicate matching by '%s'",
                            entry.getKey().getClass().getName()
                        )
                    );
                } else {
                    response = entry.getValue();
                    Logger.debug(
                        this,
                        "#call('%d bytes'): matched with %s",
                        request.length(),
                        entry.getKey().getClass().getName()
                    );
                }
            }
        }
        if (response == null) {
            errors.add("No matchers found");
        }
        if (!errors.isEmpty()) {
            return new ErrorResponse(
                "Problems discovered: [%s]",
                StringUtils.join(errors, ", ")
            );
        }
        Logger.debug(
            this,
            "#call('%d bytes'): response %s found (among %d)",
            request.length(),
            response.getClass().getName(),
            this.matchers.size()
        );
        return response;
    }

    /**
     * Create new behavior.
     * @param matcher The matcher
     * @param response The response to send
     */
    public void when(final Matcher matcher, final Response response) {
        this.matchers.put(matcher, response);
    }

    /**
     * Create new behavior, using strings.
     * @param matcher The matcher
     * @param response The response to send
     */
    public void when(final String matcher, final String response) {
        this.when(new RegexMatcher(matcher), new TextResponse(response));
    }

}
