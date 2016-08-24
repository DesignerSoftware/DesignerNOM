/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import ClasesAyuda.ColumnasBusquedaAvanzada;
import Entidades.ColumnasEscenarios;
import Entidades.Empleados;
import Entidades.ResultadoBusquedaAvanzada;
import InterfacePersistencia.PersistenciaColumnasEscenariosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author PROYECTO01
 */
@Stateless
public class PersistenciaColumnasEscenarios implements PersistenciaColumnasEscenariosInterface {
    /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager
     * em;
     */

    @Override
    public List<ColumnasEscenarios> buscarColumnasEscenarios(EntityManager em) {
        try {
            em.clear();
            Query query = em.createNativeQuery("SELECT * FROM ColumnasEscenarios cc WHERE ESCENARIO = (select SECUENCIA from escenarios where QVWNOMBRE= 'QVWEMPLEADOSCORTE') ORDER BY cc.nombrecolumna ASC", ColumnasEscenarios.class);
            List<ColumnasEscenarios> competenciascargos = query.getResultList();
            return competenciascargos;
        } catch (Exception e) {
            System.out.println("Error buscarColumnasEscenarios PersistenciaColumnasEscenarios : " + e.toString());
            return null;
        }
    }

    @Override
    public List<ColumnasBusquedaAvanzada> buscarQVWEmpleadosCorteCodigoEmpleado(EntityManager em, List<Empleados> listaEmpleadosResultados, List<String> campos) {
        try {
            em.clear();
            List<ColumnasBusquedaAvanzada> registro = new ArrayList<ColumnasBusquedaAvanzada>();
            for (int j = 0; j < listaEmpleadosResultados.size(); j++) {
                ColumnasBusquedaAvanzada obj = new ColumnasBusquedaAvanzada();
                registro.add(obj);
                for (int i = 0; i < campos.size(); i++) {
                    String campo = campos.get(i);
                    if (campo.contains("FECHA")) {
                        String auxiliar = "TO_CHAR(" + campo + ",'DD/MM/YYYY')";
                        campo = auxiliar;
                    } else {
                        String auxiliar = "TO_CHAR(" + campo + ")";
                        campo = auxiliar;
                    }
                    String q = "SELECT " + campo + " FROM QVWEmpleadosCorte q WHERE q.codigoempleado=" + listaEmpleadosResultados.get(j).getCodigoempleado();
                    Query query = em.createNativeQuery(q);
                    String valor = (String) query.getSingleResult();
                    if (i == 0) {
                        registro.get(j).setColumna0(valor);
                    }
                    if (i == 1) {
                        registro.get(j).setColumna1(valor);
                    }
                    if (i == 2) {
                        registro.get(j).setColumna2(valor);
                    }
                    if (i == 3) {
                        registro.get(j).setColumna3(valor);
                    }
                    if (i == 4) {
                        registro.get(j).setColumna4(valor);
                    }
                    if (i == 5) {
                        registro.get(j).setColumna5(valor);
                    }
                    if (i == 6) {
                        registro.get(j).setColumna6(valor);
                    }
                    if (i == 7) {
                        registro.get(j).setColumna7(valor);
                    }
                    if (i == 8) {
                        registro.get(j).setColumna8(valor);
                    }
                    if (i == 9) {
                        registro.get(j).setColumna9(valor);
                    }
                }
            }
            return registro;

        } catch (Exception e) {
            System.out.println("Error buscarQVWEmpleadosCorteCodigoEmpleado PersistenciaQVWEmpleadosCorte : " + e.toString());
            return null;
        }
    }

    @Override
    public List<ResultadoBusquedaAvanzada> buscarQVWEmpleadosCorteCodigoEmpleadoCodigo(EntityManager em, List<BigInteger> listaEmpleadosResultados, String campos) {
        try {
            em.clear();
            String[] nnn = campos.split(",");
            String camposAux = "";
            int numColumna = 0;
            camposAux = "secuencia SECUENCIA, NVL(TO_CHAR(codigoempleado),' ') CODIGOEMPLEADO, NVL(primerapellido,' ') PRIMERAPELLIDO, NVL(segundoapellido,' ') SEGUNDOAPELLIDO, NVL(nombre ,' ') NOMBREEMPLEADO ";
            int i = 5;
            while (i < nnn.length) {
                String aux = nnn[i];
                if (aux.contains("FECHA")) {
                    camposAux = camposAux + ", NVL(TO_CHAR(" + nnn[i] + ",'DD/MM/YYYY'),' ') COLUMNA" + String.valueOf(numColumna) + " ";
                } else {
                    camposAux = camposAux + ", NVL(TO_CHAR('" + nnn[i] + "'),' ') COLUMNA" + String.valueOf(numColumna) + " ";
                }
                i++;
                numColumna++;
            }
            String queryMap = "SELECT " + camposAux + " FROM QVWEmpleadosCorte q WHERE q.codigoempleado= ?";
            System.out.println("queryMap: " + queryMap);
            List<ResultadoBusquedaAvanzada> registroPrueba = new ArrayList<ResultadoBusquedaAvanzada>();
            for (int j = 0; j < listaEmpleadosResultados.size(); j++) {
                ResultadoBusquedaAvanzada resultado = new ResultadoBusquedaAvanzada();
                Query query = em.createNativeQuery(queryMap, "ConsultaBusquedaAvanzada");
                query.setParameter(1, listaEmpleadosResultados.get(j));
                resultado = (ResultadoBusquedaAvanzada) query.getSingleResult();
                registroPrueba.add(resultado);
            }
            return registroPrueba;

        } catch (Exception e) {
            System.out.println("Error buscarQVWEmpleadosCorteCodigoEmpleado PersistenciaQVWEmpleadosCorte : " + e.toString());
            return null;
        }
    }
}
