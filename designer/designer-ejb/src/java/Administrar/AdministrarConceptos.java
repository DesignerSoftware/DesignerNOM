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
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * la pantalla 'Conceptos'.
 */
@Stateful
public class AdministrarConceptos implements AdministrarConceptosInterface {

    private static Logger log = Logger.getLogger(AdministrarConceptos.class);

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
    private EntityManagerFactory emf;
    private EntityManager em;
    private String idSesionBck;

    private EntityManager getEm() {
        try {
            if (this.emf != null) {
                if (this.em != null) {
                    if (this.em.isOpen()) {
                        this.em.close();
                    }
                }
            } else {
                this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
            }
            this.em = emf.createEntityManager();
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
        }
        return this.em;
    }
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        idSesionBck = idSesion;
        try {
            emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
        }
    }

    @Override
    public List<Conceptos> consultarConceptosEmpresa(BigInteger secEmpresa) {
        try {
            return persistenciaConceptos.conceptosPorEmpresa(getEm(), secEmpresa);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarConceptosEmpresa() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Conceptos> consultarConceptosEmpresaActivos_Inactivos(BigInteger secEmpresa, String estado) {
        try {
            return persistenciaConceptos.conceptosEmpresaActivos_Inactivos(getEm(), secEmpresa, estado);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarConceptosEmpresaActivos_Inactivos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Conceptos> consultarConceptosEmpresaSinPasivos(BigInteger secEmpresa) {
        try {
            return persistenciaConceptos.conceptosEmpresaSinPasivos(getEm(), secEmpresa);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarConceptosEmpresaSinPasivos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Empresas> consultarEmpresas() {
        try {
            return persistenciaEmpresas.consultarEmpresas(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarEmpresas() ERROR: " + e);
            return null;
        }
    }

    public List<Empresas> consultarEmpresaPorSecuencia(BigInteger secEmpresa) {
        try {
            return persistenciaEmpresas.buscarEmpresasLista(getEm(), secEmpresa);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarEmpresaPorSecuencia() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Unidades> consultarLOVUnidades() {
        try {
            return persistenciaUnidades.consultarUnidades(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarLOVUnidades() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Terceros> consultarLOVTerceros(BigInteger secEmpresa) {
        try {
            return persistenciaTerceros.lovTerceros(getEm(), secEmpresa);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarLOVTerceros() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String modificarConceptos(List<Conceptos> listConceptosModificados) {
        try {
            String resultado = "";
            for (int i = 0; i < listConceptosModificados.size(); i++) {
                if (listConceptosModificados.get(i).isIndependienteConcepto() == true) {
                    listConceptosModificados.get(i).setIndependiente("S");
                }
                if (listConceptosModificados.get(i).isIndependienteConcepto() == false) {
                    listConceptosModificados.get(i).setIndependiente("N");
                }
                resultado = persistenciaConceptos.editar(getEm(), listConceptosModificados.get(i));
            }
            return resultado;
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarConceptos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarConceptos(List<Conceptos> listaConceptos) {
        try {
            String resultado = "";
            for (int i = 0; i < listaConceptos.size(); i++) {
                if (listaConceptos.get(i).isIndependienteConcepto() == true) {
                    listaConceptos.get(i).setIndependiente("S");
                }
                if (listaConceptos.get(i).isIndependienteConcepto() == false) {
                    listaConceptos.get(i).setIndependiente("N");
                }
                resultado = persistenciaConceptos.borrar(getEm(), listaConceptos.get(i));
            }
            return resultado;
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarConceptos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearConceptos(List<Conceptos> listaConceptos) {
        try {
            String resultado = "";
            for (int i = 0; i < listaConceptos.size(); i++) {
                if (listaConceptos.get(i).isIndependienteConcepto() == true) {
                    listaConceptos.get(i).setIndependiente("S");
                }
                if (listaConceptos.get(i).isIndependienteConcepto() == false) {
                    listaConceptos.get(i).setIndependiente("N");
                }
                resultado = persistenciaConceptos.crear(getEm(), listaConceptos.get(i));
            }
            return resultado;
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearConceptos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public void clonarConcepto(BigInteger secConceptoOrigen, BigInteger codigoConceptoNuevo, String descripcionConceptoNuevo) {
        try {
            persistenciaConceptos.clonarConcepto(getEm(), secConceptoOrigen, codigoConceptoNuevo, descripcionConceptoNuevo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".clonarConcepto() ERROR: " + e);
        }
    }

    @Override
    public List<ClavesSap> consultarLOVClavesSap() {
        try {
            List<ClavesSap> listaClavesSap = persistenciaClavesSap.consultarClavesSap(getEm());
            return listaClavesSap;
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarLOVClavesSap() ERROR: " + e);
            return null;
        }
    }

    @Override
    public boolean ValidarUpdateConceptoAcumulados(BigInteger secuencia) {
        try {
            boolean retorno = persistenciaSolucionesNodos.solucionesNodosParaConcepto(getEm(), secuencia);
            return retorno;
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".ValidarUpdateConceptoAcumulados() ERROR: " + e);
            return false;
        }
    }
}
