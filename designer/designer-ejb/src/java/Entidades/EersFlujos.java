package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "EERSFLUJOS")
public class EersFlujos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "COMENTARIO")
    private String comentario;
    @Size(max = 1)
    @Column(name = "APROBADO")
    private String aprobado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 30)
    @Column(name = "USUARIO")
    private String usuario;
    @JoinColumn(name = "EEROPCIONESTADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private EersOpcionesEstados eeropcionestado;
    @JoinColumn(name = "EERESTADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private EersEstados eerestado;
    @JoinColumn(name = "EERCABECERA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private EersCabeceras eercabecera;

    public EersFlujos() {
    }

    public EersFlujos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public EersFlujos(BigInteger secuencia, String comentario, Date fecha) {
        this.secuencia = secuencia;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public EersOpcionesEstados getEeropcionestado() {
        return eeropcionestado;
    }

    public void setEeropcionestado(EersOpcionesEstados eeropcionestado) {
        this.eeropcionestado = eeropcionestado;
    }

    public EersEstados getEerestado() {
        return eerestado;
    }

    public void setEerestado(EersEstados eerestado) {
        this.eerestado = eerestado;
    }

    public EersCabeceras getEercabecera() {
        return eercabecera;
    }

    public void setEercabecera(EersCabeceras eercabecera) {
        this.eercabecera = eercabecera;
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
        if (!(object instanceof EersFlujos)) {
            return false;
        }
        EersFlujos other = (EersFlujos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EersFlujos[ secuencia=" + secuencia + " ]";
    }
    
}
