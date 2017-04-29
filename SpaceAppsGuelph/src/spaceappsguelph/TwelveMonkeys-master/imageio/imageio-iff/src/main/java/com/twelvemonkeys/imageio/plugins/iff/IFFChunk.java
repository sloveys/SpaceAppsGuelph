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

package com.twelvemonkeys.imageio.plugins.iff;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

/**
 * IFFChunk
 * <p/>
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @version $Id: IFFChunk.java,v 1.0 28.feb.2006 00:00:45 haku Exp$
 */
abstract class IFFChunk {
    int chunkId;
    int chunkLength;

    protected IFFChunk(int pChunkId, int pChunkLength) {
        chunkId = pChunkId;
        chunkLength = pChunkLength;
    }

    abstract void readChunk(DataInput pInput) throws IOException;

    abstract void writeChunk(DataOutput pOutput) throws IOException;

    protected static void skipData(final DataInput pInput, final int chunkLength, final int dataReadSoFar) throws IOException {
        int toSkip = chunkLength - dataReadSoFar;

        while (toSkip > 0) {
            toSkip -= pInput.skipBytes(toSkip);
        }

        // Read pad
        if (chunkLength % 2 != 0) {
            pInput.readByte();
        }
    }

    public String toString() {
        return IFFUtil.toChunkStr(chunkId) + " chunk (" + chunkLength + " bytes)";
    }
}
