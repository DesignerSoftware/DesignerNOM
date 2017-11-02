/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.MotivosAuxiliares;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaMotivosAuxiliaresInterface {

    public String crear(EntityManager em, MotivosAuxiliares motivoAux);

    public String editar(EntityManager em, MotivosAuxiliares motivoAux);

    public String borrar(EntityManager em, MotivosAuxiliares motivoAux);

    public List<MotivosAuxiliares> buscarMotivosAuxiliares(EntityManager em);

}
