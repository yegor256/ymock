/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
package com.ymock;

import com.jcabi.log.Logger;

class Client {

    public Integer calculate(final String text) {
        Logger.info(this, "#calculate('%s')", text);
        return new Calculator().calculate(text);
    }

}
