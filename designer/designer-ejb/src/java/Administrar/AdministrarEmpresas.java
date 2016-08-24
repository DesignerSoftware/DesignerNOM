/**
 * Documentación a cargo de AndresPineda
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.Circulares;
import Entidades.Empresas;
import Entidades.Monedas;
import Entidades.VigenciasMonedasBases;
import InterfaceAdministrar.AdministrarEmpresasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaCircularesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaMonedasInterface;
import InterfacePersistencia.PersistenciaVigenciasMonedasBasesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Empresas'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarEmpresas implements AdministrarEmpresasInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpresas'.
     */
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaCirculares'.
     */
    @EJB
    PersistenciaCircularesInterface persistenciaCirculares;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVigenciasMonedasBases'.
     */
    @EJB
    PersistenciaVigenciasMonedasBasesInterface persistenciaVigenciasMonedasBases;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaCentrosCostos'.
     */
    @EJB
    PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaMonedas'.
     */
    @EJB
    PersistenciaMonedasInterface persistenciaMonedas;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    //________________---------------_________________-------------------_______________//
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Empresas> listaEmpresas() {
        try {
            List<Empresas> lista = persistenciaEmpresas.buscarEmpresas(em);
            return lista;
        } catch (Exception e) {
            System.err.println("Error listaEmpresas Admi : " + e.toString());
            return null;
        }
    }
    
    public Empresas consultarEmpresaSecuencia(BigInteger secEmpresa) {
        try {
            Empresas lista = persistenciaEmpresas.buscarEmpresasSecuencia(em, secEmpresa);
            return lista;
        } catch (Exception e) {
            System.err.println("Error AdministrarEmpresas consultarEmpresaSecuencia : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empresas> listasEmpresasPorSecuenciaEmpresa(BigInteger secuencia) {
        try {
            List<Empresas> lista = persistenciaEmpresas.buscarEmpresasLista(em, secuencia);
            return lista;
        } catch (Exception e) {
            System.err.println("Error listaEmpresas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearEmpresas(List<Empresas> listaE) {
        try {
            for (int i = 0; i < listaE.size(); i++) {
                if (listaE.get(i).getCentrocosto().getSecuencia() == null) {
                    listaE.get(i).setCentrocosto(new CentrosCostos());
                }
                persistenciaEmpresas.crear(em, listaE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearEmpresas Admi : " + e.toString());
        }
    }

    @Override
    public void editarEmpresas(List<Empresas> listaE) {
        try {
            for (int i = 0; i < listaE.size(); i++) {
                /*if (listaE.get(i).getCentrocosto().getSecuencia() == null) {
                    listaE.get(i).setCentrocosto(new CentrosCostos());
                }*/
                persistenciaEmpresas.editar(em, listaE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarEmpresas Admi : " + e.toString());
        }
    }

    @Override
    public void borrarEmpresas(List<Empresas> listaE) {
        try {
            for (int i = 0; i < listaE.size(); i++) {
                if (listaE.get(i).getCentrocosto().getSecuencia() == null) {
                    listaE.get(i).setCentrocosto(new CentrosCostos());
                }
                persistenciaEmpresas.borrar(em, listaE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarEmpresas Admi : " + e.toString());
        }
    }

    @Override
    public List<Circulares> listaCircularesParaEmpresa(BigInteger secuencia) {
        try {
            List<Circulares> lista = persistenciaCirculares.buscarCircularesPorSecuenciaEmpresa(em, secuencia);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaCircularesParaEmpresa Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearCirculares(List<Circulares> listaC) {
        try {
            for (int i = 0; i < listaC.size(); i++) {
                persistenciaCirculares.crear(em, listaC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearEmpresas Admi : " + e.toString());
        }
    }

    @Override
    public void editarCirculares(List<Circulares> listaC) {
        try {
            for (int i = 0; i < listaC.size(); i++) {
                persistenciaCirculares.editar(em, listaC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarEmpresas Admi : " + e.toString());
        }
    }

    @Override
    public void borrarCirculares(List<Circulares> listaC) {
        try {
            for (int i = 0; i < listaC.size(); i++) {
                persistenciaCirculares.borrar(em, listaC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarEmpresas Admi : " + e.toString());
        }
    }

    @Override
    public List<VigenciasMonedasBases> listaVigenciasMonedasBasesParaEmpresa(BigInteger secuencia) {
        try {
            List<VigenciasMonedasBases> lista = persistenciaVigenciasMonedasBases.buscarVigenciasMonedasBasesPorSecuenciaEmpresa(em, secuencia);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaVigenciasMonedasBasesParaEmpresa Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearVigenciasMonedasBases(List<VigenciasMonedasBases> listaVMB) {
        try {
            for (int i = 0; i < listaVMB.size(); i++) {
                persistenciaVigenciasMonedasBases.crear(em, listaVMB.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearVigenciasMonedasBases Admi : " + e.toString());
        }
    }

    @Override
    public void editarVigenciasMonedasBases(List<VigenciasMonedasBases> listaVMB) {
        try {
            for (int i = 0; i < listaVMB.size(); i++) {
                persistenciaVigenciasMonedasBases.editar(em, listaVMB.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarVigenciasMonedasBases Admi : " + e.toString());
        }
    }

    @Override
    public void borrarVigenciasMonedasBases(List<VigenciasMonedasBases> listaVMB) {
        try {
            for (int i = 0; i < listaVMB.size(); i++) {
                persistenciaVigenciasMonedasBases.borrar(em, listaVMB.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarVigenciasMonedasBases Admi : " + e.toString());
        }
    }

    @Override
    public List<CentrosCostos> lovCentrosCostos() {
        try {
            List<CentrosCostos> lista = persistenciaCentrosCostos.buscarCentrosCostos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovCentrosCostos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Monedas> lovMonedas() {
        try {
            List<Monedas> lista = persistenciaMonedas.consultarMonedas(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovMonedas Admi : " + e.toString());
            return null;
        }
    }

}
