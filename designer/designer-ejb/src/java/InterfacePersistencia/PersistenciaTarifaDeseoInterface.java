/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.TarifaDeseo;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTarifaDeseoInterface {
    
    public List<TarifaDeseo> retenciones(EntityManager em, Date fechaFinal);
    
}
