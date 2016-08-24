/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposConstantes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTiposConstantesInterface {

    public void crear(EntityManager em, TiposConstantes tiposConstantes);

    public void editar(EntityManager em, TiposConstantes tiposConstantes);

    public void borrar(EntityManager em, TiposConstantes tiposConstantes);

    public List<TiposConstantes> tiposConstantes(EntityManager em, BigInteger secuenciaOperando, String tipo);
    
}