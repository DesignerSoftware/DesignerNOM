/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.SolucionesNodos;
import Entidades.SolucionesNodosAux;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaSolucionesNodos implements PersistenciaSolucionesNodosInterface {

   @Override
   public void crear(EntityManager em, SolucionesNodos solucionNodo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(solucionNodo);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaSolucionesNodos.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, SolucionesNodos solucionNodo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(solucionNodo);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaSolucionesNodos.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, SolucionesNodos solucionNodo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(solucionNodo));
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaSolucionesNodos.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<SolucionesNodos> buscarSolucionesNodos(EntityManager em) {
      em.clear();
      try {
         javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(SolucionesNodos.class));
         List<SolucionesNodos> listSNodos = em.createQuery(cq).getResultList();

         if (listSNodos != null) {
            if (!listSNodos.isEmpty()) {
               System.out.println("resultado.size() : " + listSNodos.size());
               for (int i = 0; i < listSNodos.size(); i++) {
                  em.clear();
                  String nitTercero = "NULL";
                  if (listSNodos.get(i).getNittercero() != null) {
                     nitTercero = "" + listSNodos.get(i).getNittercero();
                  }
                  String stringSQLQuery = "SELECT sn.secuencia,\n"
                          + "c.CODIGO CODIGOCONCEPTO,\n"
                          + "c.DESCRIPCION NOMBRECONCEPTO,\n"
                          + "(select NOMBRE from TERCEROS WHERE " + nitTercero + " = SECUENCIA(+)) NOMBRETERCERO,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentad() + ") CODIGOCUENTAD,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentac() + ") CODIGOCUENTAC,\n"
                          + "(select p.PRIMERAPELLIDO||' '|| p.SEGUNDOAPELLIDO ||' '||p.NOMBRE NOMBREEMPLEADO from personas p, empleados e where e.persona = p.secuencia and e.secuencia = " + listSNodos.get(i).getEmpleado() + ") NOMBREEMPLEADO,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostod() + ") NOMBRECENTROCOSTOD,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostoc() + ") NOMBRECENTROCOSTOC\n"
                          + "FROM SOLUCIONESNODOS sn, (select CODIGO,DESCRIPCION from conceptos where secuencia = " + listSNodos.get(i).getConcepto() + ") c \n"
                          + "where sn.SECUENCIA = " + listSNodos.get(i).getSecuencia();
                  Query query2 = em.createNativeQuery(stringSQLQuery, SolucionesNodosAux.class);
                  SolucionesNodosAux sNAux = (SolucionesNodosAux) query2.getSingleResult();
                  listSNodos.get(i).setCodigoconcepto(sNAux.getCodigoconcepto());
                  listSNodos.get(i).setNombreconcepto(sNAux.getNombreconcepto());
                  listSNodos.get(i).setNombretercero(sNAux.getNombretercero());
                  listSNodos.get(i).setCodigocuentad(sNAux.getCodigocuentad());
                  listSNodos.get(i).setCodigocuentac(sNAux.getCodigocuentac());
                  listSNodos.get(i).setNombreempleado(sNAux.getNombreempleado());
                  listSNodos.get(i).setNombrecentrocostod(sNAux.getNombrecentrocostod());
                  listSNodos.get(i).setNombrecentrocostoc(sNAux.getNombrecentrocostoc());
               }
            }
         }
         return listSNodos;
      } catch (Exception e) {
         System.out.println("Error: (PersistenciaSolucionesNodos.buscarSolucionesNodos) : " + e.getMessage());
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> solucionNodoCorteProcesoEmpleado(EntityManager em, BigInteger secuenciaCorteProceso, BigInteger secuenciaEmpleado) {
      try {
         em.clear();
         String sql = "SELECT sn.*\n"
                 + "FROM SOLUCIONESNODOS sn\n"
                 + "WHERE sn.ESTADO = 'CERRADO'\n"
                 + "AND sn.TIPO IN ('PAGO','DESCUENTO') \n"
                 + "AND sn.CORTEPROCESO = " + secuenciaCorteProceso + "\n"
                 + "AND sn.EMPLEADO = " + secuenciaEmpleado;
         Query query = em.createNativeQuery(sql, SolucionesNodos.class);
         List<SolucionesNodos> listSNodos = query.getResultList();

         if (listSNodos != null) {
            if (!listSNodos.isEmpty()) {
               System.out.println("resultado.size() : " + listSNodos.size());
               for (int i = 0; i < listSNodos.size(); i++) {
                  em.clear();
                  String nitTercero = "NULL";
                  if (listSNodos.get(i).getNittercero() != null) {
                     nitTercero = "" + listSNodos.get(i).getNittercero();
                  }
                  String stringSQLQuery = "SELECT sn.secuencia,\n"
                          + "c.CODIGO CODIGOCONCEPTO,\n"
                          + "c.DESCRIPCION NOMBRECONCEPTO,\n"
                          + "(select NOMBRE from TERCEROS WHERE " + nitTercero + " = SECUENCIA(+)) NOMBRETERCERO,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentad() + ") CODIGOCUENTAD,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentac() + ") CODIGOCUENTAC,\n"
                          + "(select p.PRIMERAPELLIDO||' '|| p.SEGUNDOAPELLIDO ||' '||p.NOMBRE NOMBREEMPLEADO from personas p, empleados e where e.persona = p.secuencia and e.secuencia = " + secuenciaEmpleado + ") NOMBREEMPLEADO,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostod() + ") NOMBRECENTROCOSTOD,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostoc() + ") NOMBRECENTROCOSTOC\n"
                          + "FROM SOLUCIONESNODOS sn, (select CODIGO,DESCRIPCION from conceptos where secuencia = " + listSNodos.get(i).getConcepto() + ") c \n"
                          + "where sn.SECUENCIA = " + listSNodos.get(i).getSecuencia();
                  if (i == 0) {
//                     System.out.println("stringSQLQuery : " + stringSQLQuery);
                  }
                  Query query2 = em.createNativeQuery(stringSQLQuery, SolucionesNodosAux.class);
                  SolucionesNodosAux sNAux = (SolucionesNodosAux) query2.getSingleResult();
                  listSNodos.get(i).setCodigoconcepto(sNAux.getCodigoconcepto());
                  listSNodos.get(i).setNombreconcepto(sNAux.getNombreconcepto());
                  listSNodos.get(i).setNombretercero(sNAux.getNombretercero());
                  listSNodos.get(i).setCodigocuentad(sNAux.getCodigocuentad());
                  listSNodos.get(i).setCodigocuentac(sNAux.getCodigocuentac());
                  listSNodos.get(i).setNombreempleado(sNAux.getNombreempleado());
                  listSNodos.get(i).setNombrecentrocostod(sNAux.getNombrecentrocostod());
                  listSNodos.get(i).setNombrecentrocostoc(sNAux.getNombrecentrocostoc());
               }
               List<SolucionesNodos> listaReturn = new ArrayList<SolucionesNodos>();
               listaReturn.add(listSNodos.get(0));
               listSNodos.remove(0);
               try {
                  while (!listSNodos.isEmpty()) {
                     if (listSNodos.get(0).getCodigoconcepto() != null) {
                        List<SolucionesNodos> listaAux = new ArrayList<SolucionesNodos>();
                        int banderita = 0;
//                        for (int j = 0; j < listaReturn.size(); j++) {
                        int j = 0;
                        while (j < listaReturn.size()) {
                           if (listSNodos.get(0).getCodigoconcepto().intValue() < listaReturn.get(j).getCodigoconcepto().intValue()) {
                              listaAux.add(listaReturn.get(j));
                              listaReturn.remove(j);
                              banderita++;
                           } else {
                              j++;
                           }
                        }
                        if (banderita == 0) {
                           listaReturn.add(listSNodos.get(0));
                           listSNodos.remove(0);
                        } else {
                           listaReturn.add(listSNodos.get(0));
                           listaReturn.addAll(listaAux);
                           listSNodos.remove(0);
                        }
                     }
                  }
               } catch (Exception e2) {
                  System.out.println("PersistenciaSolucionesNodos.solucionNodoCorteProcesoEmpleado() CATCH2 : ERROR CONSULTANDO TRANSIENTS");
               }
               listSNodos = listaReturn;
            }
         }

         return listSNodos;
      } catch (Exception e) {
         System.out.println("Error: (PersistenciaSolucionesNodos.solucionNodoCorteProcesoEmpleado)" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> solucionNodoCorteProcesoEmpleador(EntityManager em, BigInteger secuenciaCorteProceso, BigInteger secuenciaEmpleado) {
      try {
         em.clear();
         String sql = "SELECT sn.* \n"
                 + "FROM SOLUCIONESNODOS sn \n"
                 + "WHERE sn.ESTADO = 'CERRADO'\n"
                 + "AND sn.TIPO IN ('PASIVO','GASTO','NETO')\n"
                 + "AND SN.CORTEPROCESO = " + secuenciaCorteProceso + " \n"
                 + "AND sn.EMPLEADO = " + secuenciaEmpleado;
         Query query = em.createNativeQuery(sql, SolucionesNodos.class);
         List<SolucionesNodos> listSNodos = query.getResultList();

         if (listSNodos != null) {
            if (!listSNodos.isEmpty()) {
               System.out.println("resultado.size() : " + listSNodos.size());
               for (int i = 0; i < listSNodos.size(); i++) {
                  em.clear();
                  String nitTercero = "NULL";
                  if (listSNodos.get(i).getNittercero() != null) {
                     nitTercero = "" + listSNodos.get(i).getNittercero();
                  }
                  String stringSQLQuery = "SELECT sn.secuencia,\n"
                          + "c.CODIGO CODIGOCONCEPTO,\n"
                          + "c.DESCRIPCION NOMBRECONCEPTO,\n"
                          + "(select NOMBRE from TERCEROS WHERE " + nitTercero + " = SECUENCIA(+)) NOMBRETERCERO,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentad() + ") CODIGOCUENTAD,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentac() + ") CODIGOCUENTAC,\n"
                          + "(select p.PRIMERAPELLIDO||' '|| p.SEGUNDOAPELLIDO ||' '||p.NOMBRE NOMBREEMPLEADO from personas p, empleados e where e.persona = p.secuencia and e.secuencia = " + secuenciaEmpleado + ") NOMBREEMPLEADO,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostod() + ") NOMBRECENTROCOSTOD,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostoc() + ") NOMBRECENTROCOSTOC\n"
                          + "FROM SOLUCIONESNODOS sn, (select CODIGO,DESCRIPCION from conceptos where secuencia = " + listSNodos.get(i).getConcepto() + ") c \n"
                          + "where sn.SECUENCIA = " + listSNodos.get(i).getSecuencia();
                  if (i == 0) {
                     System.out.println("stringSQLQuery : " + stringSQLQuery);
                  }
                  Query query2 = em.createNativeQuery(stringSQLQuery, SolucionesNodosAux.class);
                  SolucionesNodosAux sNAux = (SolucionesNodosAux) query2.getSingleResult();
                  listSNodos.get(i).setCodigoconcepto(sNAux.getCodigoconcepto());
                  listSNodos.get(i).setNombreconcepto(sNAux.getNombreconcepto());
                  listSNodos.get(i).setNombretercero(sNAux.getNombretercero());
                  listSNodos.get(i).setCodigocuentad(sNAux.getCodigocuentad());
                  listSNodos.get(i).setCodigocuentac(sNAux.getCodigocuentac());
                  listSNodos.get(i).setNombreempleado(sNAux.getNombreempleado());
                  listSNodos.get(i).setNombrecentrocostod(sNAux.getNombrecentrocostod());
                  listSNodos.get(i).setNombrecentrocostoc(sNAux.getNombrecentrocostoc());
               }
               List<SolucionesNodos> listaReturn = new ArrayList<SolucionesNodos>();
               listaReturn.add(listSNodos.get(0));
               listSNodos.remove(0);
               try {
                  while (!listSNodos.isEmpty()) {
                     System.out.println("PersistenciaSolucionesNodos.solucionNodoCorteProcesoEmpleador() cod(0):" + listSNodos.get(0).getCodigoconcepto());
                     if (listSNodos.get(0).getCodigoconcepto() != null) {
                        List<SolucionesNodos> listaAux = new ArrayList<SolucionesNodos>();
                        int banderita = 0;
//                        for (int j = 0; j < listaReturn.size(); j++) {
                        int j = 0;
                        while (j < listaReturn.size()) {
                           if (listSNodos.get(0).getCodigoconcepto().intValue() < listaReturn.get(j).getCodigoconcepto().intValue()) {
                              listaAux.add(listaReturn.get(j));
                              listaReturn.remove(j);
                              banderita++;
                           } else {
                              j++;
                           }
                        }
                        if (banderita == 0) {
                           listaReturn.add(listSNodos.get(0));
                           listSNodos.remove(0);
                        } else {
                           listaReturn.add(listSNodos.get(0));
                           listaReturn.addAll(listaAux);
                           listSNodos.remove(0);
                        }
                     }
                  }
               } catch (Exception e2) {
                  System.out.println("PersistenciaSolucionesNodos.solucionNodoCorteProcesoEmpleador() CATCH2 : ERROR CONSULTANDO TRANSIENTS");
               }
               listSNodos = listaReturn;
            }
         }
         return listSNodos;
      } catch (Exception e) {
         System.out.println("Error: (PersistenciaSolucionesNodos.solucionNodoCorteProcesoEmpleador) e: " + e.getMessage());
         return null;
      }
   }

   @Override
   public BigDecimal diasProvisionados(EntityManager em, BigInteger secuencia
   ) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT SN1.unidades "
                 + "FROM SolucionesNodos SN1, Conceptos c "
                 + "WHERE SN1.empleado =:secuenciaempleado "
                 + "AND c.secuencia=SN1.concepto "
                 + "AND SN1.tipo='PASIVO' "
                 + "AND SN1.estado='CERRADO' "
                 + "AND SN1.concepto=(SELECT GC.concepto.secuencia "
                 + "FROM  GruposProvisiones GC "
                 + "WHERE GC.nombre='VACACIONES' "
                 + "AND GC.empresa.secuencia=c.empresa.secuencia) "
                 + "AND SN1.fechahasta = (SELECT MAX(SN2.fechahasta) "
                 + "FROM SolucionesNodos SN2 "
                 + "WHERE SN2.fechadesde<=CURRENT_DATE "
                 + "AND SN1.empleado=SN2.empleado "
                 + "AND SN1.concepto=SN2.concepto "
                 + "AND SN2.tipo = 'PASIVO' "
                 + "AND SN2.estado='CERRADO')");
         query.setParameter("secuenciaempleado", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         BigDecimal llegada = (BigDecimal) query.getSingleResult();
         return llegada;
      } catch (Exception e) {
         System.out.println("Error: (PersistenciaSolucionesNodos.diasProvisionados)" + e.toString());
         return null;
      }
   }

   @Override
   public Long validacionTercerosVigenciaAfiliacion(EntityManager em, BigInteger secuencia, Date fechaInicial, BigInteger secuenciaTE, BigInteger secuenciaTer
   ) {
      DateFormat formatoF = new SimpleDateFormat("ddMMyyy");
      String fecha = formatoF.format(fechaInicial);
      try {
         em.clear();
         String strQuery = "SELECT count(*)\n"
                 + "FROM SolucionesNodos v, TERCEROS T\n"
                 + "where \n"
                 + "v.fechapago > TO_DATE('" + fecha + "','ddMMyyy') \n"
                 + "AND V.EMPLEADO = " + secuencia + "\n"
                 + "AND V.NIT = T.SECUENCIA\n"
                 + "AND v.estado ='CERRADO'\n"
                 + "AND T.secuencia != " + secuenciaTer + "\n"
                 + "AND exists (\n"
                 + "  SELECT cs.SECUENCIA \n"
                 + "  FROM ConceptosSoportes cs\n"
                 + "  WHERE CS.CONCEPTO = v.concepto\n"
                 + "  AND CS.TIPOENTIDAD = " + secuenciaTE + "\n"
                 + "  and cs.tipo='AUTOLIQUIDACION' \n"
                 + "  AND cs.subgrupo='COTIZACION')";
         Query query = em.createNativeQuery(strQuery);
         BigDecimal count = (BigDecimal) query.getSingleResult();
         Long conteo = count.longValue();
         return conteo;
      } catch (Exception e) {
         System.out.println("Error validacionTercerosVigenciaAfiliacion Persistencia : " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal activos(EntityManager em, BigInteger secuencia
   ) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT count(sn) FROM SolucionesNodos sn where sn.empleado.secuencia = :secuencia;)");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         BigDecimal r = (BigDecimal) query.getSingleResult();
         System.out.println("Resultado : " + r);
         return r;
      } catch (Exception e) {
         System.out.println("Error activos Persistencia : " + e.toString());
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> solucionNodoEmpleado(EntityManager em, BigInteger secuenciaEmpleado
   ) {
      try {
         em.clear();
         String sql = "SELECT sn.* \n"
                 + "FROM SOLUCIONESNODOS sn, CONCEPTOS c\n"
                 + "WHERE sn.ESTADO = 'LIQUIDADO'\n"
                 + "AND sn.CONCEPTO = c.SECUENCIA\n"
                 + "AND sn.TIPO IN ('PAGO','DESCUENTO')\n"
                 + "AND sn.EMPLEADO = " + secuenciaEmpleado
                 + " ORDER BY c.codigo ASC";
         Query query = em.createNativeQuery(sql, SolucionesNodos.class);
         List<SolucionesNodos> listSNodos = query.getResultList();
         System.out.println("sql: " + sql);

         if (listSNodos != null) {
            if (!listSNodos.isEmpty()) {
               System.out.println("listSNodos.size() : " + listSNodos.size());
               for (int i = 0; i < listSNodos.size(); i++) {
                  em.clear();
                  String nitTercero = "NULL";
                  if (listSNodos.get(i).getNittercero() != null) {
                     nitTercero = "" + listSNodos.get(i).getNittercero();
                  }
                  String stringSQLQuery = "SELECT sn.secuencia,\n"
                          + "c.CODIGO CODIGOCONCEPTO,\n"
                          + "c.DESCRIPCION NOMBRECONCEPTO,\n"
                          + "(select NOMBRE from TERCEROS WHERE " + nitTercero + " = SECUENCIA(+)) NOMBRETERCERO,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentad() + ") CODIGOCUENTAD,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentac() + ") CODIGOCUENTAC,\n"
                          + "(select p.PRIMERAPELLIDO||' '|| p.SEGUNDOAPELLIDO ||' '||p.NOMBRE NOMBREEMPLEADO from personas p, empleados e where e.persona = p.secuencia and e.secuencia = " + secuenciaEmpleado + ") NOMBREEMPLEADO,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostod() + ") NOMBRECENTROCOSTOD,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostoc() + ") NOMBRECENTROCOSTOC\n"
                          + "FROM SOLUCIONESNODOS sn, (select CODIGO,DESCRIPCION from conceptos where secuencia = " + listSNodos.get(i).getConcepto() + ") c \n"
                          + "where sn.SECUENCIA = " + listSNodos.get(i).getSecuencia();
                  Query query2 = em.createNativeQuery(stringSQLQuery, SolucionesNodosAux.class);
                  SolucionesNodosAux sNAux = (SolucionesNodosAux) query2.getSingleResult();
                  listSNodos.get(i).setCodigoconcepto(sNAux.getCodigoconcepto());
                  listSNodos.get(i).setNombreconcepto(sNAux.getNombreconcepto());
                  listSNodos.get(i).setNombretercero(sNAux.getNombretercero());
                  listSNodos.get(i).setCodigocuentad(sNAux.getCodigocuentad());
                  listSNodos.get(i).setCodigocuentac(sNAux.getCodigocuentac());
                  listSNodos.get(i).setNombreempleado(sNAux.getNombreempleado());
                  listSNodos.get(i).setNombrecentrocostod(sNAux.getNombrecentrocostod());
                  listSNodos.get(i).setNombrecentrocostoc(sNAux.getNombrecentrocostoc());
               }
            }
         }
         return listSNodos;
      } catch (Exception e) {
         System.out.println("Error: (PersistenciaSolucionesNodos.solucionNodoEmpleado)" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> solucionNodoEmpleador(EntityManager em, BigInteger secuenciaEmpleado
   ) {
      try {
         em.clear();
         String sql = "SELECT sn.* \n"
                 + "FROM SOLUCIONESNODOS sn, CONCEPTOS c\n"
                 + "WHERE sn.ESTADO = 'LIQUIDADO' \n"
                 + "AND sn.VALOR <> 0 \n"
                 + "AND sn.TIPO IN ('PASIVO','GASTO','NETO')\n"
                 + "AND sn.CONCEPTO = c.SECUENCIA\n"
                 + "AND sn.EMPLEADO = " + secuenciaEmpleado
                 + " ORDER BY c.codigo ASC";
         Query query = em.createNativeQuery(sql, SolucionesNodos.class);
         List<SolucionesNodos> listSNodos = query.getResultList();

         if (listSNodos != null) {
            if (!listSNodos.isEmpty()) {
               System.out.println("listSNodos.size() : " + listSNodos.size());
               for (int i = 0; i < listSNodos.size(); i++) {
                  em.clear();
                  String nitTercero = "NULL";
                  if (listSNodos.get(i).getNittercero() != null) {
                     nitTercero = "" + listSNodos.get(i).getNittercero();
                  }
                  String stringSQLQuery = "SELECT sn.secuencia,\n"
                          + "c.CODIGO CODIGOCONCEPTO,\n"
                          + "c.DESCRIPCION NOMBRECONCEPTO,\n"
                          + "(select NOMBRE from TERCEROS WHERE " + nitTercero + " = SECUENCIA(+)) NOMBRETERCERO,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentad() + ") CODIGOCUENTAD,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentac() + ") CODIGOCUENTAC,\n"
                          + "(select p.PRIMERAPELLIDO||' '|| p.SEGUNDOAPELLIDO ||' '||p.NOMBRE NOMBREEMPLEADO from personas p, empleados e where e.persona = p.secuencia and e.secuencia = " + secuenciaEmpleado + ") NOMBREEMPLEADO,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostod() + ") NOMBRECENTROCOSTOD,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostoc() + ") NOMBRECENTROCOSTOC\n"
                          + "FROM SOLUCIONESNODOS sn, (select CODIGO,DESCRIPCION from conceptos where secuencia = " + listSNodos.get(i).getConcepto() + ") c \n"
                          + "where sn.SECUENCIA = " + listSNodos.get(i).getSecuencia();
                  Query query2 = em.createNativeQuery(stringSQLQuery, SolucionesNodosAux.class);
                  SolucionesNodosAux sNAux = (SolucionesNodosAux) query2.getSingleResult();
                  listSNodos.get(i).setCodigoconcepto(sNAux.getCodigoconcepto());
                  listSNodos.get(i).setNombreconcepto(sNAux.getNombreconcepto());
                  listSNodos.get(i).setNombretercero(sNAux.getNombretercero());
                  listSNodos.get(i).setCodigocuentad(sNAux.getCodigocuentad());
                  listSNodos.get(i).setCodigocuentac(sNAux.getCodigocuentac());
                  listSNodos.get(i).setNombreempleado(sNAux.getNombreempleado());
                  listSNodos.get(i).setNombrecentrocostod(sNAux.getNombrecentrocostod());
                  listSNodos.get(i).setNombrecentrocostoc(sNAux.getNombrecentrocostoc());
               }
            }
         }
         return listSNodos;
      } catch (Exception e) {
         System.out.println("Error: (PersistenciaSolucionesNodos.solucionNodoEmpleador)" + e.getMessage());
         return null;
      }
   }

   @Override
   public Integer ContarProcesosSN(EntityManager em, BigInteger secProceso
   ) {
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(distinct empleado)\n"
                 + " FROM solucionesnodos sn\n"
                 + " WHERE estado='LIQUIDADO'\n"
                 + " AND sn.proceso = ?\n"
                 + " AND exists (select 'x' from parametros p, usuarios u\n"
                 + " where u.secuencia = p.usuario\n"
                 + " and p.proceso = sn.proceso\n"
                 + " AND p.empleado = sn.empleado)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secProceso);
         BigDecimal conteo = (BigDecimal) query.getSingleResult();
         Integer conteoProcesosSN = conteo.intValueExact();
         return conteoProcesosSN;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaSolucionesNodos.ContarProcesosSN()" + e.getMessage());
         return null;
      }
   }

   @Override
   public boolean solucionesNodosParaConcepto(EntityManager em, BigInteger secuencia
   ) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT count(sn) FROM SolucionesNodos sn WHERE sn.concepto.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long valor = (Long) query.getSingleResult();
         return (valor == 0);
      } catch (Exception e) {
         System.out.println("Error solucionesNodosParaConcepto PersistenciaSolucionesNodos : " + e.toString());
         return false;
      }
   }

   @Override
   public List<SolucionesNodos> buscarSolucionesNodosParaParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal
   ) {
      try {
         em.clear();

         String sql = "SELECT * FROM SolucionesNodos s WHERE EXISTS (SELECT 'X' FROM CONTABILIZACIONES C \n"
                 + "WHERE C.flag='GENERADO' AND C.fechageneracion \n"
                 + "BETWEEN ? AND ? \n"
                 + "AND c.solucionnodo = s.secuencia)\n"
                 + "AND  EXISTS (SELECT 'X' FROM  cortesprocesos cp , procesos p WHERE  cp.secuencia = s.corteproceso\n"
                 + "AND p.secuencia = cp.proceso AND CONTABILIZACION = 'S')\n"
                 + "and exists (select 'x' from empleados e where e.secuencia=s.empleado)"
                 + "and s.valor <> 0";
         Query query = em.createNativeQuery(sql, SolucionesNodos.class);
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         List<SolucionesNodos> listSNodos = query.getResultList();

         if (listSNodos != null) {
            if (!listSNodos.isEmpty()) {
               System.out.println("listSNodos.size() : " + listSNodos.size());

               String sql2 = "SELECT S.SECUENCIA, C.CODIGO CODIGOCONCEPTO, C.DESCRIPCION NOMBRECONCEPTO, T.NIT NITTERCERO, CUD.CODIGO CODIGOCUENTAD, CUC.CODIGO CODIGOCUENTAC,\n"
                       + "              p.PRIMERAPELLIDO||' '|| p.SEGUNDOAPELLIDO ||' '||p.NOMBRE NOMBREEMPLEADO, PRO.DESCRIPCION NOMBREPROCESO\n"
                       + "              FROM SOLUCIONESNODOS S, CONCEPTOS C, TERCEROS T, CUENTAS CUD,  CUENTAS CUC, EMPLEADOS EM, PERSONAS P, PROCESOS PRO\n"
                       + "              WHERE EXISTS (SELECT 'X' FROM contabilizaciones C\n"
                       + "              where C.flag='GENERADO' and C.fechageneracion between ? and ? and c.solucionnodo = s.secuencia)\n"
                       + "              AND  EXISTS (SELECT 'X' FROM  cortesprocesos cp , procesos p WHERE  cp.secuencia = s.corteproceso AND p.secuencia = cp.proceso AND CONTABILIZACION = 'S')\n"
                       + "              and exists (select 'x' from empleados e where e.secuencia=s.empleado)\n"
                       + "              and s.valor <> 0\n"
                       + "              AND S.CONCEPTO = C.SECUENCIA\n"
                       + "              AND S.NIT = T.SECUENCIA(+)\n"
                       + "              AND S.CUENTAD = CUD.SECUENCIA\n"
                       + "              AND S.CUENTAC = CUC.SECUENCIA\n"
                       + "              AND S.EMPLEADO = EM.SECUENCIA\n"
                       + "              AND EM.PERSONA = P.SECUENCIA\n"
                       + "              AND S.PROCESO = PRO.SECUENCIA";
               em.clear();
               Query query2 = em.createNativeQuery(sql2, SolucionesNodosAux.class);
               query2.setParameter(1, fechaInicial);
               query2.setParameter(2, fechaFinal);
               List<SolucionesNodosAux> listSNodosAux = query2.getResultList();

               if (listSNodosAux != null) {
                  if (!listSNodosAux.isEmpty()) {
                     System.out.println("listSNodosAux.size() : " + listSNodosAux.size());

                     for (int i = 0; i < listSNodos.size(); i++) {
                        for (int j = 0; j < listSNodosAux.size(); j++) {
                           if (listSNodos.get(i).getSecuencia().equals(listSNodosAux.get(j).getSecuencia())) {
                              listSNodos.get(i).setCodigoconcepto(listSNodosAux.get(j).getCodigoconcepto());
                              listSNodos.get(i).setNombreconcepto(listSNodosAux.get(j).getNombreconcepto());
                              listSNodos.get(i).setNittercero(listSNodosAux.get(j).getNittercero());
                              listSNodos.get(i).setCodigocuentad(listSNodosAux.get(j).getCodigocuentad());
                              listSNodos.get(i).setCodigocuentac(listSNodosAux.get(j).getCodigocuentac());
                              listSNodos.get(i).setNombreempleado(listSNodosAux.get(j).getNombreempleado());
                              listSNodos.get(i).setNombreproceso(listSNodosAux.get(j).getNombreproceso());
                              listSNodosAux.remove(listSNodosAux.get(j));
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }

         return listSNodos;
      } catch (Exception e) {
         System.out.println("Error buscarSolucionesNodosParaParametroContable PersistenciaSolucionesNodos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> buscarSolucionesNodosParaParametroContable_SAP(EntityManager em, Date fechaInicial, Date fechaFinal
   ) {
      try {
         em.clear();
         String sql = "select * from SolucionesNodos s "
                 + "WHERE EXISTS (SELECT 'X' FROM contabilizaciones C \n"
                 + "where C.flag='GENERADO' and C.fechageneracion \n"
                 + "between ? and ? \n"
                 + "and c.solucionnodo = s.secuencia) \n"
                 + "AND EXISTS (SELECT 'X' FROM  cortesprocesos cp , procesos p WHERE  cp.secuencia = s.corteproceso \n"
                 + "AND p.secuencia = cp.proceso AND CONTABILIZACION = 'S') \n"
                 + "and exists (select 'x' from empleados e where e.secuencia=s.empleado)";
         Query query = em.createNativeQuery(sql, SolucionesNodos.class
         );
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         List<SolucionesNodos> listSNodos = query.getResultList();

         if (listSNodos != null) {
            if (!listSNodos.isEmpty()) {
               for (int i = 0; i < listSNodos.size(); i++) {
                  em.clear();
                  String nitTercero = "NULL";
                  if (listSNodos.get(i).getNittercero() != null) {
                     nitTercero = "" + listSNodos.get(i).getNittercero();
                  }
                  String stringSQLQuery = "SELECT sn.secuencia,\n"
                          + "c.CODIGO CODIGOCONCEPTO,\n"
                          + "c.DESCRIPCION NOMBRECONCEPTO,\n"
                          + "(select NOMBRE from TERCEROS WHERE " + nitTercero + " = SECUENCIA(+)) NOMBRETERCERO,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentad() + ") CODIGOCUENTAD,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentac() + ") CODIGOCUENTAC,\n"
                          + "(select p.PRIMERAPELLIDO||' '|| p.SEGUNDOAPELLIDO ||' '||p.NOMBRE NOMBREEMPLEADO from personas p, empleados e where e.persona = p.secuencia and e.secuencia = " + listSNodos.get(i).getEmpleado() + ") NOMBREEMPLEADO,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostod() + ") NOMBRECENTROCOSTOD,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostoc() + ") NOMBRECENTROCOSTOC\n"
                          + "FROM SOLUCIONESNODOS sn, (select CODIGO,DESCRIPCION from conceptos where secuencia = " + listSNodos.get(i).getConcepto() + ") c \n"
                          + "where sn.SECUENCIA = " + listSNodos.get(i).getSecuencia();
                  Query query2 = em.createNativeQuery(stringSQLQuery, SolucionesNodosAux.class
                  );
                  SolucionesNodosAux sNAux = (SolucionesNodosAux) query2.getSingleResult();
                  listSNodos.get(i).setCodigoconcepto(sNAux.getCodigoconcepto());
                  listSNodos.get(i).setNombreconcepto(sNAux.getNombreconcepto());
                  listSNodos.get(i).setNombretercero(sNAux.getNombretercero());
                  listSNodos.get(i).setCodigocuentad(sNAux.getCodigocuentad());
                  listSNodos.get(i).setCodigocuentac(sNAux.getCodigocuentac());
                  listSNodos.get(i).setNombreempleado(sNAux.getNombreempleado());
                  listSNodos.get(i).setNombrecentrocostod(sNAux.getNombrecentrocostod());
                  listSNodos.get(i).setNombrecentrocostoc(sNAux.getNombrecentrocostoc());
               }
            }
         }
         return listSNodos;
      } catch (Exception e) {
         System.out.println("Error buscarSolucionesNodosParaParametroContable_SAP PersistenciaSolucionesNodos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> buscarSolucionesNodosParaParametroContable_Dynamics(EntityManager em, Date fechaInicial, Date fechaFinal
   ) {
      try {
         em.clear();
         String sql = "select * from solucionesnodos s where EXISTS (SELECT 'X' FROM contabilizaciones C\n"
                 + "              where C.flag='GENERADO' and C.fechageneracion \n"
                 + "               between ? and ?\n"
                 + "                   and c.solucionnodo = s.secuencia)\n"
                 + "AND  EXISTS (SELECT 'X' FROM  cortesprocesos cp , procesos p WHERE  cp.secuencia = s.corteproceso\n"
                 + "AND p.secuencia = cp.proceso AND CONTABILIZACION = 'S')\n"
                 + "and exists (select 'x' from empleados e where e.secuencia=s.empleado)";
         Query query = em.createNativeQuery(sql, SolucionesNodos.class
         );
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         List<SolucionesNodos> listSNodos = query.getResultList();

         if (listSNodos != null) {
            if (!listSNodos.isEmpty()) {
               System.out.println("resultado.size() : " + listSNodos.size());
               for (int i = 0; i < listSNodos.size(); i++) {
                  em.clear();
                  String nitTercero = "NULL";
                  if (listSNodos.get(i).getNittercero() != null) {
                     nitTercero = "" + listSNodos.get(i).getNittercero();
                  }
                  String stringSQLQuery = "SELECT sn.secuencia,\n"
                          + "c.CODIGO CODIGOCONCEPTO,\n"
                          + "c.DESCRIPCION NOMBRECONCEPTO,\n"
                          + "(select NOMBRE from TERCEROS WHERE " + nitTercero + " = SECUENCIA(+)) NOMBRETERCERO,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentad() + ") CODIGOCUENTAD,\n"
                          + "(select CODIGO from CUENTAS WHERE SECUENCIA = " + listSNodos.get(i).getCuentac() + ") CODIGOCUENTAC,\n"
                          + "(select p.PRIMERAPELLIDO||' '|| p.SEGUNDOAPELLIDO ||' '||p.NOMBRE NOMBREEMPLEADO from personas p, empleados e where e.persona = p.secuencia and e.secuencia = " + listSNodos.get(i).getEmpleado() + ") NOMBREEMPLEADO,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostod() + ") NOMBRECENTROCOSTOD,\n"
                          + "(select NOMBRE from CENTROSCOSTOS WHERE SECUENCIA = " + listSNodos.get(i).getCentrocostoc() + ") NOMBRECENTROCOSTOC\n"
                          + "FROM SOLUCIONESNODOS sn, (select CODIGO,DESCRIPCION from conceptos where secuencia = " + listSNodos.get(i).getConcepto() + ") c \n"
                          + "where sn.SECUENCIA = " + listSNodos.get(i).getSecuencia();
                  Query query2 = em.createNativeQuery(stringSQLQuery, SolucionesNodosAux.class
                  );
                  SolucionesNodosAux sNAux = (SolucionesNodosAux) query2.getSingleResult();
                  listSNodos.get(i).setCodigoconcepto(sNAux.getCodigoconcepto());
                  listSNodos.get(i).setNombreconcepto(sNAux.getNombreconcepto());
                  listSNodos.get(i).setNombretercero(sNAux.getNombretercero());
                  listSNodos.get(i).setCodigocuentad(sNAux.getCodigocuentad());
                  listSNodos.get(i).setCodigocuentac(sNAux.getCodigocuentac());
                  listSNodos.get(i).setNombreempleado(sNAux.getNombreempleado());
                  listSNodos.get(i).setNombrecentrocostod(sNAux.getNombrecentrocostod());
                  listSNodos.get(i).setNombrecentrocostoc(sNAux.getNombrecentrocostoc());
               }
            }
         }
         return listSNodos;
      } catch (Exception e) {
         System.out.println("Error buscarSolucionesNodosParaParametroContable_Dynamics PersistenciaSolucionesNodos : " + e.toString());
         return null;
      }
   }
}
