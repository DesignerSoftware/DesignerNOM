package Administrar;

import Entidades.Inforeportes;
import Entidades.envioCorreos;
import InterfaceAdministrar.AdministrarEnvioCorreosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEnvioCorreosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'EnvioCorreos'.
 *
 */
@Stateful
public class AdministrarEnvioCorreos implements AdministrarEnvioCorreosInterface {

    @EJB
    PersistenciaEnvioCorreosInterface PersistenciaEnvioCorreos;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
//    private List<envioCorreos> enviocorreos;
    private envioCorreos ec;
    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<envioCorreos> consultarEnvioCorreos(BigInteger secReporte) {
        System.out.println("Administrar.AdministrarEnvioCorreos.consultarEnvioCorreos()");
        List<envioCorreos> enviocorreos;
        try {
            enviocorreos = PersistenciaEnvioCorreos.consultarEnvios(em, secReporte);
        } catch (Exception e) {
            System.out.println("Error Administrar.AdministrarEnvioCorreos.consultarEnvioCorreos() " + e);
            enviocorreos = null;
        }
        return enviocorreos;
    }

    @Override
    public void editarEnvioCorreos(envioCorreos listaEC) {
        try {
            System.out.println("Administrar.AdministrarEnvioCorreos.editarEnvioCorreos()  " + listaEC.getSecuencia());
            PersistenciaEnvioCorreos.editar(em, listaEC);
        } catch (Exception ex) {
            System.out.println("Error Administrar.AdministrarEnvioCorreos.editarEnvioCorreos() " + ex);
        }
    }

    @Override
    public void modificarEC(List<envioCorreos> listECModificadas) {
        for (int i = 0; i < listECModificadas.size(); i++) {
            System.out.println("Modificando...");
            if (listECModificadas.get(i).getCodigoEmpleado() != null && listECModificadas.get(i).getCodigoEmpleado().getSecuencia() == null) {
                listECModificadas.get(i).setCodigoEmpleado(null);
                ec = listECModificadas.get(i);
                PersistenciaEnvioCorreos.editar(em, ec);
            } else {
                ec = listECModificadas.get(i);
                PersistenciaEnvioCorreos.editar(em, ec);
            }

        }
    }

    @Override
    public void borrarEnvioCorreos(envioCorreos listaEC) {
        try {
            PersistenciaEnvioCorreos.borrar(em, listaEC);
        } catch (Exception e) {
            System.out.println("Error" + e);
        }

    }

    @Override
    public Inforeportes consultarPorSecuencia(BigInteger envio) {
        System.out.println("Administrar.AdministrarEnvioCorreos.consultarPorSecuencia()");
        System.out.println("envio: " + envio);
        Inforeportes envioConsultado;
        try {
            envioConsultado = PersistenciaEnvioCorreos.buscarEnvioCorreoporSecuencia(em, envio);
        } catch (Exception e) {
            envioConsultado = null;
        }
        System.out.println("ec: " + ec);
        return envioConsultado;
    }

//    @Override
//    public List<Empleados> consultarEmpleados(BigInteger reporte) {
//         System.out.println("Administrar.AdministrarEnvioCorreos.consultarEmpleados()");
//        List<Empleados> empleado;
//        try {
//            empleado = PersistenciaEnvioCorreos.buscarEmpleados(em, reporte);
//        } catch (Exception e) {
//            System.out.println("Error Administrar.AdministrarEnvioCorreos.consultarEnvioCorreos() " + e);
//            empleado = null;
//        }
//        System.out.println("*******************************************************************************");
//        System.out.println("empleado: " + empleado);
//        return empleado;
//    }
   
}
