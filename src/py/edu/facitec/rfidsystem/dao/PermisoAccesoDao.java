package py.edu.facitec.rfidsystem.dao;

import java.util.List;

import py.edu.facitec.rfidsystem.entidad.PermisoAcceso;

public class PermisoAccesoDao extends GenericDao<PermisoAcceso> {

	public PermisoAccesoDao() {
		super(PermisoAcceso.class);
	}
	
	public List<PermisoAcceso> recuperarPorFiltro(String filtro){
		int filtroId = 0;
		try {
			filtroId = Integer.parseInt(filtro);
		} catch (NumberFormatException e) {
		}
		instanciarCriteria();
		criteriaQuery.where(
				builder.or(
						builder.equal(root.<Integer>get("id"), filtroId))
				);
		lista = session.createQuery(criteriaQuery).getResultList();
		cerrar();
		return lista;
	}

}