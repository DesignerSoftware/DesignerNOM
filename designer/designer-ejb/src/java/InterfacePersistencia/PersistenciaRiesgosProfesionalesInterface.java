/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.RiesgosProfesionales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaRiesgosProfesionalesInterface {
     public void crear(EntityManager em, RiesgosProfesionales riesgop);
     public void editar(EntityManager em, RiesgosProfesionales riesgop);
     public void borrar(EntityManager em, RiesgosProfesionales riesgop);
      public List<RiesgosProfesionales> riesgosProfesionales(EntityManager em);
    
}
