/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposFormulas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTiposFormulasInterface {

    public void crear(EntityManager em, TiposFormulas tiposFormulas);

    public void editar(EntityManager em, TiposFormulas tiposFormulas);

    public void borrar(EntityManager em, TiposFormulas tiposFormulas);

    public List<TiposFormulas> tiposFormulas(EntityManager em, BigInteger secuenciaOperando, String tipo);
    
}
