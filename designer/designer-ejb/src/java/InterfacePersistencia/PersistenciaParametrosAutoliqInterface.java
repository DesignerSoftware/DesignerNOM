/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ParametrosAutoliq;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaParametrosAutoliqInterface {

    public void crear(EntityManager em, ParametrosAutoliq autoliq);

    public void editar(EntityManager em, ParametrosAutoliq autoliq);

    public void borrar(EntityManager em, ParametrosAutoliq autoliq);

    public List<ParametrosAutoliq> consultarParametrosAutoliq(EntityManager em);

    public List<ParametrosAutoliq> consultarParametrosAutoliqPorEmpresas(EntityManager em);

}
