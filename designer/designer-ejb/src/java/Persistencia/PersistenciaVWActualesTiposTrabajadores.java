/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Empleados;
import Entidades.VWActualesTiposTrabajadores;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista
 * 'VWActualesTiposTrabajadores' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesTiposTrabajadores implements PersistenciaVWActualesTiposTrabajadoresInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public VWActualesTiposTrabajadores buscarTipoTrabajador(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.empleado.secuencia = :empleado");
            query.setParameter("empleado", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesTiposTrabajadores vwActualesTiposTrabajadores = (VWActualesTiposTrabajadores) query.getSingleResult();
            return vwActualesTiposTrabajadores;
        } catch (Exception e) {
            VWActualesTiposTrabajadores vwActualesTiposTrabajadores = null;
            return vwActualesTiposTrabajadores;
        }
    }

    @Override
    public List<VWActualesTiposTrabajadores> FiltrarTipoTrabajador(EntityManager em, String p_tipo) {
        try {
            em.clear();
            if (!p_tipo.isEmpty()) {
                List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadoresLista = (List<VWActualesTiposTrabajadores>) em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.tipoTrabajador.tipo = :tipotrabajador")
                        .setParameter("tipotrabajador", p_tipo)
                        .getResultList();
                return vwActualesTiposTrabajadoresLista;
            } else {
                System.out.println("Error en PersistenciaVWActualesTiposTrabajadores.FiltrarTipoTrabajador. "
                        + "No recibió el parametro");
                List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadores = null;
                return vwActualesTiposTrabajadores;
            }
        } catch (Exception e) {
            List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadores = null;
            return vwActualesTiposTrabajadores;
        }
    }

    @Override
    public VWActualesTiposTrabajadores filtrarTipoTrabajadorPosicion(EntityManager em, String p_tipo, int posicion) {
        try {
            em.clear();
            if (!p_tipo.isEmpty()) {
                Query query = em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.tipoTrabajador.tipo = :tipotrabajador");
                query.setParameter("tipotrabajador", p_tipo);
                query.setFirstResult(posicion);
                query.setMaxResults(1);
                VWActualesTiposTrabajadores vwActualesTiposTrabajadores = (VWActualesTiposTrabajadores) query.getSingleResult();
                return vwActualesTiposTrabajadores;
            } else {
                System.out.println("Error en PersistenciaVWActualesTiposTrabajadores.filtrarTipoTrabajadorPosicion. " + "No recibió el parametro");
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int obtenerTotalRegistrosTipoTrabajador(EntityManager em, String p_tipo) {
        try {
            em.clear();
            if (!p_tipo.isEmpty()) {
                Query query = em.createQuery("SELECT COUNT(vwatt) FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.tipoTrabajador.tipo = :tipotrabajador");
                query.setParameter("tipotrabajador", p_tipo);
                Long totalRegistros = (Long) query.getSingleResult();
                System.out.println("Valor total Registros: " + totalRegistros);
                System.out.println("Tipo: " + p_tipo);
                return totalRegistros.intValue();
            } else {
                System.out.println("Error en PersistenciaVWActualesTiposTrabajadores.obtenerTotalRegistrosTipoTrabajador. " + "No recibió el parametro");
                return -1;
            }
        } catch (Exception e) {
            System.out.println("AQUI");
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<VWActualesTiposTrabajadores> busquedaRapidaTrabajadores(EntityManager em) {
        try {
            em.clear();
            List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadoresLista = (List<VWActualesTiposTrabajadores>) em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt")
                    .getResultList();
            return vwActualesTiposTrabajadoresLista;
        } catch (Exception e) {
            List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadores = null;
            return vwActualesTiposTrabajadores;
        }
    }

    //VALIDACION ARCHIVO PLANO
    @Override
    public boolean verificarTipoTrabajador(EntityManager em, Empleados empleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw.tipoTrabajador.tipo FROM VWActualesTiposTrabajadores vw WHERE vw.empleado.secuencia= :secuencia");
            query.setParameter("secuencia", empleado.getSecuencia());
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            String tipoEmpleado = (String) query.getSingleResult();
            return tipoEmpleado.equalsIgnoreCase("ACTIVO");
        } catch (Exception e) {
            System.out.println("Exepcion en PersistenciaVWActualesTiposTrabajadores.verificarTipoTrabajador");
            return false;
        }
    }

    @Override
    public String consultarTipoTrabajador(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw.tipoTrabajador.tipo FROM VWActualesTiposTrabajadores vw WHERE vw.empleado.secuencia= :secuencia");
            query.setParameter("secuencia", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            String tipoEmpleado = (String) query.getSingleResult();
            return tipoEmpleado;
        } catch (Exception e) {
            System.out.println("Exepcion en PersistenciaVWActualesTiposTrabajadores.consultarTipoTrabajador");
            return "";
        }
    }

    @Override
    public List<VWActualesTiposTrabajadores> tipoTrabajadorEmpleado(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesTiposTrabajadores vw where vw.tipoTrabajador.tipo IN ('ACTIVO','PENSIONADO','RETIRADO')");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VWActualesTiposTrabajadores> tipoEmpleado = query.getResultList();
            System.out.println("Tiene: " + tipoEmpleado.size() + " registros");
            return tipoEmpleado;
        } catch (Exception e) {
            System.out.println("Exepcion en PersistenciaVWActualesTiposTrabajadores.tipoTrabajadorEmpleado" + e);
            return null;
        }
    }
}
