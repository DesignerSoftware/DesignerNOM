/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Organigramas;
import InterfaceAdministrar.AdministrarEstructurasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaOrganigramasInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrator
 */
@Stateless
public class AdministrarEstructuras implements AdministrarEstructurasInterface {

    //------------------------------------------------------------------------------------------
    //EJB
    //------------------------------------------------------------------------------------------
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    @EJB
    PersistenciaOrganigramasInterface persistenciaOrganigramas;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    /**
     * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
     * conexión del usuario que está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;
    //------------------------------------------------------------------------------------------
    //ATRIBUTOS
    //------------------------------------------------------------------------------------------
    //ESTRUCTURAS
    private List<Estructuras> estructuras;
    private List<Estructuras> estructurasLOV;
    private Estructuras estr;
    //CARGOS
    private List<Cargos> cargos;
    private Cargos cargo;
    private Organigramas org;
    //------------------------------------------------------------------------------------------
    //CONSTRUCTOR
    //------------------------------------------------------------------------------------------
/*
     * public AdministrarEstructuras() { //Estructuras persistenciaEstructuras =
     * new PersistenciaEstructuras(); estructurasLOV = new
     * ArrayList<Estructuras>(); //Cargos persistenciaCargos = new
     * PersistenciaCargos(); }
     */
    //------------------------------------------------------------------------------------------
    //METODOS DE LA INTERFACE
    //------------------------------------------------------------------------------------------
    //Estructuras--------------------------------------------------------

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Estructuras> consultarTodoEstructuras() {
        try {
            estructuras = persistenciaEstructuras.buscarEstructuras(em);
        } catch (Exception ex) {
            estructuras = null;
        }
        return estructuras;
    }

    @Override
    public Estructuras consultarEstructuraPorSecuencia(BigInteger secuenciaE) {
        try {
            estr = persistenciaEstructuras.buscarEstructura(em, secuenciaE);
        } catch (Exception ex) {
            estr = null;
        }
        return estr;
    }

    @Override
    public List<Estructuras> consultarNativeQueryEstructuras(String fechaVigencia) {
        try {
            estructurasLOV = persistenciaEstructuras.buscarlistaValores(em, fechaVigencia);
            return estructurasLOV;
        } catch (Exception ex) {
            System.out.println("Administrar: Fallo al consultar el nativeQuery");
            return estructurasLOV = null;
        }
    }
    //Cargos-----------------------------------------------------------

    @Override
    public List<Cargos> consultarTodoCargos() {
        try {
            cargos = persistenciaCargos.consultarCargos(em);
        } catch (Exception ex) {
            cargos = null;
        }
        return cargos;
    }

    @Override
    public Cargos consultarCargoPorSecuencia(BigInteger secuenciaC) {
        try {
            cargo = persistenciaCargos.buscarCargoSecuencia(em, secuenciaC);
        } catch (Exception ex) {
            cargo = null;
        }
        return cargo;
    }

    //PANTALLA ESTRUCTURAS
    @Override
    public List<Estructuras> estructuraPadre(short codigoOrg) {
        List<Estructuras> listaEstructurasPadre;
        Organigramas organigrama = persistenciaOrganigramas.organigramaBaseArbol(em, codigoOrg);
        if (organigrama != null) {
            listaEstructurasPadre = persistenciaEstructuras.estructuraPadre(em, organigrama.getSecuencia());
        } else {
            listaEstructurasPadre = null;
        }
        return listaEstructurasPadre;
    }

    @Override
    public List<Estructuras> estructurasHijas(BigInteger secEstructuraPadre, Short codigoEmpresa) {
        List<Estructuras> listaEstructurasHijas;
        listaEstructurasHijas = persistenciaEstructuras.estructurasHijas(em, secEstructuraPadre, codigoEmpresa);
        return listaEstructurasHijas;
    }

    @Override
    public List<Organigramas> obtenerOrganigramas() {
//        List<Organigramas> listaOrganigramas;
//        listaOrganigramas = persistenciaOrganigramas.buscarOrganigramas(em);

        List<Empresas> listaEmpresas = consultarEmpresas();
        List<Organigramas> listaOrganigramas = new ArrayList<Organigramas>();
        System.out.println("listaEmpresas : " + listaEmpresas);
        System.out.println("em : " + em);

        if (listaEmpresas != null) {
            System.out.println("listaEmpresas.size() : " + listaEmpresas.size());

            for (int i = 0; i < listaEmpresas.size(); i++) {
                try {
                    List<Organigramas> lista = persistenciaOrganigramas.buscarOrganigramasEmpresa(em, listaEmpresas.get(i).getSecuencia());
                    listaOrganigramas.addAll(lista);
                } catch (Exception e) {
                    System.out.println("Error listaOrganigramas Empresa: " + listaEmpresas.get(i).getSecuencia() + " ex: " + e.toString());
                }
            }
        } else {
            System.out.println("listaEmpresas = null");
        }
        return listaOrganigramas;
    }

    public List<Empresas> consultarEmpresas() {
        List<Empresas> listaEmpresas = null;
        try {
            listaEmpresas = persistenciaEmpresas.buscarEmpresas(em);
            return listaEmpresas;
        } catch (Exception e) {
            System.out.println("Error AdministrarEstructurasPlantas.consutlarEmpresas()");
            e.printStackTrace();
            return listaEmpresas;
        }
    }

    @Override
    public List<Empresas> obtenerEmpresas() {
        List<Empresas> listaEmpresas;
        listaEmpresas = persistenciaEmpresas.consultarEmpresas(em);
        return listaEmpresas;
    }

    @Override
    public void modificarOrganigrama(List<Organigramas> listOrganigramasModificados) {
        for (int i = 0; i < listOrganigramasModificados.size(); i++) {
            System.out.println("Modificando...");
            org = listOrganigramasModificados.get(i);
            persistenciaOrganigramas.editar(em, org);
        }
    }

    @Override
    public void borrarOrganigrama(Organigramas organigrama) {
        persistenciaOrganigramas.borrar(em, organigrama);
    }

    @Override
    public void crearOrganigrama(Organigramas organigrama) {
        persistenciaOrganigramas.crear(em, organigrama);
    }

    @Override
    public List<Estructuras> Estructuras() {
        List<Estructuras> listaEstructuras;
        listaEstructuras = persistenciaEstructuras.estructuras(em);
        return listaEstructuras;
    }

    @Override
    public List<Estructuras> lovEstructuras() {
        return persistenciaEstructuras.estructuras(em);
    }
}
