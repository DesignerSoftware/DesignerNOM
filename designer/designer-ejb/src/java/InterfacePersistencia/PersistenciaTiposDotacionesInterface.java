/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposDotaciones;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTiposDotacionesInterface {
    public String crear(EntityManager em, TiposDotaciones tipodotacion);

    public String editar(EntityManager em, TiposDotaciones tipodotacion);

    public String borrar(EntityManager em, TiposDotaciones tipodotacion);

    public List<TiposDotaciones> consultarTiposDotaciones(EntityManager em );

}
