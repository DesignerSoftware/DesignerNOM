/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.ConceptosJuridicos;
import Entidades.Empresas;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la pantalla 'ConceptosJuridicos'.
 * @author Betelgeuse.
 */
public interface AdministrarConceptoJuridicoInterface {
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de crear ConceptosJuridicos.
     * @param listaConceptoJuridico Lista de los ConceptosJuridicos que se van a crear.
     */
    public void crearConceptosJuridicos(List<ConceptosJuridicos> listaConceptoJuridico);
    /**
     * Método encargado de editar ConceptosJuridicos.
     * @param listaConceptoJuridico Lista de los ConceptosJuridicos que se van a modificar.
     */
    public void editarConceptosJuridicos(List<ConceptosJuridicos> listaConceptoJuridico);
    /**
     * Método encargado de borrar ConceptosJuridicos.
     * @param listaConceptoJuridico Lista de los ConceptosJuridicos que se van a eliminar.
     */
    public void borrarConceptosJuridicos(List<ConceptosJuridicos> listaConceptoJuridico);
    /**
     * Método encargado de recuperar los conceptos juridicos de una empresa específica.
     * @param secEmpresa Seciencia de la empresa.
     * @return Retorna la lista de ConceptosJuridicos que estan asociados a una empresa.
     */
    public List<ConceptosJuridicos> consultarConceptosJuridicosEmpresa(BigInteger secEmpresa);
    /**
     * Método encargado de recuperar todas las Empresas.
     * @return Retorna una lista de Empresas.
     */
    public List<Empresas> consultarEmpresas();
}
