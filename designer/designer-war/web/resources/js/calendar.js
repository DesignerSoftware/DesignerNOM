var Calendar = (function() {
    return {
        maskedCalendar: function() {
            $('.masked_calendar').datepicker({
                yearRange: 'c-70:c+70',
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
                showButtonPanel:"true",
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
                allDayText: 'Todo el día',
                dateFormat: 'dd/mm/yy',
                changeMonth: true,
                changeYear: true,
                showAnim: "fadeIn",
                duration: "normal"
            });
        }
    };
})();

$(document).ready(function() {
    Calendar.maskedCalendar();
});

$(document).ajaxComplete(function() {
    $('.masked_calendar').ready(function() {
        Calendar.maskedCalendar();
    });
});

