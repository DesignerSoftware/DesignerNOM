/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import Entidades.VWAcumulados;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author user
 */
public class LazySorterEmplAcumulados implements Comparator<VWAcumulados> {

    private String sortField;

    private SortOrder sortOrder;

    public LazySorterEmplAcumulados(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(VWAcumulados o1, VWAcumulados o2) {
        try {
            int value = 0;
            if (sortField.equals("concepto_Codigo")) {
                value = o1.getConcepto_Codigo().compareTo(o2.getConcepto_Codigo());
            } else if (sortField.equals("concepto_Descripcion")) {
                value = o1.getConcepto_Descripcion().compareTo(o2.getConcepto_Descripcion());
            } else if (sortField.equals("fechaDesde")) {
                value = o1.getFechaDesde().toString().compareTo(o2.getFechaDesde().toString());
            } else if (sortField.equals("fechaPago")) {
                value = o1.getFechaPago().toString().compareTo(o2.getFechaPago().toString());
            } else if (sortField.equals("unidades")) {
                value = o1.getUnidades().toString().compareTo(o2.getUnidades().toString());
            } else if (sortField.equals("valor")) {
                value = o1.getValor().compareTo(o2.getValor());
            } else if (sortField.equals("saldo")) {
                value = o1.getSaldo().toString().compareTo(o2.getSaldo().toString());
            } else if (sortField.equals("tipo")) {
                value = o1.getTipo().compareTo(o2.getTipo());
            } else if (sortField.equals("corteProceso.proceso.descripcion")) {
                value = o1.getCorteProceso().getProceso().getDescripcion().compareTo(o2.getCorteProceso().getProceso().getDescripcion());
            } else if (sortField.equals("nit_nombre")) {
                value = o1.getNit_nombre().compareTo(o2.getNit_nombre());
            } else if (sortField.equals("formula.nombrelargo")) {
                value = o1.getFormula().getNombrelargo().compareTo(o2.getFormula().getNombrelargo());
            } else if (sortField.equals("cuentaD.codigo")) {
                value = o1.getCuentaD().getCodigo().compareTo(o2.getCuentaD().getCodigo());
            } else if (sortField.equals("centroCostoD.nombre")) {
                value = o1.getCentroCostoD().getNombre().compareTo(o2.getCentroCostoD().getNombre());
            } else if (sortField.equals("cuentaC.codigo")) {
                value = o1.getCuentaC().getCodigo().compareTo(o2.getCuentaC().getCodigo());
            } else if (sortField.equals("centroCostoC.nombre")) {
                value = o1.getCentroCostoC().getNombre().compareTo(o2.getCentroCostoC().getNombre());
            } else if (sortField.equals("ultimaModificacion")) {
                value = o1.getUltimaModificacion().compareTo(o2.getUltimaModificacion());
            } else if (sortField.equals("observacionesMovedad")) {
                value = o1.getObservacionesMovedad().compareTo(o2.getObservacionesMovedad());
            }
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;

        } catch (Exception e) {
            System.out.println("error en compare : " + e.getMessage());
            System.out.println("error en compare : " + e.getCause());
            throw new RuntimeException();
        }
    }

}
