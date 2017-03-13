package py.edu.facitec.rfidsystem.dao;

import java.util.List;

import py.edu.facitec.rfidsystem.entidad.Funcionario;

public class FuncionarioDao extends GenericDao<Funcionario> {

	public FuncionarioDao() {
		super(Funcionario.class);
	}
	
	public List<Funcionario> recuperarPorFiltro(String filtro){
		instanciarCriteria();
		criteriaQuery.where(
				builder.or(
						builder.like(builder.lower(root.<String>get("nombre")), "%"+filtro.toLowerCase()+"%"),
						builder.like(builder.lower(root.<String>get("apellido")), "%"+filtro.toLowerCase()+"%"),
						builder.like(builder.lower(root.<String>get("tarjeta")), "%"+filtro.toLowerCase()+"%"))
				);
		lista = session.createQuery(criteriaQuery).getResultList();
		cerrar();
		return lista;
	}

}
