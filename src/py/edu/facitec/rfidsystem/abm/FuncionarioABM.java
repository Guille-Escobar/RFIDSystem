package py.edu.facitec.rfidsystem.abm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import py.edu.facitec.rfidsystem.contenedores.BotonPersonalizadoABM;
import py.edu.facitec.rfidsystem.dao.FuncionarioDao;
import py.edu.facitec.rfidsystem.dao.PermisoAccesoDao;
import py.edu.facitec.rfidsystem.entidad.Funcionario;
import py.edu.facitec.rfidsystem.entidad.PermisoAcceso;
import py.edu.facitec.rfidsystem.tablas.TablaFuncionario;
import py.edu.facitec.rfidsystem.util.FechaUtil;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FuncionarioABM extends GenericABM {
	private JTextField tfBuscar;
	private JTextField tfCodigo;
	private JTextField tfDocumento;
	private JTextField tfNombre;
	private JTextField tfApellido;
	private JTextField tfTarjeta;
	private Funcionario funcionario;
	private FuncionarioDao dao;
	private PermisoAccesoDao permisoAccesoDao;
	private JFormattedTextField tfFechaNacimiento;
	private JFormattedTextField tfFechaIncorporacion;
	private JComboBox cbSexo;
	private TablaFuncionario tablaFuncionario;
	private List<Funcionario> funcionarios;
	private List<PermisoAcceso> permisoAccesos;
	private byte bandera;

	/**
	 * Create the dialog.
	 */
	public FuncionarioABM() {
		setTitle("Registro de Funcionario");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		});
		
		tablaFuncionario = new TablaFuncionario();
		table.setModel(tablaFuncionario);
		
		getContentPane().setMinimumSize(new Dimension(700, 500));
		getContentPane().setMaximumSize(new Dimension(800, 600));
		setBounds(100, 100, 800, 530);
		setLocationRelativeTo(this);
		
		
		JLabel lblTitulo = new JLabel("Registros De Funcionarios");
		lblTitulo.setForeground(Color.BLUE);
		lblTitulo.setFont(new Font("Algerian", Font.PLAIN, 24));
		lblTitulo.setBounds(244, 11, 540, 53);
		getContentPane().add(lblTitulo);
		
		BotonPersonalizadoABM btnBuscar = new BotonPersonalizadoABM();
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarFuncionarios();
			}
		});
		btnBuscar.setText("Buscar");
		btnBuscar.setBounds(412, 95, 90, 26);
		getContentPane().add(btnBuscar);
		
		tfBuscar = new JTextField();
		tfBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c== e.VK_ENTER) {
					buscarFuncionarios();
				}
			}
		});
		tfBuscar.setBounds(529, 98, 228, 20);
		getContentPane().add(tfBuscar);
		tfBuscar.setColumns(10);
		
		JLabel lblCodigo = new JLabel("C\u00F3digo:");
		lblCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		lblCodigo.setBounds(10, 145, 46, 14);
		getContentPane().add(lblCodigo);
		
		tfCodigo = new JTextField();
		tfCodigo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c)) {
					e.consume();
					if(bandera!=1 & c!= e.VK_BACK_SPACE & c!= e.VK_ENTER){
						JOptionPane.showMessageDialog(null, "Solo se permiten numeros enteros");
						bandera=1;
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == e.VK_ENTER) {
					tfDocumento.requestFocus();
				}
			}
		});
		tfCodigo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
					if (modificar==false){
						verificarCodigo();
					}
			}
		});
		tfCodigo.setEnabled(false);
		tfCodigo.setBounds(133, 143, 92, 20);
		getContentPane().add(tfCodigo);
		tfCodigo.setColumns(10);
		
		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setFont(new Font("Arial", Font.BOLD, 12));
		lblSexo.setBounds(235, 145, 46, 14);
		getContentPane().add(lblSexo);
		
		cbSexo = new JComboBox();
		cbSexo.setEnabled(false);
		cbSexo.setModel(new DefaultComboBoxModel(new String[] {" Masculino", " Femenino"}));
		cbSexo.setFont(new Font("Arial", Font.PLAIN, 12));
		cbSexo.setBounds(291, 142, 102, 20);
		getContentPane().add(cbSexo);
		
		JLabel lblDocumento = new JLabel("Nro. Documento:");
		lblDocumento.setFont(new Font("Arial", Font.BOLD, 12));
		lblDocumento.setBounds(10, 185, 102, 14);
		getContentPane().add(lblDocumento);
		
		tfDocumento = new JTextField();
		tfDocumento.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c)) {
					e.consume();
					if(bandera!=2 & c!= e.VK_BACK_SPACE & c!= e.VK_ENTER){
						JOptionPane.showMessageDialog(null, "Solo se permiten numeros enteros");
						bandera=2;
					}
				}
				if (tfDocumento.getText().length()==10) {
					e.consume();
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == e.VK_ENTER) {
					tfNombre.requestFocus();
				}
			}
		});
		tfDocumento.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (modificar==false){
					verificarCi();
				}
			}
		});
		tfDocumento.setEnabled(false);
		tfDocumento.setBounds(133, 183, 158, 20);
		getContentPane().add(tfDocumento);
		tfDocumento.setColumns(10);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
		lblNombre.setBounds(10, 225, 102, 14);
		getContentPane().add(lblNombre);
		
		tfNombre = new JTextField();
		tfNombre.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (Character.isDigit(c)) {
					e.consume();
					if(bandera!=3){
						JOptionPane.showMessageDialog(null, "No se permiten Numeros");
						bandera=3;
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == e.VK_ENTER) {
					tfApellido.requestFocus();
				}
			}
		});
		tfNombre.setEnabled(false);
		tfNombre.setBounds(133, 223, 228, 20);
		getContentPane().add(tfNombre);
		tfNombre.setColumns(10);
		
		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setFont(new Font("Arial", Font.BOLD, 12));
		lblApellido.setBounds(10, 265, 102, 14);
		getContentPane().add(lblApellido);
		
		tfApellido = new JTextField();
		tfApellido.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (Character.isDigit(c)) {
					e.consume();
					if(bandera!=4){
						JOptionPane.showMessageDialog(null, "No se permiten Numeros");
						bandera=4;
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == e.VK_ENTER) {
					tfFechaNacimiento.requestFocus();
				}
			}
		});
		tfApellido.setEnabled(false);
		tfApellido.setBounds(133, 263, 228, 20);
		getContentPane().add(tfApellido);
		tfApellido.setColumns(10);
		
		JLabel lblFechaNacimiento = new JLabel("Fecha Nacimiento:");
		lblFechaNacimiento.setFont(new Font("Arial", Font.BOLD, 12));
		lblFechaNacimiento.setBounds(10, 305, 113, 14);
		getContentPane().add(lblFechaNacimiento);
		
		tfFechaNacimiento = new JFormattedTextField(FechaUtil.getFormato());
		tfFechaNacimiento.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == e.VK_ENTER) {
					tfFechaIncorporacion.requestFocus();
				}
			}
		});
		tfFechaNacimiento.setEnabled(false);
		tfFechaNacimiento.setBounds(133, 303, 102, 20);
		getContentPane().add(tfFechaNacimiento);
		
		JLabel lblFechaIncorporacion = new JLabel("Fecha Incorporaci\u00F3n:");
		lblFechaIncorporacion.setFont(new Font("Arial", Font.BOLD, 12));
		lblFechaIncorporacion.setBounds(10, 345, 126, 14);
		getContentPane().add(lblFechaIncorporacion);
		
		tfFechaIncorporacion = new JFormattedTextField(FechaUtil.getFormato());
		tfFechaIncorporacion.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == e.VK_ENTER) {
					tfTarjeta.requestFocus();
				}
			}
		});
		tfFechaIncorporacion.setEnabled(false);
		tfFechaIncorporacion.setBounds(133, 343, 102, 20);
		getContentPane().add(tfFechaIncorporacion);
		
		JLabel lblTarjeta = new JLabel("Tarjeta:");
		lblTarjeta.setFont(new Font("Arial", Font.BOLD, 12));
		lblTarjeta.setBounds(10, 385, 102, 14);
		getContentPane().add(lblTarjeta);
		
		tfTarjeta = new JTextField();
		tfTarjeta.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (modificar==false){
					verificarTarjeta();
				}
			}
		});
		tfTarjeta.setEnabled(false);
		tfTarjeta.setBounds(133, 383, 158, 20);
		getContentPane().add(tfTarjeta);
		tfTarjeta.setColumns(10);

		consultarFuncionarios();
		funcionario=null;
		
	}
	
	//----------------Metodos Genericos---------------
	@Override
	protected void limpiar() {
		tfCodigo.setText("");
		tfDocumento.setText("");
		tfNombre.setText("");
		tfApellido.setText("");
		tfFechaNacimiento.setText("");
		tfFechaIncorporacion.setText("");
		tfTarjeta.setText("");
	}
	
	@Override
	protected void habilitarCampos(boolean e) {
		if (modificar==true) {
			tfCodigo.setEnabled(!e);
		}else {
			tfCodigo.setEnabled(e);
		}
		tfDocumento.setEnabled(e);
		tfNombre.setEnabled(e);
		tfApellido.setEnabled(e);
		tfFechaNacimiento.setEnabled(e);
		tfFechaIncorporacion.setEnabled(e);
		tfTarjeta.setEnabled(e);
		cbSexo.setEnabled(e);
	}
	
	@Override
	protected void guardar() {
		if (campoObligatorio()==true) return;
		if(validarFecha()==false) return;
		cargarDatos();
		dao = new FuncionarioDao();
		dao.insertarOModificar(funcionario);
		try {
			dao.ejecutar();
		} catch (Exception e) {
			dao.cancelar();
		}
		modificar=false;
		consultarFuncionarios();
		habilitarCampos(false);
		limpiar();
		accionesPrimarias(true);
		funcionario=null;
	}
	
	@Override
	protected void fechaActual() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		tfFechaIncorporacion.setText(""+dateFormat.format(date));
	}
	
	//Carag en el formulario el dato que se selecciono en la tabla
	protected void cargarFormulario(int index) {
		if (index < 0) return;
		funcionario = funcionarios.get(index);
		tfCodigo.setText(funcionario.getId()+"");
		tfDocumento.setText(funcionario.getNoDocumento()+"");
		tfNombre.setText(funcionario.getNombre());
		tfApellido.setText(funcionario.getApellido());
		tfTarjeta.setText(funcionario.getTarjeta());
		tfFechaIncorporacion.setValue(FechaUtil.fechaAString(funcionario.getFechaIncorporacion()));
		tfFechaNacimiento.setValue(FechaUtil.fechaAString(funcionario.getFechaNacimiento()));
		if (funcionario.getSexo().equals("F")) {
			cbSexo.setSelectedIndex(1);
		}else{
			cbSexo.setSelectedIndex(0);
		}
		modificar=false;
		habilitarCampos(false);
		accionesPrimarias(false);
	}
		
	//Metodo para eliminar 
	public void eliminar(){
		if(verificarRelacion()==false) return;
		if (table.getSelectedRow()<0) return;
		int respuesta = JOptionPane.showConfirmDialog(null, 
				"Esta seguro que desea eliminar el funcionario "+funcionario.getNombre(),
				"Atenci�n",
				JOptionPane.YES_NO_OPTION);
		if(respuesta == JOptionPane.YES_OPTION){
			dao = new FuncionarioDao();
			dao.eliminar(funcionario);
			try {
				dao.ejecutar();
			} catch (Exception e) {
				dao.cancelar();
			}
			consultarFuncionarios();
		}
		limpiar();
		accionesPrimarias(true);
	}
	
	//Metodo para cargargar los datos en el funcionario
	public void cargarDatos(){
		if (modificar==false) {
			funcionario = new Funcionario();
		}
		funcionario.setApellido(tfApellido.getText());
		funcionario.setFechaIncorporacion(FechaUtil.dSql(tfFechaIncorporacion.getText()));
		funcionario.setFechaNacimiento(FechaUtil.dSql(tfFechaNacimiento.getText()));
		funcionario.setId(Integer.parseInt(tfCodigo.getText()));
		funcionario.setNoDocumento(Integer.parseInt(tfDocumento.getText()));
		funcionario.setNombre(tfNombre.getText()); 
		if(cbSexo.getSelectedIndex() == 0){
			funcionario.setSexo("M");
		}else{
			funcionario.setSexo("F");
		}
		funcionario.setTarjeta(tfTarjeta.getText());
	}
	
	//Carga la tabla
	public void consultarFuncionarios(){
		dao = new FuncionarioDao();
		funcionarios = dao.recuperarTodo();
		tablaFuncionario.setLista(funcionarios);
		tablaFuncionario.fireTableDataChanged();
	}

	private void buscarFuncionarios() {
		dao = new FuncionarioDao();
		funcionarios = dao.recuperarPorFiltro(tfBuscar.getText());
		tablaFuncionario.setLista(funcionarios);
		tablaFuncionario.fireTableDataChanged();
	}
	
	//***********************VALIDACIONES Y REQUISITOS********************
	
	//Metodos para verificar los campos unicos 
	private void verificarCi() {
		if (tfDocumento.getText().isEmpty()) {
			return;
		}
		for (int i = 0; i < funcionarios.size(); i++) {
			if (Integer.parseInt(tfDocumento.getText())==funcionarios.get(i).getNoDocumento()) {
				JOptionPane.showMessageDialog(null, "Documento duplicado", "Atenci�n",JOptionPane.ERROR_MESSAGE);
				tfDocumento.requestFocus();
				tfDocumento.selectAll();
			}
		}
	}
	private void verificarCodigo() {
		if (tfCodigo.getText().isEmpty()) {
			return;
		}
		for (int i = 0; i < funcionarios.size(); i++) {
			if (Integer.parseInt(tfCodigo.getText())==funcionarios.get(i).getId()) {
				JOptionPane.showMessageDialog(null, "Codigo duplicado", "Atenci�n",JOptionPane.ERROR_MESSAGE);
				tfCodigo.requestFocus();
				tfCodigo.selectAll();
			}
		}
	}
	private void verificarTarjeta() {
		if (tfTarjeta.getText().isEmpty()) {
			return;
		}
		for (int i = 0; i < funcionarios.size(); i++) {
			if (funcionarios.get(i).getTarjeta().equals(tfTarjeta.getText())) {
				JOptionPane.showMessageDialog(null, "Tarjeta duplicada", "Atenci�n",JOptionPane.ERROR_MESSAGE);
				tfTarjeta.requestFocus();
				tfTarjeta.selectAll();
			}
		}
	}
	
	private boolean campoObligatorio() {
		if(tfCodigo.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Codigo es un campo obligatorio");
			tfCodigo.requestFocus();
			return true;
		}
		if(tfDocumento.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Documento es un campo obligatorio");
			tfDocumento.requestFocus();
			return true;
		}
		if(tfNombre.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Nombre es un campo obligatorio");
			tfNombre.requestFocus();
			return true;
		}
		if(tfApellido.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Apellido es un campo obligatorio");
			tfApellido.requestFocus();
			return true;
		}
		if(tfFechaIncorporacion.getValue()==null){
			JOptionPane.showMessageDialog(null, "Fecha Incorporaci�n es un campo obligatorio");
			tfFechaIncorporacion.requestFocus();
			return true;
		}
		if(tfFechaNacimiento.getValue()==null){
			JOptionPane.showMessageDialog(null, "Fecha Nacimiento es un campo obligatorio");
			tfFechaNacimiento.requestFocus();
			return true;
		}
		if(tfTarjeta.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Tarjeta es un campo obligatorio");
			tfTarjeta.requestFocus();
			return true;
		}
		return false;
	}
	
	//verifica si el funcionario esta siendo utilizado en otra tabla
	private boolean verificarRelacion() {
		permisoAccesoDao = new PermisoAccesoDao();
		permisoAccesos = permisoAccesoDao.recuperarTodo();
		for (int i = 0; i < permisoAccesos.size(); i++) {
			if (Integer.parseInt(tfCodigo.getText())==permisoAccesos.get(i).getFuncionario().getId()) {
				JOptionPane.showMessageDialog(null, "Funcionario Con Permisos de Acceso", "Atencion",JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}

	//valida si la fecha no es del dia de hoy
	private boolean validarFecha() {
		Date fecha1, fecha2;
		fecha1 = FechaUtil.stringAFecha(tfFechaNacimiento.getText());
		fecha2 = FechaUtil.stringAFecha(tfFechaIncorporacion.getText());
		if (fecha1.compareTo(fecha2)>=0) {
			JOptionPane.showMessageDialog(null, "Fecha de Nacimiento Invalida");
			return false;
		}
		return true;
	}
	
	//Metodo para Inicializar los datos
	public void inicializarFuncionario() {
		String tabla= "Funcionario";
		dao = new FuncionarioDao();
		dao.eliminarTodos(tabla);
		try {
			dao.ejecutar();
		} catch (Exception e) {
			dao.cancelar();
		}

	}
}