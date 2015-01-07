<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>Lego - Smarthouse</title>
  <script type="text/javascript" src="../js/jquery-2.1.1.js"></script>
  <script type="text/javascript" src="../js/sweet-alert.min.js"></script>
  <script type="text/javascript" src="../js/alertas.js"></script>
  <link rel="stylesheet" type="text/css" href="../css/sweet-alert.css">

</head>
<body>
<h1>Datos del lego</h1>
<div id="idDatosLego"></div>
<h2>Recursos asignados a lego</h2>
<table id="tablaRecursos">
  <tr>
    <th>#</th>
    <th>Id recurso</th>
    <th>Descripcion</th>
    <th>Solicitar</th>
    <th>Solicitar</th>
    <th>Horario</th>
  </tr>
</table>
<a href="legos.html">Regresar</a>
<script type="text/javascript">

  var urlLegos = "/SmarthouseWS/api/legos";
  var idLego = <%= request.getParameter("idLego") %>
  var urlRecursosPorLego = "/SmarthouseWS/api/recursos/porLego";

  var urlEjecutarOrden = "/SmarthouseWS/api/recursos/crearOrden"
  var urlModificarHorario = "/SmarthouseWS/api/horarios/modificarHorario";
  var urlHorariosPorLegoYRecurso = "/SmarthouseWS/api/horarios/porLegoYRecurso";
  var idRecursoAlarma = 8;
  $(document).ready(function(){
    $.getJSON(urlLegos +  "/" + idLego, function (lego) {
      $("#idDatosLego").append("<b>Id:</b> " + lego.id + "<br>");
      $("#idDatosLego").append("<b>MAC:</b> " + lego.mac + "<br>");
      $("#idDatosLego").append("<b>Tipo:</b> " + lego.tipo + "<br>");
    });

    $.getJSON(urlRecursosPorLego + "/" +  idLego, function (recursos) {
      $.each(recursos, function (index, recurso) {
        $("#tablaRecursos").append("<tr>" +
        "<td>" + (index + 1) + "</td>" +
        "<td>" + recurso.id + "</td>" +
        "<td>" + recurso.descripcion + "</td>" +
        "<td>" + '<input type="button" value ="Off" id="btnOffRecurso' + recurso.id + '" class="btnApagar" />' + "</td>" +
        "<td>" + '<input type="button" value ="On" id="btnOnRecurso' + recurso.id + '" class="btnEncender" />' + "</td>" +
        "<td>" +
        'Hora inicio: <input type="time" id="timerOn' + recurso.id + '" value="' + recurso.horaInicio + '" />' +
        'Hora fin: <input type="time" id="timerOff' + recurso.id + '" value="' + recurso.horaFin + '" />' +
        '<input type="button" value ="Establecer" id="btnTimerRecurso' + recurso.id + '" class="btnTimer" />' +
        "</td>" +
        "</tr>");

        if (recurso.id == idRecursoAlarma){
          $("#timerOn"+recurso.id).prop('disabled', true);
          $("#timerOff"+recurso.id).prop('disabled', true);
          $("#btnOnRecurso"+recurso.id).prop('disabled', true);
          $("#btnTimerRecurso"+recurso.id).prop('disabled', true);
        }
        $.getJSON(urlHorariosPorLegoYRecurso + "/" +  idLego + "/" + recurso.id, function (horario) {
          $("#timerOn" + recurso.id).val(horario.horaInicio);
          $("#timerOff" + recurso.id).val(horario.horaFin);

          $("#btnTimerRecurso" + recurso.id).on('click', function(){
            var horaInicio = $("#timerOn" + recurso.id).val();
            if (horaInicio == ""){
              alerta("Introduce una hora válida", "error");
              return;
            }
            var horaFin = $("#timerOff" + recurso.id).val();
            if (horaFin == ""){
              alerta("Introduce una hora válida", "error");
              return;
            }
            //{idLego}/{idRecurso}/{idLegoRecurso}/{horaInicio}/{horaFin}
            $.ajax({
              url: urlModificarHorario + "/" + horario.idLego + "/" + horario.idRecurso + "/" + horario.idLegoRecurso + "/" + horaInicio + "/" + horaFin,
              success: function (respuesta) {
                alerta("Configuración de horario enviada correctamente", "success")
              },
              error: function(jqXHR, textoEstatus, errorLanzado){
                alerta("Ocurrió un error al procesar en el servidor", "error");
              }
            });
        });


        $("#btnOnRecurso" + recurso.id).on('click', function(){
          $.ajax({
            type: "GET",
            url: urlEjecutarOrden + "/" + idLego + "/" + recurso.id + "/1",
            success: function (respuesta) {
              alerta("Orden enviada correctamente", "success")
            },
            error: function(jqXHR, textoEstatus, errorLanzado){
              alerta("Ocurrió un error al procesar en el servidor", "error");
            }
          });
        });

        $("#btnOffRecurso" + recurso.id).on('click', function(){
          $.ajax({
            type: "GET",
            url: urlEjecutarOrden + "/" + idLego + "/" + recurso.id + "/0",
            success: function (respuesta) {
              alerta("Orden enviada correctamente", "success")
            },
            error: function(jqXHR, textoEstatus, errorLanzado){
              alerta("Ocurrió un error al procesar en el servidor", "error");
            }
          });
        });
        });
      });;
    })

  })
</script>
</body>
</html>