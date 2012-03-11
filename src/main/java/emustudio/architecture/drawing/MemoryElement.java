/*
 * MemoryElement.java
 *
 * Created on 4.7.2008, 7:55:56
 * hold to: KISS, YAGNI
 *
 * Copyright (C) 2008-2011 Peter Jakubčo <pjakubco at gmail.com>
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

package emustudio.architecture.drawing;

import java.awt.Color;
import java.awt.Point;
import java.util.Properties;

/**
 *
 * @author vbmacher
 */
public class MemoryElement extends Element {
    private final static Color BACK_COLOR = new Color(0xeeeeee);

    /**
     * Creates an instance of the MemoryElement.
     *
     * @param pluginName file name of this plug-in, without '.jar' extension.
     * @param settings settings of this element from virtual configuration
     * @throws Exception when some settings are not well parseable
     */
    public MemoryElement(String pluginName, Properties settings) throws Exception {
        super(pluginName, settings, BACK_COLOR);
    }

    /**
     * Creates an instance of the MemoryElement.
     *
     * @param pluginName name of the compiler
     * @param location the point where the compiler is located in the schema
     */
    public MemoryElement(String pluginName, Point location) {
        super(pluginName, location, BACK_COLOR);
    }

    @Override
    protected String getPluginType() {
        return "Memory";
    }

}