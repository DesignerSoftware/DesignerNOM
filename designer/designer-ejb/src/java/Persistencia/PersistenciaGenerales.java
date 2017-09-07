package Persistencia;

import Entidades.Generales;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

@Stateless
public class PersistenciaGenerales implements PersistenciaGeneralesInterface {

   private static Logger log = Logger.getLogger(PersistenciaGenerales.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    public Generales obtenerRutas(EntityManager em) {
        log.warn(this.getClass().getName() + ".obtenerRutas()");
        try {
            em.clear();
            Query query = em.createQuery("SELECT g FROM Generales g");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Generales> listaGenerales = query.getResultList();
            if (listaGenerales != null && !listaGenerales.isEmpty()) {
                log.warn("PersistenciaGenerales obtenerRutas Tamaño listaGenerales.getPathreportes : " + listaGenerales.get(0).getPathreportes());
                return listaGenerales.get(0);
            }
            return null;
        } catch (Exception e) {
            log.error("Error PersistenciaGenerales.obtenerRutas " + e);
            return null;
        }
    }

    public String obtenerPreValidadContabilidad(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT NVL(PREVALIDACONTABILIDAD,'N') FROM GENERALES GROUP BY PREVALIDACONTABILIDAD";
            Query query = em.createNativeQuery(sql);
            String variable = (String) query.getSingleResult();
            return variable;
        } catch (Exception e) {
            log.error("Error obtenerPreValidadContabilidad PersistenciaGenerales : " + e.toString());
            return null;
        }
    }

    public String obtenerPreValidaBloqueAIngreso(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT NVL(PREVALIDABLOQUEAINGRESO,'N') FROM GENERALES GROUP BY PREVALIDABLOQUEAINGRESO";
            Query query = em.createNativeQuery(sql);
            String variable = (String) query.getSingleResult();
            return variable;
        } catch (Exception e) {
            log.error("Error obtenerPreValidaBloqueAIngreso PersistenciaGenerales : " + e.toString());
            return null;
        }
    }

    public String obtenerPathServidorWeb(EntityManager em) {
        try {
            em.clear();
            String sql = "select pathservidorweb \n"
                    + "  from  generales\n"
                    + "  group by pathservidorweb";
            Query query = em.createNativeQuery(sql);
            String path = (String) query.getSingleResult();
            path = path + "\\";
            return path;
        } catch (Exception e) {
            log.error("Error obtenerPathServidorWeb PersistenciaGenerales : " + e.toString());
            return null;
        }
    }

    public String obtenerPathProceso(EntityManager em) {
        try {
            em.clear();
            String sql = "select pathproceso\n"
                    + "  from  generales\n"
                    + "  group by pathproceso";
            Query query = em.createNativeQuery(sql);
            String path = (String) query.getSingleResult();
            return path;
        } catch (Exception e) {
            log.error("Error obtenerPathProceso PersistenciaGenerales : " + e.toString());
            return null;
        }
    }

    @Override
    public String obtenerPathError(EntityManager em) {
        try {
            em.clear();
            String sql = "select patherror\n"
                    + "  from  generales\n"
                    + "  group by patherror";
            Query query = em.createNativeQuery(sql);
            String path = (String) query.getSingleResult();
            return path;
        } catch (Exception e) {
            log.error("Error obtenerPathError PersistenciaGenerales : " + e.toString());
            return null;
        }
    }
}
