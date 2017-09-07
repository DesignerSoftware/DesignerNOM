/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Generales;
import InterfaceAdministrar.AdministrarSalidasUTLInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarSalidasUTL implements AdministrarSalidasUTLInterface {

    private static Logger log = Logger.getLogger(AdministrarSucursales.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaGeneralesInterface persistenciaGenerales;

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
    public List<File> consultarArchivosProceso() {
        try {
            Generales general = persistenciaGenerales.obtenerRutas(getEm());
            String rutaReporte = general.getPathproceso();
            List<File> listaArchivos = new ArrayList<File>();

            File folder = new File("C:\\DesignerRHN\\SalidasUTL\\BOMEDICA\\");
            File[] listOfFiles = folder.listFiles();
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    String archivo = listOfFile.getName();
                    String archivoSinExt = archivo.substring(0, archivo.lastIndexOf("."));
                    if (archivo.startsWith("proceso") || archivo.startsWith("cierre")) {
                        listaArchivos.add(listOfFile);
                    }
                }
            }
            return listaArchivos;
        } catch (Exception ex) {
            log.error("Error AdministrarSalidasUTL.consultarArchivosProceso: " + ex);
            return null;
        }
    }

    @Override
    public List<File> consultarArchivosError() {
        try {
            Generales general = persistenciaGenerales.obtenerRutas(getEm());
            String rutaReporte = general.getPathproceso();
            List<File> listaArchivos = new ArrayList<File>();

            File folder = new File(rutaReporte);
//            File folder = new File("C:\\DesignerRHN\\SalidasUTL\\BOMEDICA\\");
            File[] listOfFiles = folder.listFiles();
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    String archivo = listOfFile.getName();
                    String archivoSinExt = archivo.substring(0, archivo.lastIndexOf("."));
//                    if (archivo.startsWith("errores")) {
                        listaArchivos.add(listOfFile);
//                    }
                }
            }
            return listaArchivos;
        } catch (Exception ex) {
            log.error("Error AdministrarSalidasUTL.consultarArchivosProceso: " + ex);
            return null;
        }
    }

    @Override
    public String pathError() {
        try {
            return persistenciaGenerales.obtenerPathError(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarSalidasUTL.pathError: " + e);
            return "";
        }
    }

    @Override
    public String pathProceso() {
        try {
            return persistenciaGenerales.obtenerPathProceso(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarSalidasUTL.pathProceso: " + e);
            return "";
        }
    }
}
