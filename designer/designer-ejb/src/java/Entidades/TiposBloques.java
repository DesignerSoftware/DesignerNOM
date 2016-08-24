package Entidades;

import java.io.Serializable;
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
@Table(name = "TIPOSBLOQUES")
public class TiposBloques implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private int codigo;
    @Lob
    @Column(name = "BLOQUEPLSQL")
    private String bloqueplsql;
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
    @Size(max = 10)
    @Column(name = "TIPO")
    private String tipo;
    @JoinColumn(name = "OPERANDO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Operandos operando;
    @Transient
    private String estadoTipo;

    public TiposBloques() {
    }

    public TiposBloques(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposBloques(BigInteger secuencia, int codigo, Date fechainicial, Date fechafinal) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.fechainicial = fechainicial;
        this.fechafinal = fechafinal;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getBloqueplsql() {
        if(bloqueplsql == null){
            bloqueplsql = " ";
        }
        return bloqueplsql.toUpperCase();
    }

    public void setBloqueplsql(String bloqueplsql) {
        this.bloqueplsql = bloqueplsql;
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
        if (tipo == null) {
            tipo = "NUMBER";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstadoTipo() {
        if (estadoTipo == null) {
            if (tipo == null) {
                estadoTipo = "NUMBER";

            } else {

                if (tipo.equalsIgnoreCase("NUMBER")) {
                    estadoTipo = "NUMERICO";
                } else if (tipo.equalsIgnoreCase("VARCHAR")) {
                    estadoTipo = "CARACTER";
                } else if (tipo.equalsIgnoreCase("DATE")) {
                    estadoTipo = "FECHA";
                }
            }
        }
        return estadoTipo;
    }

    public void setEstadoTipo(String estadoTipo) {

        if (estadoTipo.equalsIgnoreCase("NUMERICO")) {
            setTipo("NUMBER");
        } else if (estadoTipo.equals("CARACTER")) {
            setTipo("VARCHAR");
        } else if (estadoTipo.equals("FECHA")) {
            setTipo("DATE");
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
        if (!(object instanceof TiposBloques)) {
            return false;
        }
        TiposBloques other = (TiposBloques) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TiposBloques[ secuencia=" + secuencia + " ]";
    }

}
