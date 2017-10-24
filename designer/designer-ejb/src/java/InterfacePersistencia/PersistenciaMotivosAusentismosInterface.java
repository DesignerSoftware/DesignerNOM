/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.MotivosAusentismos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaMotivosAusentismosInterface {
  
public String crear(EntityManager em, MotivosAusentismos motivoAusentismo);    
public String editar(EntityManager em, MotivosAusentismos motivoAusentismo);    
public String borrar(EntityManager em, MotivosAusentismos motivoAusentismo);    
public List<MotivosAusentismos> buscarMotivosAusentismo(EntityManager em);
}
