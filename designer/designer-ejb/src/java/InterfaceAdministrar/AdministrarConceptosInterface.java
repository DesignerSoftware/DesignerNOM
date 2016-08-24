/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.ClavesSap;
import Entidades.Conceptos;
import Entidades.Empresas;
import Entidades.Terceros;
import Entidades.Unidades;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'Conceptos'.
 *
 * @author betelgeuse.
 */
public interface AdministrarConceptosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de crear Conceptos.
     *
     * @param listaConceptos Lista de los Conceptos que se van a crear.
     */
    public void crearConceptos(List<Conceptos> listaConceptos);

    /**
     * Método encargado de editar Conceptos.
     *
     * @param listaConceptos Lista de los Conceptos que se van a modificar.
     */
    public void modificarConceptos(List<Conceptos> listaConceptos);

    /**
     * Método encargado de borrar Conceptos.
     *
     * @param listaConceptos Lista de los Conceptos que se van a eliminar.
     */
    public void borrarConceptos(List<Conceptos> listaConceptos);

    /**
     * Método encargado de recuperar las unidades necesarias para la lista de
     * valores.
     *
     * @return Retorna una lista de Unidades.
     */
    public List<Unidades> consultarLOVUnidades();

    /**
     * Método encargado de recuperar los terceros para la lista de valores de
     * una empresa específica.
     *
     * @param secEmpresa Secuencia de la empresa.
     * @return Retorna una lista de terceros.
     */
    public List<Terceros> consultarLOVTerceros(BigInteger secEmpresa);

    /**
     * Método encargado de recuperar todas las Empresas.
     *
     * @return Retorna una lista de Empresas.
     */
    public List<Empresas> consultarEmpresas();

    /**
     * Método encargado de recuperar los conceptos según la empresa que tengan
     * asociada.
     *
     * @param secEmpresa Secuencia de la Empresa.
     * @return Retorna una lista de conceptos.
     */
    public List<Conceptos> consultarConceptosEmpresa(BigInteger secEmpresa);

    /**
     * Método encargado de recuperar los conceptos de una empresa según su
     * estado. Su estado puede ser 'ACTIVO' o 'INACTIVO'.
     *
     * @param secEmpresa Secuencia de la empresa a la que pertenecen los
     * conceptos.
     * @param estado Estado del concepto.
     * @return Retorna una lista de conceptos.
     */
    public List<Conceptos> consultarConceptosEmpresaActivos_Inactivos(BigInteger secEmpresa, String estado);

    /**
     * M[etodo encargado de recuperar los todos conceptos NO pasivos de una
     * empresa.
     *
     * @param secEmpresa Secuencia de la Empresa.
     * @return Retorna una lista de Conceptos.
     */
    public List<Conceptos> consultarConceptosEmpresaSinPasivos(BigInteger secEmpresa);

    /**
     * Método encargado de clonar un concepto.
     *
     * @param secConceptoOrigen Secuencia del concepto que se va a clonar.
     * @param codigoConceptoNuevo Código del nuevo concepto.
     * @param descripcionConceptoNuevo Descripción del nuevo concepto.
     */
    public void clonarConcepto(BigInteger secConceptoOrigen, BigInteger codigoConceptoNuevo, String descripcionConceptoNuevo);

    public List<ClavesSap> consultarLOVClavesSap();

    public List<Empresas> consultarEmpresaPorSecuencia(BigInteger secEmpresa);

    public boolean ValidarUpdateConceptoAcumulados(BigInteger secuencia);
}
