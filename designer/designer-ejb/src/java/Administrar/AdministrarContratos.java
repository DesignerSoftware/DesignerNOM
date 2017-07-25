/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Contratos;
import Entidades.TiposCotizantes;
import InterfaceAdministrar.AdministrarContratosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaContratosInterface;
import InterfacePersistencia.PersistenciaTiposCotizantesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Contratos'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarContratos implements AdministrarContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarContratos.class);

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaContratos'.
     */
    @EJB
    PersistenciaContratosInterface persistenciaContratos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaTiposCotizantes'.
     */
    @EJB
    PersistenciaTiposCotizantesInterface persistenciaTiposCotizantes;
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
    public List<Contratos> consultarContratos() {
        return persistenciaContratos.lovContratos(em);
    }

    @Override
    public List<TiposCotizantes> consultaLOVTiposCotizantes() {
        return persistenciaTiposCotizantes.lovTiposCotizantes(em);
    }

    @Override
    public void modificarConceptos(List<Contratos> listContratosModificados) {
        for (int i = 0; i < listContratosModificados.size(); i++) {
            if (listContratosModificados.get(i).getTipocotizante().getSecuencia() == null) {
                listContratosModificados.get(i).setTipocotizante(null);
            }
            persistenciaContratos.editar(em, listContratosModificados.get(i));
        }
    }

    @Override
    public void borrarConceptos(List<Contratos> listaContratos) {
        for (int i = 0; i < listaContratos.size(); i++) {
            if (listaContratos.get(i).getTipocotizante().getSecuencia() == null) {
                listaContratos.get(i).setTipocotizante(null);
                persistenciaContratos.borrar(em, listaContratos.get(i));
            } else {
                persistenciaContratos.borrar(em, listaContratos.get(i));
            }
        }
    }

    @Override
    public void crearConceptos(List<Contratos> listaContratos) {
        for (int i = 0; i < listaContratos.size(); i++) {
            if (listaContratos.get(i).getTipocotizante().getSecuencia() == null) {
                listaContratos.get(i).setTipocotizante(null);
                persistenciaContratos.crear(em, listaContratos.get(i));
            } else {
                persistenciaContratos.crear(em, listaContratos.get(i));
            }
        }
    }

    @Override
    public void reproducirContrato(Short codigoOrigen, Short codigoDestino) {
        persistenciaContratos.reproducirContrato(em, codigoOrigen, codigoDestino);
    }
}
