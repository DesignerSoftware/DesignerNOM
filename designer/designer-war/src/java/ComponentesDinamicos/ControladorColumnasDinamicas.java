package ComponentesDinamicos;

import Entidades.ResultadoBusquedaAvanzada;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.component.column.Column;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class ControladorColumnasDinamicas implements Serializable {

   private final static List<String> NOMBRE_COLUMNAS_VALIDAS = Arrays.asList("codigoEmpleado", "nombre", "primerApellido", "segundoApellido",
           "columna0", "columna1", "columna2", "columna3", "columna4", "columna5", "columna6", "columna7", "columna8", "columna9");
   private List<ColumnModel> columns;
   private String columnas;

   public ControladorColumnasDinamicas() {
      this.columnas = "CODIGOEMPLEADO,NOMBRE,PRIMERAPELLIDO,SEGUNDOAPELLIDO";
   }

   @PostConstruct
   public void init() {
      createDynamicColumns();
   }

   private void createDynamicColumns() {
      String[] columnKeys = columnas.split(",");
      columns = new ArrayList<ColumnModel>();

      System.out.println("Esta en createDynamicColumns()");
      for (int i = 0; i < columnKeys.length; i++) {
         //String columnKey = columnKeys[i];
         columns.add(new ColumnModel(columnKeys[i].toUpperCase(), NOMBRE_COLUMNAS_VALIDAS.get(i)));
         System.out.println("columnKeys[" + i + "].toUpperCase() :  " + columnKeys[i].toUpperCase() + " ---- \t NOMBRE_COLUMNAS_VALIDAS.get(" + i + ")" + NOMBRE_COLUMNAS_VALIDAS.get(i));
      }
      columns.get(0).setWidthColumn(130);
      columns.get(1).setWidthColumn(200);
      columns.get(2).setWidthColumn(130);
      columns.get(3).setWidthColumn(130);
   }

   private void createDynamicColumns(ResultadoBusquedaAvanzada primerResultado) {
      String[] columnKeys = columnas.split(",");
      columns = new ArrayList<ColumnModel>();

      System.out.println("Esta en createDynamicColumns()");
      for (int i = 0; i < columnKeys.length; i++) {
         //String columnKey = columnKeys[i];
         columns.add(new ColumnModel(columnKeys[i].toUpperCase(), NOMBRE_COLUMNAS_VALIDAS.get(i)));
         System.out.println("columnKeys[" + i + "].toUpperCase() :  " + columnKeys[i].toUpperCase() + " ---- \t NOMBRE_COLUMNAS_VALIDAS.get(" + i + ")" + NOMBRE_COLUMNAS_VALIDAS.get(i));
      }
      System.out.println("primerResultado : " + primerResultado);
      if (primerResultado != null) {
         try {
            columns.get(0).setWidthColumn(130);
            columns.get(1).setWidthColumn(200);
            columns.get(2).setWidthColumn(130);
            columns.get(3).setWidthColumn(130);
            if (columnKeys[4].length() >= primerResultado.getColumna0().length()) {
               columns.get(4).setWidthColumn(calcularAncho(columnKeys[4].length()));
            } else {
               columns.get(4).setWidthColumn(calcularAncho(primerResultado.getColumna0().length()));
            }
            
            if (columnKeys[5].length() >= primerResultado.getColumna1().length()) {
               columns.get(5).setWidthColumn(calcularAncho(columnKeys[5].length()));
            } else {
               columns.get(5).setWidthColumn(calcularAncho(primerResultado.getColumna1().length()));
            }
            
            if (columnKeys[6].length() >= primerResultado.getColumna2().length()) {
               columns.get(6).setWidthColumn(calcularAncho(columnKeys[6].length()));
            } else {
               columns.get(6).setWidthColumn(calcularAncho(primerResultado.getColumna2().length()));
            }
            
            if (columnKeys[7].length() >= primerResultado.getColumna3().length()) {
               columns.get(7).setWidthColumn(calcularAncho(columnKeys[7].length()));
            } else {
               columns.get(7).setWidthColumn(calcularAncho(primerResultado.getColumna3().length()));
            }
            
            if (columnKeys[8].length() >= primerResultado.getColumna4().length()) {
               columns.get(8).setWidthColumn(calcularAncho(columnKeys[8].length()));
            } else {
               columns.get(8).setWidthColumn(calcularAncho(primerResultado.getColumna4().length()));
            }
            
            if (columnKeys[9].length() >= primerResultado.getColumna5().length()) {
               columns.get(9).setWidthColumn(calcularAncho(columnKeys[9].length()));
            } else {
               columns.get(9).setWidthColumn(calcularAncho(primerResultado.getColumna5().length()));
            }
            
            if (columnKeys[10].length() >= primerResultado.getColumna6().length()) {
               columns.get(10).setWidthColumn(calcularAncho(columnKeys[10].length()));
            } else {
               columns.get(10).setWidthColumn(calcularAncho(primerResultado.getColumna6().length()));
            }
            
            if (columnKeys[11].length() >= primerResultado.getColumna7().length()) {
               columns.get(11).setWidthColumn(calcularAncho(columnKeys[11].length()));
            } else {
               columns.get(11).setWidthColumn(calcularAncho(primerResultado.getColumna7().length()));
            }
            
            if (columnKeys[12].length() >= primerResultado.getColumna8().length()) {
               columns.get(12).setWidthColumn(calcularAncho(columnKeys[12].length()));
            } else {
               columns.get(12).setWidthColumn(calcularAncho(primerResultado.getColumna8().length()));
            }
            
            if (columnKeys[13].length() >= primerResultado.getColumna0().length()) {
               columns.get(13).setWidthColumn(calcularAncho(columnKeys[9].length()));
            } else {
               columns.get(13).setWidthColumn(calcularAncho(primerResultado.getColumna9().length()));
            }
//            columns.get(6).setWidthColumn(calcularAncho(primerResultado.getColumna2().length()));
//            columns.get(7).setWidthColumn(calcularAncho(primerResultado.getColumna3().length()));
//            columns.get(8).setWidthColumn(calcularAncho(primerResultado.getColumna4().length()));
//            columns.get(9).setWidthColumn(calcularAncho(primerResultado.getColumna5().length()));
//            columns.get(10).setWidthColumn(calcularAncho(primerResultado.getColumna6().length()));
//            columns.get(11).setWidthColumn(calcularAncho(primerResultado.getColumna7().length()));
//            columns.get(12).setWidthColumn(calcularAncho(primerResultado.getColumna8().length()));
//            columns.get(13).setWidthColumn(calcularAncho(primerResultado.getColumna9().length()));
         } catch (Exception e) {
            System.out.println("Entro al catch() e : " + e);
         }
      }
   }

   public int calcularAncho(int tamanoString) {
      if (tamanoString > 0) {
         return (tamanoString * 9);
      } else {
         return 100;
      }
   }

   public void updateColumns(String columnas) {
      //reset table state
      UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent("form:resultadoBusqueda");
      table.setValueExpression("sortBy", null);
      //update columns
      this.columnas = columnas;
      createDynamicColumns();
   }

   public void updateColumns(String columnas, ResultadoBusquedaAvanzada primerResultado) {
      //reset table state
      UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent("form:resultadoBusqueda");
      table.setValueExpression("sortBy", null);
      //update columns
      this.columnas = columnas;
      createDynamicColumns(primerResultado);
      RequestContext.getCurrentInstance().update("form:resultadoBusqueda");
   }

   static public class ColumnModel implements Serializable {

      private String header;
      private String property;
      private int widthColumn;

      public ColumnModel(String header, String property) {
         this.header = header;
         this.property = property;
      }

      public ColumnModel(String header, String property, int widthColumn) {
         this.header = header;
         this.property = property;
         this.widthColumn = widthColumn;
      }

      public String getHeader() {
         return header;
      }

      public String getProperty() {
         return property;
      }

      public int getWidthColumn() {
         return widthColumn;
      }

      public void setWidthColumn(int widthColumn) {
         this.widthColumn = widthColumn;
      }
   }

   public List<ColumnModel> getColumns() {
      return columns;
   }
}
