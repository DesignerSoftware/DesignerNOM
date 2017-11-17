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

    public String crear(EntityManager em, PermisosPantallas permisosp);
    public String editar(EntityManager em, PermisosPantallas permisosp);
    public String borrar(EntityManager em, PermisosPantallas permisosp);
    public List<PermisosPantallas> consultarPermisosPorPerfil(EntityManager em, BigInteger secPerfil);
}
