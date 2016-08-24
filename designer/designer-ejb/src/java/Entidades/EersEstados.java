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
@Table(name = "EERSESTADOS")
public class EersEstados implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPOEER")
    private String tipoeer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private short codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 1)
    @Column(name = "ENVIAREMAIL")
    private String enviaremail;
    @Size(max = 200)
    @Column(name = "MENSAJE")
    private String mensaje;
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    @JoinColumn(name = "EEROPCIONESTADOAPRUEBA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private EersOpcionesEstados eeropcionestadoaprueba;
    @JoinColumn(name = "EEROPCIONESTADOCANCELA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private EersOpcionesEstados eeropcionestadocancela;

    public EersEstados() {
    }

    public EersEstados(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public EersEstados(BigInteger secuencia, String tipoeer, short codigo, String descripcion) {
        this.secuencia = secuencia;
        this.tipoeer = tipoeer;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getTipoeer() {
        return tipoeer;
    }

    public void setTipoeer(String tipoeer) {
        this.tipoeer = tipoeer;
    }

    public short getCodigo() {
        return codigo;
    }

    public void setCodigo(short codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEnviaremail() {
        return enviaremail;
    }

    public void setEnviaremail(String enviaremail) {
        this.enviaremail = enviaremail;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EersOpcionesEstados getEeropcionestadoaprueba() {
        return eeropcionestadoaprueba;
    }

    public void setEeropcionestadoaprueba(EersOpcionesEstados eeropcionestadoaprueba) {
        this.eeropcionestadoaprueba = eeropcionestadoaprueba;
    }

    public EersOpcionesEstados getEeropcionestadocancela() {
        return eeropcionestadocancela;
    }

    public void setEeropcionestadocancela(EersOpcionesEstados eeropcionestadocancela) {
        this.eeropcionestadocancela = eeropcionestadocancela;
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
        if (!(object instanceof EersEstados)) {
            return false;
        }
        EersEstados other = (EersEstados) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EersEstados[ secuencia=" + secuencia + " ]";
    }
    
}
