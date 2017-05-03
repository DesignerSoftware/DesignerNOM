/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.Perfiles;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

public interface PersistenciaPerfilesInterface {
    
    public Perfiles consultarPerfil(EntityManager em, BigInteger secuencia);
    public List<Perfiles> consultarPerfiles(EntityManager em);
    public Perfiles consultarPerfilPorUsuario(EntityManager em);
    public void crear(EntityManager em, Perfiles perfil);
    public void editar(EntityManager em, Perfiles perfil);
    public void borrar(EntityManager em, Perfiles perfil);
    public List<Perfiles> consultarPerfilesAdmon(EntityManager em);
    public void ejecutarPKGRecrearPerfil(EntityManager em, String descripcion,String pwd);
    public void ejecutarPKGEliminarPerfil(EntityManager em, String descripcion);
     public void clonarPantallas(EntityManager em, String nomPerfil);
     public void clonarPermisosObjetos(EntityManager em, String nomPerfil);
    
}
