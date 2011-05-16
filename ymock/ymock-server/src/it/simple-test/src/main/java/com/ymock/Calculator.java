/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
package com.ymock;

import com.ymock.client.YMockClient;
import com.ymock.commons.Logger;
import com.ymock.commons.YMockException;

class Calculator {

    public Integer calculate(final String text) {
        Logger.info(this, "#calculate('%s')", text);
        Integer result;
        try {
            result = Integer.valueOf(new YMockClient("calculator").call(text));
        } catch (YMockException ex) {
            throw new RuntimeException(ex);
        }
        Logger.info(this, "#calculate('%s'): returned %d", result);
        return result;
    }

}
