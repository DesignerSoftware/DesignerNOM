/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.PermisosObjetosDB;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

public interface PersistenciaPermisosObjetosDBInterface {
   public void crear(EntityManager em, PermisosObjetosDB permisosp);
   public void editar(EntityManager em, PermisosObjetosDB permisosp);
   public void borrar(EntityManager em, PermisosObjetosDB permisosp);
   public List<PermisosObjetosDB> consultarPermisosPorPerfil(EntityManager em, BigInteger secPerfil);   
}
