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
package net.sf.emustudio.devices.mits88sio.gui;

import net.sf.emustudio.devices.mits88sio.impl.SIOSettings;

import javax.swing.*;

public class ConfigDialog extends javax.swing.JDialog {
    private SIOSettings settings;

    public ConfigDialog(SIOSettings settings) {
        super((JFrame)null, true);
        this.settings = settings;
        initComponents();
        setLocationRelativeTo(null);

        readSettings();
    }

    private void readSettings() {
        spnStatusPort.setValue(settings.getStatusPortNumber());
        spnDataPort.setValue(settings.getDataPortNumber());
    }

    private void writeSettings() {
        try {
            settings.setStatusPortNumber((Integer)spnStatusPort.getValue());
            settings.setDataPortNumber((Integer)spnDataPort.getValue());
        } catch (NumberFormatException e) {}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        spnStatusPort = new javax.swing.JSpinner();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        spnDataPort = new javax.swing.JSpinner();
        javax.swing.JButton btnStatusDefault = new javax.swing.JButton();
        javax.swing.JButton btnDataDefault = new javax.swing.JButton();
        javax.swing.JButton btnOK = new javax.swing.JButton();
        chkSaveSettings = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MITS 88-SIO Configuration");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CPU Connection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel1.setText("Status port:");

        spnStatusPort.setModel(new javax.swing.SpinnerNumberModel(17, 0, 255, 1));

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel2.setText("Data port:");

        spnDataPort.setModel(new javax.swing.SpinnerNumberModel(18, 0, 255, 1));

        btnStatusDefault.setFont(btnStatusDefault.getFont().deriveFont(btnStatusDefault.getFont().getStyle() & ~java.awt.Font.BOLD));
        btnStatusDefault.setText("Default");
        btnStatusDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatusDefaultActionPerformed(evt);
            }
        });

        btnDataDefault.setFont(btnDataDefault.getFont().deriveFont(btnDataDefault.getFont().getStyle() & ~java.awt.Font.BOLD));
        btnDataDefault.setText("Default");
        btnDataDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataDefaultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(spnStatusPort, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnStatusDefault))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(spnDataPort, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDataDefault)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(spnStatusPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStatusDefault))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(spnDataPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDataDefault)))
        );

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        chkSaveSettings.setFont(chkSaveSettings.getFont().deriveFont(chkSaveSettings.getFont().getStyle() & ~java.awt.Font.BOLD));
        chkSaveSettings.setSelected(true);
        chkSaveSettings.setText("Save settings to the file");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chkSaveSettings)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(12, 248, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkSaveSettings)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOK)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStatusDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatusDefaultActionPerformed
        spnStatusPort.setValue(SIOSettings.DEFAULT_STATUS_PORT_NUMBER);
    }//GEN-LAST:event_btnStatusDefaultActionPerformed

    private void btnDataDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataDefaultActionPerformed
        spnDataPort.setValue(SIOSettings.DEFAULT_DATA_PORT_NUMBER);
    }//GEN-LAST:event_btnDataDefaultActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        writeSettings();
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkSaveSettings;
    private javax.swing.JSpinner spnDataPort;
    private javax.swing.JSpinner spnStatusPort;
    // End of variables declaration//GEN-END:variables
}
