/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarPapelesInterface;
import Entidades.Papeles;
import Entidades.Empresas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPapelesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarPapeles implements AdministrarPapelesInterface {

    // --------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaPapeles'.
     */
    @EJB
    PersistenciaPapelesInterface persistenciaPapeles;

    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpresas'.
     */
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;

    //-------------------------------------------------------------------------------------
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
    
    @Override
    public List<Empresas> consultarEmpresas() {
        try {
            List<Empresas> listaEmpresas = persistenciaEmpresas.consultarEmpresas(em);
            return listaEmpresas;
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARPAPELES CONSULTAREMPRESAS ERROR : " + e);
            return null;
        }
    }
    @Override
    public void modificarPapeles(List<Papeles> listaPapeles) {
        try {
            for (int i = 0; i < listaPapeles.size(); i++) {
                System.out.println("Modificando...");
                persistenciaPapeles.editar(em, listaPapeles.get(i));
            }
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARPAPELES MODIFICARPAPELES ERROR : " + e);
        }
    }
    @Override
    public void borrarPapeles(List<Papeles> listaPapeles) {
        try {
            for (int i = 0; i < listaPapeles.size(); i++) {
                System.out.println("Borrando...");
                persistenciaPapeles.borrar(em, listaPapeles.get(i));
            }
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARPAPELES BORRARPAPELES ERROR : " + e);
        }
    }
    @Override
    public void crearPapeles(List<Papeles> listaPapeles) {
        try {
               System.out.println("Creando... tamaño "+listaPapeles.size());
            for (int i = 0; i < listaPapeles.size(); i++) {
                System.out.println("Creando...");
                persistenciaPapeles.crear(em, listaPapeles.get(i));
            }
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARPAPELES CREARPAPELES ERROR : " + e);
        }
    }
    @Override
    public List<Papeles> consultarPapelesPorEmpresa(BigInteger secEmpresa) {
        try {
            List<Papeles> listaPapeles = persistenciaPapeles.consultarPapelesEmpresa(em, secEmpresa);
            return listaPapeles;
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARPAPELES CONSULTARPAPELESPOREMPRESA ERROR : " + e);
            return null;
        }
    }
    @Override
    public BigInteger contarVigenciasCargosPapel(BigInteger secPapeles) {
        BigInteger contadorComprobantesContables;
        try {
            contadorComprobantesContables = persistenciaPapeles.contarVigenciasCargosPapel(em, secPapeles);
            return contadorComprobantesContables;
        } catch (Exception e) {
            System.out.println("ERROR ADMINISTRARPAPELES CONTARVIGENCIASCARGOSPAPEL ERRO : " + e);
            return null;
        }
    }

}
