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
import InterfacePersistencia.PersistenciaPantallasInterface;
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

    @EJB
    PersistenciaPapelesInterface persistenciaPapeles;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaPantallasInterface persistenciaPantallas;

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

    @Override
    public String interfaceContable(BigInteger secEmpresa) {
       String intcontable = persistenciaPantallas.buscarIntContable(em, secEmpresa);
       return intcontable;
    }

}
