/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.ClavesSap;
import Entidades.Conceptos;
import Entidades.Empresas;
import Entidades.Terceros;
import Entidades.Unidades;
import InterfaceAdministrar.AdministrarConceptosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClavesSapInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUnidadesInterface;
//import Persistencia.PersistenciaSolucionesNodos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * la pantalla 'Conceptos'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarConceptos implements AdministrarConceptosInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaConceptos'.
     */
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaConceptos'.
     */
    @EJB
    PersistenciaClavesSapInterface persistenciaClavesSap;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaUnidades'.
     */
    @EJB
    PersistenciaUnidadesInterface persistenciaUnidades;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaTerceros'.
     */
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaEmpresas'.
     */
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    /**
     * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
     * conexión del usuario que está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Conceptos> consultarConceptosEmpresa(BigInteger secEmpresa) {
        return persistenciaConceptos.conceptosPorEmpresa(em, secEmpresa);
    }

    @Override
    public List<Conceptos> consultarConceptosEmpresaActivos_Inactivos(BigInteger secEmpresa, String estado) {
        return persistenciaConceptos.conceptosEmpresaActivos_Inactivos(em, secEmpresa, estado);
    }

    @Override
    public List<Conceptos> consultarConceptosEmpresaSinPasivos(BigInteger secEmpresa) {
        return persistenciaConceptos.conceptosEmpresaSinPasivos(em, secEmpresa);
    }

    @Override
    public List<Empresas> consultarEmpresas() {
        return persistenciaEmpresas.consultarEmpresas(em);
    }

    public List<Empresas> consultarEmpresaPorSecuencia(BigInteger secEmpresa) {
        return persistenciaEmpresas.buscarEmpresasLista(em, secEmpresa);
    }

    @Override
    public List<Unidades> consultarLOVUnidades() {
        return persistenciaUnidades.consultarUnidades(em);
    }

    @Override
    public List<Terceros> consultarLOVTerceros(BigInteger secEmpresa) {
        return persistenciaTerceros.lovTerceros(em, secEmpresa);
    }

    @Override
    public void modificarConceptos(List<Conceptos> listConceptosModificados) {
        for (int i = 0; i < listConceptosModificados.size(); i++) {
            if (listConceptosModificados.get(i).isIndependienteConcepto() == true) {
                listConceptosModificados.get(i).setIndependiente("S");
            }
            if (listConceptosModificados.get(i).isIndependienteConcepto() == false) {
                listConceptosModificados.get(i).setIndependiente("N");
            }
            if (listConceptosModificados.get(i).getTercero().getSecuencia() == null) {
                listConceptosModificados.get(i).setTercero(null);
            }
            persistenciaConceptos.editar(em, listConceptosModificados.get(i));

        }
    }

    @Override
    public void borrarConceptos(List<Conceptos> listaConceptos) {
        for (int i = 0; i < listaConceptos.size(); i++) {
            if (listaConceptos.get(i).isIndependienteConcepto() == true) {
                listaConceptos.get(i).setIndependiente("S");
            }
            if (listaConceptos.get(i).isIndependienteConcepto() == false) {
                listaConceptos.get(i).setIndependiente("N");
            }

            if (listaConceptos.get(i).getTercero().getSecuencia() == null) {
                listaConceptos.get(i).setTercero(null);
            }
            persistenciaConceptos.borrar(em, listaConceptos.get(i));

        }
    }

    @Override
    public void crearConceptos(List<Conceptos> listaConceptos) {
        for (int i = 0; i < listaConceptos.size(); i++) {
            if (listaConceptos.get(i).isIndependienteConcepto() == true) {
                listaConceptos.get(i).setIndependiente("S");
            }
            if (listaConceptos.get(i).isIndependienteConcepto() == false) {
                listaConceptos.get(i).setIndependiente("N");
            }
            if (listaConceptos.get(i).getTercero().getSecuencia() == null) {
                listaConceptos.get(i).setTercero(null);

            }
            persistenciaConceptos.crear(em, listaConceptos.get(i));

        }
    }

    @Override
    public void clonarConcepto(BigInteger secConceptoOrigen, BigInteger codigoConceptoNuevo, String descripcionConceptoNuevo) {
        persistenciaConceptos.clonarConcepto(em, secConceptoOrigen, codigoConceptoNuevo, descripcionConceptoNuevo);
    }

    @Override
    public List<ClavesSap> consultarLOVClavesSap() {
        List<ClavesSap> listaClavesSap = persistenciaClavesSap.consultarClavesSap(em);
        return listaClavesSap;
    }

    @Override
    public boolean ValidarUpdateConceptoAcumulados(BigInteger secuencia) {
        boolean retorno = persistenciaSolucionesNodos.solucionesNodosParaConcepto(em, secuencia);
        return retorno;
    }
}