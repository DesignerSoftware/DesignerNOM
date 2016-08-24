package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "CENTROSCOSTOS")
public class CentrosCostos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "MANOOBRA")
    private String manoobra;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "COMODIN")
    private String comodin;
    @Column(name = "CODIGOALTERNATIVO")
    private String codigoalternativo;
    @Column(name = "OBSOLETO")
    private String obsoleto;
    @Column(name = "CODIGOCTT")
    private String codigoctt;
    @Column(name = "DIMENSIONES")
    private BigInteger dimensiones;
    @JoinColumn(name = "TIPOCENTROCOSTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposCentrosCostos tipocentrocosto;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "CLASERIESGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private ClasesRiesgos claseriesgo;
    @Transient
    private String codigoNombre;
    @Transient
    private String variableManoObra;
    @Transient
    private String variableObsoleto;

    public CentrosCostos() {
    }

    public CentrosCostos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public CentrosCostos(BigInteger secuencia, String codigo, String nombre) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getManoobra() {
        return manoobra;
    }

    public void setManoobra(String manoobra) {
        this.manoobra = manoobra;
    }

    public String getNombre() {
        if (nombre != null) {
            nombre = nombre.toUpperCase();
        } else {
            nombre = "";
        }
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre.toUpperCase();
        } else {
            this.nombre = nombre;
        }
    }

    public String getComodin() {
        return comodin;
    }

    public void setComodin(String comodin) {
        this.comodin = comodin;
    }

    public String getCodigoalternativo() {
        return codigoalternativo;
    }

    public void setCodigoalternativo(String codigoalternativo) {
        this.codigoalternativo = codigoalternativo;
    }

    public String getObsoleto() {
        return obsoleto;
    }

    public void setObsoleto(String obsoleto) {
        this.obsoleto = obsoleto;
    }

    public String getCodigoctt() {
        return codigoctt;
    }

    public void setCodigoctt(String codigoctt) {
        this.codigoctt = codigoctt;
    }

    public BigInteger getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(BigInteger dimensiones) {
        this.dimensiones = dimensiones;
    }

    public String getCodigoNombre() {
        if (codigoNombre == null) {
            if (this.codigo == null && this.nombre == null) {
                this.codigoNombre = null;
            } else {
                this.codigoNombre = this.codigo + "-" + this.nombre;
            }
        }
        return codigoNombre;
    }

    public void setCodigoNombre(String codigoNombre) {
        this.codigoNombre = codigoNombre;
    }

    public TiposCentrosCostos getTipocentrocosto() {
        return tipocentrocosto;
    }

    public void setTipocentrocosto(TiposCentrosCostos tipocentrocosto) {
        this.tipocentrocosto = tipocentrocosto;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public ClasesRiesgos getClaseriesgo() {
        return claseriesgo;
    }

    public void setClaseriesgo(ClasesRiesgos claseriesgo) {
        this.claseriesgo = claseriesgo;
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
        if (!(object instanceof CentrosCostos)) {
            return false;
        }
        CentrosCostos other = (CentrosCostos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Centroscostos[ secuencia=" + secuencia + " ]";
    }

    public String getVariableManoObra() {
        if (variableManoObra == null) {
            if (manoobra == null) {
                variableManoObra = " ";
            } else {
                if (manoobra.equalsIgnoreCase("D")) {
                    variableManoObra = "DIRECTA";
                } else if (manoobra.equalsIgnoreCase("I")) {
                    variableManoObra = "INDIRECTA";
                }
            }
        }
        return variableManoObra;
    }

    public void setVariableManoObra(String variableManoObra) {
        this.variableManoObra = variableManoObra;
    }

    public String getVariableObsoleto() {
        if (variableObsoleto == null) {
            if (obsoleto == null) {
                variableObsoleto = " ";
            } else {
                if (obsoleto.equalsIgnoreCase("S")) {
                    variableObsoleto = "SI";
                } else if (obsoleto.equalsIgnoreCase("N")) {
                    variableObsoleto = "NO";
                }
            }
        }
        return variableObsoleto;
    }

    public void setVariableObsoleto(String variableObsoleto) {
        this.variableObsoleto = variableObsoleto;
    }
}
