/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
package com.ymock;

class Client {

    public Integer calculate(final String text) {
        return new Calculator().calculate(text);
    }

}
