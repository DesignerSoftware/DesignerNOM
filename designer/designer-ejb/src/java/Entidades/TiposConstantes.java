package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "TIPOSCONSTANTES")
public class TiposConstantes implements Serializable {
   
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "VALORREAL")
    private BigDecimal valorreal;
    @Column(name = "VALORDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date valordate;
    @Size(max = 100)
    @Column(name = "VALORSTRING")
    private String valorstring;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "TIPO")
    private String tipo;
    @JoinColumn(name = "OPERANDO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Operandos operando;
    @Transient
    private String estadoTipo;

    public TiposConstantes() {
    }

    public TiposConstantes(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposConstantes(BigInteger secuencia, Date fechainicial, Date fechafinal, String tipo) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
        this.fechafinal = fechafinal;
        this.tipo = tipo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getValorreal() {
        return valorreal;
    }

    public void setValorreal(BigDecimal valorreal) {
        this.valorreal = valorreal;
    }

    public Date getValordate() {
        return valordate;
    }

    public void setValordate(Date valordate) {
        this.valordate = valordate;
    }

    public String getValorstring() {
        return valorstring;
    }

    public void setValorstring(String valorstring) {
        this.valorstring = valorstring;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public String getTipo() {
        if(tipo == null){
            tipo = "N";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getEstadoTipo() {
        if (estadoTipo == null) {
            if (tipo == null) {
                estadoTipo = "NUMERICO";

            } else {

                if (tipo.equalsIgnoreCase("N")) {
                    estadoTipo = "NUMERICO";
                } else if (tipo.equalsIgnoreCase("F")) {
                    estadoTipo = "FECHA";
                } else if (tipo.equalsIgnoreCase("C")) {
                    estadoTipo = "CADENA";
                }
            }
        }
        return estadoTipo;
    }

    public void setEstadoTipo(String estadoTipo) {

        if (estadoTipo.equalsIgnoreCase("NUMERICO")) {
            setTipo("N");
        } else if (estadoTipo.equals("FECHA")) {
            setTipo("F");
        } else if (estadoTipo.equals("CADENA")) {
            setTipo("C");
        } 
        this.estadoTipo = estadoTipo;
    }

    public Operandos getOperando() {
        return operando;
    }

    public void setOperando(Operandos operando) {
        this.operando = operando;
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
        if (!(object instanceof TiposConstantes)) {
            return false;
        }
        TiposConstantes other = (TiposConstantes) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TiposConstantes[ secuencia=" + secuencia + " ]";
    }
    
}
