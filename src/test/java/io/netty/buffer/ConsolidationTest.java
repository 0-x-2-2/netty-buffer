/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.buffer;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static io.netty.buffer.Unpooled.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests buffer consolidation
 */
public class ConsolidationTest {
    @Test
    public void shouldWrapInSequence() {
        ByteBuf currentBuffer = wrappedBuffer(wrappedBuffer("a".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("=".getBytes(StandardCharsets.US_ASCII)));
        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("1".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("&".getBytes(StandardCharsets.US_ASCII)));

        ByteBuf copy = currentBuffer.copy();
        String s = copy.toString(StandardCharsets.US_ASCII);
        assertEquals("a=1&", s);

        currentBuffer.release();
        copy.release();
    }

    @Test
    public void shouldConsolidationInSequence() {
        ByteBuf currentBuffer = wrappedBuffer(wrappedBuffer("a".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("=".getBytes(StandardCharsets.US_ASCII)));
        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("1".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("&".getBytes(StandardCharsets.US_ASCII)));

        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("b".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("=".getBytes(StandardCharsets.US_ASCII)));
        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("2".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("&".getBytes(StandardCharsets.US_ASCII)));

        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("c".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("=".getBytes(StandardCharsets.US_ASCII)));
        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("3".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("&".getBytes(StandardCharsets.US_ASCII)));

        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("d".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("=".getBytes(StandardCharsets.US_ASCII)));
        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("4".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("&".getBytes(StandardCharsets.US_ASCII)));

        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("e".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("=".getBytes(StandardCharsets.US_ASCII)));
        currentBuffer = wrappedBuffer(currentBuffer, wrappedBuffer("5".getBytes(StandardCharsets.US_ASCII)),
                wrappedBuffer("&".getBytes(StandardCharsets.US_ASCII)));

        ByteBuf copy = currentBuffer.copy();
        String s = copy.toString(StandardCharsets.US_ASCII);
        assertEquals("a=1&b=2&c=3&d=4&e=5&", s);

        currentBuffer.release();
        copy.release();
    }
}
