/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.SolucionesFormulas;
import Entidades.SolucionesNodosAux;
import InterfacePersistencia.PersistenciaSolucionesFormulasInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaSolucionesFormulas implements PersistenciaSolucionesFormulasInterface {

   private static Logger log = Logger.getLogger(PersistenciaSolucionesFormulas.class);

    @Override
    public int validarNovedadesNoLiquidadas(EntityManager em, BigInteger secNovedad) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(sf) FROM SolucionesFormulas sf WHERE sf.novedad.secuencia = :secNovedad");
            query.setParameter("secNovedad", secNovedad);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
               log.warn("validarNovedadesNoLiquidadas() retorna 1");
                return 1;
            } else {
               log.warn("validarNovedadesNoLiquidadas() retorna 0");
                return 0;
            }
        } catch (Exception e) {
            log.error("Exepcion: (validarNovedadesNoLiquidadas)  ", e);
            return 1;
        }
    }

    @Override
    public List<SolucionesFormulas> listaSolucionesFormulasParaEmpleadoYNovedad(EntityManager em, BigInteger secEmpleado, BigInteger secNovedad) {
        try {
            em.clear();
            String sql = "SELECT * FROM SOLUCIONESFORMULAS SF, NOVEDADES N WHERE SF.NOVEDAD = N.SECUENCIA AND N.EMPLEADO =? AND SF.NOVEDAD=? ";
            Query query = em.createNativeQuery(sql, SolucionesFormulas.class);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, secNovedad);
            List<SolucionesFormulas> lista = query.getResultList();

            List<SolucionesNodosAux> listaAux = new ArrayList<SolucionesNodosAux>();
            if (lista != null) {
                if (!lista.isEmpty()) {
                    String sql2 = "SELECT SF.SECUENCIA, P.DESCRIPCION NOMBREPROCESO \n"
                            + "FROM SOLUCIONESFORMULAS SF, NOVEDADES N, SOLUCIONESNODOS SN, PROCESOS P\n"
                            + "WHERE SF.NOVEDAD = N.SECUENCIA \n"
                            + "AND  SF.SOLUCIONNODO = SN.SECUENCIA \n"
                            + "AND SN.PROCESO = P.SECUENCIA\n"
                            + "AND N.EMPLEADO =?\n"
                            + "AND SF.NOVEDAD=? ";
                    Query query2 = em.createNativeQuery(sql2, SolucionesNodosAux.class);
                    query2.setParameter(1, secEmpleado);
                    query2.setParameter(2, secNovedad);
                    listaAux = query2.getResultList();
                }
            }
            if (listaAux != null) {
                if (!listaAux.isEmpty()) {
                    for (int i = 0; i < lista.size(); i++) {
                        for (int j = 0; j < listaAux.size(); j++) {
                            if (lista.get(i).getSecuencia().equals(listaAux.get(j).getSecuencia())) {
                                lista.get(i).getSolucionnodo().setNombreproceso(listaAux.get(j).getNombreproceso());
                                listaAux.remove(j);
                                break;
                            }
                        }
                    }

                }
            }

            return lista;
        } catch (Exception e) {
            log.error("Error listaSolucionesFormulasParaEmpleadoYNovedad PersistenciaSolucionhesFormulas :  ", e);
            return null;
        }
    }

    @Override
    public void borrar(EntityManager em, SolucionesFormulas solucionf) {
       em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(solucionf));
         tx.commit();
      } catch (Exception e) {
          log.error("PersistenciaSolucionesFormulas.borrar():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
    }
}
