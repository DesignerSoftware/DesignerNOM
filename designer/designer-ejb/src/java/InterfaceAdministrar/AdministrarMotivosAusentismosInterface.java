/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosAusentismos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarMotivosAusentismosInterface {

    public void obtenerConexion(String idSesion);

    public String modificarMotivosAusentismos(MotivosAusentismos motivoAusentismo);

    public String crearMotivosAusentismos(MotivosAusentismos motivoAusentismo);

    public String borrarMotivosAusentismos(MotivosAusentismos motivoAusentismo);
    
    public List<MotivosAusentismos> consultarMotivosAusentismos();

}
