package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "RASTROSTABLAS")
public class RastrosTablas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 1)
    @Column(name = "INSERTAR")
    private String insertar;
    @Size(max = 1)
    @Column(name = "ACTUALIZAR")
    private String actualizar;
    @Size(max = 1)
    @Column(name = "ELIMINAR")
    private String eliminar;
    @Size(max = 1)
    @Column(name = "RASTREARVALORES")
    private String rastrearvalores;
    @JoinColumn(name = "OBJETO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private ObjetosDB objeto;

    public RastrosTablas() {
    }

    public RastrosTablas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getInsertar() {
        return insertar;
    }

    public void setInsertar(String insertar) {
        this.insertar = insertar;
    }

    public String getActualizar() {
        return actualizar;
    }

    public void setActualizar(String actualizar) {
        this.actualizar = actualizar;
    }

    public String getEliminar() {
        return eliminar;
    }

    public void setEliminar(String eliminar) {
        this.eliminar = eliminar;
    }

    public String getRastrearvalores() {
        return rastrearvalores;
    }

    public void setRastrearvalores(String rastrearvalores) {
        this.rastrearvalores = rastrearvalores;
    }

    public ObjetosDB getObjeto() {
        return objeto;
    }

    public void setObjeto(ObjetosDB objeto) {
        this.objeto = objeto;
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
        if (!(object instanceof RastrosTablas)) {
            return false;
        }
        RastrosTablas other = (RastrosTablas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.RastrosTablas[ secuencia=" + secuencia + " ]";
    }
    
}
