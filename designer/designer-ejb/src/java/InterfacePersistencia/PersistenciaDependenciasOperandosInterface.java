/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.DependenciasOperandos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaDependenciasOperandosInterface {
    
    public void crear(EntityManager em,DependenciasOperandos dependenciasOperandos);

    public void editar(EntityManager em,DependenciasOperandos dependenciasOperandos);

    public void borrar(EntityManager em,DependenciasOperandos dependenciasOperandos);

    public List<DependenciasOperandos> dependenciasOperandos(EntityManager em,BigInteger secuenciaOperando);
    
    public String nombreOperandos(EntityManager em,int codigo);
    
}
