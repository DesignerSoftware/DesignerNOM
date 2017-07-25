/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Cuentas;
import Entidades.VigenciasCuentas;
import InterfaceAdministrar.AdministrarDetalleCuentaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCuentasInterface;
import InterfacePersistencia.PersistenciaVigenciasCuentasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'DetalleCuenta'.
 *
 * @author Betelgeuse.
 */
@Stateful
public class AdministrarDetalleCuenta implements AdministrarDetalleCuentaInterface {

   private static Logger log = Logger.getLogger(AdministrarDetalleCuenta.class);

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVigenciasCuentas'.
     */
    @EJB
    PersistenciaVigenciasCuentasInterface persistenciaVigenciasCuentas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaCuentas'.
     */
    @EJB
    PersistenciaCuentasInterface persistenciaCuentas;
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
    public List<VigenciasCuentas> consultarListaVigenciasCuentasCredito(BigInteger secuenciaC) {
        try {
            List<VigenciasCuentas> listCCredito = persistenciaVigenciasCuentas.buscarVigenciasCuentasPorCredito(em,secuenciaC);
            return listCCredito;
        } catch (Exception e) {
            log.warn("Error listVigenciasCuentasCredito Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<VigenciasCuentas> consultarListaVigenciasCuentasDebito(BigInteger secuenciaC) {
        try {
            List<VigenciasCuentas> listCDebito = persistenciaVigenciasCuentas.buscarVigenciasCuentasPorDebito(em,secuenciaC);
            return listCDebito;
        } catch (Exception e) {
            log.warn("Error listVigenciasCuentasDebito Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Cuentas mostrarCuenta(BigInteger secuencia) {
        try {
            Cuentas cuentaActual = persistenciaCuentas.buscarCuentasSecuencia(em,secuencia);
            return cuentaActual;
        } catch (Exception e) {
            log.warn("Error cuentaActual Admi: " + e.toString());
            return null;
        }
    }
}
