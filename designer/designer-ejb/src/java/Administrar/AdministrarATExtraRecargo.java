/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.Contratos;
import Entidades.DetallesExtrasRecargos;
import Entidades.ExtrasRecargos;
import Entidades.TiposDias;
import Entidades.TiposJornadas;
import InterfaceAdministrar.AdministrarATExtraRecargoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaContratosInterface;
import InterfacePersistencia.PersistenciaDetallesExtrasRecargosInterface;
import InterfacePersistencia.PersistenciaExtrasRecargosInterface;
import InterfacePersistencia.PersistenciaTiposDiasInterface;
import InterfacePersistencia.PersistenciaTiposJornadasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'ATExtraRecargo'.
 *
 * @author Andres Pineda
 */
@Stateful
public class AdministrarATExtraRecargo implements AdministrarATExtraRecargoInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    

    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaExtrasRecargos'.
     */
    @EJB
    PersistenciaExtrasRecargosInterface persistenciaExtrasRecargos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaDetallesExtrasRecargos'.
     */
    @EJB
    PersistenciaDetallesExtrasRecargosInterface persistenciaDetallesExtrasRecargos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaTiposDias'.
     */
    @EJB
    PersistenciaTiposDiasInterface persistenciaTiposDias;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaTiposJornadas'.
     */
    @EJB
    PersistenciaTiposJornadasInterface persistenciaTiposJornadas;
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
     * 'persistenciaConceptos'.
     */
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;

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
    public List<ExtrasRecargos> consultarExtrasRecargos() {
        try {
            List<ExtrasRecargos> lista = persistenciaExtrasRecargos.buscarExtrasRecargos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listExtrasRecargos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearExtrasRecargos(List<ExtrasRecargos> listaER) {
        try {
            for (int i = 0; i < listaER.size(); i++) {
                persistenciaExtrasRecargos.crear(em,listaER.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearExtrasRecargos Admi : " + e.toString());
        }
    }

    @Override
    public void editarExtrasRecargos(List<ExtrasRecargos> listaER) {
        try {
            for (int i = 0; i < listaER.size(); i++) {
                persistenciaExtrasRecargos.editar(em,listaER.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarExtrasRecargos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarExtrasRecargos(List<ExtrasRecargos> listaER) {
        try {
            for (int i = 0; i < listaER.size(); i++) {
                persistenciaExtrasRecargos.borrar(em,listaER.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarExtrasRecargos Admi : " + e.toString());
        }
    }

    @Override
    public List<DetallesExtrasRecargos> consultarDetallesExtrasRecargos(BigInteger secuencia) {
        try {
            List<DetallesExtrasRecargos> lista = persistenciaDetallesExtrasRecargos.buscaDetallesExtrasRecargosPorSecuenciaExtraRecargo(em,secuencia);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listDetallesExtrasRecargos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearDetallesExtrasRecargos(List<DetallesExtrasRecargos> listaDER) {
        try {
            for (int i = 0; i < listaDER.size(); i++) {
                persistenciaDetallesExtrasRecargos.crear(em,listaDER.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearDetallesExtrasRecargos Admi : " + e.toString());
        }
    }

    @Override
    public void editarDetallesExtrasRecargos(List<DetallesExtrasRecargos> listaDER) {
        try {
            for (int i = 0; i < listaDER.size(); i++) {
                persistenciaDetallesExtrasRecargos.editar(em,listaDER.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarDetallesExtrasRecargos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarDetallesExtrasRecargos(List<DetallesExtrasRecargos> listaDER) {
        try {
            for (int i = 0; i < listaDER.size(); i++) {
                persistenciaDetallesExtrasRecargos.borrar(em,listaDER.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarDetallesExtrasRecargos Admi : " + e.toString());
        }
    }

    @Override
    public List<TiposDias> consultarLOVListaTiposDias() {
        try {
            List<TiposDias> lista = persistenciaTiposDias.buscarTiposDias(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listTiposDias Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposJornadas> consultarLOVTiposJornadas() {
        try {
            List<TiposJornadas> lista = persistenciaTiposJornadas.buscarTiposJornadas(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listTiposJornadas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Contratos> consultarLOVContratos() {
        try {
            List<Contratos> lista = persistenciaContratos.buscarContratos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listContratos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Conceptos> consultarLOVConceptos() {
        try {
            List<Conceptos> lista = persistenciaConceptos.buscarConceptos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listConceptos Admi : " + e.toString());
            return null;
        }
    }
}
