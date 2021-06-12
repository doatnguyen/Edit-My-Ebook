/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doat.main;

import java.awt.Dialog;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;

/**
 *
 * @author Administrator
 */
public class main extends javax.swing.JFrame {

    private final String PREF_KEY = "doat.editepub";
    private JFileChooser choice = null;
    private JFileChooser save_choice = null;
    private Book book = null;
    private boolean isSecBook = false;
    private ArrayList<TOCReference> listSec = new ArrayList<>();
    private int current_sec = 0, max_chap = 0, current_chap = 0;

    /**
     * Creates new form main
     */
    public main() {
        initComponents();
        init();
    }

    private void init() {
        this.setLocationRelativeTo(null);
        choice = new JFileChooser(new File(getReg()));
        choice.setFileSelectionMode(JFileChooser.FILES_ONLY);
        choice.setDialogType(JFileChooser.OPEN_DIALOG);
        choice.setFileFilter(new FileNameExtensionFilter(
                "Epub2 file (*.epub)", "epub")
        );
        choice.setDialogTitle("Open Epub File");

        pan_section_edit.setVisible(false);
        btn_change_bookname.setEnabled(false);
        this.pack();
//        txt_sec_name.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String name = txt_sec_name.getText();
//                if (!"".equals(name)) {
//                    listSec.get(current_sec).setTitle(name);
//                }
//            }
//        });
        tb_content.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    current_chap = row;
                    EditChapDialog mydialog = new EditChapDialog(main.this, true);
                    mydialog.setSize(mydialog.getPreferredSize());
                    mydialog.setTitle("Trình Sửa Chương");
                    mydialog.setLocationRelativeTo(null);
                    mydialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // prevent user from doing something else
                    if (isSecBook) {
                        mydialog.setData(listSec.get(current_sec).getChildren().get(row));
                    } else {
                        mydialog.setData(listSec.get(row));
                    }
                    mydialog.setVisible(true);
                    if (mydialog.isDelete()) {
                        deleteChap();
                    } else {
                        changeDataRow(row, 1, mydialog.getData().getTitle());
                    }
                }
            }
        });
    }

    private void resetView() {
        DefaultTableModel dtm = (DefaultTableModel) tb_content.getModel();
        dtm.setRowCount(0);
        txt_sec_name.setText("");
    }

    private void createDefaultView() {
        ArrayList<TOCReference> listview = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) tb_content.getModel();
        if (isSecBook) {
            pan_section_edit.setVisible(true);
            this.pack();
            listview.addAll(listSec.get(0).getChildren());
            txt_sec_name.setText(listSec.get(0).getTitle());
            txt_num_sec.setText(listSec.size() + "");
            txt_curent_sec.setText("1");
            current_sec = 0;

        } else {
            listview = listSec;
        }
        txt_bookname.setText(book.getTitle());
        btn_change_bookname.setEnabled(true);
        for (TOCReference t : listview) {
            model.addRow(new Object[]{listview.indexOf(t) + 1, t.getTitle()});
        }
    }

    private void refeshView() {
        JOptionPane msg = new JOptionPane("Reload View", JOptionPane.INFORMATION_MESSAGE);
        final JDialog dlg = msg.createDialog("Xin Chờ...");
        dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<TOCReference> listview = new ArrayList<>();
                DefaultTableModel model = (DefaultTableModel) tb_content.getModel();
                model.setRowCount(0);
                if (isSecBook) {
                    listview.addAll(listSec.get(current_sec).getChildren());
                    txt_sec_name.setText(listSec.get(current_sec).getTitle());
                    txt_num_sec.setText(listSec.size() + "");
                    txt_curent_sec.setText((current_sec + 1) + "");
                } else {
                    listview = listSec;
                }
                txt_bookname.setText(book.getTitle());
                for (TOCReference t : listview) {
                    model.addRow(new Object[]{listview.indexOf(t) + 1, t.getTitle()});
                }
                dlg.dispose();
            }
        });
        t.start();
        dlg.setVisible(true);
    }

    private void readBook(List<TOCReference> toc) {
        if (toc == null) {
            return;
        }
        listSec = new ArrayList<>();
        isSecBook = false;
        for (TOCReference tocReference : toc) {
            if (tocReference.getChildren().size() > 1) {
                listSec.add(tocReference);
                max_chap += tocReference.getChildren().size();
                isSecBook = true;
            }
        }
        if (!isSecBook) {
            listSec.addAll(toc);
            max_chap = toc.size();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem5 = new javax.swing.JMenuItem();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_content = new javax.swing.JTable();
        pan_section_edit = new javax.swing.JPanel();
        btn_pre_sec = new javax.swing.JButton();
        txt_sec_name = new javax.swing.JTextField();
        btn_next_sec = new javax.swing.JButton();
        txt_curent_sec = new javax.swing.JTextField();
        txt_num_sec = new javax.swing.JLabel();
        btn_go_page = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btn_change_name_sec = new javax.swing.JButton();
        btn_delete_sec = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txt_bookname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btn_change_bookname = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menu_file = new javax.swing.JMenu();
        menu_open = new javax.swing.JMenuItem();
        menu_save = new javax.swing.JMenuItem();
        menu_refesh = new javax.swing.JMenuItem();
        menu_open_edit = new javax.swing.JMenu();
        btn_del = new javax.swing.JMenuItem();
        menu_find_replace = new javax.swing.JMenuItem();

        jMenuItem5.setText("jMenuItem5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tb_content.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tiêu Đề"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_content.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tb_content);
        if (tb_content.getColumnModel().getColumnCount() > 0) {
            tb_content.getColumnModel().getColumn(0).setMinWidth(30);
            tb_content.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        btn_pre_sec.setText("Trước");
        btn_pre_sec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pre_secActionPerformed(evt);
            }
        });

        btn_next_sec.setText("Sau");
        btn_next_sec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_next_secActionPerformed(evt);
            }
        });

        txt_curent_sec.setText("1");

        txt_num_sec.setText("1");

        btn_go_page.setText("Đến");
        btn_go_page.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_go_pageActionPerformed(evt);
            }
        });

        jLabel1.setText("/");

        btn_change_name_sec.setText("Đổi Tên Quyển");
        btn_change_name_sec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_change_name_secActionPerformed(evt);
            }
        });

        btn_delete_sec.setForeground(new java.awt.Color(204, 0, 51));
        btn_delete_sec.setText("Xóa Quyển");
        btn_delete_sec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_delete_secActionPerformed(evt);
            }
        });

        jLabel3.setText("Tên Quyển:");

        javax.swing.GroupLayout pan_section_editLayout = new javax.swing.GroupLayout(pan_section_edit);
        pan_section_edit.setLayout(pan_section_editLayout);
        pan_section_editLayout.setHorizontalGroup(
            pan_section_editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_section_editLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_section_editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pan_section_editLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(20, 20, 20)
                        .addComponent(txt_sec_name, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_change_name_sec)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_delete_sec))
                    .addGroup(pan_section_editLayout.createSequentialGroup()
                        .addComponent(btn_pre_sec)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_next_sec)
                        .addGap(18, 18, 18)
                        .addComponent(txt_curent_sec, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_num_sec, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_go_page)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pan_section_editLayout.setVerticalGroup(
            pan_section_editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pan_section_editLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pan_section_editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_sec_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btn_change_name_sec)
                    .addComponent(btn_delete_sec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(pan_section_editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_next_sec)
                    .addComponent(txt_curent_sec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txt_num_sec)
                    .addComponent(btn_go_page)
                    .addComponent(btn_pre_sec)))
        );

        jLabel2.setText("Tên Truyện:");

        btn_change_bookname.setText("Đổi Tên");
        btn_change_bookname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_change_booknameActionPerformed(evt);
            }
        });

        jMenuBar1.setMinimumSize(new java.awt.Dimension(30, 21));

        menu_file.setText("File");

        menu_open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menu_open.setText("Open");
        menu_open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_openActionPerformed(evt);
            }
        });
        menu_file.add(menu_open);

        menu_save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menu_save.setText("Save");
        menu_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_saveActionPerformed(evt);
            }
        });
        menu_file.add(menu_save);

        menu_refesh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menu_refesh.setText("Refesh");
        menu_refesh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_refeshActionPerformed(evt);
            }
        });
        menu_file.add(menu_refesh);

        jMenuBar1.add(menu_file);

        menu_open_edit.setText("Edit");

        btn_del.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        btn_del.setText("Xóa");
        btn_del.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_delActionPerformed(evt);
            }
        });
        menu_open_edit.add(btn_del);

        menu_find_replace.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menu_find_replace.setText("Tìm và thay thế");
        menu_find_replace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_find_replaceActionPerformed(evt);
            }
        });
        menu_open_edit.add(menu_find_replace);

        jMenuBar1.add(menu_open_edit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txt_bookname, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(btn_change_bookname))
                    .addComponent(pan_section_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_bookname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btn_change_bookname))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan_section_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menu_openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_openActionPerformed
        // TODO add your handling code here:

        int result = choice.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane msg = new JOptionPane("Đang Tải Sách", JOptionPane.INFORMATION_MESSAGE);
            final JDialog dlg = msg.createDialog("Xin Chờ...");
            dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            menu_open.setEnabled(false);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EpubReader epubReader = new EpubReader();
                        book = epubReader.readEpub(new FileInputStream(choice.getSelectedFile()), "utf-8");
                        if (book == null) {
                            showErrorMess("Lỗi load sách");
                            menu_open.setEnabled(true);
                            dlg.dispose();
                            return;
                        }
                        setReg(choice.getSelectedFile().getParent());
                        readBook(book.getTableOfContents().getTocReferences());
                        resetView();
                        createDefaultView();
                        menu_open.setEnabled(true);
                        dlg.dispose();
                    } catch (IOException ex) {
                        menu_open.setEnabled(true);
                        dlg.dispose();
                        showErrorMess(ex.getMessage());
                    }
                }
            });
            t.start();
            dlg.setVisible(true);
        }
    }//GEN-LAST:event_menu_openActionPerformed

    private void btn_next_secActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_next_secActionPerformed
        // TODO add your handling code here:
        if (isSecBook && current_sec < listSec.size() - 1) {
            current_sec++;
            refeshView();
        }
    }//GEN-LAST:event_btn_next_secActionPerformed

    private void menu_refeshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_refeshActionPerformed
        // TODO add your handling code here:
        refeshView();
    }//GEN-LAST:event_menu_refeshActionPerformed

    private void btn_pre_secActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pre_secActionPerformed
        // TODO add your handling code here:
        if (isSecBook && current_sec > 0) {
            current_sec--;
            refeshView();
        }
    }//GEN-LAST:event_btn_pre_secActionPerformed

    private void btn_go_pageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_go_pageActionPerformed
        // TODO add your handling code here:
        int num = Integer.parseInt(txt_curent_sec.getText());
        if (num >= 1 && listSec.size() >= num) {
            current_sec = num - 1;
            refeshView();
        }
    }//GEN-LAST:event_btn_go_pageActionPerformed

    private void menu_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_saveActionPerformed
        // TODO add your handling code here:
        if (book == null) {
            return;
        }
        JOptionPane msg = new JOptionPane("Đang Lưu Sách", JOptionPane.INFORMATION_MESSAGE);
        final JDialog dlg = msg.createDialog("Xin Chờ...");
        dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    save_choice = new JFileChooser(choice.getSelectedFile().getParent());
                    save_choice.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    save_choice.setDialogType(JFileChooser.SAVE_DIALOG);
                    save_choice.setFileFilter(new FileNameExtensionFilter(
                            "Epub2 file (*.epub)", "epub")
                    );
                    save_choice.setDialogTitle("Save Epub File");
                    save_choice.setSelectedFile(choice.getSelectedFile());
                    int userSelection = save_choice.showSaveDialog(main.this);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        EpubWriter epubWriter = new EpubWriter();
                        epubWriter.write(book, new FileOutputStream(save_choice.getSelectedFile().getAbsolutePath()));
                        dlg.dispose();
                        showMess("Đã lưu File");
                    }

                } catch (FileNotFoundException ex) {
                    System.err.println("Lỗi file tồn tại, auto fix....");
                    dlg.dispose();
                    showErrorMess("Lỗi file tồn tại");
                } catch (IOException ex) {
                    System.err.println("Lỗi file tồn tại");
                    dlg.dispose();
                    showErrorMess("Lỗi file tồn tại");
                } catch (NullPointerException ex) {
                    dlg.dispose();
                }
            }
        });
        t.start();
        dlg.setVisible(true);
    }//GEN-LAST:event_menu_saveActionPerformed

    private void menu_find_replaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_find_replaceActionPerformed
        // TODO add your handling code here:
        if (book == null) {
            return;
        }
        FindNReplaceDialog mydialog = new FindNReplaceDialog(null, true);
        mydialog.setSize(mydialog.getPreferredSize());
        mydialog.setTitle("Tìm Và Thay Thế");
        mydialog.setLocationRelativeTo(null);
        mydialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        mydialog.setData(listSec, isSecBook, current_sec, max_chap);
        mydialog.setVisible(true);
        if (mydialog.isHasChange()) {
            listSec = mydialog.getToc();
            refeshView();
        }

    }//GEN-LAST:event_menu_find_replaceActionPerformed

    private void btn_change_booknameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_change_booknameActionPerformed
        // TODO add your handling code here:
        if (book == null) {
            return;
        }
        try {
            String newName = txt_bookname.getText();
            if ("".equals(newName)) {
                System.out.println("new name null");
                txt_bookname.setText(book.getTitle());
                showErrorMess("Book name is Empty");
                return;
            }
            ArrayList<String> name = new ArrayList<>();
            name.add(newName);
            book.getMetadata().setTitles(name);
        } catch (NullPointerException e) {
            System.out.println("new name null");
            showErrorMess(null);
        }

    }//GEN-LAST:event_btn_change_booknameActionPerformed

    private void btn_delActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_delActionPerformed
        // TODO add your handling code here:
        showMess("Wait for update...");
    }//GEN-LAST:event_btn_delActionPerformed

    private void btn_change_name_secActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_change_name_secActionPerformed
        // TODO add your handling code here:
        String name = txt_sec_name.getText();
        if (!"".equals(name)) {
            listSec.get(current_sec).setTitle(name);
            JOptionPane.showMessageDialog(this, "Đã Đổi Tên Quyển");
        }
    }//GEN-LAST:event_btn_change_name_secActionPerformed

    private void btn_delete_secActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_delete_secActionPerformed
        // TODO add your handling code here:
        int res = JOptionPane.showConfirmDialog(this, "Xóa Vĩnh Viễn Quyển Này", "Cảnh Báo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        switch (res) {
            case 0: {
                if (listSec.size() > 1) {
                    listSec.remove(current_sec);
                    current_sec = 0;
                    refeshView();
                }
                break;
            }
            case 1: {
                break;
            }
        }
    }//GEN-LAST:event_btn_delete_secActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }
//method

    public void deleteChap() {
        if (isSecBook) {
            listSec.get(current_sec).getChildren().remove(current_chap);
        } else {
            listSec.remove(current_chap);
        }
        refeshView();
    }

    private String getFilenme(String bookname) {
        while (bookname.contains("?")) {
            bookname = bookname.replace("?", "!");
        }
        while (bookname.contains("*")) {
            bookname = bookname.replace("*", "^");
        }
        while (bookname.contains("|")) {
            bookname = bookname.replace("|", "^");
        }
        return bookname.replaceAll(" ", "").replaceAll("<", "_").replaceAll(">", "_").replaceAll(":", "_")
                .replaceAll("\"", "_").replaceAll("/", "_").replaceAll("\\\\", "_");
    }

    public void changeDataRow(int row, int col, String data) {
        DefaultTableModel model = (DefaultTableModel) tb_content.getModel();
        model.setValueAt(data, row, col);
    }

    public String getReg() {

        Preferences userPref = Preferences.userRoot();
        return userPref.get(PREF_KEY, "c:\\\\");

    }

    public void setReg(String reg) {
        if (choice != null) {
            Preferences userPref = Preferences.userRoot();
            userPref.put(PREF_KEY, reg);
        }
    }

    public void showErrorMess(String error) {
        if (error == null) {
            JOptionPane.showMessageDialog(this, "Đã có lỗi xảy ra", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showMess(String mess) {
        JOptionPane.showMessageDialog(this, mess, "Thông tin", JOptionPane.INFORMATION_MESSAGE);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_change_bookname;
    private javax.swing.JButton btn_change_name_sec;
    private javax.swing.JMenuItem btn_del;
    private javax.swing.JButton btn_delete_sec;
    private javax.swing.JButton btn_go_page;
    private javax.swing.JButton btn_next_sec;
    private javax.swing.JButton btn_pre_sec;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenu menu_file;
    private javax.swing.JMenuItem menu_find_replace;
    private javax.swing.JMenuItem menu_open;
    private javax.swing.JMenu menu_open_edit;
    private javax.swing.JMenuItem menu_refesh;
    private javax.swing.JMenuItem menu_save;
    private javax.swing.JPanel pan_section_edit;
    private javax.swing.JTable tb_content;
    private javax.swing.JTextField txt_bookname;
    private javax.swing.JTextField txt_curent_sec;
    private javax.swing.JLabel txt_num_sec;
    private javax.swing.JTextField txt_sec_name;
    // End of variables declaration//GEN-END:variables
}
