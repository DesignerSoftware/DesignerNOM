/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import Entidades.VWAcumulados;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author user
 */
public class LazyEmplAcumuladosDataModel extends LazyDataModel<VWAcumulados> {

    private List<VWAcumulados> lista;

    public LazyEmplAcumuladosDataModel(List<VWAcumulados> lista) {
        this.lista = lista;
    }

    @Override
    public VWAcumulados getRowData(String rowKey) {
        for (VWAcumulados acumulado : this.lista) {
            if (acumulado.getSecuencia().toString().equals(rowKey)) {
                return acumulado;
            }
        }
        return null;
    }

    @Override
    public void setPageSize(int pageSize) {
        super.setPageSize(pageSize); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowKey(VWAcumulados acumulado) {
        return acumulado.getSecuencia();
    }

    @Override
    public List<VWAcumulados> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            List<VWAcumulados> data = new ArrayList<VWAcumulados>();

            //filtro
            for (VWAcumulados acumulado : lista) {
                boolean match = true;
                if (filters != null) {
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            String filterProperty = it.next();
                            Object valorFiltro = filters.get(filterProperty);

                            if (valorFiltro != null && acumulado.getConcepto_Codigo().toString().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getConcepto_Descripcion().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getFechaDesde().toString().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getFechaPago().toString().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getUnidades().toString().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getValor().toString().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getSaldo().toString().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getTipo().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getCorteProceso().getProceso().getDescripcion().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getNit_nombre().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getFormula().getNombrelargo().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getCuentaD().getCodigo().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getCentroCostoD().getNombre().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getCuentaC().getCodigo().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getCentroCostoC().getNombre().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getUltimaModificacion().toString().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getObservacionesMovedad().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && acumulado.getMotivoNovedad().contains(valorFiltro.toString())) {
                                match = true;
                            } else {
                                match = false;
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("error en match : " + e.getMessage());
                            match = false;
                        }
                    }
                }
                if (match == true) {
                    data.add(acumulado);
                }
            }
            //ordenamiento
            if (sortField != null) {
                Collections.sort(data, new LazySorterEmplAcumulados(sortField, sortOrder));
            }
            //conteo de registros
            int dataSize = data.size();
            this.setRowCount(dataSize);
            //paginador
            if (dataSize > pageSize) {
                try {
                    return data.subList(first, first + pageSize);
                } catch (IndexOutOfBoundsException e) {
                    return data.subList(first, first + (dataSize % pageSize));
                }
            } else {
                return data;
            }
        } catch (Exception w) {
            System.out.println("error en load : " + w.getMessage());
            return null;
        }
    }

}
