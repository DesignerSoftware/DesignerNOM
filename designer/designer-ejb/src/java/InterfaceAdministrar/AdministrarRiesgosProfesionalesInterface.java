/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.RiesgosProfesionales;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarRiesgosProfesionalesInterface {
   public void obtenerConexion(String idSesion);
    
    public List<RiesgosProfesionales> listRiesgoProfesional();

    public void crearRiesgoProfesional(List<RiesgosProfesionales> listaVD);

    public void editarRiesgoProfesional(List<RiesgosProfesionales> listaVD);

    public void borrarRiesgoProfesional(List<RiesgosProfesionales> listaVD);

}
