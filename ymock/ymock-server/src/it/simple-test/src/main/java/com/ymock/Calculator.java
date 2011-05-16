/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
package com.ymock;

import com.ymock.client.YMockClient;
import com.ymock.commons.YMockException;

class Calculator {

    public Integer calculate(final String text) {
        try {
            return Integer.valueOf(new YMockClient("calculator").call(text));
        } catch (YMockException ex) {
            throw new RuntimeException(ex);
        }
    }

}
