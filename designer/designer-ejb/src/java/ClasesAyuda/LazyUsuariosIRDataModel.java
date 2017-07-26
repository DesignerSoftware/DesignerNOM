/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import Entidades.UsuariosInforeportes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 *
 * @author user
 */
public class LazyUsuariosIRDataModel extends LazyDataModel<UsuariosInforeportes> {

    private List<UsuariosInforeportes> lista;

    public LazyUsuariosIRDataModel(List<UsuariosInforeportes> lista) {
        this.lista = lista;
    }

    @Override
    public UsuariosInforeportes getRowData(String rowKey) {
        for (UsuariosInforeportes uir : this.lista) {
            if (uir.getSecuencia().toString().equals(rowKey)) {
                return uir;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(UsuariosInforeportes uir) {
        return uir.getSecuencia();
    }

    @Override
    public List<UsuariosInforeportes> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            List<UsuariosInforeportes> data = new ArrayList<UsuariosInforeportes>();

            //filtro
            for (UsuariosInforeportes uir : lista) {
                boolean match = true;

                if (filters != null) {
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            String filterProperty = it.next();
                            Object valorFiltro = filters.get(filterProperty);

                            if (valorFiltro != null && uir.getInforeporte().getNombre().toUpperCase().contains(valorFiltro.toString())) {
                                match = true;
                            }else if(valorFiltro != null && uir.getInforeporte().getCodigo().toString().toUpperCase().contains(valorFiltro.toString())){
                            match = true;
                            }else if(valorFiltro != null && uir.getInforeporte().getModulo().getNombre().toUpperCase().contains(valorFiltro.toString())){
                            match = true;
                            }else if(valorFiltro != null && uir.getInforeporte().getTipo().toUpperCase().contains(valorFiltro.toString())){
                            match = true;
                            }
                            else {
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
                    data.add(uir);
                }
            }

            //ordenamiento
            if (sortField != null) {
                System.out.println("sortField: "+sortField);
                //Collections.sort(data);
                Collections.sort(data, new LazySorterUIR(sortField, sortOrder));
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
