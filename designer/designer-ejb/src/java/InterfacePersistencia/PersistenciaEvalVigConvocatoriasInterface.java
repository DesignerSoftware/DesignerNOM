/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Evalvigconvocatorias;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaEvalVigConvocatoriasInterface {
public void crear(EntityManager em,Evalvigconvocatorias evalconvocatoria);    
public void editar(EntityManager em,Evalvigconvocatorias evalconvocatoria);    
public void borrar(EntityManager em,Evalvigconvocatorias evalconvocatoria);   
public List<Evalvigconvocatorias> consultarEvalConvocatorias(EntityManager em);     
}
