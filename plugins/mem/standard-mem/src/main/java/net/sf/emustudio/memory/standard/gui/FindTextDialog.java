/*
 * KISS, YAGNI, DRY
 *
 * (c) Copyright 2006-2017, Peter Jakubčo
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
package net.sf.emustudio.memory.standard.gui;

import net.sf.emustudio.memory.standard.gui.model.MemoryTableModel;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindTextDialog extends javax.swing.JDialog {
    private final MemoryTableModel memModel;
    private final int currentAddress;
    private int foundAddress = -1;

    public FindTextDialog(JDialog parent, MemoryTableModel memModel, int currentAddress) {
        super(parent, true);
        super.setLocationRelativeTo(parent);

        this.memModel = Objects.requireNonNull(memModel);
        this.currentAddress = currentAddress;
        initComponents();
    }

    public int getFoundAddress() {
        return foundAddress;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupSequenceToFind = new javax.swing.ButtonGroup();
        btnGroupStartPosition = new javax.swing.ButtonGroup();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        txtSequence = new javax.swing.JTextField();
        radioPlainText = new javax.swing.JRadioButton();
        radioBytes = new javax.swing.JRadioButton();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        radioCurrentPage = new javax.swing.JRadioButton();
        radioSpecificPosition = new javax.swing.JRadioButton();
        txtPosition = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        btnGroupSequenceToFind.add(radioPlainText);
        btnGroupSequenceToFind.add(radioBytes);

        btnGroupStartPosition.add(radioCurrentPage);
        btnGroupStartPosition.add(radioSpecificPosition);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Find sequence");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Sequence to find"));

        radioPlainText.setFont(radioPlainText.getFont().deriveFont(radioPlainText.getFont().getStyle() & ~java.awt.Font.BOLD));
        radioPlainText.setSelected(true);
        radioPlainText.setText("Plain text (case-sensitive)");

        radioBytes.setFont(radioBytes.getFont().deriveFont(radioBytes.getFont().getStyle() & ~java.awt.Font.BOLD));
        radioBytes.setText("Sequence of bytes (space-separated)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(radioBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(radioPlainText)
                        .addComponent(txtSequence))
                    .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtSequence, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(radioPlainText)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(radioBytes)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Start position"));

        radioCurrentPage.setFont(radioCurrentPage.getFont().deriveFont(radioCurrentPage.getFont().getStyle() & ~java.awt.Font.BOLD));
        radioCurrentPage.setSelected(true);
        radioCurrentPage.setText("Current page");

        radioSpecificPosition.setFont(radioSpecificPosition.getFont().deriveFont(radioSpecificPosition.getFont().getStyle() & ~java.awt.Font.BOLD));
        radioSpecificPosition.setText("Specific position:");

        txtPosition.setText("0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(txtPosition))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(radioCurrentPage)
                                .addComponent(radioSpecificPosition))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(radioCurrentPage)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(radioSpecificPosition)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(24, Short.MAX_VALUE))
        );

        btnFind.setFont(btnFind.getFont().deriveFont(btnFind.getFont().getStyle() & ~java.awt.Font.BOLD));
        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(btnFind)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        try {
            int from = radioCurrentPage.isSelected() ? currentAddress : Integer.decode(txtPosition.getText());

            String text = txtSequence.getText();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter the sequence", "Find sequence", JOptionPane.ERROR_MESSAGE);
                txtSequence.requestFocus();
                return;
            }

            final byte[] sequence;
            if (radioPlainText.isSelected()) {
                sequence = text.getBytes();
            } else {
                List<Integer> mapped = Stream.of(text.split(" ")).map(Integer::decode).collect(Collectors.toList());
                sequence = new byte[mapped.size()];
                for (int i = 0; i < sequence.length; i++) {
                    sequence[i] = mapped.get(i).byteValue();
                }
            }

            foundAddress = memModel.findSequence(sequence, from);
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this, "Specific position", "Please enter valid address", JOptionPane.ERROR_MESSAGE
            );
            txtPosition.selectAll();
            txtPosition.requestFocus();
        }
    }//GEN-LAST:event_btnFindActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFind;
    private javax.swing.ButtonGroup btnGroupSequenceToFind;
    private javax.swing.ButtonGroup btnGroupStartPosition;
    private javax.swing.JRadioButton radioBytes;
    private javax.swing.JRadioButton radioCurrentPage;
    private javax.swing.JRadioButton radioPlainText;
    private javax.swing.JRadioButton radioSpecificPosition;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextField txtSequence;
    // End of variables declaration//GEN-END:variables
}
