package Administrar;

import Entidades.Recordatorios;
import InterfaceAdministrar.AdministrarGeneraConsultaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRecordatoriosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Edwin Hastamorir
 */
@Stateful
public class AdministrarGeneraConsulta implements AdministrarGeneraConsultaInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaRecordatoriosInterface persistenciaRecordatorios;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        System.out.println("AdministrarGeneraConsulta.obtenerConexion");
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public Recordatorios consultarPorSecuencia(BigInteger secuencia) {
        try {
            Recordatorios recordatorio = persistenciaRecordatorios.consultaRecordatorios(em, secuencia);
            return recordatorio;
        } catch (Exception e) {
            System.out.println("consultarPorSecuencia en " + this.getClass().getName() + ": ");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> ejecutarConsulta(BigInteger secuencia) {
        System.out.println("AdministrarGeneraConsulta.ejecutarConsulta");
        try {
            List<String> lista = persistenciaRecordatorios.ejecutarConsultaRecordatorio(em, secuencia);
            return lista;
        } catch (Exception e) {
            System.out.println("ejecutarConsulta en " + this.getClass().getName() + ": ");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Remove
    public void salir() {

    }

}
