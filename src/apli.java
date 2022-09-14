import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTable;

public class apli extends JFrame {

	private JPanel contentPane;
	private JTextField txtID;
	private JTextField txtDtCadastro;
	private JTextField txtDescricao;
	private JTable tabProduto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					apli frame = new apli();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public apli() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 100, 728, 457);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setBounds(10, 23, 19, 14);
		contentPane.add(lblNewLabel);

		txtID = new JTextField();
		txtID.setEditable(false);
		txtID.setBounds(31, 20, 68, 20);
		contentPane.add(txtID);
		txtID.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Data Cadastro");
		lblNewLabel_1.setBounds(132, 23, 93, 14);
		contentPane.add(lblNewLabel_1);

		txtDtCadastro = new JTextField();
		txtDtCadastro.setEditable(false);
		txtDtCadastro.setBounds(220, 20, 86, 20);
		contentPane.add(txtDtCadastro);
		txtDtCadastro.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Descri\u00E7\u00E3o");
		lblNewLabel_2.setBounds(10, 59, 46, 14);
		contentPane.add(lblNewLabel_2);

		txtDescricao = new JTextField();
		txtDescricao.setEditable(false);
		txtDescricao.setBounds(66, 56, 199, 20);
		contentPane.add(txtDescricao);
		txtDescricao.setColumns(10);

		JButton btnGravar = new JButton("Gravar");
		btnGravar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					gravar();
				}catch(Exception e1){
					e1.printStackTrace();
					
				}
			}
		});
		btnGravar.setBounds(275, 55, 80, 23);
		contentPane.add(btnGravar);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					excluir();
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
				
			}
			
		});
		btnExcluir.setBounds(462, 55, 75, 23);
		contentPane.add(btnExcluir);

		tabProduto = new JTable();
		tabProduto.setBounds(10, 87, 692, 298);
		tabProduto.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				linhaSelecionada();

			}
		});
		contentPane.add(tabProduto);

		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					atualizar();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnAtualizar.setBounds(365, 55, 89, 23);
		contentPane.add(btnAtualizar);
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtDescricao.setEditable(true);
				txtDescricao.setText(null);
				txtID.setText(null);
				txtDtCadastro.setText(null);
				txtDescricao.requestFocus();
			}
		});
		btnNovo.setBounds(547, 55, 89, 23);
		contentPane.add(btnNovo);
		this.listarProduto();

	}
	
	private void excluir() throws SQLException {
		Connection con = null;
		ConexaoBanco objconexao = new ConexaoBanco();
		
		try {
			con = objconexao.conectar();
			
			if(con == null) {
				JOptionPane.showMessageDialog(null, "Conexao não realizada!");
			}
			else {
				Statement stmt = con.createStatement();
                String query = "DELETE FROM produto WHERE id=" + txtID.getText();
				stmt.executeUpdate(query);
				listarProduto();
				
			}	
		}
		catch(Exception ex) {
			con.close();
			JOptionPane.showMessageDialog(null, "Não foi possível excluir. "+ex.getMessage());
		}
	}
	
	private void atualizar() throws SQLException{
        ConexaoBanco conexao = new ConexaoBanco();
        Connection con = conexao.conectar();
        txtDescricao.setEditable(true);
        try {
            if(con ==null) {
                JOptionPane.showMessageDialog(null,"conexao nao realizada");
            } else {
                Statement stat = con.createStatement();
                String query = "UPDATE produto SET descricao = '"+ txtDescricao.getText() + "' WHERE id = "+txtID.getText();
                stat.executeUpdate(query);
                listarProduto();
            }
        } catch (Exception e) {
            con.close();
            JOptionPane.showMessageDialog(null,"Nao foi possivel atualizar esse produto. "+e.getMessage());
        }
    }
	

	private void listarProduto() throws SQLException {
		Connection con = null;
		ConexaoBanco objconexao = new ConexaoBanco();
		con = objconexao.conectar();
		if (con == null) {
			JOptionPane.showMessageDialog(null, "conexão não realizada");
		} else {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM db_pedidos.produto");
			String[] colunasTabela = new String[] { "ID", "Descrição", "Pontuação" };
			DefaultTableModel modeloTabela = new DefaultTableModel(null, colunasTabela);
			modeloTabela.addRow(new String[] { "ID", "DESCRIÇÃO", "CADASTRO" });
			if (rs != null) {
				while (rs.next()) {
					modeloTabela.addRow(new String[] { String.valueOf(rs.getInt("ID")), rs.getString("descricao"),
							rs.getString("data_cadastro") });
					
				}
			}
			tabProduto.setModel(modeloTabela);
			
		}
	}

	private void linhaSelecionada() {
		desabilitarText();
		DefaultTableModel tableModel = (DefaultTableModel) tabProduto.getModel();
		int row = tabProduto.getSelectedRow();
		if (tableModel.getValueAt(row, 0).toString() != "ID") {
			txtID.setText(tableModel.getValueAt(row, 0).toString());
			txtDescricao.setText(tableModel.getValueAt(row, 1).toString());
			txtDtCadastro.setText(tableModel.getValueAt(row, 2).toString());
		}
	}

	private void desabilitarText() {
		txtDescricao.setEditable(false);
		txtID.setEditable(false);
		txtDtCadastro.setEditable(false);
	}
	
	private void gravar() throws SQLException {
		Connection con = null;
		ConexaoBanco objconexao = new ConexaoBanco();
		
		try {
			con = objconexao.conectar();
			
			if(con == null) {
				JOptionPane.showMessageDialog(null, "Conexao não realizada!");
			}
			else {
				Statement stmt = con.createStatement();
				String query="insert into db_pedidos.produto(descricao) values('"+txtDescricao.getText()+"')";
				stmt.executeUpdate(query);
				listarProduto();
				txtDescricao.setText(null);
				desabilitarText();
			}	
		}
		catch(Exception ex) {
			con.close();
			JOptionPane.showMessageDialog(null, "Não foi possível gravar. "+ex.getMessage());
		}
	}
	

	

}
