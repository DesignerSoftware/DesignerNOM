/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Evalconvocatorias;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaEvalConvocatoriasInterface {


public void crear(EntityManager em,Evalconvocatorias evalconvocatoria);    
public void editar(EntityManager em,Evalconvocatorias evalconvocatoria);    
public void borrar(EntityManager em,Evalconvocatorias evalconvocatoria);   
public List<Evalconvocatorias> consultarEvalConvocatorias(EntityManager em,BigInteger secuenciaEmpleado);
}
