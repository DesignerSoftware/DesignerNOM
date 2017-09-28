/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.DetallesEmpresas;
import Entidades.DetallesEmpresasAux;
import Entidades.TercerosSucursales;
import InterfacePersistencia.PersistenciaDetallesEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'DetallesEmpresas' de
 * la base de datos.
 *
 * @author betelgeuse
 * @version 1.1 AndresPineda
 * (Crear-Editar-Borrar-BuscarDetallesEmpresas-BuscarDetalleEmpresaPorSecuencia)
 */
@Stateless
public class PersistenciaDetallesEmpresas implements PersistenciaDetallesEmpresasInterface {

   private static Logger log = Logger.getLogger(PersistenciaDetallesEmpresas.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    *
    * @param em
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, DetallesEmpresas detallesEmpresas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(detallesEmpresas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesEmpresas.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, DetallesEmpresas detallesEmpresas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(detallesEmpresas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesEmpresas.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, DetallesEmpresas detallesEmpresas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(detallesEmpresas));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaDetallesEmpresas.borrar:  ", e);
      }
   }

   @Override
   public DetallesEmpresas buscarDetalleEmpresa(EntityManager em, Short codigoEmpresa) {
      try {
         em.clear();
         Query q = em.createNativeQuery("SELECT de.* FROM DetallesEmpresas de, empresas e WHERE de.empresa = e.secuencia and e.codigo = " + codigoEmpresa, DetallesEmpresas.class);
         DetallesEmpresas detalleEmpresa = (DetallesEmpresas) q.getSingleResult();
         if (detalleEmpresa != null) {
            em.clear();
            Query query = em.createNativeQuery("SELECT D.SECUENCIA,\n"
                    + " P.PRIMERAPELLIDO||' '||P.SEGUNDOAPELLIDO||' '||P.NOMBRE NOMBRE_PERSONAFIRMACONSTANCIA,\n"
                    + " E.NOMBRE NOMBRE_EMPRESA,"
                    + " E.NIT NIT,\n"
                    + " E.CONTROLEMPLEADOS CONTROLEMPLEADOS,\n"
                    + " P2.PRIMERAPELLIDO||' '||P2.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_REPRESENTANTECIR,\n"
                    + " P3.PRIMERAPELLIDO||' '||P3.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_SUBGERENTE,\n"
                    + " P4.PRIMERAPELLIDO||' '||P4.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_ARQUITECTO,\n"
                    + " P5.PRIMERAPELLIDO||' '||P5.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_GERENTEGENERAL,\n"
                    + " C.NOMBRE NOMBRE_CIUDAD, C2.NOMBRE NOMBRE_CIUDADDOCREPRESENTANTE, CA.NOMBRE NOMBRE_CARGOFIRMACONSTANCIA\n"
                    + " FROM DETALLESEMPRESAS D, PERSONAS P, EMPRESAS E, EMPLEADOS E2, PERSONAS P2, EMPLEADOS E3, PERSONAS P3,\n"
                    + " EMPLEADOS E4, PERSONAS P4, EMPLEADOS E5, PERSONAS P5, CIUDADES C, CIUDADES C2, CARGOS CA\n"
                    + " WHERE P.SECUENCIA(+) = D.PERSONAFIRMACONSTANCIA\n"
                    + " AND E.SECUENCIA = D.EMPRESA\n"
                    + " AND E.SECUENCIA = " + detalleEmpresa.getRef_empresa() + "\n"
                    + " AND E2.SECUENCIA(+) = D.REPRESENTANTECIR\n"
                    + " AND P2.SECUENCIA(+) = E2.PERSONA\n"
                    + " AND E3.SECUENCIA(+) = D.SUBGERENTE\n"
                    + " AND P3.SECUENCIA(+) = E3.PERSONA\n"
                    + " AND E4.SECUENCIA(+) = D.ARQUITECTO\n"
                    + " AND P4.SECUENCIA(+) = E4.PERSONA\n"
                    + " AND E5.SECUENCIA(+) = D.GERENTEGENERAL\n"
                    + " AND P5.SECUENCIA(+) = E5.PERSONA\n"
                    + " AND C.SECUENCIA(+) = D.CIUDAD\n"
                    + " AND C2.SECUENCIA(+) = D.CIUDADDOCUMENTOREPRESENTANTE\n"
                    + " AND CA.SECUENCIA(+) = D.CARGOFIRMACONSTANCIA", DetallesEmpresasAux.class);
            DetallesEmpresasAux detalleAux = (DetallesEmpresasAux) query.getSingleResult();
            if (detalleAux != null) {
               if (detalleEmpresa.getSecuencia().equals(detalleAux.getSecuencia())) {
                  detalleEmpresa.setNombre_arquitecto(detalleAux.getNombre_arquitecto());
                  detalleEmpresa.setNombre_cargofirmaconstancia(detalleAux.getNombre_cargofirmaconstancia());
                  detalleEmpresa.setNombre_ciudad(detalleAux.getNombre_ciudad());
                  detalleEmpresa.setNombre_ciudaddocumentorepresentante(detalleAux.getRef_ciudaddocrepresentante());
                  detalleEmpresa.setNombre_empresa(detalleAux.getNombre_empresa());
                  detalleEmpresa.setNit_Empresa(detalleAux.getNit_Empresa());
                  detalleEmpresa.setControlempleados(detalleAux.getControlempleados());
                  detalleEmpresa.setNombre_gerentegeneral(detalleAux.getNombre_gerentegeneral());
                  detalleEmpresa.setNombre_personafirmaconstancia(detalleAux.getNombre_personafirmaconstancia());
                  detalleEmpresa.setNombre_representantecir(detalleAux.getNombre_representantecir());
                  detalleEmpresa.setNombre_subgerente(detalleAux.getNombre_subgerente());
               }
            }
         }
         return detalleEmpresa;
      } catch (Exception e) {
         log.error("error PersistenciaDetallesEmpresas.buscarDetalleEmpresa.  ", e);
         return null;
      }
   }

   @Override
   public DetallesEmpresas buscarDetalleEmpresaPorSecuencia(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         DetallesEmpresas detallesEmpresas;
         Query query = em.createQuery("SELECT de FROM DetallesEmpresas de WHERE de.ref_empresa =:secEmpresa");
         query.setParameter("secEmpresa", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         detallesEmpresas = (DetallesEmpresas) query.getSingleResult();
         return detallesEmpresas;
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesEmpresas.buscarDetalleEmpresaPorSecuencia.  ", e);
         return null;
      }
   }

   @Override
   public List<DetallesEmpresas> buscarDetallesEmpresas(EntityManager em) {
      try {
         em.clear();
         Query cq = em.createNativeQuery("SELECT D.* FROM DETALLESEMPRESAS D, EMPRESAS E WHERE E.SECUENCIA = D.EMPRESA", DetallesEmpresas.class);
         List<DetallesEmpresas> listaResultado = cq.getResultList();
         if (listaResultado != null) {
            log.warn("PersistenciaDetallesEmpresas.buscarDetallesEmpresas() listaResultado : " + listaResultado);
            if (!listaResultado.isEmpty()) {
               em.clear();
               Query query = em.createNativeQuery("SELECT D.SECUENCIA,\n"
                       + " P.PRIMERAPELLIDO||' '||P.SEGUNDOAPELLIDO||' '||P.NOMBRE NOMBRE_PERSONAFIRMACONSTANCIA,\n"
                       + " E.NOMBRE NOMBRE_EMPRESA,"
                       + " E.NIT NIT,\n"
                       + " E.CONTROLEMPLEADOS CONTROLEMPLEADOS,\n"
                       + " P2.PRIMERAPELLIDO||' '||P2.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_REPRESENTANTECIR,\n"
                       + " P3.PRIMERAPELLIDO||' '||P3.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_SUBGERENTE,\n"
                       + " P4.PRIMERAPELLIDO||' '||P4.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_ARQUITECTO,\n"
                       + " P5.PRIMERAPELLIDO||' '||P5.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_GERENTEGENERAL,\n"
                       + " C.NOMBRE NOMBRE_CIUDAD, C2.NOMBRE NOMBRE_CIUDADDOCREPRESENTANTE, CA.NOMBRE NOMBRE_CARGOFIRMACONSTANCIA\n"
                       + " FROM DETALLESEMPRESAS D, PERSONAS P, EMPRESAS E, EMPLEADOS E2, PERSONAS P2, EMPLEADOS E3, PERSONAS P3,\n"
                       + " EMPLEADOS E4, PERSONAS P4, EMPLEADOS E5, PERSONAS P5, CIUDADES C, CIUDADES C2, CARGOS CA\n"
                       + " WHERE P.SECUENCIA(+) = D.PERSONAFIRMACONSTANCIA\n"
                       + " AND E.SECUENCIA = D.EMPRESA\n"
                       + " AND E2.SECUENCIA(+) = D.REPRESENTANTECIR\n"
                       + " AND P2.SECUENCIA(+) = E2.PERSONA\n"
                       + " AND E3.SECUENCIA(+) = D.SUBGERENTE\n"
                       + " AND P3.SECUENCIA(+) = E3.PERSONA\n"
                       + " AND E4.SECUENCIA(+) = D.ARQUITECTO\n"
                       + " AND P4.SECUENCIA(+) = E4.PERSONA\n"
                       + " AND E5.SECUENCIA(+) = D.GERENTEGENERAL\n"
                       + " AND P5.SECUENCIA(+) = E5.PERSONA\n"
                       + " AND C.SECUENCIA(+) = D.CIUDAD\n"
                       + " AND C2.SECUENCIA(+) = D.CIUDADDOCUMENTOREPRESENTANTE\n"
                       + " AND CA.SECUENCIA(+) = D.CARGOFIRMACONSTANCIA", DetallesEmpresasAux.class);
               List<DetallesEmpresasAux> listaAux = query.getResultList();
               if (listaAux != null) {
                  log.warn("PersistenciaDetallesEmpresas.buscarDetallesEmpresas() listaAux : " + listaAux);
                  if (!listaAux.isEmpty()) {
                     for (int j = 0; j < listaResultado.size(); j++) {
                        for (int i = 0; i < listaAux.size(); i++) {
                           if (listaResultado.get(j).getSecuencia().equals(listaAux.get(i).getSecuencia())) {
                              listaResultado.get(j).setNombre_arquitecto(listaAux.get(i).getNombre_arquitecto());
                              listaResultado.get(j).setNombre_cargofirmaconstancia(listaAux.get(i).getNombre_cargofirmaconstancia());
                              listaResultado.get(j).setNombre_ciudad(listaAux.get(i).getNombre_ciudad());
                              listaResultado.get(j).setNombre_ciudaddocumentorepresentante(listaAux.get(i).getRef_ciudaddocrepresentante());
                              listaResultado.get(j).setNombre_empresa(listaAux.get(i).getNombre_empresa());
                              listaResultado.get(j).setNit_Empresa(listaAux.get(i).getNit_Empresa());
                              listaResultado.get(j).setControlempleados(listaAux.get(i).getControlempleados());
                              listaResultado.get(j).setNombre_gerentegeneral(listaAux.get(i).getNombre_gerentegeneral());
                              listaResultado.get(j).setNombre_personafirmaconstancia(listaAux.get(i).getNombre_personafirmaconstancia());
                              listaResultado.get(j).setNombre_representantecir(listaAux.get(i).getNombre_representantecir());
                              listaResultado.get(j).setNombre_subgerente(listaAux.get(i).getNombre_subgerente());
                              listaAux.remove(listaAux.get(i));
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
         return listaResultado;
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesEmpresas.buscarDetallesEmpresas :  ", e);
         return null;
      }
   }

   @Override
   public TercerosSucursales buscarARLPorEmpresa(EntityManager em, BigInteger secuenciaEmpresa) {
      try {
         em.clear();
         String q = "SELECT * FROM TercerosSucursales TS WHERE TS.SECUENCIA = ( "
                 + "SELECT DE.ENTIDADSUCURSALARL FROM EMPRESAS E, DETALLESEMPRESAS DE\n"
                 + "  WHERE DE.EMPRESA = E.SECUENCIA AND E.SECUENCIA = " + secuenciaEmpresa + " )";
         Query query = em.createNativeQuery(q, TercerosSucursales.class);
         TercerosSucursales tercerosArl = (TercerosSucursales) query.getSingleResult();
         return tercerosArl;
      } catch (Exception e) {
         log.error("PersistenciaDetallesEmpresas ERROR buscarARLPorEmpresa() :  ", e);
         return null;
      }
   }

}
