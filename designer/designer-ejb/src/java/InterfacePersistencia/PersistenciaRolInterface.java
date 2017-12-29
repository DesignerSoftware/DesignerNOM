/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Roles;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaRolInterface {

    public List<Roles> consultarRol(EntityManager em);

    public List<Roles> buscarRol(EntityManager em, BigInteger secuenciaMod);

    public String crear(EntityManager em, Roles rol);

    public String editar(EntityManager em, Roles rol);

    public String borrar(EntityManager em, Roles rol);
}
