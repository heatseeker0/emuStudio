/*
 * BrainTerminal.java
 * 
 * Copyright (C) 2009-2012 Peter Jakubčo
 * KISS, YAGNI, DRY
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
package net.sf.emustudio.brainduck.terminal.impl;

import emulib.annotations.PLUGIN_TYPE;
import emulib.annotations.PluginType;
import emulib.emustudio.SettingsManager;
import emulib.plugins.device.AbstractDevice;
import emulib.plugins.device.DeviceContext;
import emulib.runtime.ContextPool;
import emulib.runtime.InvalidContextException;
import emulib.runtime.StaticDialogs;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.sf.emustudio.brainduck.cpu.BrainCPUContext;
import net.sf.emustudio.brainduck.terminal.gui.BrainTerminalDialog;

@PluginType(type = PLUGIN_TYPE.DEVICE,
title = "BrainDuck terminal",
copyright = "\u00A9 Copyright 2009-2012, Peter Jakubčo",
description = "Terminal device for abstract BrainDuck architecture.")
public class BrainTerminal extends AbstractDevice {
    private BrainCPUContext cpu;
    private BrainTerminalContext terminal;
    private BrainTerminalDialog gui;

    public BrainTerminal(Long pluginID) {
        super(pluginID);
        gui = new BrainTerminalDialog();
        terminal = new BrainTerminalContext(gui);
        
        try {
            ContextPool.getInstance().register(pluginID, terminal, DeviceContext.class);
        } catch (Exception e) {
            StaticDialogs.showErrorMessage("Could not register CPU Context",
                    getTitle());
        }
    }

    @Override
    public String getVersion() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("net.sf.emustudio.brainduck.terminal.version");
            return bundle.getString("version");
        } catch (MissingResourceException e) {
            return "(unknown)";
        }
    }

    @Override
    public boolean initialize(SettingsManager settings) {
        super.initialize(settings);

        try {
            cpu = (BrainCPUContext)ContextPool.getInstance().getCPUContext(pluginID, BrainCPUContext.class);
        } catch (InvalidContextException e) {
            // Will be processed
        }
        
        if (cpu == null) {
            StaticDialogs.showErrorMessage("BrainTerminal needs to be connected to the BrainCPU.");
            return false;
        }
        cpu.attachDevice(terminal);

        // read settings
        String s = settings.readSetting(pluginID, "verbose");
        if ((s != null) && (s.toUpperCase().equals("TRUE"))) {
            gui.setVerbose(true);
            gui.setVisible(true);
        } else {
            gui.setVerbose(false);
        }
        return true;
    }

    @Override
    public void reset() {
        gui.clearScreen();
    }

    @Override
    public void destroy() {
        gui.dispose();
        gui = null;
    }
   

    @Override
    public void showGUI() {
        gui.setVisible(true);
    }

    @Override
    public void showSettings() {
        // we don't have settings GUI
    }

    @Override
    public boolean isShowSettingsSupported() {
        return false;
    }
}