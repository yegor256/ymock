/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
package com.ymock;

import com.ymock.client.YMockClient;

class Calculator {

    public Integer calculate(final String text) {
        try {
            return Integer.valueOf(new YMockClient().call(text));
        } catch (YMockException ex) {
            throw new RuntimeException(ex);
        }
    }

}
