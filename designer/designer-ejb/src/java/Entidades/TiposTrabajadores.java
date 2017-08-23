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
@Table(name = "TIPOSTRABAJADORES")
public class TiposTrabajadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private Short codigo;
    @Size(max = 1)
    @Column(name = "MODALIDAD")
    private String modalidad;
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 15)
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "NIVELENDEUDAMIENTO")
    private Short nivelendeudamiento;
    @Size(max = 20)
    @Column(name = "BASEENDEUDAMIENTO")
    private String baseendeudamiento;
    @Column(name = "PORCENTAJESML")
    private Short porcentajesml;
    @Column(name = "DERECHODIASVACACIONES")
    private BigInteger derechodiasvacaciones;
    @Column(name = "FACTORDESALARIZACION")
    private BigInteger factordesalarizacion;
    @Column(name = "DIASVACACIONESNOORDINARIOS")
    private BigInteger diasvacacionesnoordinarios;
    @Size(max = 1)
    @Column(name = "CESANTIASECTORCONSTRUCCION")
    private String cesantiasectorconstruccion;
    @Size(max = 1)
    @Column(name = "PATRONPAGASALUD")
    private String patronpagasalud;
    @Size(max = 1)
    @Column(name = "PATRONPAGAPENSION")
    private String patronpagapension;
    @Size(max = 1)
    @Column(name = "PATRONPAGARETENCION")
    private String patronpagaretencion;
    @Size(max = 1)
    @Column(name = "DOCENTECOLEGIO")
    private String docentecolegio;
    @Size(max = 1)
    @Column(name = "MODALIDADPENSIONSECTORSALUD")
    private String modalidadpensionsectorsalud;
    @Size(max = 1)
    @Column(name = "SEMESTREESPECIAL")
    private String semestreespecial;
    @Size(max = 1)
    @Column(name = "PROMEDIABASICOACUMULADOS")
    private String promediabasicoacumulados;
    @JoinColumn(name = "TIPOCOTIZANTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposCotizantes tipocotizante;
    @Transient
    private String modalidadpensionsectorsaludcmp;
    @Transient
    private String patronpagasaludcmp;
    @Transient
    private String patronpagapensioncmp;
    @Transient
    private String patronpagaretencioncmp;
    @Transient
    private String cesantiasectorconstruccioncmp;
    @Transient
    private String modalidadcmp;

    public TiposTrabajadores() {
    }

    public TiposTrabajadores(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposTrabajadores(BigInteger secuencia, short codigo, String nombre) {
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

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Short getNivelendeudamiento() {
        return nivelendeudamiento;
    }

    public void setNivelendeudamiento(Short nivelendeudamiento) {
        this.nivelendeudamiento = nivelendeudamiento;
    }

    public String getBaseendeudamiento() {
        return baseendeudamiento;
    }

    public void setBaseendeudamiento(String baseendeudamiento) {
        this.baseendeudamiento = baseendeudamiento;
    }

    public Short getPorcentajesml() {
        return porcentajesml;
    }

    public void setPorcentajesml(Short porcentajesml) {
        this.porcentajesml = porcentajesml;
    }

    public BigInteger getDerechodiasvacaciones() {
        return derechodiasvacaciones;
    }

    public void setDerechodiasvacaciones(BigInteger derechodiasvacaciones) {
        this.derechodiasvacaciones = derechodiasvacaciones;
    }

    public BigInteger getFactordesalarizacion() {
        return factordesalarizacion;
    }

    public void setFactordesalarizacion(BigInteger factordesalarizacion) {
        this.factordesalarizacion = factordesalarizacion;
    }

    public BigInteger getDiasvacacionesnoordinarios() {
        return diasvacacionesnoordinarios;
    }

    public void setDiasvacacionesnoordinarios(BigInteger diasvacacionesnoordinarios) {
        this.diasvacacionesnoordinarios = diasvacacionesnoordinarios;
    }

    public String getCesantiasectorconstruccion() {
        return cesantiasectorconstruccion;
    }

    public void setCesantiasectorconstruccion(String cesantiasectorconstruccion) {
        this.cesantiasectorconstruccion = cesantiasectorconstruccion;
    }

    public String getPatronpagasalud() {
        return patronpagasalud;
    }

    public void setPatronpagasalud(String patronpagasalud) {
        this.patronpagasalud = patronpagasalud;
    }

    public String getPatronpagapension() {
        return patronpagapension;
    }

    public void setPatronpagapension(String patronpagapension) {
        this.patronpagapension = patronpagapension;
    }

    public String getPatronpagaretencion() {
        return patronpagaretencion;
    }

    public void setPatronpagaretencion(String patronpagaretencion) {
        this.patronpagaretencion = patronpagaretencion;
    }

    public String getDocentecolegio() {
        return docentecolegio;
    }

    public void setDocentecolegio(String docentecolegio) {
        this.docentecolegio = docentecolegio;
    }

    public String getModalidadpensionsectorsalud() {
        return modalidadpensionsectorsalud;
    }

    public void setModalidadpensionsectorsalud(String modalidadpensionsectorsalud) {
        this.modalidadpensionsectorsalud = modalidadpensionsectorsalud;
    }

    public String getSemestreespecial() {
        return semestreespecial;
    }

    public void setSemestreespecial(String semestreespecial) {
        this.semestreespecial = semestreespecial;
    }

    public TiposCotizantes getTipocotizante() {
        return tipocotizante;
    }

    public void setTipocotizante(TiposCotizantes tipocotizante) {
        this.tipocotizante = tipocotizante;
    }

    public String getModalidadpensionsectorsaludcmp() {
        if (getModalidadpensionsectorsalud() == null) {
            modalidadpensionsectorsaludcmp = "";
        } else if (getModalidadpensionsectorsalud().equals("S")) {
            modalidadpensionsectorsaludcmp = "SI";
        } else if (getModalidadpensionsectorsalud().equals("N")) {
            modalidadpensionsectorsaludcmp = "NO";
        } else {
            modalidadpensionsectorsaludcmp = "";
        }
        return modalidadpensionsectorsaludcmp;
    }

    public void setModalidadpensionsectorsaludcmp(String modalidadpensionsectorsaludcmp) {
        this.modalidadpensionsectorsaludcmp = modalidadpensionsectorsaludcmp;
        if (this.modalidadpensionsectorsaludcmp.equals("SI")) {
            setModalidadpensionsectorsalud("S");
        } else if (this.modalidadpensionsectorsaludcmp.equals("NO")) {
            setModalidadpensionsectorsalud("N");
        } else {
            setModalidadpensionsectorsalud("");
        }
    }

    public String getCesantiasectorconstruccioncmp() {
        if (getCesantiasectorconstruccion() == null) {
            cesantiasectorconstruccioncmp = "";
        } else if (getCesantiasectorconstruccion().equals("S")) {
            cesantiasectorconstruccioncmp = "SI";
        } else if (getCesantiasectorconstruccion().equals("N")) {
            cesantiasectorconstruccioncmp = "NO";
        } else {
            cesantiasectorconstruccioncmp = "";
        }
        return cesantiasectorconstruccioncmp;
    }

    public void setCesantiasectorconstruccioncmp(String cesantiasectorconstruccioncmp) {
        this.cesantiasectorconstruccioncmp = cesantiasectorconstruccioncmp;
        if (this.cesantiasectorconstruccioncmp.equals("SI")) {
            setCesantiasectorconstruccion("S");
        } else if (this.cesantiasectorconstruccioncmp.equals("NO")) {
            setCesantiasectorconstruccion("N");
        } else {
            setCesantiasectorconstruccion("");
        }
    }

    public String getPatronpagapensioncmp() {
        if (getPatronpagapension() == null) {
            patronpagapensioncmp = "";
        } else if (getPatronpagapension().equals("S")) {
            patronpagapensioncmp = "SI";
        } else if (getPatronpagapension().equals("N")) {
            patronpagapensioncmp = "NO";
        } else {
            patronpagapensioncmp = "";
        }
        return patronpagapensioncmp;
    }

    public void setPatronpagapensioncmp(String patronpagapensioncmp) {
        this.patronpagapensioncmp = patronpagapensioncmp;
        if (this.patronpagapensioncmp.equals("SI")) {
            setPatronpagapension("S");
        } else if (this.patronpagapensioncmp.equals("NO")) {
            setPatronpagapension("N");
        } else {
            setPatronpagapension("");
        }
    }

    public String getPatronpagaretencioncmp() {
        if (getPatronpagaretencion() == null) {
            patronpagaretencioncmp = "";
        } else if (getPatronpagaretencion().equals("S")) {
            patronpagaretencioncmp = "SI";
        } else if (getPatronpagaretencion().equals("N")) {
            patronpagaretencioncmp = "NO";
        } else {
            patronpagaretencioncmp = "";
        }
        return patronpagaretencioncmp;
    }

    public void setPatronpagaretencioncmp(String patronpagaretencioncmp) {
        this.patronpagaretencioncmp = patronpagaretencioncmp;
        if (this.patronpagaretencioncmp.equals("SI")) {
            setPatronpagaretencion("S");
        } else if (this.patronpagaretencioncmp.equals("NO")) {
            setPatronpagaretencion("N");
        } else {
            setPatronpagaretencion("");
        }
    }

    public String getPatronpagasaludcmp() {
        if (getPatronpagasalud() == null) {
            patronpagasaludcmp = "";
        } else if (getPatronpagasalud().equals("S")) {
            patronpagasaludcmp = "SI";
        } else if (getPatronpagasalud().equals("N")) {
            patronpagasaludcmp = "NO";
        } else {
            patronpagasaludcmp = "";
        }
        return patronpagasaludcmp;
    }

    public void setPatronpagasaludcmp(String patronpagasaludcmp) {
        this.patronpagasaludcmp = patronpagasaludcmp;
        if (this.patronpagasaludcmp.equals("SI")) {
            setPatronpagasalud("S");
        } else if (this.patronpagasaludcmp.equals("NO")) {
            setPatronpagasalud("N");
        } else {
            setPatronpagasalud("");
        }
    }

    public String getModalidadcmp() {
        if (getModalidad() == null) {
            modalidadcmp = "";
        } else if (getModalidad().equals("E")) {
            modalidadcmp = "EMPLEADO";
        } else if (getModalidad().equals("P")) {
            modalidadcmp = "PENSIONADO";
        } else if (getModalidad().equals("C")) {
            modalidadcmp = "DISPONIBLE";
        } else if (getModalidad().equals("L")) {
            modalidadcmp = "LIQUIDADO";
        } else if (getModalidad().equals("U")) {
            modalidadcmp = "UNIVERSITARIO";
        } else if (getModalidad().equals("D")) {
            modalidadcmp = "DESTAJO";
        } else {
            modalidadcmp = "";
        }
        return modalidadcmp;
    }

    public void setModalidadcmp(String modalidadcmp) {
        this.modalidadcmp = modalidadcmp;
        if (this.modalidadcmp.equals("EMPLEADO")) {
            setModalidad("E");
        } else if (this.modalidadcmp.equals("PENSIONADO")) {
            setModalidad("P");
        } else if (this.modalidadcmp.equals("DISPONIBLE")) {
            setModalidad("C");
        } else if (this.modalidadcmp.equals("LIQUIDADO")) {
            setModalidad("L");
        } else if (this.modalidadcmp.equals("UNIVERSITARIO")) {
            setModalidad("U");
        } else if (this.modalidadcmp.equals("DESTAJO")) {
            setModalidad("D");
        } else {
            setModalidad("");
        }
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
        if (!(object instanceof TiposTrabajadores)) {
            return false;
        }
        TiposTrabajadores other = (TiposTrabajadores) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tipostrabajadores[ secuencia=" + secuencia + " ]";
    }

    public String getPromediabasicoacumulados() {
        return promediabasicoacumulados;
    }

    public void setPromediabasicoacumulados(String promediabasicoacumulados) {
        this.promediabasicoacumulados = promediabasicoacumulados;
    }
}
