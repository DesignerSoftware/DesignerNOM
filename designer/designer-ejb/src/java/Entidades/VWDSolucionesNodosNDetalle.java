/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entidades;

import InterfacePersistencia.PersistenciaEmpleadoInterface;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.ejb.EJB;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */

@Entity
@Cacheable(false)
public class VWDSolucionesNodosNDetalle implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMPLEADO")
    private BigDecimal secuenciaEmpleado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DIMENSION")
    private String dimension;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "Z")
    @Temporal(TemporalType.DATE)
    private Date z;
    @Column(name = "VALOR")
    private BigInteger valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "X")
    private BigDecimal secuenciaConcepto;
    @Column(name = "NOMBRECONCEPTO")
    private String nombreConcepto;
    @Column(name = "Y")
    private BigDecimal secFiltro;
    @Column(name = "UNIDADES")
    private BigDecimal unidades;
    @Column(name = "NOMBREEMPLEADO")
    private String nombreEmpleado;
    @Column(name = "CODIGOEMPLEADO")
    private BigDecimal codigoEmpleado;
    @Transient
    private BigDecimal unidadesActivo;
    
    @Column(name = "CONJUNTO1")
    private BigInteger conjunto1;
    @Column(name = "CONJUNTO2")
    private BigInteger conjunto2;
    @Column(name = "CONJUNTO3")
    private BigInteger conjunto3;
    @Column(name = "CONJUNTO4")
    private BigInteger conjunto4;
    @Column(name = "CONJUNTO5")
    private BigInteger conjunto5;
    @Column(name = "CONJUNTO6")
    private BigInteger conjunto6;
    @Column(name = "CONJUNTO7")
    private BigInteger conjunto7;
    @Column(name = "CONJUNTO8")
    private BigInteger conjunto8;
    @Column(name = "CONJUNTO9")
    private BigInteger conjunto9;
    @Column(name = "CONJUNTO10")
    private BigInteger conjunto10;
    @Column(name = "CONJUNTO11")
    private BigInteger conjunto11;
    @Column(name = "CONJUNTO12")
    private BigInteger conjunto12;
    @Column(name = "CONJUNTO13")
    private BigInteger conjunto13;
    @Column(name = "CONJUNTO14")
    private BigInteger conjunto14;
    @Column(name = "CONJUNTO15")
    private BigInteger conjunto15;
    @Column(name = "CONJUNTO16")
    private BigInteger conjunto16;
    @Column(name = "CONJUNTO17")
    private BigInteger conjunto17;
    @Column(name = "CONJUNTO18")
    private BigInteger conjunto18;
    @Column(name = "CONJUNTO19")
    private BigInteger conjunto19;
    @Column(name = "CONJUNTO20")
    private BigInteger conjunto20;
    @Column(name = "CONJUNTO21")
    private BigInteger conjunto21;
    @Column(name = "CONJUNTO22")
    private BigInteger conjunto22;
    @Column(name = "CONJUNTO23")
    private BigInteger conjunto23;
    @Column(name = "CONJUNTO24")
    private BigInteger conjunto24;
    @Column(name = "CONJUNTO25")
    private BigInteger conjunto25;
    @Column(name = "CONJUNTO26")
    private BigInteger conjunto26;
    @Column(name = "CONJUNTO27")
    private BigInteger conjunto27;
    @Column(name = "CONJUNTO28")
    private BigInteger conjunto28;
    @Column(name = "CONJUNTO29")
    private BigInteger conjunto29;
    @Column(name = "CONJUNTO30")
    private BigInteger conjunto30;
    @Column(name = "CONJUNTO31")
    private BigInteger conjunto31;
    @Column(name = "CONJUNTO32")
    private BigInteger conjunto32;
    @Column(name = "CONJUNTO33")
    private BigInteger conjunto33;
    @Column(name = "CONJUNTO34")
    private BigInteger conjunto34;
    @Column(name = "CONJUNTO35")
    private BigInteger conjunto35;
    @Column(name = "CONJUNTO36")
    private BigInteger conjunto36;
    @Column(name = "CONJUNTO37")
    private BigInteger conjunto37;
    @Column(name = "CONJUNTO38")
    private BigInteger conjunto38;
    @Column(name = "CONJUNTO39")
    private BigInteger conjunto39;
    @Column(name = "CONJUNTO40")
    private BigInteger conjunto40;
    @Column(name = "CONJUNTO41")
    private BigInteger conjunto41;
    @Column(name = "CONJUNTO42")
    private BigInteger conjunto42;
    @Column(name = "CONJUNTO43")
    private BigInteger conjunto43;
    @Column(name = "CONJUNTO44")
    private BigInteger conjunto44;
    @Column(name = "CONJUNTO45")
    private BigInteger conjunto45;
    
    @Column(name = "UNIDADESCONJUNTO1")
    private BigDecimal unidadConjunto1;
    @Column(name = "UNIDADESCONJUNTO2")
    private BigDecimal unidadConjunto2;
    @Column(name = "UNIDADESCONJUNTO3")
    private BigDecimal unidadConjunto3;
    @Column(name = "UNIDADESCONJUNTO4")
    private BigDecimal unidadConjunto4;
    @Column(name = "UNIDADESCONJUNTO5")
    private BigDecimal unidadConjunto5;
    @Column(name = "UNIDADESCONJUNTO6")
    private BigDecimal unidadConjunto6;
    @Column(name = "UNIDADESCONJUNTO7")
    private BigDecimal unidadConjunto7;
    @Column(name = "UNIDADESCONJUNTO8")
    private BigDecimal unidadConjunto8;
    @Column(name = "UNIDADESCONJUNTO9")
    private BigDecimal unidadConjunto9;
    @Column(name = "UNIDADESCONJUNTO10")
    private BigDecimal unidadConjunto10;
    @Column(name = "UNIDADESCONJUNTO11")
    private BigDecimal unidadConjunto11;
    @Column(name = "UNIDADESCONJUNTO12")
    private BigDecimal unidadConjunto12;
    @Column(name = "UNIDADESCONJUNTO13")
    private BigDecimal unidadConjunto13;
    @Column(name = "UNIDADESCONJUNTO14")
    private BigDecimal unidadConjunto14;
    @Column(name = "UNIDADESCONJUNTO15")
    private BigDecimal unidadConjunto15;
    @Column(name = "UNIDADESCONJUNTO16")
    private BigDecimal unidadConjunto16;
    @Column(name = "UNIDADESCONJUNTO17")
    private BigDecimal unidadConjunto17;
    @Column(name = "UNIDADESCONJUNTO18")
    private BigDecimal unidadConjunto18;
    @Column(name = "UNIDADESCONJUNTO19")
    private BigDecimal unidadConjunto19;
    @Column(name = "UNIDADESCONJUNTO20")
    private BigDecimal unidadConjunto20;
    @Column(name = "UNIDADESCONJUNTO21")
    private BigDecimal unidadConjunto21;
    @Column(name = "UNIDADESCONJUNTO22")
    private BigDecimal unidadConjunto22;
    @Column(name = "UNIDADESCONJUNTO23")
    private BigDecimal unidadConjunto23;
    @Column(name = "UNIDADESCONJUNTO24")
    private BigDecimal unidadConjunto24;
    @Column(name = "UNIDADESCONJUNTO25")
    private BigDecimal unidadConjunto25;
    @Column(name = "UNIDADESCONJUNTO26")
    private BigDecimal unidadConjunto26;
    @Column(name = "UNIDADESCONJUNTO27")
    private BigDecimal unidadConjunto27;
    @Column(name = "UNIDADESCONJUNTO28")
    private BigDecimal unidadConjunto28;
    @Column(name = "UNIDADESCONJUNTO29")
    private BigDecimal unidadConjunto29;
    @Column(name = "UNIDADESCONJUNTO30")
    private BigDecimal unidadConjunto30;
    @Column(name = "UNIDADESCONJUNTO31")
    private BigDecimal unidadConjunto31;
    @Column(name = "UNIDADESCONJUNTO32")
    private BigDecimal unidadConjunto32;
    @Column(name = "UNIDADESCONJUNTO33")
    private BigDecimal unidadConjunto33;
    @Column(name = "UNIDADESCONJUNTO34")
    private BigDecimal unidadConjunto34;
    @Column(name = "UNIDADESCONJUNTO35")
    private BigDecimal unidadConjunto35;
    @Column(name = "UNIDADESCONJUNTO36")
    private BigDecimal unidadConjunto36;
    @Column(name = "UNIDADESCONJUNTO37")
    private BigDecimal unidadConjunto37;
    @Column(name = "UNIDADESCONJUNTO38")
    private BigDecimal unidadConjunto38;
    @Column(name = "UNIDADESCONJUNTO39")
    private BigDecimal unidadConjunto39;
    @Column(name = "UNIDADESCONJUNTO40")
    private BigDecimal unidadConjunto40;
    @Column(name = "UNIDADESCONJUNTO41")
    private BigDecimal unidadConjunto41;
    @Column(name = "UNIDADESCONJUNTO42")
    private BigDecimal unidadConjunto42;
    @Column(name = "UNIDADESCONJUNTO43")
    private BigDecimal unidadConjunto43;
    @Column(name = "UNIDADESCONJUNTO44")
    private BigDecimal unidadConjunto44;
    @Column(name = "UNIDADESCONJUNTO45")
    private BigDecimal unidadConjunto45;

    public BigDecimal getSecuenciaEmpleado() {
        return secuenciaEmpleado;
    }

    public void setSecuenciaEmpleado(BigDecimal SecuenciaEmpleado) {
        this.secuenciaEmpleado = SecuenciaEmpleado;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getZ() {
        return z;
    }

    public void setZ(Date z) {
        this.z = z;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

    public BigInteger getConjunto1() {
        return conjunto1;
    }

    public void setConjunto1(BigInteger conjunto1) {
        this.conjunto1 = conjunto1;
    }

    public BigInteger getConjunto2() {
        return conjunto2;
    }

    public void setConjunto2(BigInteger conjunto2) {
        this.conjunto2 = conjunto2;
    }

    public BigInteger getConjunto3() {
        return conjunto3;
    }

    public void setConjunto3(BigInteger conjunto3) {
        this.conjunto3 = conjunto3;
    }

    public BigInteger getConjunto4() {
        return conjunto4;
    }

    public void setConjunto4(BigInteger conjunto4) {
        this.conjunto4 = conjunto4;
    }

    public BigInteger getConjunto5() {
        return conjunto5;
    }

    public void setConjunto5(BigInteger conjunto5) {
        this.conjunto5 = conjunto5;
    }

    public BigInteger getConjunto6() {
        return conjunto6;
    }

    public void setConjunto6(BigInteger conjunto6) {
        this.conjunto6 = conjunto6;
    }

    public BigInteger getConjunto7() {
        return conjunto7;
    }

    public void setConjunto7(BigInteger conjunto7) {
        this.conjunto7 = conjunto7;
    }

    public BigInteger getConjunto8() {
        return conjunto8;
    }

    public void setConjunto8(BigInteger conjunto8) {
        this.conjunto8 = conjunto8;
    }

    public BigInteger getConjunto9() {
        return conjunto9;
    }

    public void setConjunto9(BigInteger conjunto9) {
        this.conjunto9 = conjunto9;
    }

    public BigInteger getConjunto10() {
        return conjunto10;
    }

    public void setConjunto10(BigInteger conjunto10) {
        this.conjunto10 = conjunto10;
    }

    public BigInteger getConjunto11() {
        return conjunto11;
    }

    public void setConjunto11(BigInteger conjunto11) {
        this.conjunto11 = conjunto11;
    }

    public BigInteger getConjunto12() {
        return conjunto12;
    }

    public void setConjunto12(BigInteger conjunto12) {
        this.conjunto12 = conjunto12;
    }

    public BigInteger getConjunto13() {
        return conjunto13;
    }

    public void setConjunto13(BigInteger conjunto13) {
        this.conjunto13 = conjunto13;
    }

    public BigInteger getConjunto14() {
        return conjunto14;
    }

    public void setConjunto14(BigInteger conjunto14) {
        this.conjunto14 = conjunto14;
    }

    public BigInteger getConjunto15() {
        return conjunto15;
    }

    public void setConjunto15(BigInteger conjunto15) {
        this.conjunto15 = conjunto15;
    }

    public BigInteger getConjunto16() {
        return conjunto16;
    }

    public void setConjunto16(BigInteger conjunto16) {
        this.conjunto16 = conjunto16;
    }

    public BigInteger getConjunto17() {
        return conjunto17;
    }

    public void setConjunto17(BigInteger conjunto17) {
        this.conjunto17 = conjunto17;
    }

    public BigInteger getConjunto18() {
        return conjunto18;
    }

    public void setConjunto18(BigInteger conjunto18) {
        this.conjunto18 = conjunto18;
    }

    public BigInteger getConjunto19() {
        return conjunto19;
    }

    public void setConjunto19(BigInteger conjunto19) {
        this.conjunto19 = conjunto19;
    }

    public BigInteger getConjunto20() {
        return conjunto20;
    }

    public void setConjunto20(BigInteger conjunto20) {
        this.conjunto20 = conjunto20;
    }

    public BigInteger getConjunto21() {
        return conjunto21;
    }

    public void setConjunto21(BigInteger conjunto21) {
        this.conjunto21 = conjunto21;
    }

    public BigInteger getConjunto22() {
        return conjunto22;
    }

    public void setConjunto22(BigInteger conjunto22) {
        this.conjunto22 = conjunto22;
    }

    public BigInteger getConjunto23() {
        return conjunto23;
    }

    public void setConjunto23(BigInteger conjunto23) {
        this.conjunto23 = conjunto23;
    }

    public BigInteger getConjunto24() {
        return conjunto24;
    }

    public void setConjunto24(BigInteger conjunto24) {
        this.conjunto24 = conjunto24;
    }

    public BigInteger getConjunto25() {
        return conjunto25;
    }

    public void setConjunto25(BigInteger conjunto25) {
        this.conjunto25 = conjunto25;
    }

    public BigInteger getConjunto26() {
        return conjunto26;
    }

    public void setConjunto26(BigInteger conjunto26) {
        this.conjunto26 = conjunto26;
    }

    public BigInteger getConjunto27() {
        return conjunto27;
    }

    public void setConjunto27(BigInteger conjunto27) {
        this.conjunto27 = conjunto27;
    }

    public BigInteger getConjunto28() {
        return conjunto28;
    }

    public void setConjunto28(BigInteger conjunto28) {
        this.conjunto28 = conjunto28;
    }

    public BigInteger getConjunto29() {
        return conjunto29;
    }

    public void setConjunto29(BigInteger conjunto29) {
        this.conjunto29 = conjunto29;
    }

    public BigInteger getConjunto30() {
        return conjunto30;
    }

    public void setConjunto30(BigInteger conjunto30) {
        this.conjunto30 = conjunto30;
    }

    public BigInteger getConjunto31() {
        return conjunto31;
    }

    public void setConjunto31(BigInteger conjunto31) {
        this.conjunto31 = conjunto31;
    }

    public BigInteger getConjunto32() {
        return conjunto32;
    }

    public void setConjunto32(BigInteger conjunto32) {
        this.conjunto32 = conjunto32;
    }

    public BigInteger getConjunto33() {
        return conjunto33;
    }

    public void setConjunto33(BigInteger conjunto33) {
        this.conjunto33 = conjunto33;
    }

    public BigInteger getConjunto34() {
        return conjunto34;
    }

    public void setConjunto34(BigInteger conjunto34) {
        this.conjunto34 = conjunto34;
    }

    public BigInteger getConjunto35() {
        return conjunto35;
    }

    public void setConjunto35(BigInteger conjunto35) {
        this.conjunto35 = conjunto35;
    }

    public BigInteger getConjunto36() {
        return conjunto36;
    }

    public void setConjunto36(BigInteger conjunto36) {
        this.conjunto36 = conjunto36;
    }

    public BigInteger getConjunto37() {
        return conjunto37;
    }

    public void setConjunto37(BigInteger conjunto37) {
        this.conjunto37 = conjunto37;
    }

    public BigInteger getConjunto38() {
        return conjunto38;
    }

    public void setConjunto38(BigInteger conjunto38) {
        this.conjunto38 = conjunto38;
    }

    public BigInteger getConjunto39() {
        return conjunto39;
    }

    public void setConjunto39(BigInteger conjunto39) {
        this.conjunto39 = conjunto39;
    }

    public BigInteger getConjunto40() {
        return conjunto40;
    }

    public void setConjunto40(BigInteger conjunto40) {
        this.conjunto40 = conjunto40;
    }

    public BigInteger getConjunto41() {
        return conjunto41;
    }

    public void setConjunto41(BigInteger conjunto41) {
        this.conjunto41 = conjunto41;
    }

    public BigInteger getConjunto42() {
        return conjunto42;
    }

    public void setConjunto42(BigInteger conjunto42) {
        this.conjunto42 = conjunto42;
    }

    public BigInteger getConjunto43() {
        return conjunto43;
    }

    public void setConjunto43(BigInteger conjunto43) {
        this.conjunto43 = conjunto43;
    }

    public BigInteger getConjunto44() {
        return conjunto44;
    }

    public void setConjunto44(BigInteger conjunto44) {
        this.conjunto44 = conjunto44;
    }

    public BigInteger getConjunto45() {
        return conjunto45;
    }

    public void setConjunto45(BigInteger conjunto45) {
        this.conjunto45 = conjunto45;
    }

    public BigDecimal getUnidadConjunto1() {
        return unidadConjunto1;
    }

    public void setUnidadConjunto1(BigDecimal unidadConjunto1) {
        this.unidadConjunto1 = unidadConjunto1;
    }

    public BigDecimal getUnidadConjunto2() {
        return unidadConjunto2;
    }

    public void setUnidadConjunto2(BigDecimal unidadConjunto2) {
        this.unidadConjunto2 = unidadConjunto2;
    }

    public BigDecimal getUnidadConjunto3() {
        return unidadConjunto3;
    }

    public void setUnidadConjunto3(BigDecimal unidadConjunto3) {
        this.unidadConjunto3 = unidadConjunto3;
    }

    public BigDecimal getUnidadConjunto4() {
        return unidadConjunto4;
    }

    public void setUnidadConjunto4(BigDecimal unidadConjunto4) {
        this.unidadConjunto4 = unidadConjunto4;
    }

    public BigDecimal getUnidadConjunto5() {
        return unidadConjunto5;
    }

    public void setUnidadConjunto5(BigDecimal unidadConjunto5) {
        this.unidadConjunto5 = unidadConjunto5;
    }

    public BigDecimal getUnidadConjunto6() {
        return unidadConjunto6;
    }

    public void setUnidadConjunto6(BigDecimal unidadConjunto6) {
        this.unidadConjunto6 = unidadConjunto6;
    }

    public BigDecimal getUnidadConjunto7() {
        return unidadConjunto7;
    }

    public void setUnidadConjunto7(BigDecimal unidadConjunto7) {
        this.unidadConjunto7 = unidadConjunto7;
    }

    public BigDecimal getUnidadConjunto8() {
        return unidadConjunto8;
    }

    public void setUnidadConjunto8(BigDecimal unidadConjunto8) {
        this.unidadConjunto8 = unidadConjunto8;
    }

    public BigDecimal getUnidadConjunto9() {
        return unidadConjunto9;
    }

    public void setUnidadConjunto9(BigDecimal unidadConjunto9) {
        this.unidadConjunto9 = unidadConjunto9;
    }

    public BigDecimal getUnidadConjunto10() {
        return unidadConjunto10;
    }

    public void setUnidadConjunto10(BigDecimal unidadConjunto10) {
        this.unidadConjunto10 = unidadConjunto10;
    }

    public BigDecimal getUnidadConjunto11() {
        return unidadConjunto11;
    }

    public void setUnidadConjunto11(BigDecimal unidadConjunto11) {
        this.unidadConjunto11 = unidadConjunto11;
    }

    public BigDecimal getUnidadConjunto12() {
        return unidadConjunto12;
    }

    public void setUnidadConjunto12(BigDecimal unidadConjunto12) {
        this.unidadConjunto12 = unidadConjunto12;
    }

    public BigDecimal getUnidadConjunto13() {
        return unidadConjunto13;
    }

    public void setUnidadConjunto13(BigDecimal unidadConjunto13) {
        this.unidadConjunto13 = unidadConjunto13;
    }

    public BigDecimal getUnidadConjunto14() {
        return unidadConjunto14;
    }

    public void setUnidadConjunto14(BigDecimal unidadConjunto14) {
        this.unidadConjunto14 = unidadConjunto14;
    }

    public BigDecimal getUnidadConjunto15() {
        return unidadConjunto15;
    }

    public void setUnidadConjunto15(BigDecimal unidadConjunto15) {
        this.unidadConjunto15 = unidadConjunto15;
    }

    public BigDecimal getUnidadConjunto16() {
        return unidadConjunto16;
    }

    public void setUnidadConjunto16(BigDecimal unidadConjunto16) {
        this.unidadConjunto16 = unidadConjunto16;
    }

    public BigDecimal getUnidadConjunto17() {
        return unidadConjunto17;
    }

    public void setUnidadConjunto17(BigDecimal unidadConjunto17) {
        this.unidadConjunto17 = unidadConjunto17;
    }

    public BigDecimal getUnidadConjunto18() {
        return unidadConjunto18;
    }

    public void setUnidadConjunto18(BigDecimal unidadConjunto18) {
        this.unidadConjunto18 = unidadConjunto18;
    }

    public BigDecimal getUnidadConjunto19() {
        return unidadConjunto19;
    }

    public void setUnidadConjunto19(BigDecimal unidadConjunto19) {
        this.unidadConjunto19 = unidadConjunto19;
    }

    public BigDecimal getUnidadConjunto20() {
        return unidadConjunto20;
    }

    public void setUnidadConjunto20(BigDecimal unidadConjunto20) {
        this.unidadConjunto20 = unidadConjunto20;
    }

    public BigDecimal getUnidadConjunto21() {
        return unidadConjunto21;
    }

    public void setUnidadConjunto21(BigDecimal unidadConjunto21) {
        this.unidadConjunto21 = unidadConjunto21;
    }

    public BigDecimal getUnidadConjunto22() {
        return unidadConjunto22;
    }

    public void setUnidadConjunto22(BigDecimal unidadConjunto22) {
        this.unidadConjunto22 = unidadConjunto22;
    }

    public BigDecimal getUnidadConjunto23() {
        return unidadConjunto23;
    }

    public void setUnidadConjunto23(BigDecimal unidadConjunto23) {
        this.unidadConjunto23 = unidadConjunto23;
    }

    public BigDecimal getUnidadConjunto24() {
        return unidadConjunto24;
    }

    public void setUnidadConjunto24(BigDecimal unidadConjunto24) {
        this.unidadConjunto24 = unidadConjunto24;
    }

    public BigDecimal getUnidadConjunto25() {
        return unidadConjunto25;
    }

    public void setUnidadConjunto25(BigDecimal unidadConjunto25) {
        this.unidadConjunto25 = unidadConjunto25;
    }

    public BigDecimal getUnidadConjunto26() {
        return unidadConjunto26;
    }

    public void setUnidadConjunto26(BigDecimal unidadConjunto26) {
        this.unidadConjunto26 = unidadConjunto26;
    }

    public BigDecimal getUnidadConjunto27() {
        return unidadConjunto27;
    }

    public void setUnidadConjunto27(BigDecimal unidadConjunto27) {
        this.unidadConjunto27 = unidadConjunto27;
    }

    public BigDecimal getUnidadConjunto28() {
        return unidadConjunto28;
    }

    public void setUnidadConjunto28(BigDecimal unidadConjunto28) {
        this.unidadConjunto28 = unidadConjunto28;
    }

    public BigDecimal getUnidadConjunto29() {
        return unidadConjunto29;
    }

    public void setUnidadConjunto29(BigDecimal unidadConjunto29) {
        this.unidadConjunto29 = unidadConjunto29;
    }

    public BigDecimal getUnidadConjunto30() {
        return unidadConjunto30;
    }

    public void setUnidadConjunto30(BigDecimal unidadConjunto30) {
        this.unidadConjunto30 = unidadConjunto30;
    }

    public BigDecimal getUnidadConjunto31() {
        return unidadConjunto31;
    }

    public void setUnidadConjunto31(BigDecimal unidadConjunto31) {
        this.unidadConjunto31 = unidadConjunto31;
    }

    public BigDecimal getUnidadConjunto32() {
        return unidadConjunto32;
    }

    public void setUnidadConjunto32(BigDecimal unidadConjunto32) {
        this.unidadConjunto32 = unidadConjunto32;
    }

    public BigDecimal getUnidadConjunto33() {
        return unidadConjunto33;
    }

    public void setUnidadConjunto33(BigDecimal unidadConjunto33) {
        this.unidadConjunto33 = unidadConjunto33;
    }

    public BigDecimal getUnidadConjunto34() {
        return unidadConjunto34;
    }

    public void setUnidadConjunto34(BigDecimal unidadConjunto34) {
        this.unidadConjunto34 = unidadConjunto34;
    }

    public BigDecimal getUnidadConjunto35() {
        return unidadConjunto35;
    }

    public void setUnidadConjunto35(BigDecimal unidadConjunto35) {
        this.unidadConjunto35 = unidadConjunto35;
    }

    public BigDecimal getUnidadConjunto36() {
        return unidadConjunto36;
    }

    public void setUnidadConjunto36(BigDecimal unidadConjunto36) {
        this.unidadConjunto36 = unidadConjunto36;
    }

    public BigDecimal getUnidadConjunto37() {
        return unidadConjunto37;
    }

    public void setUnidadConjunto37(BigDecimal unidadConjunto37) {
        this.unidadConjunto37 = unidadConjunto37;
    }

    public BigDecimal getUnidadConjunto38() {
        return unidadConjunto38;
    }

    public void setUnidadConjunto38(BigDecimal unidadConjunto38) {
        this.unidadConjunto38 = unidadConjunto38;
    }

    public BigDecimal getUnidadConjunto39() {
        return unidadConjunto39;
    }

    public void setUnidadConjunto39(BigDecimal unidadConjunto39) {
        this.unidadConjunto39 = unidadConjunto39;
    }

    public BigDecimal getUnidadConjunto40() {
        return unidadConjunto40;
    }

    public void setUnidadConjunto40(BigDecimal unidadConjunto40) {
        this.unidadConjunto40 = unidadConjunto40;
    }

    public BigDecimal getUnidadConjunto41() {
        return unidadConjunto41;
    }

    public void setUnidadConjunto41(BigDecimal unidadConjunto41) {
        this.unidadConjunto41 = unidadConjunto41;
    }

    public BigDecimal getUnidadConjunto42() {
        return unidadConjunto42;
    }

    public void setUnidadConjunto42(BigDecimal unidadConjunto42) {
        this.unidadConjunto42 = unidadConjunto42;
    }

    public BigDecimal getUnidadConjunto43() {
        return unidadConjunto43;
    }

    public void setUnidadConjunto43(BigDecimal unidadConjunto43) {
        this.unidadConjunto43 = unidadConjunto43;
    }

    public BigDecimal getUnidadConjunto44() {
        return unidadConjunto44;
    }

    public void setUnidadConjunto44(BigDecimal unidadConjunto44) {
        this.unidadConjunto44 = unidadConjunto44;
    }

    public BigDecimal getUnidadConjunto45() {
        return unidadConjunto45;
    }

    public void setUnidadConjunto45(BigDecimal unidadConjunto45) {
        this.unidadConjunto45 = unidadConjunto45;
    }

    public BigDecimal getSecuenciaConcepto() {
        return secuenciaConcepto;
    }

    public void setSecuenciaConcepto(BigDecimal secuenciaConcepto) {
        this.secuenciaConcepto = secuenciaConcepto;
    }

    public BigDecimal getSecFiltro() {
        return secFiltro;
    }

    public void setSecFiltro(BigDecimal secFiltro) {
        this.secFiltro = secFiltro;
    }

    public BigDecimal getUnidades() {
        return unidades;
    }

    public void setUnidades(BigDecimal unidades) {
        this.unidades = unidades;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public BigDecimal getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(BigDecimal codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getNombreConcepto() {
        return nombreConcepto;
    }

    public void setNombreConcepto(String nombreConcepto) {
        this.nombreConcepto = nombreConcepto;
    }

    public BigDecimal getUnidadesActivo() {
        if(unidadesActivo == null){
            unidadesActivo = new BigDecimal(0);
        }
        return unidadesActivo;
    }

    public void setUnidadesActivo(BigDecimal unidadesActivo) {
        this.unidadesActivo = unidadesActivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuenciaEmpleado != null ? secuenciaEmpleado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VWDSolucionesNodosNDetalle)) {
            return false;
        }
        VWDSolucionesNodosNDetalle other = (VWDSolucionesNodosNDetalle) object;
        if ((this.secuenciaEmpleado == null && other.secuenciaEmpleado != null) || (this.secuenciaEmpleado != null && !this.secuenciaEmpleado.equals(other.secuenciaEmpleado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades."+this.getClass().getName()+"[ id=" + secuenciaEmpleado + " ]";
    }
    
}
