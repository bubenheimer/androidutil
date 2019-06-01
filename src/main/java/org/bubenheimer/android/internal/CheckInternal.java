/*
 * Copyright (c) 2015-2019 Uli Bubenheimer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.bubenheimer.android.internal;

import org.bubenheimer.util.Uninstantiable;

@SuppressWarnings("unused")
public final class CheckInternal extends Uninstantiable {
    public static void notNull(final Object obj) {
        if (obj == null) {
            throw new AssertionError();
        }
    }

    public static void isNull(final Object obj) {
        if (obj != null) {
            throw new AssertionError();
        }
    }

    public static void equals(final int expected, final int actual) {
        if (expected != actual) {
            throw new AssertionError();
        }
    }

    public static void equals(final long expected, final long actual) {
        if (expected != actual) {
            throw new AssertionError();
        }
    }

    public static void equals(final short expected, final short actual) {
        if (expected != actual) {
            throw new AssertionError();
        }
    }

    public static void equals(final byte expected, final byte actual) {
        if (expected != actual) {
            throw new AssertionError();
        }
    }

    public static void equals(final char expected, final char actual) {
        if (expected != actual) {
            throw new AssertionError();
        }
    }

    public static void equals(final Object expected, final Object actual) {
        if (expected != actual && (expected == null || !expected.equals(actual))) {
            throw new AssertionError();
        }
    }

    public static void notEquals(final int expected, final int actual) {
        if (expected == actual) {
            throw new AssertionError();
        }
    }

    public static void notEquals(final long expected, final long actual) {
        if (expected == actual) {
            throw new AssertionError();
        }
    }

    public static void notEquals(final short expected, final short actual) {
        if (expected == actual) {
            throw new AssertionError();
        }
    }

    public static void notEquals(final byte expected, final byte actual) {
        if (expected == actual) {
            throw new AssertionError();
        }
    }

    public static void notEquals(final char expected, final char actual) {
        if (expected == actual) {
            throw new AssertionError();
        }
    }

    public static void notEquals(final Object expected, final Object actual) {
        if (expected == actual || (expected != null && expected.equals(actual))) {
            throw new AssertionError();
        }
    }

    public static void isTrue(final boolean value) {
        if (!value) {
            throw new AssertionError();
        }
    }

    public static void isFalse(final boolean value) {
        if (value) {
            throw new AssertionError();
        }
    }
}
