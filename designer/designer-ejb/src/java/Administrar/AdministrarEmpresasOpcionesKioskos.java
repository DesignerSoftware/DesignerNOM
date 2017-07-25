/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EmpresasOpcionesKioskos;
import InterfaceAdministrar.AdministrarEmpresasOpcionesKioskosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpresasOpcionesKioskosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarEmpresasOpcionesKioskos implements AdministrarEmpresasOpcionesKioskosInterface {

   private static Logger log = Logger.getLogger(AdministrarEmpresasOpcionesKioskos.class);

    @EJB
    PersistenciaEmpresasOpcionesKioskosInterface persistenciaEmpresasOK;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
            em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarEmpresasOpcionesKioskos(List<EmpresasOpcionesKioskos> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaEmpresasOK.editar(em, lista.get(i));
            }
        } catch (Exception e) {
            log.warn("error en modificarEmpresasOpcionesKioskos admi : " + e.getMessage());
        }
    }

    @Override
    public void borrarEmpresasOpcionesKioskos(List<EmpresasOpcionesKioskos> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaEmpresasOK.borrar(em, lista.get(i));
            }
        } catch (Exception e) {
            log.warn("error en borrarEmpresasOpcionesKioskos admi : " + e.getMessage());
        }
    }

    @Override
    public void crearEmpresasOpcionesKioskos(List<EmpresasOpcionesKioskos> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaEmpresasOK.crear(em, lista.get(i));
            }
        } catch (Exception e) {
            log.warn("error en crearEmpresasOpcionesKioskos admi : " + e.getMessage());
        }
    }

    @Override
    public List<EmpresasOpcionesKioskos> consultarEmpresasOpcionesKioskos() {
       try {
            List<EmpresasOpcionesKioskos> empresaOK = persistenciaEmpresasOK.consultarEmpresaOpKioskos(em);
            return empresaOK;
        } catch (Exception e) {
            log.warn("error en consultarEmpresasOpcionesKioskos : " + e.getMessage());
            return null;
        }
    }

}
