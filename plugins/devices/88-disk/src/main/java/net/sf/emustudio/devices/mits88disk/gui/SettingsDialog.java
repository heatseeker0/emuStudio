/*
 * KISS, YAGNI, DRY
 *
 * (c) Copyright 2006-2016, Peter Jakubčo
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
package net.sf.emustudio.devices.mits88disk.gui;

import emulib.emustudio.SettingsManager;
import emulib.runtime.StaticDialogs;
import net.sf.emustudio.devices.mits88disk.impl.DiskImpl;
import net.sf.emustudio.devices.mits88disk.impl.Drive;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static net.sf.emustudio.devices.mits88disk.impl.SettingsConstants.IMAGE;
import static net.sf.emustudio.devices.mits88disk.impl.SettingsConstants.PORT1_CPU;
import static net.sf.emustudio.devices.mits88disk.impl.SettingsConstants.PORT2_CPU;
import static net.sf.emustudio.devices.mits88disk.impl.SettingsConstants.PORT3_CPU;
import static net.sf.emustudio.devices.mits88disk.impl.SettingsConstants.SECTORS_COUNT;
import static net.sf.emustudio.devices.mits88disk.impl.SettingsConstants.SECTOR_LENGTH;

public class SettingsDialog extends javax.swing.JDialog {
    private final SettingsManager settings;
    private final List<Drive> drives;
    private final long pluginId;

    public SettingsDialog(DiskFrame parent, long pluginId, SettingsManager settings, List<Drive> drives) {
        super(parent);
        
        this.pluginId = pluginId;

        this.settings = Objects.requireNonNull(settings);
        this.drives = Objects.requireNonNull(drives);

        initComponents();

        readSettings();
        cmbDrive.setSelectedIndex(0);
        updateGUI(drives.get(0));
        setLocationRelativeTo(null);
    }

    private void readSettings() {
        String s;
        s = settings.readSetting(pluginId, PORT1_CPU);
        if (s != null) {
            txtPort1.setText(s);
        }
        s = settings.readSetting(pluginId, PORT2_CPU);
        if (s != null) {
            txtPort2.setText(s);
        }
        s = settings.readSetting(pluginId, PORT3_CPU);
        if (s != null) {
            txtPort3.setText(s);
        }
    }

    private void writeSettings() {
        settings.writeSetting(pluginId, PORT1_CPU, txtPort1.getText());
        settings.writeSetting(pluginId, PORT2_CPU, txtPort2.getText());
        settings.writeSetting(pluginId, PORT3_CPU, txtPort3.getText());

        for (int i = 0; i < drives.size(); i++) {
            Drive drive = drives.get(i);

            settings.writeSetting(pluginId, SECTORS_COUNT + i, String.valueOf(drive.getSectorsCount()));
            settings.writeSetting(pluginId, SECTOR_LENGTH + i, String.valueOf(drive.getSectorLength()));
            
            File file = drive.getImageFile();
            if (file != null) {
                settings.writeSetting(pluginId, IMAGE + i, file.getAbsolutePath());
            } else {
                settings.removeSetting(pluginId, IMAGE + i);
            }
        }
    }

    private void updateGUI(Drive drive) {
        txtSectorLength.setText(String.valueOf(drive.getSectorLength()));
        txtSectorsCount.setText(String.valueOf(drive.getSectorsCount()));

        File f = drive.getImageFile();
        if (f != null) {
            txtImageFile.setText(f.getAbsolutePath());
            btnUnmount.setEnabled(true);
        } else {
            txtImageFile.setText("");
            btnUnmount.setEnabled(false);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JTabbedPane jTabbedPane1 = new javax.swing.JTabbedPane();
        javax.swing.JPanel panelImages = new javax.swing.JPanel();
        cmbDrive = new javax.swing.JComboBox();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        txtImageFile = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        btnMount = new javax.swing.JButton();
        btnUnmount = new javax.swing.JButton();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel10 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel11 = new javax.swing.JLabel();
        btnDefaultParams = new javax.swing.JButton();
        txtSectorsCount = new javax.swing.JTextField();
        txtSectorLength = new javax.swing.JTextField();
        javax.swing.JPanel panelPorts = new javax.swing.JPanel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        txtPort1 = new javax.swing.JTextField();
        txtPort2 = new javax.swing.JTextField();
        txtPort3 = new javax.swing.JTextField();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel9 = new javax.swing.JLabel();
        btnDefault = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        chkSaveSettings = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MITS 88-DISK Settings");

        jTabbedPane1.setFont(jTabbedPane1.getFont());

        cmbDrive.setFont(cmbDrive.getFont().deriveFont((cmbDrive.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        cmbDrive.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Drive 0 (A)", "Drive 1 (B)", "Drive 2 (C)", "Drive 3 (D)", "Drive 4 (E)", "Drive 5 (F)", "Drive 6 (G)", "Drive 7 (H)", "Drive 8 (I)", "Drive 9 (J)", "Drive 10 (K)", "Drive 11 (L)", "Drive 12 (M)", "Drive 13 (N)", "Drive 14 (O)", "Drive 15 (P)" }));
        cmbDrive.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDriveItemStateChanged(evt);
            }
        });

        jLabel1.setFont(jLabel1.getFont().deriveFont((jLabel1.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        jLabel1.setText("Image file name:");

        txtImageFile.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtImageFileInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        btnBrowse.setFont(btnBrowse.getFont().deriveFont((btnBrowse.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        btnBrowse.setText("Browse...");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Image operations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        btnMount.setText("Mount");
        btnMount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMountActionPerformed(evt);
            }
        });

        btnUnmount.setText("Umount");
        btnUnmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnmountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUnmount)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMount)
                    .addComponent(btnUnmount))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(jLabel2.getFont().deriveFont((jLabel2.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        jLabel2.setText("Disk drive:");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Drive parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel10.setText("Sectors count:");

        jLabel11.setFont(jLabel11.getFont().deriveFont(jLabel11.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel11.setText("Sector length:");

        btnDefaultParams.setFont(btnDefaultParams.getFont().deriveFont(btnDefaultParams.getFont().getStyle() & ~java.awt.Font.BOLD));
        btnDefaultParams.setText("Change to Default");
        btnDefaultParams.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefaultParamsActionPerformed(evt);
            }
        });

        txtSectorsCount.setText("32");

        txtSectorLength.setText("137");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDefaultParams)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSectorsCount)
                            .addComponent(txtSectorLength, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtSectorsCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(txtSectorLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnDefaultParams)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelImagesLayout = new javax.swing.GroupLayout(panelImages);
        panelImages.setLayout(panelImagesLayout);
        panelImagesLayout.setHorizontalGroup(
            panelImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImagesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtImageFile)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelImagesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBrowse))
                    .addGroup(panelImagesLayout.createSequentialGroup()
                        .addGroup(panelImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(cmbDrive, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelImagesLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelImagesLayout.setVerticalGroup(
            panelImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImagesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbDrive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtImageFile, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBrowse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelImagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelImagesLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Disk Images", panelImages);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Note"));

        jLabel3.setFont(jLabel3.getFont().deriveFont((jLabel3.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        jLabel3.setText("Settings in this tab will be reflected after the restart of emuStudio.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Port 1:");

        jLabel5.setText("Port 2:");
        jLabel5.setToolTipText("");

        jLabel6.setText("Port 3:");

        txtPort1.setText("0x08");

        txtPort2.setText("0x09");
        txtPort2.setToolTipText("");

        txtPort3.setText("0x0A");

        jLabel7.setFont(jLabel7.getFont().deriveFont((jLabel7.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        jLabel7.setText("(IN: flags, OUT: select/unselect drive)");

        jLabel8.setFont(jLabel8.getFont().deriveFont((jLabel8.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        jLabel8.setText("(IN: current sector, OUT: set flags)");

        jLabel9.setFont(jLabel9.getFont().deriveFont((jLabel9.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        jLabel9.setText("(IN: read data, OUT: write data)");

        btnDefault.setFont(btnDefault.getFont().deriveFont((btnDefault.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        btnDefault.setText("Change to Default");
        btnDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefaultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPortsLayout = new javax.swing.GroupLayout(panelPorts);
        panelPorts.setLayout(panelPortsLayout);
        panelPortsLayout.setHorizontalGroup(
            panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPortsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelPortsLayout.createSequentialGroup()
                        .addGroup(panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPortsLayout.createSequentialGroup()
                                .addGroup(panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelPortsLayout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPort1))
                                    .addGroup(panelPortsLayout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPort2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelPortsLayout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPort3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addComponent(btnDefault))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelPortsLayout.setVerticalGroup(
            panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPortsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPort1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPort2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPort3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addComponent(btnDefault)
                .addContainerGap())
        );

        jTabbedPane1.addTab("CPU Ports", panelPorts);

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        chkSaveSettings.setFont(chkSaveSettings.getFont().deriveFont((chkSaveSettings.getFont().getStyle() & ~java.awt.Font.ITALIC) & ~java.awt.Font.BOLD));
        chkSaveSettings.setSelected(true);
        chkSaveSettings.setText("Save settings to the file");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chkSaveSettings)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(chkSaveSettings))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbDriveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDriveItemStateChanged
        updateGUI(drives.get(cmbDrive.getSelectedIndex()));
    }//GEN-LAST:event_cmbDriveItemStateChanged

    private void txtImageFileInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtImageFileInputMethodTextChanged
        if (txtImageFile.getText().equals("")) {
            btnMount.setEnabled(false);
        } else {
            btnMount.setEnabled(true);
        }
    }//GEN-LAST:event_txtImageFileInputMethodTextChanged

    private void btnUnmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnmountActionPerformed
        Drive drive = drives.get(cmbDrive.getSelectedIndex());
        drive.umount();
        updateGUI(drive);
    }//GEN-LAST:event_btnUnmountActionPerformed

    private void btnMountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMountActionPerformed
        Drive drive = drives.get(cmbDrive.getSelectedIndex());
        try {
            drive.mount(new File(txtImageFile.getText()));
        } catch (IOException ex) {
            StaticDialogs.showErrorMessage(ex.getMessage());
            txtImageFile.grabFocus();
        }
        updateGUI(drive);
    }//GEN-LAST:event_btnMountActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        JFileChooser f = new JFileChooser();
        ImageFilter f1 = new ImageFilter();
        ImageFilter f2 = new ImageFilter();

        f1.addExtension("dsk");
        f1.addExtension("bin");
        f1.setDescription("Image files (*.dsk, *.bin)");
        f2.addExtension("*");
        f2.setDescription("All files (*.*)");

        f.setDialogTitle("Open an image");
        f.setAcceptAllFileFilterUsed(false);
        f.addChoosableFileFilter(f1);
        f.addChoosableFileFilter(f2);
        f.setFileFilter(f1);
        f.setApproveButtonText("Open");
        int i = cmbDrive.getSelectedIndex();
        File ff = drives.get(i).getImageFile();
        f.setSelectedFile(ff);
        if (ff == null) {
            f.setCurrentDirectory(new File(System.getProperty("user.dir")));
        }
        int returnVal = f.showOpenDialog(this);
        f.setVisible(true);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            txtImageFile.setText(f.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        try {
            Integer.decode(txtPort1.getText());
        } catch (NumberFormatException e) {
            StaticDialogs.showErrorMessage("Port1: Bad number");
            txtPort1.grabFocus();
            return;
        }
        try {
            Integer.decode(txtPort2.getText());
        } catch (NumberFormatException e) {
            StaticDialogs.showErrorMessage("Port2: Bad number");
            txtPort2.grabFocus();
            return;
        }
        try {
            Integer.decode(txtPort3.getText());
        } catch (NumberFormatException e) {
            StaticDialogs.showErrorMessage("Port3: Bad number");
            txtPort3.grabFocus();
            return;
        }
        
        Drive drive = drives.get(cmbDrive.getSelectedIndex());
        try {
            drive.setSectorsCount(Short.parseShort(txtSectorsCount.getText()));
        } catch (Exception e) {
            StaticDialogs.showErrorMessage("Sectors count: Bad number\n" + e.getMessage());
            txtSectorsCount.grabFocus();
            return;
        }
        try {
            drive.setSectorLength(Short.parseShort(txtSectorLength.getText()));
        } catch (Exception e) {
            StaticDialogs.showErrorMessage("Sector length: Bad number\n" + e.getMessage());
            txtSectorLength.grabFocus();
            return;
        }

        if (chkSaveSettings.isSelected()) {
            writeSettings();
        }
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefaultActionPerformed
        txtPort1.setText(String.format("0x%02X", DiskImpl.DEFAULT_CPU_PORT1));
        txtPort2.setText(String.format("0x%02X", DiskImpl.DEFAULT_CPU_PORT2));
        txtPort3.setText(String.format("0x%02X", DiskImpl.DEFAULT_CPU_PORT3));
    }//GEN-LAST:event_btnDefaultActionPerformed

    private void btnDefaultParamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefaultParamsActionPerformed
        txtSectorsCount.setText(String.valueOf(Drive.DEFAULT_SECTORS_COUNT));
        txtSectorLength.setText(String.valueOf(Drive.DEFAULT_SECTOR_LENGTH));
    }//GEN-LAST:event_btnDefaultParamsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnDefault;
    private javax.swing.JButton btnDefaultParams;
    private javax.swing.JButton btnMount;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnUnmount;
    private javax.swing.JCheckBox chkSaveSettings;
    private javax.swing.JComboBox cmbDrive;
    private javax.swing.JTextField txtImageFile;
    private javax.swing.JTextField txtPort1;
    private javax.swing.JTextField txtPort2;
    private javax.swing.JTextField txtPort3;
    private javax.swing.JTextField txtSectorLength;
    private javax.swing.JTextField txtSectorsCount;
    // End of variables declaration//GEN-END:variables
}
