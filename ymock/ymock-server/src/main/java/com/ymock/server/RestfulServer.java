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

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.ymock.commons.PortDetector;
import com.ymock.commons.YMockException;
import com.ymock.util.Logger;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * RESTful Server.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
final class RestfulServer implements CallsProvider {

    /**
     * Already running instance.
     */
    public static final RestfulServer INSTANCE = new RestfulServer();

    /**
     * HTTP Context.
     */
    private static final String CONTEXT = "/ymock";

    /**
     * Catcher registered.
     */
    private transient Catcher catcher;

    /**
     * Public ctor, for a RESTful instantiation.
     */
    private RestfulServer() {
        this.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(final Catcher ctr) {
        this.catcher = ctr;
    }

    /**
     * Make a call.
     * @param request The request
     * @return The response
     * @see RestfulMock#call(String)
     * @throws YMockException If something is wrong there
     * @checkstyle RedundantThrows (2 lines)
     */
    public Response call(final String request) throws YMockException {
        return this.catcher.call(request);
    }

    /**
     * What HTTP port shall we use?
     * @return The port number
     * @see #start()
     */
    public Integer port() {
        return new PortDetector().port();
    }

    /**
     * Start HTTP server.
     * @see #RestfulServer()
     * @todo #1 Would be nice to add exception mapping here, in order
     *  to properly wrap exception messages sent to clients
     */
    private void start() {
        final java.util.logging.Logger root =
            LogManager.getLogManager().getLogger("");
        final Handler[] handlers = root.getHandlers();
        for (int pos = 0; pos < handlers.length; pos += 1) {
            root.removeHandler(handlers[pos]);
        }
        SLF4JBridgeHandler.install();
        final GrizzlyWebServer gws = new GrizzlyWebServer(this.port(), ".");
        final ServletAdapter adapter = new ServletAdapter();
        adapter.addInitParameter(
            "com.sun.jersey.config.property.packages",
            "com.ymock.server"
        );
        adapter.setContextPath(this.CONTEXT);
        adapter.setServletInstance(new ServletContainer());
        gws.addGrizzlyAdapter(adapter, new String[] {this.CONTEXT});
        try {
            gws.start();
        } catch (java.io.IOException ex) {
            throw new IllegalStateException(ex);
        }
        Logger.debug(
            this,
            "#start(): server started"
        );
    }

}
