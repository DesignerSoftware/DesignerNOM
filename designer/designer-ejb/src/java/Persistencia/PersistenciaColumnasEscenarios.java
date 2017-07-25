/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ColumnasEscenarios;
import Entidades.ResultadoBusquedaAvanzada;
import InterfacePersistencia.PersistenciaColumnasEscenariosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author PROYECTO01
 */
@Stateless
public class PersistenciaColumnasEscenarios implements PersistenciaColumnasEscenariosInterface {

   private static Logger log = Logger.getLogger(PersistenciaColumnasEscenarios.class);

   /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager
     * em;
    */
   @Override
   public List<ColumnasEscenarios> buscarColumnasEscenarios(EntityManager em) {
      try {
         em.clear();
//         Query query = em.createNativeQuery("SELECT * FROM ColumnasEscenarios cc WHERE ESCENARIO = (select SECUENCIA from escenarios where QVWNOMBRE= 'QVWEMPLEADOSCORTE') ORDER BY cc.nombrecolumna ASC", ColumnasEscenarios.class);
         Query query = em.createNativeQuery("SELECT ce.* FROM ESCENARIOS e, columnasescenarios ce where e.qvwnombre='QVWEMPLEADOSCORTE' and ce.escenario=e.secuencia ORDER BY ce.nombrecolumna ASC", ColumnasEscenarios.class);
         List<ColumnasEscenarios> competenciascargos = query.getResultList();
         return competenciascargos;
      } catch (Exception e) {
         log.error("Error buscarColumnasEscenarios PersistenciaColumnasEscenarios : " + e.toString());
         return null;
      }
   }

   @Override
   public List<ResultadoBusquedaAvanzada> buscarQVWEmpleadosCorteCodigoEmpleado(EntityManager em, List<ResultadoBusquedaAvanzada> listaEmpleadosResultados, List<String> campos) {
      try {
         log.error("Persistencia.PersistenciaColumnasEscenarios.buscarQVWEmpleadosCorteCodigoEmpleado()");
         em.clear();
         List<ResultadoBusquedaAvanzada> registrosConDatos = new ArrayList<ResultadoBusquedaAvanzada>();
         for (int j = 0; j < listaEmpleadosResultados.size(); j++) {
            ResultadoBusquedaAvanzada obj = new ResultadoBusquedaAvanzada();
            registrosConDatos.add(obj);
            for (int i = 0; i < campos.size(); i++) {
               String auxiliarCampo;
               if (campos.get(i).contains("FECHA")) {
                  auxiliarCampo = "TO_CHAR(" + campos.get(i) + ",'DD/MM/YYYY')";
               } else {
                  auxiliarCampo = "TO_CHAR(" + campos.get(i) + ")";
               }
               try {
                  String q = "SELECT " + auxiliarCampo + " FROM QVWEmpleadosCorte q WHERE q.codigoempleado = " + listaEmpleadosResultados.get(j).getCodigoEmpleado();
                  Query query = em.createNativeQuery(q);
                  if (i == 0) {
                     log.error("Persistencia - buscarQVWEmpleadosCorteCodigoEmpleado() Query : " + q);
                  }
                  String valor = (String) query.getSingleResult();
                  registrosConDatos.get(j).setCodigoEmpleado(listaEmpleadosResultados.get(j).getCodigoEmpleado());
                  if (i == 0) {
                     registrosConDatos.get(j).setColumna0(valor);
                  }
                  if (i == 1) {
                     registrosConDatos.get(j).setColumna1(valor);
                  }
                  if (i == 2) {
                     registrosConDatos.get(j).setColumna2(valor);
                  }
                  if (i == 3) {
                     registrosConDatos.get(j).setColumna3(valor);
                  }
                  if (i == 4) {
                     registrosConDatos.get(j).setColumna4(valor);
                  }
                  if (i == 5) {
                     registrosConDatos.get(j).setColumna5(valor);
                  }
                  if (i == 6) {
                     registrosConDatos.get(j).setColumna6(valor);
                  }
                  if (i == 7) {
                     registrosConDatos.get(j).setColumna7(valor);
                  }
                  if (i == 8) {
                     registrosConDatos.get(j).setColumna8(valor);
                  }
                  if (i == 9) {
                     registrosConDatos.get(j).setColumna9(valor);
                  }
               } catch (PersistenceException errorPersistence) {
                  log.error("Error consultando Datos errorPersistence : " + errorPersistence);
                  if (j == 0) {
                     log.error("i : " + i + "   , campos.get(i) : " + campos.get(i));
                  }
               }
            }
         }
         return registrosConDatos;

      } catch (Exception e) {
         log.error("Error buscarQVWEmpleadosCorteCodigoEmpleado PersistenciaQVWEmpleadosCorte : " + e.toString());
         e.printStackTrace();
         return null;
      }
   }

   /**
    *
    * @param em
    * @param listaEmpleadosResultados
    * @return
    */
   @Override
   public List<ResultadoBusquedaAvanzada> buscarQVWEmpleadosCortePorEmpleadoCodigo(EntityManager em, List<BigInteger> listaEmpleadosResultados) {
      try {
         log.error("Persistencia.PersistenciaColumnasEscenarios.buscarQVWEmpleadosCorteCodigoEmpleadoCodigo()");
         String camposAux = "secuencia SECUENCIA, NVL(TO_CHAR(codigoempleado),' ') CODIGOEMPLEADO, NVL(primerapellido,' ') PRIMERAPELLIDO, NVL(segundoapellido,' ') SEGUNDOAPELLIDO, NVL(nombre ,' ') NOMBREEMPLEADO";
         em.clear();

         String queryMap = "SELECT " + camposAux + " FROM QVWEmpleadosCorte q WHERE q.codigoempleado = ?";
         log.error("queryMap: " + queryMap);
         List<ResultadoBusquedaAvanzada> registroPrueba = new ArrayList<ResultadoBusquedaAvanzada>();
         for (int j = 0; j < listaEmpleadosResultados.size(); j++) {
            if (j == 0) {
               log.error("listaEmpleadosResultados.get(0) : " + listaEmpleadosResultados.get(0));
            }
            ResultadoBusquedaAvanzada resultado = new ResultadoBusquedaAvanzada();
            Query query = em.createNativeQuery(queryMap, "ConsultaBusquedaAvanzada");
            query.setParameter(1, listaEmpleadosResultados.get(j));
            resultado = (ResultadoBusquedaAvanzada) query.getSingleResult();
            registroPrueba.add(resultado);
         }
         return registroPrueba;

      } catch (Exception e) {
         log.error("Error buscarQVWEmpleadosCorteCodigoEmpleado PersistenciaQVWEmpleadosCorte : " + e.toString());
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public List<ResultadoBusquedaAvanzada> buscarQVWEmpleadosCortePorEmpleadoCodigoCompletos(EntityManager em, List<BigInteger> listaEmpleadosResultados, List<String> campos) {
      try {
         log.error("Persistencia.PersistenciaColumnasEscenarios.buscarQVWEmpleadosCortePorEmpleadoCodigoCompletos()");
         String camposAux = "secuencia SECUENCIA, NVL(TO_CHAR(codigoempleado),' ') CODIGOEMPLEADO, NVL(primerapellido,' ') PRIMERAPELLIDO, NVL(segundoapellido,' ') SEGUNDOAPELLIDO, NVL(nombre ,' ') NOMBREEMPLEADO";
         em.clear();
         if (!campos.isEmpty()) {
            String auxiliarCampo = "";
            for (int i = 0; i < campos.size(); i++) {
               String cualColumna = "";
               if (i == 0) {
                  cualColumna = "COLUMNA0";
               }
               if (i == 1) {
                  cualColumna = "COLUMNA1";
               }
               if (i == 2) {
                  cualColumna = "COLUMNA2";
               }
               if (i == 3) {
                  cualColumna = "COLUMNA3";
               }
               if (i == 4) {
                  cualColumna = "COLUMNA4";
               }
               if (i == 5) {
                  cualColumna = "COLUMNA5";
               }
               if (i == 6) {
                  cualColumna = "COLUMNA6";
               }
               if (i == 7) {
                  cualColumna = "COLUMNA7";
               }
               if (i == 8) {
                  cualColumna = "COLUMNA8";
               }
               if (i == 9) {
                  cualColumna = "COLUMNA9";
               }
               if (campos.get(i).contains("FECHA")) {
                  auxiliarCampo = auxiliarCampo + ", TO_CHAR(" + campos.get(i) + ",'DD/MM/YYYY') " + cualColumna;
               } else {
                  auxiliarCampo = auxiliarCampo + ", TO_CHAR(" + campos.get(i) + ") " + cualColumna;
               }
            }
            camposAux = camposAux + auxiliarCampo;
         }
         String queryMap = "SELECT " + camposAux + " FROM QVWEmpleadosCorte q WHERE q.codigoempleado = ?";
         log.error("queryMap: " + queryMap);
         List<ResultadoBusquedaAvanzada> registroPrueba = new ArrayList<ResultadoBusquedaAvanzada>();
         for (int j = 0; j < listaEmpleadosResultados.size(); j++) {
            if (j == 0) {
               log.error("listaEmpleadosResultados.get(0) : " + listaEmpleadosResultados.get(0));
            }
            ResultadoBusquedaAvanzada resultado = new ResultadoBusquedaAvanzada();
            Query query = em.createNativeQuery(queryMap, "ConsultaBusquedaAvanzada");
            query.setParameter(1, listaEmpleadosResultados.get(j));
            resultado = (ResultadoBusquedaAvanzada) query.getSingleResult();
            registroPrueba.add(resultado);
         }
         return registroPrueba;

      } catch (Exception e) {
         e.printStackTrace();
         log.error("Error buscarQVWEmpleadosCorteCodigoEmpleado PersistenciaQVWEmpleadosCorte : " + e.toString());
         return null;
      }
   }
}