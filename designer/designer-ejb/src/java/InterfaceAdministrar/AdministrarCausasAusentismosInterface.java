package InterfaceAdministrar;

import Entidades.Causasausentismos;
import Entidades.Clasesausentismos;
import java.util.List;

public interface AdministrarCausasAusentismosInterface {

    public void obtenerConexion(String idSesion);

    public List<Causasausentismos> consultarCausasAusentismos();

    public List<Clasesausentismos> consultarClasesAusentismos();

    public void modificarCausasAusentismos(List<Causasausentismos> listaCausasAusentismo);

    public void borrarCausasAusentismos(List<Causasausentismos> listaCausasAusentismo);

    public void crearCausasAusentismos(List<Causasausentismos> listaCausasAusentismo);

}
