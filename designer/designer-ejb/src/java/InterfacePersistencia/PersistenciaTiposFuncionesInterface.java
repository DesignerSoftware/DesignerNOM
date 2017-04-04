/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.TiposFunciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTiposFuncionesInterface {
    
    public List<TiposFunciones> tiposFunciones(EntityManager em, BigInteger secuenciaOperando);
    
    public void crear(EntityManager em, TiposFunciones tiposFunciones);

    public void editar(EntityManager em, TiposFunciones tiposFunciones);
    
    public void borrar(EntityManager em, TiposFunciones tiposFunciones);
    
}
