package utilidadesUI;

import java.util.List;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Felipe Trivinio
 */
public class PrimefacesContextUI {

    public static void actualizar(String componente) {
        RequestContext.getCurrentInstance().update(componente);
    }

    public static void actualizarLista(List<String> componente) {
        RequestContext.getCurrentInstance().update(componente);
    }

    public static void ejecutar(String instruccion) {
        RequestContext.getCurrentInstance().execute(instruccion);
    }
}
