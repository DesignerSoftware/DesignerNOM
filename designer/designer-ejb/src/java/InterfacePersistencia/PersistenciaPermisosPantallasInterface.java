/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.PermisosPantallas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaPermisosPantallasInterface {

    public void crear(EntityManager em, PermisosPantallas permisosp);
    public void editar(EntityManager em, PermisosPantallas permisosp);
    public void borrar(EntityManager em, PermisosPantallas permisosp);
    public List<PermisosPantallas> consultarPermisosPorPerfil(EntityManager em, BigInteger secPerfil);
}
