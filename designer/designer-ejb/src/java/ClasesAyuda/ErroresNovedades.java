package ClasesAyuda;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class ErroresNovedades implements Serializable{
    
    private BigInteger secNovedad;
    private List<String> MensajeError;
    private int numeroErrores;

    public BigInteger getSecNovedad() {
        return secNovedad;
    }

    public void setSecNovedad(BigInteger secNovedad) {
        this.secNovedad = secNovedad;
    }

    public List<String> getMensajeError() {
        return MensajeError;
    }

    public void setMensajeError(List<String> MensajeError) {
        this.MensajeError = MensajeError;
    }

    public int getNumeroErrores() {
        return numeroErrores;
    }

    public void setNumeroErrores(int numeroErrores) {
        this.numeroErrores = numeroErrores;
    }
}
