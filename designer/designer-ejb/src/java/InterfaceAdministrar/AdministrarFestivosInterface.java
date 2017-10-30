/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Festivos;
import Entidades.Paises;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarFestivosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public String modificarFestivos(Festivos festivo);

    public String borrarFestivos(Festivos festivo);

    public String crearFestivos(Festivos festivo);

    public List<Festivos> consultarFestivosPais(BigInteger secPais);

    public List<Paises> consultarLOVPaises();
}
