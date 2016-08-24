package InterfaceAdministrar;

import Entidades.Jornadas;
import Entidades.JornadasLaborales;
import Entidades.JornadasSemanales;
import java.util.List;

/**
 *
 * @author Administrador
 */
public interface AdministrarJornadasSemanalesInterface {

    public void obtenerConexion(String idSesion);

    public List<JornadasSemanales> consultarJornadasSemanales();

    public List<JornadasLaborales> consultarJornadasLaborales();

    public List<Jornadas> consultarJornadas();

    public void modificarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales);

    public void borrarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales);

    public void crearJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales);

}
