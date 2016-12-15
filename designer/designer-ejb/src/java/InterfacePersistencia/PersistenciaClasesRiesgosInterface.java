/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ClasesRiesgos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaClasesRiesgosInterface {
    
public void crear(EntityManager em, ClasesRiesgos claseRiesgo);    
public void editar(EntityManager em, ClasesRiesgos claseRiesgo);    
public void borrar(EntityManager em, ClasesRiesgos claseRiesgo);    
public List<ClasesRiesgos> consultarListaClasesRiesgos(EntityManager em);
    
}
