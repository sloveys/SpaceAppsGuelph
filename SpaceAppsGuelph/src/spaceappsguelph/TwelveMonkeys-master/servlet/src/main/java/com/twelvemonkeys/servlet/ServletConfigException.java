/*
 * Copyright (c) 2008, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name "TwelveMonkeys" nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.twelvemonkeys.servlet;

import javax.servlet.ServletException;

/**
 * ServletConfigException.
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @version $Id: ServletConfigException.java#2 $
 */
public class ServletConfigException extends ServletException {

    // TODO: Parameters for init-param at fault, and possibly servlet name?

    /**
     * Creates a {@code ServletConfigException} with the given message.
     *
     * @param pMessage the exception message
     */
    public ServletConfigException(String pMessage) {
        super(pMessage);
    }

    /**
     * Creates a {@code ServletConfigException} with the given message and cause.
     *
     * @param pMessage the exception message
     * @param pCause the exception cause
     */
    public ServletConfigException(final String pMessage, final Throwable pCause) {
        super(pMessage, pCause);

        maybeInitCause(pCause);
    }

    /**
     * Creates a {@code ServletConfigException} with the cause.
     *
     * @param pCause the exception cause
     */
    public ServletConfigException(final Throwable pCause) {
        super(String.format("Error in Servlet configuration: %s", pCause.getMessage()), pCause);

        maybeInitCause(pCause);
    }

    private void maybeInitCause(Throwable pCause) {
        // Workaround for ServletExceptions that does not do proper exception chaining
        if (getCause() == null) {
            initCause(pCause);
        }
    }
}
