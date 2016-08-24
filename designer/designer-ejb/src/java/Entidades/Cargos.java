package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "CARGOS")
public class Cargos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "SUELDOMAXIMO")
    private BigDecimal sueldomaximo;
    @Column(name = "SUELDOMINIMO")
    private BigDecimal sueldominimo;
    @Size(max = 1)
    @Column(name = "TURNOROTATIVO")
    private String turnorotativo;
    @Column(name = "SUELDOMERCADO")
    private BigDecimal sueldomercado;
    @Size(max = 1)
    @Column(name = "JEFE")
    private String jefe;
    @Column(name = "CODIGOALTERNATIVO")
    private String codigoalternativo;
    @JoinColumn(name = "PROCESOPRODUCTIVO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private ProcesosProductivos procesoproductivo;
    @JoinColumn(name = "GRUPOVIATICO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposViaticos grupoviatico;
    @JoinColumn(name = "GRUPOSALARIAL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposSalariales gruposalarial;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @Transient
    private BigDecimal sueldoCargo;
    @Transient
    private boolean checkCargoRotativo;
    @Transient
    private String strCargoRotativo;
    @Transient
    private boolean chechJefe;
    @Transient
    private String strJefe;

    public Cargos() {
    }

    public Cargos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Cargos(BigInteger secuencia, String nombre) {
        this.secuencia = secuencia;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        if (nombre != null) {
            return nombre.toUpperCase();
        } else {
            return nombre;
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSueldomaximo() {
        return sueldomaximo;
    }

    public void setSueldomaximo(BigDecimal sueldomaximo) {
        this.sueldomaximo = sueldomaximo;
    }

    public BigDecimal getSueldominimo() {
        return sueldominimo;
    }

    public void setSueldominimo(BigDecimal sueldominimo) {
        this.sueldominimo = sueldominimo;
    }

    public String getTurnorotativo() {
        if (turnorotativo == null) {
            turnorotativo = "N";
        }
        return turnorotativo;
    }

    public void setTurnorotativo(String turnorotativo) {
        this.turnorotativo = turnorotativo;
    }

    public BigDecimal getSueldomercado() {
        return sueldomercado;
    }

    public void setSueldomercado(BigDecimal sueldomercado) {
        this.sueldomercado = sueldomercado;
    }

    public String getJefe() {
        if (jefe == null) {
            jefe = "N";
        }
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public String getCodigoalternativo() {
        return codigoalternativo;
    }

    public void setCodigoalternativo(String codigoalternativo) {
        this.codigoalternativo = codigoalternativo;
    }

    public ProcesosProductivos getProcesoproductivo() {
        return procesoproductivo;
    }

    public void setProcesoproductivo(ProcesosProductivos procesoproductivo) {
        this.procesoproductivo = procesoproductivo;
    }

    public GruposViaticos getGrupoviatico() {
        return grupoviatico;
    }

    public void setGrupoviatico(GruposViaticos grupoviatico) {
        this.grupoviatico = grupoviatico;
    }

    public GruposSalariales getGruposalarial() {
        return gruposalarial;
    }

    public void setGruposalarial(GruposSalariales gruposalarial) {
        this.gruposalarial = gruposalarial;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public boolean isCheckCargoRotativo() {
        getTurnorotativo();
        if (turnorotativo == null || turnorotativo.equalsIgnoreCase("N")) {
            checkCargoRotativo = false;
        } else {
            checkCargoRotativo = true;
        }
        return checkCargoRotativo;
    }

    public void setCheckCargoRotativo(boolean checkCargoRotativo) {
        if (checkCargoRotativo == false) {
            turnorotativo = "N";
        } else {
            turnorotativo = "S";
        }
        this.checkCargoRotativo = checkCargoRotativo;
    }

    public String getStrCargoRotativo() {
        getTurnorotativo();
        if (turnorotativo == null || turnorotativo.equalsIgnoreCase("N")) {
            strCargoRotativo = "NO";
        } else {
            strCargoRotativo = "SI";
        }
        return strCargoRotativo;
    }

    public void setStrCargoRotativo(String strCargoRotativo) {
        this.strCargoRotativo = strCargoRotativo;
    }

    public boolean isChechJefe() {
        getJefe();
        if (jefe == null || jefe.equalsIgnoreCase("N")) {
            chechJefe = false;
        } else {
            chechJefe = true;
        }
        return chechJefe;
    }

    public void setChechJefe(boolean chechJefe) {
        if (chechJefe == false) {
            jefe = "N";
        } else {
            jefe = "S";
        }
        this.chechJefe = chechJefe;
    }

    public String getStrJefe() {
        getJefe();
        if (jefe == null || jefe.equalsIgnoreCase("N")) {
            strJefe = "NO";
        } else {
            strJefe = "SI";
        }
        return strJefe;
    }

    public void setStrJefe(String strJefe) {
        this.strJefe = strJefe;
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
        if (!(object instanceof Cargos)) {
            return false;
        }
        Cargos other = (Cargos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Cargos[ secuencia=" + secuencia + " ]";
    }

    public BigDecimal getSueldoCargo() {
        return sueldoCargo;
    }

    public void setSueldoCargo(BigDecimal sueldoCargo) {
        this.sueldoCargo = sueldoCargo;
    }
}
