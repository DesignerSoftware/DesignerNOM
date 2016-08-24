/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ParametrosContables;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaParametrosContablesInterface {

    public void crear(EntityManager em, ParametrosContables parametrosContables);

    public void editar(EntityManager em, ParametrosContables parametrosContables);

    public void borrar(EntityManager em, ParametrosContables parametrosContables);

    public List<ParametrosContables> buscarParametrosContablesUsuarioBD(EntityManager em, String usuarioBD);
}
