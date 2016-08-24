/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ClavesSap;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaClavesSapInterface {

    public void crear(EntityManager em, ClavesSap clavesap);

    public void editar(EntityManager em, ClavesSap clavesap);

    public void borrar(EntityManager em, ClavesSap clavesap);

    public List<ClavesSap> consultarClavesSap(EntityManager em);

    public BigInteger contarClavesContablesCreditoClaveSap(EntityManager em, BigInteger secuencia);

    public BigInteger contarClavesContablesDebitoClaveSap(EntityManager em, BigInteger secuencia);
}
