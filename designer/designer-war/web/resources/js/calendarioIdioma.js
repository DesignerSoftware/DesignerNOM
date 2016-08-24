PrimeFaces.locales['es'] = {
    closeText: 'Cerrar',
    prevText: 'Anterior',
    nextText: 'Siguiente',
    monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
    dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
    dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
    dayNamesMin: ['D', 'L', 'M', 'Mi', 'J', 'V', 'S'],
    weekHeader: 'Semana',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Sólo hora',
    timeText: 'Tiempo',
    hourText: 'Hora',
    minuteText: 'Minuto',
    secondText: 'Segundo',
    currentText: 'Fecha actual',
    ampm: false,
    month: 'Mes',
    week: 'Semana',
    day: 'Día',
    allDayText: 'Todo el día'
};

PrimeFaces.widget.Dialog.prototype.applyFocus = function() {
    var firstInput = this.jq.find(':not(:submit):not(:button):input:visible:enabled:first');
    if (!firstInput.hasClass('hasDatepicker')) {
        firstInput.focus();
    }
};

function comprobantesScroll() {
    datosComprobantes.scrollOffset = 3;
}

function cortesProcesosScroll() {
    datosCortesProcesos.scrollOffset = 3;
}
/*SCROLL DINAMICOS TABLAS CON COLUMNA FIJA*/
function iniciarScrollEmpleado() {
    var leftDiv = document.getElementById('inferiorIzquierda');
    var rightDiv = document.getElementById('inferiorDerecha');
    var rightDivSub = document.getElementById('inferiorDerechaSubtotalesEmpleado');
    var topDiv = document.getElementById('superiorDerecha');

    rightDiv.onscroll = function() {
        leftDiv.scrollTop = rightDiv.scrollTop;
    };
    rightDivSub.onscroll = function() {
        topDiv.scrollLeft = rightDivSub.scrollLeft;
        rightDiv.scrollLeft = rightDivSub.scrollLeft;
    };
    leftDiv.onscroll = function() {
        rightDiv.scrollTop = leftDiv.scrollTop;
    };
    
    var leftDiv2 = document.getElementById('inferiorIzquierdaEM');
    var rightDiv2 = document.getElementById('inferiorDerechaEM');
    var rightDivSub2 = document.getElementById('inferiorDerechaSubtotalesEmpleador');
    var topDiv2 = document.getElementById('superiorDerechaEM');

    rightDiv2.onscroll = function() {
        leftDiv2.scrollTop = rightDiv2.scrollTop;
    };
    rightDivSub2.onscroll = function() {
        topDiv2.scrollLeft = rightDivSub2.scrollLeft;
        rightDiv2.scrollLeft = rightDivSub2.scrollLeft;
    };
    leftDiv2.onscroll = function() {
        rightDiv2.scrollTop = leftDiv2.scrollTop;
    };
}

