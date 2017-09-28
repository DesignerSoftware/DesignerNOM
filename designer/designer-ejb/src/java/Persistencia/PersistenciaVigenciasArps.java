package Persistencia;

import Entidades.VigenciasArps;
import Entidades.VigenciasArpsAux;
import InterfacePersistencia.PersistenciaVigenciasArpsInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaVigenciasArps implements PersistenciaVigenciasArpsInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasArps.class);

   @Override
   public String actualARPVig(EntityManager em, BigInteger secEstructura, BigInteger secCargo, Date fechaHasta) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
         String fecha = formato.format(fechaHasta);
         Query query = em.createNativeQuery("SELECT VIGENCIASARPS_PKG.ConsultarPorcentaje(?, ?, to_date(?, 'dd/mm/yyyy')) FROM dual");
         query.setParameter(1, secEstructura);
         query.setParameter(2, secCargo);
         query.setParameter(3, fecha);
         String actualARP = (String) query.getSingleResult().toString();
         tx.commit();
         return actualARP;
      } catch (Exception e) {
         log.error("Exepcion: PersistenciaVigenciasArps.actualARPVig  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return null;
      }
   }

   @Override
   public String actualARP(EntityManager em, BigInteger secEmpleado) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createNativeQuery("SELECT APORTESENTIDADES_PKG.TARIFACTT( ?, (SELECT SECUENCIA FROM TIPOSENTIDADES WHERE CODIGO=2),\n"
                 + " TO_CHAR((SELECT FECHAHASTACAUSADO FROM VWACTUALESFECHAS),'YYYY'),\n"
                 + " TO_CHAR((SELECT FECHAHASTACAUSADO FROM VWACTUALESFECHAS),'MM')) ARL FROM dual");
         query.setParameter(1, secEmpleado);
         String actualARP = (String) query.getSingleResult().toString();
         tx.commit();
         return actualARP;
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("Exepcion: PersistenciaVigenciasArps.actualARP " + e);
         } else {
            log.error("Exepcion: PersistenciaVigenciasArps.actualARP  ", e);
         }
         return null;
      }
   }

   public int contarVigenciasARPsPorEstructuraYCargo(EntityManager em, BigInteger estructura, BigInteger cargo) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT Count(*) FROM VIGENCIASARPS VA WHERE VA.ESTRUCTURA = ? AND VA.CARGO = ?");
         query.setParameter(1, estructura);
         query.setParameter(2, cargo);
         BigDecimal variable = (BigDecimal) query.getSingleResult();
         if (variable != null) {
            return variable.intValue();
         } else {
            return 0;
         }
      } catch (Exception e) {
         log.error("Error contarVigenciasARPsPorEstructuraYCargo PersistenciaVigenciasArps :  ", e);
         return 0;
      }
   }

   @Override
   public void crear(EntityManager em, VigenciasArps vigarp) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigarp);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasArps.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasArps vigarp) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigarp));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasArps.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasArps vigarp) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigarp);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasArps.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public List<VigenciasArps> consultarVigenciasArps(EntityManager em) {
      List<VigenciasArps> listaVigencias = null;
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT v.* FROM VigenciasArps v, ESTRUCTURAS E WHERE v.ESTRUCTURA = E.SECUENCIA", VigenciasArps.class);
//         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         listaVigencias = query.getResultList();
         if (listaVigencias != null) {
            if (!listaVigencias.isEmpty()) {
               em.clear();
               Query q2 = em.createNativeQuery("SELECT V.secuencia SECUENCIA, E.NOMBRE NOMBREESTRUCTURA, C.NOMBRE NOMBRECARGO\n"
                       + " FROM VIGENCIASARPS V, ESTRUCTURAS E, CARGOS C WHERE V.ESTRUCTURA = E.SECUENCIA AND V.CARGO = C.SECUENCIA", VigenciasArpsAux.class);
               List<VigenciasArpsAux> listaAux = q2.getResultList();
               if (listaAux != null) {
                  log.warn("PersistenciaVigenciasArps.consultarVigenciasArps() listaVigencias.size(): " + listaVigencias.size());
                  if (!listaAux.isEmpty()) {
                     for (int j = 0; j < listaVigencias.size(); j++) {
                        for (int i = 0; i < listaAux.size(); i++) {
                           if (listaAux.get(i).getSecuencia().equals(listaVigencias.get(j).getSecuencia())) {
                              listaVigencias.get(j).setNombreEstructura(listaAux.get(i).getNombreEstructura());
                              listaVigencias.get(j).setNombreCargo(listaAux.get(i).getNombreCargo());
                              listaAux.remove(i);
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
         return listaVigencias;
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasArps.consultarVigenciasArps:  ", e);
         return listaVigencias;
      }
   }

}
