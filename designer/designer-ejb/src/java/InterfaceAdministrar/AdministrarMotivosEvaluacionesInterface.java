/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosEvaluaciones;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarMotivosEvaluacionesInterface {

    public void obtenerConexion(String idSesion);

    public String modificarMotivosEvaluaciones(MotivosEvaluaciones motivo);

    public String crearMotivosEvaluaciones(MotivosEvaluaciones motivo);

    public String borrarMotivosEvaluaciones(MotivosEvaluaciones motivo);

    public List<MotivosEvaluaciones> consultarMotivosEvaluaciones();
}
