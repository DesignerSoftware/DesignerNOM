/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposBloques;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTiposBloquesInterface {

    public void crear(EntityManager em, TiposBloques tiposBloques);

    public void editar(EntityManager em, TiposBloques tiposBloques);

    public void borrar(EntityManager em, TiposBloques tiposBloques);

    public List<TiposBloques> tiposBloques(EntityManager em, BigInteger secuenciaOperando, String tipo);
    
}