/*
 * Copyright (c) 2015-2016 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android;

@SuppressWarnings("unused")
public final class Equals {
    //TODO replace with Objects.equals() once I can use Java 7 APIs
    public static boolean equals(final Object o1, final Object o2) {
        return o1 == o2 || o1 != null && o1.equals(o2);
    }

    public static boolean nanSafeEquals(final float f1, final float f2) {
        return f1 == f2 || f1 != f1 && f2 != f2;
    }

    private Equals() {
        throw new UnsupportedOperationException();
    }
}
