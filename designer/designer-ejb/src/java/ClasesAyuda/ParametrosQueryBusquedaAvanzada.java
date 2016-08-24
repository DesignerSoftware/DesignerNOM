/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

/**
 *
 * @author PROYECTO01
 */
public class ParametrosQueryBusquedaAvanzada {

    private String modulo;
    private String nombreParametro;
    private String valorParametro;

    public ParametrosQueryBusquedaAvanzada(String modulo, String nombreParametro, String valorParametro) {
        this.modulo = modulo;
        this.nombreParametro = nombreParametro;
        this.valorParametro = valorParametro;
    }

    public ParametrosQueryBusquedaAvanzada() {
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }

    public String getValorParametro() {
        return valorParametro;
    }

    public void setValorParametro(String valorParametro) {
        this.valorParametro = valorParametro;
    }

}
