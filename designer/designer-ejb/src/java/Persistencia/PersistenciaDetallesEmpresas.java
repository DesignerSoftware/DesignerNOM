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
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

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
         System.out.println("Error PersistenciaDetallesEmpresas.crear: " + e);
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
         System.out.println("Error PersistenciaDetallesEmpresas.editar: " + e);
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
         System.out.println("Error PersistenciaDetallesEmpresas.borrar: " + e);
      }
   }

   @Override
   public DetallesEmpresas buscarDetalleEmpresa(EntityManager em, Short codigoEmpresa) {
      DetallesEmpresas detallesEmpresas;
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT de.* FROM DetallesEmpresas de, empresas e WHERE de.empresa = e.secuencia and e.codigo = " + codigoEmpresa);
//         query.setParameter("codigoEmpresa", codigoEmpresa);
//         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         detallesEmpresas = (DetallesEmpresas) query.getSingleResult();
         return detallesEmpresas;
      } catch (Exception e) {
         System.out.println("error PersistenciaDetallesEmpresas.buscarDetalleEmpresa. ");
         System.out.println(e.getMessage());
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
         System.out.println("Error PersistenciaDetallesEmpresas.buscarDetalleEmpresaPorSecuencia. " + e.toString());
         return null;
      }
   }

   @Override
   public List<DetallesEmpresas> buscarDetallesEmpresas(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(DetallesEmpresas.class));
         List<DetallesEmpresas> listaResultado = em.createQuery(cq).getResultList();
         if (listaResultado != null) {
            System.out.println("PersistenciaDetallesEmpresas.buscarDetallesEmpresas() listaResultado : " + listaResultado);
            if (!listaResultado.isEmpty()) {
               em.clear();
               Query query = em.createNativeQuery("SELECT D.SECUENCIA,\n"
                       + " P.PRIMERAPELLIDO||' '||P.SEGUNDOAPELLIDO||' '||P.NOMBRE NOMBRE_PERSONAFIRMACONSTANCIA,\n"
                       + " E.NOMBRE NOMBRE_EMPRESA,\n"
                       + " P2.PRIMERAPELLIDO||' '||P2.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_REPRESENTANTECIR,\n"
                       + " P3.PRIMERAPELLIDO||' '||P3.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_SUBGERENTE,\n"
                       + " P4.PRIMERAPELLIDO||' '||P4.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_ARQUITECTO,\n"
                       + " P5.PRIMERAPELLIDO||' '||P5.SEGUNDOAPELLIDO||' '||P2.NOMBRE NOMBRE_GERENTEGENERAL,\n"
                       + " C.NOMBRE NOMBRE_CIUDAD, C2.NOMBRE NOMBRE_CIUDADDOCREPRESENTANTE, CA.NOMBRE NOMBRE_CARGOFIRMACONSTANCIA\n"
                       + " FROM DETALLESEMPRESAS D, PERSONAS P, EMPRESAS E, EMPLEADOS E2, PERSONAS P2, EMPLEADOS E3, PERSONAS P3,\n"
                       + " EMPLEADOS E4, PERSONAS P4, EMPLEADOS E5, PERSONAS P5, CIUDADES C, CIUDADES C2, CARGOS CA\n"
                       + " WHERE P.SECUENCIA(+) = D.PERSONAFIRMACONSTANCIA\n"
                       + " AND E.SECUENCIA(+) = D.EMPRESA\n"
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
                  System.out.println("PersistenciaDetallesEmpresas.buscarDetallesEmpresas() listaAux : " + listaResultado);
                  if (!listaAux.isEmpty()) {
                     for (int j = 0; j < listaResultado.size(); j++) {
                        for (int i = 0; i < listaAux.size(); i++) {
                           if (listaResultado.get(j).getSecuencia().equals(listaAux.get(i).getSecuencia())) {
                              listaResultado.get(j).setNombre_arquitecto(listaAux.get(i).getNombre_arquitecto());
                              listaResultado.get(j).setNombre_cargofirmaconstancia(listaAux.get(i).getNombre_cargofirmaconstancia());
                              listaResultado.get(j).setNombre_ciudad(listaAux.get(i).getNombre_ciudad());
                              listaResultado.get(j).setNombre_ciudaddocumentorepresentante(listaAux.get(i).getRef_ciudaddocrepresentante());
                              listaResultado.get(j).setNombre_empresa(listaAux.get(i).getNombre_empresa());
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
         System.out.println("Error PersistenciaDetallesEmpresas.buscarDetallesEmpresas : " + e.toString());
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
         System.out.println("PersistenciaDetallesEmpresas ERROR buscarARLPorEmpresa() : " + e.toString());
         return null;
      }
   }

}
