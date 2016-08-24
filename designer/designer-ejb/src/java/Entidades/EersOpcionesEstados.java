package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "EERSOPCIONESESTADOS")
public class EersOpcionesEstados implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NOMBRECORTO")
    private String nombrecorto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "NOMBRELARGO")
    private String nombrelargo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "TIPOEER")
    private String tipoeer;

    public EersOpcionesEstados() {
    }

    public EersOpcionesEstados(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public EersOpcionesEstados(BigInteger secuencia, String nombrecorto, String nombrelargo, String tipoeer) {
        this.secuencia = secuencia;
        this.nombrecorto = nombrecorto;
        this.nombrelargo = nombrelargo;
        this.tipoeer = tipoeer;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getNombrecorto() {
        return nombrecorto;
    }

    public void setNombrecorto(String nombrecorto) {
        this.nombrecorto = nombrecorto;
    }

    public String getNombrelargo() {
        return nombrelargo;
    }

    public void setNombrelargo(String nombrelargo) {
        this.nombrelargo = nombrelargo;
    }

    public String getTipoeer() {
        return tipoeer;
    }

    public void setTipoeer(String tipoeer) {
        this.tipoeer = tipoeer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EersOpcionesEstados)) {
            return false;
        }
        EersOpcionesEstados other = (EersOpcionesEstados) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EersOpcionesEstados[ secuencia=" + secuencia + " ]";
    }
    
}
