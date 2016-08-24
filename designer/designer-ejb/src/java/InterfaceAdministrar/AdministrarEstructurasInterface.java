/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Organigramas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrator
 */
@Local
public interface AdministrarEstructurasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<Estructuras> consultarTodoEstructuras();

    public Estructuras consultarEstructuraPorSecuencia(BigInteger secuenciaE);

    public List<Estructuras> consultarNativeQueryEstructuras(String fechaVigencia);

    public List<Cargos> consultarTodoCargos();

    public Cargos consultarCargoPorSecuencia(BigInteger secuenciaC);

    public List<Estructuras> lovEstructuras();

    public List<Estructuras> Estructuras();
    //PANTALLA ESTRUCTURAS

    public List<Estructuras> estructuraPadre(short codigoOrg);

    public List<Estructuras> estructurasHijas(BigInteger secEstructuraPadre, Short codigoEmpresa);

    public List<Organigramas> obtenerOrganigramas();

    public List<Empresas> obtenerEmpresas();

    public void modificarOrganigrama(List<Organigramas> listOrganigramasModificados);

    public void borrarOrganigrama(Organigramas organigrama);

    public void crearOrganigrama(Organigramas organigrama);
}
