/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.PryPlataformas;
import InterfaceAdministrar.AdministrarPryPlataformasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPryPlataformasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarPryPlataformas implements AdministrarPryPlataformasInterface {

   private static Logger log = Logger.getLogger(AdministrarPryPlataformas.class);

    @EJB
    PersistenciaPryPlataformasInterface persistenciaPryPlataformas;
    private PryPlataformas pryPlataformasSeleccionado;
    private PryPlataformas pryPlataformas;
    private List<PryPlataformas> listPryPlataformas;
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
    public void modificarPryPlataformas(List<PryPlataformas> listaPryClientes) {
        for (int i = 0; i < listaPryClientes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaPryPlataformas.editar(em, listaPryClientes.get(i));
        }
    }

    @Override
    public void borrarPryPlataformas(List<PryPlataformas> listaPryClientes) {
        for (int i = 0; i < listaPryClientes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaPryPlataformas.borrar(em, listaPryClientes.get(i));
        }
    }

    @Override
    public void crearPryPlataformas(List<PryPlataformas> listaPryClientes) {
        for (int i = 0; i < listaPryClientes.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaPryPlataformas.crear(em, listaPryClientes.get(i));
        }
    }
    @Override
    public List<PryPlataformas> mostrarPryPlataformas() {
        listPryPlataformas = persistenciaPryPlataformas.buscarPryPlataformas(em);
        return listPryPlataformas;
    }
    @Override
    public PryPlataformas mostrarPryPlataformas(BigInteger secPryClientes) {
        pryPlataformas = persistenciaPryPlataformas.buscarPryPlataformaSecuencia(em, secPryClientes);
        return pryPlataformas;
    }
    @Override
    public BigInteger contarProyectosPryPlataformas(BigInteger secuenciaProyectos) {
        BigInteger verificadorProyectos;
        try {
            log.error("Secuencia Borrado Competencias Cargos" + secuenciaProyectos);
            return verificadorProyectos = persistenciaPryPlataformas.contadorProyectos(em, secuenciaProyectos);
        } catch (Exception e) {
            log.error("ERROR AdministrarPryPlataformas verificarBorradoProyecto ERROR :" + e);
            return null;
        }
    }
}
