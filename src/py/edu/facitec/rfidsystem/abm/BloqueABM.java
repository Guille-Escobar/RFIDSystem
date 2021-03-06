package py.edu.facitec.rfidsystem.abm;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import py.edu.facitec.rfidsystem.buscadores.BuscadorInstitucion;
import py.edu.facitec.rfidsystem.contenedores.BotonPersonalizadoABM;
import py.edu.facitec.rfidsystem.dao.BloqueDao;
import py.edu.facitec.rfidsystem.dao.OficinaDao;
import py.edu.facitec.rfidsystem.entidad.Bloque;
import py.edu.facitec.rfidsystem.entidad.Institucion;
import py.edu.facitec.rfidsystem.entidad.Oficina;
import py.edu.facitec.rfidsystem.interfaces.InterfazBuscarInstitucion;
import py.edu.facitec.rfidsystem.tablas.TablaBloque;

public class BloqueABM extends GenericABM implements InterfazBuscarInstitucion {
	private JTextField tfBuscar;
	private JTextField tfCodigo;
	private JTextField tfDescripcion;
	private JTextField tfInstitucion;
	private Bloque bloque;
	private Institucion institucion;
	private BloqueDao dao;
	private List<Bloque> bloques;
	private TablaBloque tablaBloque;
	private OficinaDao oficinaDao;
	private List<Oficina> oficinas;
	private BotonPersonalizadoABM btnBuscarInstitucion;
	
	public BloqueABM() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(BloqueABM.class.getResource("/py/edu/facitec/rfidsystem/img/bloque.png")));
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		});
		setTitle("Registro de Bloque");
		setBounds(100, 100, 697, 449);
		setLocationRelativeTo(this);
		tablaBloque=new TablaBloque();
		table.setModel(tablaBloque);
		btnGuardar.setLocation(10, 381);
		btnCancelar.setLocation(239, 381);
		scrollPane.setBounds(350, 132, 328, 276);
		
		BotonPersonalizadoABM btnBuscar = new BotonPersonalizadoABM();
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscarBloque();
			}
		});
		btnBuscar.setText("Buscar");
		btnBuscar.setBounds(346, 95, 90, 26);
		getContentPane().add(btnBuscar);
		
		tfBuscar = new JTextField();
		tfBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c== e.VK_ENTER) {
					buscarBloque();
				}
			}
		});
		tfBuscar.setColumns(10);
		tfBuscar.setBounds(463, 98, 215, 20);
		getContentPane().add(tfBuscar);
		
		JLabel lblCodigo = new JLabel("C\u00F3digo:");
		lblCodigo.setFont(new Font("Arial", Font.BOLD, 12));
		lblCodigo.setBounds(10, 168, 46, 14);
		getContentPane().add(lblCodigo);
		
		tfCodigo = new JTextField();
		tfCodigo.setEnabled(false);
		tfCodigo.setColumns(10);
		tfCodigo.setBounds(113, 166, 92, 20);
		getContentPane().add(tfCodigo);
		
		tfDescripcion = new JTextField();
		tfDescripcion.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == e.VK_ENTER) {
					btnBuscarInstitucion.requestFocus();
				}
			}
		});
		tfDescripcion.setEnabled(false);
		tfDescripcion.setColumns(10);
		tfDescripcion.setBounds(113, 221, 158, 20);
		getContentPane().add(tfDescripcion);
		
		JLabel lblDescripcin = new JLabel("Descripci\u00F3n:");
		lblDescripcin.setFont(new Font("Arial", Font.BOLD, 12));
		lblDescripcin.setBounds(10, 223, 102, 14);
		getContentPane().add(lblDescripcin);
		
		JLabel lblRegistrosDeInstitucin = new JLabel("Registro De Bloque");
		lblRegistrosDeInstitucin.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegistrosDeInstitucin.setForeground(Color.BLUE);
		lblRegistrosDeInstitucin.setFont(new Font("Algerian", Font.PLAIN, 24));
		lblRegistrosDeInstitucin.setBounds(1, 11, 690, 53);
		getContentPane().add(lblRegistrosDeInstitucin);
		
		JLabel lblIntitucin = new JLabel("Intituci\u00F3n:");
		lblIntitucin.setFont(new Font("Arial", Font.BOLD, 12));
		lblIntitucin.setBounds(10, 288, 102, 14);
		getContentPane().add(lblIntitucin);
		
		tfInstitucion = new JTextField();
		tfInstitucion.setEditable(false);
		tfInstitucion.setColumns(10);
		tfInstitucion.setBounds(113, 286, 158, 20);
		getContentPane().add(tfInstitucion);
		
		btnBuscarInstitucion = new BotonPersonalizadoABM();
		btnBuscarInstitucion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirBuscadorInstitucion();
			}
		});
		btnBuscarInstitucion.setEnabled(false);
		btnBuscarInstitucion.setText("...");
		btnBuscarInstitucion.setBounds(281, 286, 46, 20);
		getContentPane().add(btnBuscarInstitucion);
		consultarBloque();
		
	}
	
	//---------------Metodos Genericos-------------
	@Override
	protected void limpiar() {
		tfCodigo.setText("");
		tfDescripcion.setText("");
		tfInstitucion.setText("");
		tfCodigo.requestFocus();
	}
	
	@Override
	protected void habilitarCampos(boolean e) {
		tfDescripcion.setEnabled(e);
		btnBuscarInstitucion.setEnabled(e);
	}
	
	@Override
	protected void guardar() {
		if (campoObligatorio()==true) return;
		cargarDatos();
		dao = new BloqueDao();
		dao.insertarOModificar(bloque);
		try {
			dao.ejecutar();
		} catch (Exception e) {
			dao.cancelar();
		}
		modificar=false;
		consultarBloque();
		habilitarCampos(false);
		limpiar();
		accionesPrimarias(true);
		bloque=null;
	}
	
	@Override
	protected void fechaActual() {}
	
	protected void cargarFormulario(int index) {
		if (index < 0) return;
		bloque=bloques.get(index);
		institucion= bloque.getInstitucion();
		tfCodigo.setText(bloque.getId()+"");
		tfDescripcion.setText(bloque.getNombre());
		tfInstitucion.setText(bloque.getInstitucion().getDescripcion());
		modificar=false;
		habilitarCampos(false);
		accionesPrimarias(false);
	}
	
	public void cargarCodigo(){
		dao = new BloqueDao();
		bloques = dao.recuperarTodo();
		int c =0, i=0;
		i= bloques.size()-1;
		if (i>=0) c = bloques.get(i).getId();
		tfCodigo.setText(""+(c+1));
		tfDescripcion.requestFocus();
	}
	
	private void eliminar() {
		if(verificarRelacion()==false) return;
		if (table.getSelectedRow()<0) return;
		int respuesta = JOptionPane.showConfirmDialog(null, 
				"Esta seguro que desea eliminar el registro de Bloque "+bloque.getNombre(),
				"Atenci�n",
				JOptionPane.YES_NO_OPTION);
		if(respuesta == JOptionPane.YES_OPTION){
			dao = new BloqueDao();
			dao.eliminar(bloque);
			try {
				dao.ejecutar();
			} catch (Exception e) {
				dao.cancelar();
			}
			consultarBloque();
		}
		limpiar();
		accionesPrimarias(true);
	}
	
	private void cargarDatos() {
		if (modificar==false) bloque = new Bloque();
		bloque.setNombre(tfDescripcion.getText());
		bloque.setInstitucion(institucion);
	}
	
	private void consultarBloque() {
		dao = new BloqueDao();
		bloques = dao.recuperarTodo();
		tablaBloque.setLista(bloques);
		tablaBloque.fireTableDataChanged();
	}

	private void buscarBloque() {
		dao = new BloqueDao();
		bloques = dao.recuperarPorFiltro(tfBuscar.getText());
		tablaBloque.setLista(bloques);
		tablaBloque.fireTableDataChanged();
	}

	@Override
	public void setInstitucion(Institucion institucion) {
		this.institucion=institucion;
		tfInstitucion.setText(institucion.getDescripcion());
	}
	private void abrirBuscadorInstitucion() {
		BuscadorInstitucion buscadorInstitucion = new BuscadorInstitucion();
		buscadorInstitucion.setInterfaz(this);
		buscadorInstitucion.setVisible(true);

	}
	
	//-----------------------Validaciones-----------------
	private boolean campoObligatorio() {
		if(tfDescripcion.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Descripci�n es un campo obligatorio");
			tfDescripcion.requestFocus();
			return true;
		}
		if(tfInstitucion.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Instituci�n es un campo obligatorio");
			btnBuscarInstitucion.requestFocus();
			return true;
		}	
		return false;
	}

	private boolean verificarRelacion() {
		oficinaDao = new OficinaDao();
		oficinas = oficinaDao.recuperarTodo();
		for (int i = 0; i < oficinas.size(); i++) {
			if (Integer.parseInt(tfCodigo.getText())==oficinas.get(i).getBloque().getId()) {
				JOptionPane.showMessageDialog(null, "Bloque con Oficinas Registradas", "Atenci�n",JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
	
	//Metodo para inicializar los datos
	public void inicializarBloques(){
		String tabla = "Bloque";
		dao = new BloqueDao();
		dao.eliminarTodos(tabla);
		try {
			dao.ejecutar();
		} catch (Exception e) {
			dao.cancelar();
		}
	}
	
}
