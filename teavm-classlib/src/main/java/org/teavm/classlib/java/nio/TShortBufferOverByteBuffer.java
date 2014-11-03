/*
 *  Copyright 2014 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.classlib.java.nio;

/**
 *
 * @author Alexey Andreev <konsoletyper@gmail.com>
 */
class TShortBufferOverByteBuffer extends TShortBufferImpl {
    private TByteBufferImpl byteByffer;
    TByteOrder byteOrder = TByteOrder.BIG_ENDIAN;
    boolean readOnly;
    private int start;

    public TShortBufferOverByteBuffer(int start, int capacity, TByteBufferImpl byteBuffer, int position, int limit,
            boolean readOnly) {
        super(capacity, position, limit);
        this.start = start;
        this.byteByffer = byteBuffer;
        this.readOnly = readOnly;
    }

    @Override
    TShortBuffer duplicate(int start, int capacity, int position, int limit, boolean readOnly) {
        TShortBufferOverByteBuffer result = new TShortBufferOverByteBuffer(this.start + start * 2, capacity,
                byteByffer, position, limit, readOnly);
        result.byteOrder = byteOrder;
        return result;
    }

    @Override
    short getElement(int index) {
        int value;
        if (byteOrder == TByteOrder.BIG_ENDIAN) {
            value = (byteByffer.array[start + index * 2] << 8) | (byteByffer.array[start + index * 2 + 1]);
        } else {
            value = (byteByffer.array[start + index * 2 + 1] << 8) | (byteByffer.array[start + index * 2]);
        }
        return (short)value;
    }

    @Override
    void putElement(int index, short value) {
        if (byteOrder == TByteOrder.BIG_ENDIAN) {
            byteByffer.array[start + index * 2] = (byte)(value >> 8);
            byteByffer.array[start + index * 2 + 1] = (byte)value;
        } else {
            byteByffer.array[start + index * 2] = (byte)value;
            byteByffer.array[start + index * 2 + 1] = (byte)(value >> 8);
        }
    }

    @Override
    boolean isArrayPresent() {
        return false;
    }

    @Override
    short[] getArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    int getArrayOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    boolean readOnly() {
        return readOnly;
    }

    @Override
    public TByteOrder order() {
        return byteOrder;
    }
}