/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import java.util.Date;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
@Local
public interface PersistenciaVWActualesFechasInterface {

    public Date actualFechaHasta(EntityManager em);

    public Date actualFechaDesde(EntityManager em);
}
