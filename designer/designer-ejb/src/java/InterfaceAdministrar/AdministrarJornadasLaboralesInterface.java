/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Jornadas;
import Entidades.JornadasLaborales;
import Entidades.JornadasSemanales;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrador
 */
public interface AdministrarJornadasLaboralesInterface {

    public List<JornadasLaborales> consultarJornadasLaborales();

    public void obtenerConexion(String idSesion);

    public List<Jornadas> consultarJornadas();

    public void modificarJornadasLaborales(List<JornadasLaborales> listaJornadasLaborales);

    public void borrarJornadasLaborales(List<JornadasLaborales> listaJornadasLaborales);

    public void crearJornadasLaborales(List<JornadasLaborales> listaJornadasLaborales);

    public void modificarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales);

    public void borrarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales);

    public void crearJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales);

    public List<JornadasSemanales> consultarJornadasSemanales(BigInteger secuencia);
}
