/*
 * KISS, YAGNI, DRY
 *
 * (c) Copyright 2006-2020, Peter Jakubčo
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.sf.emustudio.devices.adm3a.impl;

import java.util.Objects;

import static net.sf.emustudio.devices.adm3a.impl.LoadCursorPosition.ExpectedSequence.*;

class LoadCursorPosition {
    // Escape sequence byte
    private static final int ASCII_ESC = 0x1B;
    // Offset in ASCII for X and Y coordinate
    private static final int ASCII_COORDINATE_OFFSET = 32;

    enum ExpectedSequence {
        ESCAPE, ASSIGN, X, Y
    }

    private final Cursor cursor;

    private volatile ExpectedSequence expect = ESCAPE;
    private int cursorY;

    LoadCursorPosition(Cursor cursor) {
        this.cursor = Objects.requireNonNull(cursor);
    }

    /**
     * Checks bounds of the X and Y variables
     *
     * @param data received char
     * @return true if in bounds
     */
    private boolean checkBounds(Short data) {
        return data >= ' ' && data <= 'o';
    }

    boolean accept(Short data) {
        if (expect == ESCAPE && data == ASCII_ESC) {
            expect = ASSIGN;
            return true;
        } else if (expect == ASSIGN && data == '=') {
            expect = Y;
            return true;
        } else if (expect == Y) {
            if (!checkBounds(data)) {
                expect = ESCAPE;
                return true;
            }

            cursorY = data - ASCII_COORDINATE_OFFSET;
            expect = X;
            return true;
        } else if (expect == X) {
            int cursorX = data - ASCII_COORDINATE_OFFSET;

            if (checkBounds(data)) {
                cursor.set(cursorX, cursorY);
            }

            expect = ESCAPE;
            return true;
        }

        return false;
    }
}
