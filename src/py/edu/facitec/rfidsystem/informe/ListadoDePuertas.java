package py.edu.facitec.rfidsystem.informe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.sf.jasperreports.engine.JRException;
import py.edu.facitec.rfidsystem.dao.PuertaDao;
import py.edu.facitec.rfidsystem.entidad.Puerta;
import py.edu.facitec.rfidsystem.tablas.TablaPuerta;
import py.edu.facitec.rfidsystem.util.ConexionReportes;

public class ListadoDePuertas extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfDesde;
	private JTable table;
	private JTextField tfHasta;
	private TablaPuerta tablaPuerta;
	private PuertaDao dao;
	private List<Puerta> puertas;
	private JComboBox cbxOrder;
	private JLabel lblTotalNumer;
	private JComboBox cbFiltro;
	private JButton btnImprimir;
	private JLabel lblsolonumeros;
	private HashMap par;
	private int c;

	/**
	 * Create the dialog.
	 */
	public ListadoDePuertas() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ListadoDePuertas.class.getResource("/py/edu/facitec/rfidsystem/img/listapuerta.png")));
		setTitle("Listado de Puertas");
		setBounds(100, 100, 700, 470);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		setLocationRelativeTo(this);
		setModal(true);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		tablaPuerta = new TablaPuerta();
		
		JLabel lblDesde = new JLabel("Desde:");
		lblDesde.setBounds(234, 14, 46, 14);
		contentPanel.add(lblDesde);
		
		JLabel lblHasta = new JLabel("Hasta ");
		lblHasta.setBounds(234, 54, 46, 14);
		contentPanel.add(lblHasta);
		
		tfDesde = new JTextField();
		tfDesde.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (seleccionarFiltro()==false) {
					if (!Character.isDigit(c) & c!= e.VK_ENTER & c!= e.VK_BACK_SPACE) {
						e.consume();
						lblsolonumeros.setVisible(true);
						lblsolonumeros.setText("*Solo Numeros");
					}else{
						lblsolonumeros.setVisible(false);
					}
				}else{
					if (Character.isDigit(c)) {
						e.consume();
						lblsolonumeros.setVisible(true);
						lblsolonumeros.setText("*Solo Letras");
					}else{
						lblsolonumeros.setVisible(false);
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c== e.VK_ENTER) {
					tfHasta.requestFocus();
				}
			}
		});
		tfDesde.setBounds(290, 14, 186, 20);
		contentPanel.add(tfDesde);
		tfDesde.setColumns(10);
		
		tfHasta = new JTextField();
		tfHasta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (seleccionarFiltro()==false) {
					if (!Character.isDigit(c) & c!= e.VK_ENTER & c!= e.VK_BACK_SPACE) {
						e.consume();
						lblsolonumeros.setVisible(true);
						lblsolonumeros.setText("*Solo Numeros");
					}else{
						lblsolonumeros.setVisible(false);
					}
				}else{
					if (Character.isDigit(c)) {
						e.consume();
						lblsolonumeros.setVisible(true);
						lblsolonumeros.setText("*Solo Letras");
					}else{
						lblsolonumeros.setVisible(false);
					}
				}
			}
		});
		tfHasta.setBounds(290, 54, 186, 20);
		contentPanel.add(tfHasta);
		tfHasta.setColumns(10);
		
		JButton btnProcesas = new JButton("Procesar");
		btnProcesas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				verificarYConsultar();
				verificarLista();
			}
		});
		btnProcesas.setBounds(501, 11, 108, 30);
		contentPanel.add(btnProcesas);
		
		cbxOrder = new JComboBox();
		cbxOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verificarYConsultar();
				verificarLista();
			}
		});
		cbxOrder.setModel(new DefaultComboBoxModel(new String[] {"C\u00F3digo", "Oficina", "Numero"}));
		cbxOrder.setBounds(88, 54, 108, 20);
		contentPanel.add(cbxOrder);
		
		JLabel lblOrdenarPor = new JLabel("Ordenar Por:");
		lblOrdenarPor.setBounds(10, 57, 114, 14);
		contentPanel.add(lblOrdenarPor);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 95, 674, 294);
		contentPanel.add(scrollPane);
		
		table = new JTable(tablaPuerta);
		scrollPane.setViewportView(table);
		
		btnImprimir = new JButton("Imprimir");
		btnImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConexionReportes<Puerta> conexionReportes = new ConexionReportes<Puerta>();
				if (par==null) {
					par = new HashMap<>();
					par.put("parametro", "Listado General		Ordenado por: "+cbxOrder.getSelectedItem());;
				}
				try {
					conexionReportes.GerarRealatorio(puertas, par, "ListadoDePuertas");
					conexionReportes.viewer.setVisible(true);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		});
		btnImprimir.setBounds(58, 400, 89, 30);
		contentPanel.add(btnImprimir);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnSalir.setBounds(525, 400, 89, 30);
		contentPanel.add(btnSalir);
		
		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTotal.setBounds(501, 54, 46, 14);
		contentPanel.add(lblTotal);
		
		lblTotalNumer = new JLabel("");
		lblTotalNumer.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTotalNumer.setBounds(545, 54, 46, 14);
		contentPanel.add(lblTotalNumer);
		
		JLabel lblBuscarPor = new JLabel("Buscar Por:");
		lblBuscarPor.setBounds(10, 14, 70, 14);
		contentPanel.add(lblBuscarPor);
		
		cbFiltro = new JComboBox();
		cbFiltro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seleccionarFiltro();
				tfDesde.setText("");
				tfHasta.setText("");
				lblsolonumeros.setVisible(false);
				tfDesde.requestFocus();
			}
		});
		cbFiltro.setModel(new DefaultComboBoxModel(new String[] {"Nro. de Puerta", "Oficina"}));
		cbFiltro.setBounds(88, 11, 108, 20);
		contentPanel.add(cbFiltro);
		
		lblsolonumeros = new JLabel("*SoloNumeros");
		lblsolonumeros.setVisible(false);
		lblsolonumeros.setForeground(Color.RED);
		lblsolonumeros.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblsolonumeros.setBounds(290, 36, 114, 14);
		contentPanel.add(lblsolonumeros);
		ordenarTodo();
		verificarLista();
		c=puertas.size();
		tfDesde.requestFocus();
	}// fin del metodo constructor
	
	private void filtrarPuertas() {
		dao = new PuertaDao();
		if(cbxOrder.getSelectedIndex()==0) puertas = dao.filtroPorOficina(tfDesde.getText(), tfHasta.getText(), "id");
		if(cbxOrder.getSelectedIndex()==1) puertas = dao.filtroPorOficina(tfDesde.getText(), tfHasta.getText(), "descripcion");
		if(cbxOrder.getSelectedIndex()==2) puertas = dao.filtroPorOficina(tfDesde.getText(), tfHasta.getText(), "numeroDePuerta");
		tablaPuerta.setLista(puertas);
		tablaPuerta.fireTableDataChanged();
		lblTotalNumer.setText(puertas.size()+"");
	}

	private void verificarYConsultar() {
		if (seleccionarFiltro()==false) {
			if (tfHasta.getText().isEmpty() & tfDesde.getText().isEmpty()) { ordenarTodo(); parametro(); return;}
			if(tfHasta.getText().isEmpty()) tfHasta.setText("999999999");
			if(tfDesde.getText().isEmpty()) tfDesde.setText("0");
			filtrarPuertas();
		}
		if (seleccionarFiltro()==true) {
			if (tfHasta.getText().isEmpty() & tfDesde.getText().isEmpty()) { ordenarTodo(); parametro(); return;}
			if(tfDesde.getText().isEmpty()) tfDesde.setText("A");
			if(tfHasta.getText().isEmpty()) tfHasta.setText("z");
			filtrarPuertas();
		}
		parametro();
	}
	
	private void ordenarTodo() {
		dao = new PuertaDao();
		if(cbxOrder.getSelectedIndex()==0) puertas = dao.filtroPorOficina("0", "999999999", "id");
		if(cbxOrder.getSelectedIndex()==1) puertas = dao.filtroPorOficina("0", "999999999", "descripcion");
		if(cbxOrder.getSelectedIndex()==2) puertas = dao.filtroPorOficina("0", "999999999", "numeroDePuerta");
		tablaPuerta.setLista(puertas);
		tablaPuerta.fireTableDataChanged();
		lblTotalNumer.setText(puertas.size()+"");
	}
	
	private boolean seleccionarFiltro() {
		if(cbFiltro.getSelectedIndex()==0) return false;
		return true;
	}
	
	private void verificarLista(){
		if (puertas.size()==0) {
			btnImprimir.setEnabled(false);
		}else{
			btnImprimir.setEnabled(true);
		}
	}
	
	private void parametro() {
		par = new HashMap<>();
		if (tfDesde.getText().isEmpty() & tfHasta.getText().isEmpty() | c==puertas.size()) {
			par.put("parametro", "Listado General		Ordenado por: "+cbxOrder.getSelectedItem());
			return;
		}else{
			par.put("parametro", "Desde: "+tfDesde.getText()+"		Hasta: "+tfHasta.getText()+"		"
					+ "Orden por: "+cbxOrder.getSelectedItem());
		}
	}
	

}
