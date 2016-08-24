package ComponentesDinamicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class ControladorColumnasDinamicas implements Serializable {

    private final static List<String> NOMBRE_COLUMNAS_VALIDAS = Arrays.asList("secuencia", "codigoEmpleado", "nombre", "primerApellido", "segundoApellido",
            "columna0", "columna1", "columna2", "columna3", "columna4", "columna5", "columna6", "columna7", "columna8", "columna9");
    private List<ColumnModel> columns;
    private String columnas;

    public ControladorColumnasDinamicas() {
        this.columnas = "SECUENCIA,CODIGOEMPLEADO,NOMBRE,PRIMERAPELLIDO,SEGUNDOAPELLIDO";
    }

    @PostConstruct
    public void init() {
        createDynamicColumns();
    }

    private void createDynamicColumns() {
        String[] columnKeys = columnas.split(",");
        columns = new ArrayList<ColumnModel>();

        for (int i = 0; i < columnKeys.length; i++) {
            //String columnKey = columnKeys[i];
            columns.add(new ColumnModel(columnKeys[i].toUpperCase(), NOMBRE_COLUMNAS_VALIDAS.get(i)));
        }
    }

    public void updateColumns(String columnas) {
        //reset table state
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:resultadoBusqueda");
        table.setValueExpression("sortBy", null);

        //update columns
        this.columnas = columnas;
        createDynamicColumns();
    }

    static public class ColumnModel implements Serializable {

        private String header;
        private String property;

        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }

        public String getHeader() {
            return header;
        }

        public String getProperty() {
            return property;
        }
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }
}
