/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.UsuariosProcesos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaUsuariosProcesosInterface {

    public void crear(EntityManager em, UsuariosProcesos usuarioproceso);

    public void editar(EntityManager em, UsuariosProcesos usuarioproceso);

    public void borrar(EntityManager em, UsuariosProcesos usuarioproceso);

    public List<UsuariosProcesos> buscarUsuariosProcesos(EntityManager em);
}
