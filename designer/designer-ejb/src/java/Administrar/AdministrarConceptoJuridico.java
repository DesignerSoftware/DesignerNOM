/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.ConceptosJuridicos;
import Entidades.Empresas;
import InterfaceAdministrar.AdministrarConceptoJuridicoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosJuridicosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla 'ConceptoJuridico'.
 * @author betelgeuse
 */
@Stateless
public class AdministrarConceptoJuridico implements AdministrarConceptoJuridicoInterface {

   private static Logger log = Logger.getLogger(AdministrarConceptoJuridico.class);
    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia 'persistenciaConceptosJuridicos'.
     */
    @EJB
    PersistenciaConceptosJuridicosInterface persistenciaConceptosJuridicos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia 'persistenciaEmpresas'.
     */
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    	/**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
        @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    @Override
    public List<ConceptosJuridicos> consultarConceptosJuridicosEmpresa(BigInteger secuencia) {
        try {
            List<ConceptosJuridicos> conceptos = persistenciaConceptosJuridicos.buscarConceptosJuridicosEmpresa(em,secuencia);
            return conceptos;
        } catch (Exception e) {
            log.warn("Error listConceptosJuridicosPorEmpresa Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearConceptosJuridicos(List<ConceptosJuridicos> listCJ) {
        try {
            for (int i = 0; i < listCJ.size(); i++) {                
                persistenciaConceptosJuridicos.crear(em,listCJ.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearConceptosJuridicos Admi : " + e.toString());
        }
    }

    @Override
    public void editarConceptosJuridicos(List<ConceptosJuridicos> listCJ) {
        try {
            for (int i = 0; i < listCJ.size(); i++) {                
                persistenciaConceptosJuridicos.editar(em,listCJ.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarConceptosJuridicos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarConceptosJuridicos(List<ConceptosJuridicos> listCJ) {
        try {
            for (int i = 0; i < listCJ.size(); i++) {
                persistenciaConceptosJuridicos.borrar(em,listCJ.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarConceptosJuridicos Admi : " + e.toString());
        }
    }

    @Override
    public List<Empresas> consultarEmpresas() {
        try {
            List<Empresas> empresas = persistenciaEmpresas.consultarEmpresas(em);
            return empresas;
        } catch (Exception e) {
            log.warn("Error listEmpresas Admi : " + e.toString());
            return null;
        }
    }
}
