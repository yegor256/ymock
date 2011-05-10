/**
 * Copyright (c) 2011, JdbcMock.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the JdbcMock.com nor
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
package com.jdbcmock;

// JDBC API 3.0
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;

// supplementary classes
import java.util.Properties;

/**
 * Jdbc Mock Driver.
 *
 * @author Yegor Bugayenko (yegor@jdbcmock.com)
 * @version $Id$
 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/sql/Driver.html">java.sql.Driver</a>
 */
public final class JMDriver implements Driver {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean acceptsURL(final String url) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection connect(final String url, final Properties info) {
        Logger.info(this, "#connect('%s'): connected", url);
        return new JMConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMajorVersion() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinorVersion() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url,
        final Properties info) {
        return new DriverPropertyInfo[] {};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean jdbcCompliant() {
        return true;
    }

}
