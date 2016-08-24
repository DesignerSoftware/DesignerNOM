package ClasesAyuda;

import java.io.Serializable;
import java.util.List;

public class ResultadoBorrarTodoNovedades implements Serializable{
    private List<String> documentosNoBorrados;
    private int registrosBorrados;

    public List<String> getDocumentosNoBorrados() {
        return documentosNoBorrados;
    }

    public void setDocumentosNoBorrados(List<String> documentosNoBorrados) {
        this.documentosNoBorrados = documentosNoBorrados;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }
}
