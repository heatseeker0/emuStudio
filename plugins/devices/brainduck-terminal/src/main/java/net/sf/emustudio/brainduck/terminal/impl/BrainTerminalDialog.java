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
package net.sf.emustudio.brainduck.terminal.impl;

import net.sf.emustudio.brainduck.terminal.io.Cursor;
import net.sf.emustudio.brainduck.terminal.io.Display;
import net.sf.emustudio.brainduck.terminal.io.GUIUtils;
import net.sf.emustudio.brainduck.terminal.io.Keyboard;
import net.sf.emustudio.brainduck.terminal.io.OutputProvider;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Objects;
import java.util.StringTokenizer;

class BrainTerminalDialog extends JDialog implements OutputProvider, Keyboard.KeyboardListener {
    private final ImageIcon blueIcon;
    private final ImageIcon redIcon;
    private final ImageIcon greenIcon;
    
    private final Display canvas;
    private final Keyboard keyboard;

    private BrainTerminalDialog(Keyboard keyboard) {
        URL blueIconURL = getClass().getResource(
                "/net/sf/emustudio/brainduck/terminal/16_circle_blue.png"
        );
        URL redIconURL = getClass().getResource(
                "/net/sf/emustudio/brainduck/terminal/16_circle_red.png"
        );
        URL greenIconURL = getClass().getResource(
                "/net/sf/emustudio/brainduck/terminal/16_circle_green.png"
        );

        blueIcon = new ImageIcon(Objects.requireNonNull(blueIconURL));
        redIcon = new ImageIcon(Objects.requireNonNull(redIconURL));
        greenIcon = new ImageIcon(Objects.requireNonNull(greenIconURL));
        
        this.keyboard = keyboard;

        setTitle("BrainDuck Terminal");
        initComponents();
        setLocationRelativeTo(null);
        
        canvas = new Display();
        scrollPane.setViewportView(canvas);
        canvas.start();
    }

    static BrainTerminalDialog create(Keyboard keyboard) {
        BrainTerminalDialog dialog = new BrainTerminalDialog(keyboard);
        GUIUtils.addListenerRecursively(dialog, dialog.keyboard);
        dialog.keyboard.addListener(dialog);
        return dialog;
    }

    private void writeStarted() {
        lblStatusIcon.setIcon(redIcon);
        lblStatusIcon.repaint();
    }

    @Override
    public void readStarted() {
        lblStatusIcon.setIcon(greenIcon);
        lblStatusIcon.repaint();
        btnASCII.setEnabled(true);
    }

    @Override
    public void readEnded() {
        lblStatusIcon.setIcon(blueIcon);
        lblStatusIcon.repaint();
        btnASCII.setEnabled(false);
    }

    @Override
    public void reset() {
        canvas.clear();
    }

    @Override
    public void write(int character) {
        writeStarted();
        try {
            Cursor cursor = canvas.getTextCanvasCursor();
            if (character < 32) {
                switch (character) {
                    case 7:  /* bell */
                        break;
                    case 8:  /* backspace*/
                        cursor.back();
                        break;
                    case 9:
                        cursor.advance(4);
                        break;
                    case 0x0A: /* line feed */
                        cursor.newLine();
                        cursor.carriageReturn(); // simulate CR/LF
                        break;
                    case 0x0D: /* carriage return */
                        cursor.carriageReturn();
                        break;
                }
                return;
            }
            canvas.writeAtCursor(character);
        } finally {
            readEnded();
        }
    }

    @Override
    public void showGUI() {
        this.setVisible(true);
    }
    
    @Override
    public void close() {
        canvas.stop();
        GUIUtils.removeListenerRecursively(this, keyboard);
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel panelStatus = new javax.swing.JPanel();
        lblStatusIcon = new javax.swing.JLabel();
        btnASCII = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblStatusIcon.setIcon(blueIcon);
        lblStatusIcon.setToolTipText("Waiting for input? (red - yes, blue - no)");
        lblStatusIcon.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        btnASCII.setFont(btnASCII.getFont());
        btnASCII.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/emustudio/brainduck/terminal/16_ascii.png"))); // NOI18N
        btnASCII.setToolTipText("Input by ASCII code");
        btnASCII.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnASCII.setEnabled(false);
        btnASCII.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnASCII.addActionListener(this::btnASCIIActionPerformed);

        javax.swing.GroupLayout panelStatusLayout = new javax.swing.GroupLayout(panelStatus);
        panelStatus.setLayout(panelStatusLayout);
        panelStatusLayout.setHorizontalGroup(
            panelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStatusIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnASCII)
                .addContainerGap(664, Short.MAX_VALUE))
        );
        panelStatusLayout.setVerticalGroup(
            panelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblStatusIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnASCII, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
        );

        scrollPane.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnASCIIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnASCIIActionPerformed
        String asciiString = JOptionPane.showInputDialog(this, "Enter ASCII codes separated with spaces", "0");
        StringTokenizer tokenizer = new StringTokenizer(asciiString);
        
        while (tokenizer.hasMoreTokens()) {
            int ascii = Integer.decode(tokenizer.nextToken());
            keyboard.keyPressed(new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, ascii, (char)ascii));
        }
    }//GEN-LAST:event_btnASCIIActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnASCII;
    private javax.swing.JLabel lblStatusIcon;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables

}
