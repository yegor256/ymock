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
package com.ymock.mock.socket;

import com.ymock.util.Logger;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Mock version of {@link SocketImplFactory}.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 * @checkstyle AbstractClassName (50 lines)
 */
public final class SMSocketImplFactory implements SocketImplFactory {

    /**
     * Singleton.
     */
    public static final SMSocketImplFactory INSTANCE =
        new SMSocketImplFactory();

    /**
     * Regex for connections.
     */
    private final transient Collection<String> hosts =
        new ArrayList<String>();

    /**
     * Real socket.
     */
    private transient Socket original;

    /**
     * Public ctor.
     */
    private SMSocketImplFactory() {
        Logger.debug(
            this,
            "#SMSocketImplFactory(): instantiated"
        );
    }

    /**
     * Start it working.
     * @return This object
     * @throws IOException If some problem inside
     */
    public SMSocketImplFactory start() throws IOException {
        this.original = new Socket();
        Socket.setSocketImplFactory(this);
        Logger.debug(
            SMSocketImplFactory.class,
            "#start(): started"
        );
        return this;
    }

    /**
     * Add new host name to match.
     * @param host What host do we match?
     * @return This object
     */
    public SMSocketImplFactory match(final String host) {
        this.hosts.add(host);
        Logger.debug(
            SMSocketImplFactory.class,
            "#match('%s'): added",
            host
        );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SocketImpl createSocketImpl() {
        final StringBuilder pattern = new StringBuilder();
        pattern.append("^");
        boolean first = true;
        for (String host : this.hosts) {
            if (!first) {
                pattern.append("|");
            }
            first = false;
            pattern.append("(").append(Pattern.quote(host)).append(")");
        }
        pattern.append("$");
        final SocketImpl socket = new SMSocketImpl(
            Pattern.compile(pattern.toString()),
            this.original
        );
        Logger.debug(
            this,
            "#createSocketImpl(): instantiated with '%s' pattern",
            pattern.toString()
        );
        return socket;
    }

}
