/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ConexionesKioskos;
import Entidades.Empleados;
import InterfaceAdministrar.AdministrarKioAdminInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaKioAdminInterface;
import java.math.BigInteger;
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
public class AdministrarKioAdmin implements AdministrarKioAdminInterface {

   private static Logger log = Logger.getLogger(AdministrarKioAdmin.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaKioAdminInterface persistencisKioAdmin;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Empleados> listEmpleadosCK() {
        List<Empleados> empleadosck = persistencisKioAdmin.consultarEmpleadosCK(em);
        return empleadosck;
    }

    @Override
    public ConexionesKioskos listCK(BigInteger secEmpleado) {
        ConexionesKioskos ck = persistencisKioAdmin.conexionesKioskos(em,secEmpleado);
        return ck;
    }

    @Override
    public void editarCK(List<ConexionesKioskos> ck) {
        try {
            for (int i = 0; i < ck.size(); i++) {
                persistencisKioAdmin.modificarck(em, ck.get(i));
            }
        } catch (Exception e) {
            log.warn("error en editarCK : " + e.getMessage());
        }
    }

    @Override
    public void resetUsuario(BigInteger secEmpleado) {
        try{
         persistencisKioAdmin.resetUsuario(em, secEmpleado);
        }catch(Exception e){
            log.warn("erro en reset Usuario admin : "  + e.getMessage());
        }
    }

    @Override
    public void unlockUsuario(BigInteger secEmpleado) {
       try{
         persistencisKioAdmin.unlockUsuario(em, secEmpleado);
        }catch(Exception e){
            log.warn("erro en unlock Usuario admin : "  + e.getMessage());
        }
    }

}
