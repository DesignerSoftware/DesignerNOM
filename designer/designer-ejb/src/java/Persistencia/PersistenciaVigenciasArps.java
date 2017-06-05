package Persistencia;

import Entidades.VigenciasArps;
import Entidades.VigenciasArpsAux;
import InterfacePersistencia.PersistenciaVigenciasArpsInterface;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaVigenciasArps implements PersistenciaVigenciasArpsInterface {

   @Override
   public String actualARP(EntityManager em, BigInteger secEstructura, BigInteger secCargo, Date fechaHasta) {
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
         System.out.println("Exepcion: PersistenciaVigenciasArps.actualARP " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
         return null;
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
         System.out.println("Error PersistenciaVigenciasArps.crear: " + e.getMessage());
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
         System.out.println("Error PersistenciaVigenciasArps.borrar: " + e.getMessage());
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
         System.out.println("Error PersistenciaVigenciasArps.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public List<VigenciasArps> consultarVigenciasArps(EntityManager em) {
      List<VigenciasArps> listaVigencias = null;
      try {
         em.clear();
         Query query = em.createQuery("SELECT v FROM VigenciasArps v");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         listaVigencias = query.getResultList();
         if (listaVigencias != null) {
            if (!listaVigencias.isEmpty()) {
               em.clear();
               Query q2 = em.createNativeQuery("SELECT V.secuencia SECUENCIA, E.NOMBRE NOMBREESTRUCTURA, C.NOMBRE NOMBRECARGO\n"
                       + " FROM VIGENCIASARPS V, ESTRUCTURAS E, CARGOS C WHERE V.ESTRUCTURA = E.SECUENCIA AND V.CARGO = C.SECUENCIA", VigenciasArpsAux.class);
               List<VigenciasArpsAux> listaAux = q2.getResultList();
               if (listaAux != null) {
                  System.out.println("Persistencia.PersistenciaVigenciasArps.consultarVigenciasArps() listaVigencias.size(): " + listaVigencias.size());
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
         System.out.println("Error PersistenciaVigenciasArps.consultarVigenciasArps: " + e);
         return listaVigencias;
      }
   }

}
