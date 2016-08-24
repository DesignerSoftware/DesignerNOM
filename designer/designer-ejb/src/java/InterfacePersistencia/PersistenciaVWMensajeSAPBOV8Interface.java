/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.VWMensajeSAPBOV8;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaVWMensajeSAPBOV8Interface {
    public List<VWMensajeSAPBOV8> buscarListaErroresSAPBOV8(EntityManager em);
}
