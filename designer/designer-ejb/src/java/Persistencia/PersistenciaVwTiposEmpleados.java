/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Entidades.VwTiposEmpleados;
import InterfacePersistencia.PersistenciaVwTiposEmpleadosInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVwTiposEmpleados implements PersistenciaVwTiposEmpleadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVwTiposEmpleados.class);
    @Override
    public List<VwTiposEmpleados> buscarTiposEmpleadosPorTipo (EntityManager em, String tipo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vwte FROM VwTiposEmpleados vwte, Empleados e where vwte.rfEmpleado = e.secuencia and vwte.tipo = :vcTipo");
            query.setParameter("vcTipo", tipo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VwTiposEmpleados> VwTiposEmpleadosPorTipo = query.getResultList();
            return VwTiposEmpleadosPorTipo;
        } catch (Exception e) {
            log.error("Error en PersistenciaVwTiposEmpleados.buscarTiposEmpleadosPorTipo  ", e);
            return null;
        }
    }
    
    public List<VwTiposEmpleados> buscarTiposEmpleados (EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vwte FROM VwTiposEmpleados vwte, Empleados e where vwte.rfEmpleado = e.secuencia");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VwTiposEmpleados> VwTiposEmpleados = query.getResultList();
            return VwTiposEmpleados;
        } catch (Exception e) {
            log.error("Error en PersistenciaVwTiposEmpleados.buscarTiposEmpleados  ", e);
            return null;
        }
    }
}
