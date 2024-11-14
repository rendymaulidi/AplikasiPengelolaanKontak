import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
  * @author rendy
 */
public class AplikasiPengelolaanNomorFrame extends javax.swing.JFrame {
    private DefaultTableModel model;
    /**
     * Creates new form AplikasiPengelolaanNomorFrame
     */
    public AplikasiPengelolaanNomorFrame() {
        initComponents();
        model = (DefaultTableModel) TbKategori.getModel();
        model.setColumnIdentifiers(new String[]{"ID", "Nama", "Nomor Telepon", "Kategori"});
        loadData();
    }
    
    private boolean validasiNomorTelepon(String nomor){
        return nomor.matches("\\d{10,13}");
    }
    
    private void tambahKontak() {
        System.out.println("Tombol Tambah ditekan.");
        String nama = TNama.getText();
        String nomor = TNoTelepon.getText();
        String kategori = CbKategori.getSelectedItem().toString();
        String nomorTelepon = TNoTelepon.getText();
        
        if (!validasiNomorTelepon(nomor)) {
                JOptionPane.showMessageDialog(this, "Nomor telepon harus terdiri dari 10-13 digit angka.");
            return;
        }

        String sql = "INSERT INTO kontak(nama, nomor_telepon, kategori) VALUES(?,?,?)";

            try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nama);
                pstmt.setString(2, nomor);
                pstmt.setString(3, kategori);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Kontak berhasil ditambahkan");
                System.out.println("Data berhasil ditambahkan ke database.");
                
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
        }    
    }
    
    private void eksporKeCSV() {
    JFileChooser fileChooser = new JFileChooser();
    FcFile.setDialogTitle("Simpan sebagai CSV");
    int userSelection = FcFile.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = FcFile.getSelectedFile();
        
            try (FileWriter fw = new FileWriter(fileToSave + ".csv")) {
            // Menulis header kolom ke file CSV
                for (int i = 0; i < model.getColumnCount(); i++) {
                    fw.write(model.getColumnName(i) + ",");
                }
                fw.write("\n");

            // Menulis baris data dari JTable ke file CSV
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        fw.write(model.getValueAt(i, j).toString() + ",");
                    }
                    fw.write("\n");
                }
                JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke CSV!");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengekspor data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void imporDariCSV() {
        FcFile.setDialogTitle("Pilih file CSV");
        int userSelection = FcFile.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = FcFile.getSelectedFile();
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileToOpen))) {
            String line;
                boolean isFirstLine = true;

                while ((line = br.readLine()) != null) {
                    // Melewati baris pertama jika ada header
                    if (isFirstLine) {
                        isFirstLine = false;
                    continue;
                }

                    // Memisahkan setiap kolom dengan koma
                    String[] data = line.split(",");

                    // Pastikan jumlah kolom sesuai dengan tabel (misalnya ada 4 kolom: ID, Nama, Nomor Telepon, Kategori)
                if (data.length == 4) {
                    // Masukkan data ke dalam database
                    String sql = "INSERT INTO kontak(id, nama, nomor_telepon, kategori) VALUES(?, ?, ?, ?)";
                    try (Connection conn = DatabaseHelper.connect();
                        PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setInt(1, Integer.parseInt(data[0]));
                        pstmt.setString(2, data[1]);
                        pstmt.setString(3, data[2]);
                        pstmt.setString(4, data[3]);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Tambahkan data ke JTable
                    model.addRow(new Object[]{data[0], data[1], data[2], data[3]});
                }
            }
            JOptionPane.showMessageDialog(this, "Data berhasil diimpor dari CSV!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat membaca file CSV.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

 
    private void loadData(){
        DefaultTableModel model = (DefaultTableModel) TbKategori.getModel();
        model.setRowCount(0);
        
        String sql = "SELECT * FROM kontak";
        
        try (Connection conn = DatabaseHelper.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("nomor_telepon"),
                    rs.getString("kategori")
                });
            }
        } catch (SQLException e){
            e.printStackTrace();
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

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TNama = new javax.swing.JTextField();
        TNoTelepon = new javax.swing.JTextField();
        CbKategori = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        TbKategori = new javax.swing.JTable();
        BEksporData = new javax.swing.JButton();
        FcFile = new javax.swing.JFileChooser();
        BImporData = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        BTambah = new javax.swing.JButton();
        BEdit = new javax.swing.JButton();
        BHapus = new javax.swing.JButton();
        BCari = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 255, 255));

        jLabel1.setFont(new java.awt.Font("Gill Sans MT", 1, 20)); // NOI18N
        jLabel1.setText("Aplikasi Pengelola Kontak");

        jLabel2.setFont(new java.awt.Font("Gill Sans MT", 1, 14)); // NOI18N
        jLabel2.setText("Nama Kontak");

        jLabel3.setFont(new java.awt.Font("Gill Sans MT", 1, 14)); // NOI18N
        jLabel3.setText("No Telepon");

        jLabel4.setFont(new java.awt.Font("Gill Sans MT", 1, 14)); // NOI18N
        jLabel4.setText("Kategori");

        TNama.setFont(new java.awt.Font("Gill Sans MT", 1, 14)); // NOI18N

        TNoTelepon.setFont(new java.awt.Font("Gill Sans MT", 1, 14)); // NOI18N
        TNoTelepon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TNoTeleponKeyTyped(evt);
            }
        });

        CbKategori.setFont(new java.awt.Font("Gill Sans MT", 1, 14)); // NOI18N
        CbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Keluarga", "Teman", "Rekan Kerja", "Sahabat", "Orang Asing" }));

        jScrollPane1.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N

        TbKategori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Nama", "No Telepon", "Kategori"
            }
        ));
        jScrollPane1.setViewportView(TbKategori);

        BEksporData.setFont(new java.awt.Font("Gill Sans MT", 1, 12)); // NOI18N
        BEksporData.setText("Ekspor Data");
        BEksporData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEksporDataActionPerformed(evt);
            }
        });

        BImporData.setFont(new java.awt.Font("Gill Sans MT", 1, 12)); // NOI18N
        BImporData.setText("Impor Data");
        BImporData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BImporDataActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Gill Sans MT", 1, 12)); // NOI18N
        jButton1.setText("Keluar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        BTambah.setFont(new java.awt.Font("Gill Sans MT", 1, 12)); // NOI18N
        BTambah.setText("Tambah Kontak");
        BTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTambahActionPerformed(evt);
            }
        });

        BEdit.setFont(new java.awt.Font("Gill Sans MT", 1, 12)); // NOI18N
        BEdit.setText("Edit Kontak");
        BEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEditActionPerformed(evt);
            }
        });

        BHapus.setFont(new java.awt.Font("Gill Sans MT", 1, 12)); // NOI18N
        BHapus.setText("Hapus Kontak");
        BHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BHapusActionPerformed(evt);
            }
        });

        BCari.setFont(new java.awt.Font("Gill Sans MT", 1, 12)); // NOI18N
        BCari.setText("Cari Kontak");
        BCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FcFile, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel2))
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(CbKategori, 0, 347, Short.MAX_VALUE)
                                            .addComponent(TNoTelepon)
                                            .addComponent(TNama))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(BTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(BEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(BEksporData)
                        .addGap(26, 26, 26)
                        .addComponent(BImporData)
                        .addGap(18, 18, 18)
                        .addComponent(BHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(BCari)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(jLabel1)
                .addContainerGap(228, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTambah))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(TNoTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BEdit))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(CbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(BCari)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BEksporData)
                    .addComponent(BImporData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addComponent(BHapus))
                .addGap(51, 51, 51)
                .addComponent(FcFile, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTambahActionPerformed
        // TODO add your handling code here:
        String nama = TNama.getText();
        String nomor = TNoTelepon.getText();
        String kategori = CbKategori.getSelectedItem().toString();
        
        String sql = "INSERT INTO kontak(nama, nomor_telepon, kategori) VALUES(?,?,?)";
        
            try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, nama);
                pstmt.setString(2, nomor);
                pstmt.setString(3, kategori);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Kontak berhasil ditambahkan");
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
        }
    }//GEN-LAST:event_BTambahActionPerformed

    private void TNoTeleponKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoTeleponKeyTyped
        char karakter = evt.getKeyChar();
            if (!Character.isDigit(karakter)) {
                evt.consume();
        }

            if (TNoTelepon.getText().length() >= 13) {
            evt.consume(); 
        }
    }//GEN-LAST:event_TNoTeleponKeyTyped

    private void BEksporDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEksporDataActionPerformed
        eksporKeCSV();
    }//GEN-LAST:event_BEksporDataActionPerformed

    private void BImporDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BImporDataActionPerformed
        imporDariCSV();
    }//GEN-LAST:event_BImporDataActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void BCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCariActionPerformed
        // TODO add your handling code here:
        String keyword = TNama.getText();
        model.setRowCount(0);
        String sql = "SELECT * FROM kontak WHERE nama LIKE ? OR nomor_telepon LIKE ?";

        try (Connection conn = DatabaseHelper.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("nomor_telepon"),
                    rs.getString("kategori")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_BCariActionPerformed

    private void BHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BHapusActionPerformed
        // TODO add your handling code here:
        int selectedRow = TbKategori.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kontak yang ingin dihapus");
            return;
        }
        int id = (int) model.getValueAt(selectedRow, 0);

        String sql = "DELETE FROM kontak WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kontak berhasil dihapus");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_BHapusActionPerformed

    private void BEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEditActionPerformed
        // TODO add your handling code here:
        eksporKeCSV();
        int selectedRow = TbKategori.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kontak yang ingin diedit");
            return;
        }
        int id = (int) model.getValueAt(selectedRow, 0);
        String nama = TNama.getText();
        String nomor = TNoTelepon.getText();
        String kategori = CbKategori.getSelectedItem().toString();

        String sql = "UPDATE kontak SET nama = ?, nomor_telepon = ?, kategori = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nama);
            pstmt.setString(2, nomor);
            pstmt.setString(3, kategori);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kontak berhasil diperbarui");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_BEditActionPerformed

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
            java.util.logging.Logger.getLogger(AplikasiPengelolaanNomorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AplikasiPengelolaanNomorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AplikasiPengelolaanNomorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AplikasiPengelolaanNomorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AplikasiPengelolaanNomorFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BCari;
    private javax.swing.JButton BEdit;
    private javax.swing.JButton BEksporData;
    private javax.swing.JButton BHapus;
    private javax.swing.JButton BImporData;
    private javax.swing.JButton BTambah;
    private javax.swing.JComboBox<String> CbKategori;
    private javax.swing.JFileChooser FcFile;
    private javax.swing.JTextField TNama;
    private javax.swing.JTextField TNoTelepon;
    private javax.swing.JTable TbKategori;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
