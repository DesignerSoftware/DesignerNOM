/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Empresas;
import Entidades.SucursalesPila;
import Entidades.UbicacionesGeograficas;
import InterfaceAdministrar.AdministrarUbicacionesGeograficasInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaSucursalesPilaInterface;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarUbicacionesGeograficas implements AdministrarUbicacionesGeograficasInterface {

    //-------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaUbicacionesGeograficas'.
     */
    @EJB
    PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaCiudades'.
     */
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
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
     * 'persistenciaSucursalesPila'.
     */
    @EJB
    PersistenciaSucursalesPilaInterface persistenciaSucursalesPila;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
	
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    //-------------------------------------------------------------------------------------
    @Override
    public List<Empresas> consultarEmpresas() {
        try {
            List<Empresas> listaEmpresas = persistenciaEmpresas.consultarEmpresas(em);
            return listaEmpresas;
        } catch (Exception e) {
            System.out.println("ADMINISTRARUBICACIONESGEOGRAFICAS: Falló al buscar las empresas /n" + e.getMessage());
            return null;
        }
    }

    public void modificarUbicacionesGeograficas(List<UbicacionesGeograficas> listaUbicacionesGeograficas) {
        try {
            for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
                if (listaUbicacionesGeograficas.get(i).getCiudad().getSecuencia() == null) {
                    listaUbicacionesGeograficas.get(i).setCiudad(null);
                }
            }
            for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
                if (listaUbicacionesGeograficas.get(i).getSucursalPila().getSecuencia() == null) {
                    listaUbicacionesGeograficas.get(i).setSucursalPila(null);
                }
            }

            for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
                persistenciaUbicacionesGeograficas.editar(em, listaUbicacionesGeograficas.get(i));
            }
        } catch (Exception e) {
            System.err.println("AdministrarUbicacionesGeograficas: Falló al editar el CentroCosto /n" + e.getMessage());
        }
    }

    public void borrarUbicacionesGeograficas(List<UbicacionesGeograficas> listaUbicacionesGeograficas) {
        try {
            for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
                if (listaUbicacionesGeograficas.get(i).getCiudad().getSecuencia() == null) {
                    listaUbicacionesGeograficas.get(i).setCiudad(null);
                }
            }
            for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
                if (listaUbicacionesGeograficas.get(i).getSucursalPila().getSecuencia() == null) {
                    listaUbicacionesGeograficas.get(i).setSucursalPila(null);
                }
            }
            for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
                System.out.println("Borando... sucursalpila : " + listaUbicacionesGeograficas.get(i).getSucursalPila().getSecuencia());
                persistenciaUbicacionesGeograficas.borrar(em, listaUbicacionesGeograficas.get(i));
            }
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARUBICACIONESGEOGRAFICAS.borrarUbicacionesGeograficas ERROR=====" + e.getMessage());
        }
    }

    public void crearUbicacionesGeograficas(List<UbicacionesGeograficas> listaUbicacionesGeograficas) {
        try {
            for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
                if (listaUbicacionesGeograficas.get(i).getCiudad().getSecuencia() == null) {
                    listaUbicacionesGeograficas.get(i).setCiudad(null);
                }
                if (listaUbicacionesGeograficas.get(i).getSucursalPila().getSecuencia() == null) {
                    listaUbicacionesGeograficas.get(i).setSucursalPila(null);
                }
                System.out.println("ADMINISTRAR CREANDO...");
                persistenciaUbicacionesGeograficas.crear(em, listaUbicacionesGeograficas.get(i));

            }

        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARUBICACIONESGEOGRAFICAS CREARUBICACIONESGEOGRAFICAS : " + e);
        }
    }

    public List<UbicacionesGeograficas> consultarUbicacionesGeograficasPorEmpresa(BigInteger secEmpresa) {
        try {
            System.out.println("ENTRE A ADMINISTRARUBICACIONESGEOGRAFICAS.buscarUbicacionesGeograficasPorEmpresa ");
            List<UbicacionesGeograficas> listaUbicacionesGeograficas = persistenciaUbicacionesGeograficas.consultarUbicacionesGeograficasPorEmpresa(em, secEmpresa);
            return listaUbicacionesGeograficas;
        } catch (Exception e) {
            System.out.println("ERROR ADMINISTRARUBICACIONESGEOGRAFICAS CONSULTARUBICACIONESGEOGRAFICASPOREMPRESA ERROR : " + e);
            return null;
        }
    }

    public List<Ciudades> lovCiudades() {
        try {
            List<Ciudades> listaCiudades = persistenciaCiudades.consultarCiudades(em);
            return listaCiudades;
        } catch (Exception e) {
            System.out.println("\n ADMINISTRARUBICACIONESGEOGRAFICAS LOVCIUDADES ERROR : " + e);
            return null;
        }
    }

    public List<SucursalesPila> lovSucursalesPilaPorEmpresa(BigInteger secEmpresa) {
        try {
            System.out.println("AdministrarUbicacionesGeograficas lovSucursalesPilaPorEmpresa : "+secEmpresa);
            List<SucursalesPila> listaSucursalesPila = persistenciaSucursalesPila.consultarSucursalesPilaPorEmpresa(em, secEmpresa);
            return listaSucursalesPila;
        } catch (Exception e) {
            System.out.println("\n ADMINISTRARUBICACIONESGEOGRAFICAS LOVSUCURSALESPILA ERROR : " + e);
            return null;
        }
    }

    public BigInteger contarAfiliacionesEntidadesUbicacionGeografica(BigInteger secuencia) {
        BigInteger contarAfiliacionesEntidadesUbicacionGeografica;
        try {
            contarAfiliacionesEntidadesUbicacionGeografica = persistenciaUbicacionesGeograficas.contarAfiliacionesEntidadesUbicacionGeografica(em, secuencia);
            return contarAfiliacionesEntidadesUbicacionGeografica;
        } catch (Exception e) {
            return null;
        }
    }

    public BigInteger contarInspeccionesUbicacionGeografica(BigInteger secuencia) {
        BigInteger contarInspeccionesUbicacionGeografica;
        try {
            contarInspeccionesUbicacionGeografica = persistenciaUbicacionesGeograficas.contarInspeccionesUbicacionGeografica(em, secuencia);
            return contarInspeccionesUbicacionGeografica;
        } catch (Exception e) {
            return null;
        }
    }

    public BigInteger contarParametrosInformesUbicacionGeografica(BigInteger secuencia) {
        BigInteger contarParametrosInformesUbicacionGeografica;
        try {
            contarParametrosInformesUbicacionGeografica = persistenciaUbicacionesGeograficas.contarParametrosInformesUbicacionGeografica(em, secuencia);
            return contarParametrosInformesUbicacionGeografica;
        } catch (Exception e) {
            return null;
        }
    }

    public BigInteger contarRevisionesUbicacionGeografica(BigInteger secuencia) {
        BigInteger contarRevisionesUbicacionGeografica;
        try {
            contarRevisionesUbicacionGeografica = persistenciaUbicacionesGeograficas.contarRevisionesUbicacionGeografica(em, secuencia);
            return contarRevisionesUbicacionGeografica;
        } catch (Exception e) {
            return null;
        }
    }

    public BigInteger contarVigenciasUbicacionesUbicacionGeografica(BigInteger secuencia) {
        BigInteger contarVigenciasUbicacionesGeografica;
        try {
            contarVigenciasUbicacionesGeografica = persistenciaUbicacionesGeograficas.contarVigenciasUbicacionesGeografica(em, secuencia);
            return contarVigenciasUbicacionesGeografica;
        } catch (Exception e) {
            return null;
        }
    }
}
