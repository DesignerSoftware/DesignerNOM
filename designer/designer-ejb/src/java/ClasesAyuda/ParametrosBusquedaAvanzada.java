package ClasesAyuda;

public class ParametrosBusquedaAvanzada {

    private ParametrosBusquedaNomina parametrosBusquedaNomina;
    private ParametrosBusquedaPersonal parametrosBusquedaPersonal;

    public ParametrosBusquedaAvanzada() {
        parametrosBusquedaNomina = new ParametrosBusquedaNomina();
        parametrosBusquedaPersonal = new ParametrosBusquedaPersonal();
    }

    public ParametrosBusquedaNomina getParametrosBusquedaNomina() {
        return parametrosBusquedaNomina;
    }

    public void setParametrosBusquedaNomina(ParametrosBusquedaNomina parametrosBusquedaNomina) {
        this.parametrosBusquedaNomina = parametrosBusquedaNomina;
    }

    public ParametrosBusquedaPersonal getParametrosBusquedaPersonal() {
        return parametrosBusquedaPersonal;
    }

    public void setParametrosBusquedaPersonal(ParametrosBusquedaPersonal parametrosBusquedaPersonal) {
        this.parametrosBusquedaPersonal = parametrosBusquedaPersonal;
    }
}
