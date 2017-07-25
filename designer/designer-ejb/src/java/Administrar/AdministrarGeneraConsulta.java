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
import org.apache.log4j.Logger;

/**
 *
 * @author Edwin Hastamorir
 */
@Stateful
public class AdministrarGeneraConsulta implements AdministrarGeneraConsultaInterface {

   private static Logger log = Logger.getLogger(AdministrarGeneraConsulta.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaRecordatoriosInterface persistenciaRecordatorios;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        log.warn("AdministrarGeneraConsulta.obtenerConexion");
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public Recordatorios consultarPorSecuencia(BigInteger secuencia) {
        try {
            Recordatorios recordatorio = persistenciaRecordatorios.consultaRecordatorios(em, secuencia);
            return recordatorio;
        } catch (Exception e) {
            log.warn("consultarPorSecuencia en " + this.getClass().getName() + ": ");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> ejecutarConsulta(BigInteger secuencia) {
        log.warn("AdministrarGeneraConsulta.ejecutarConsulta");
        try {
            List<String> lista = persistenciaRecordatorios.ejecutarConsultaRecordatorio(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("ejecutarConsulta en " + this.getClass().getName() + ": ");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Remove
    public void salir() {

    }

}
