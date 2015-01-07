alerta = function(mensaje, tipo){
    var titulo = "NO definido";
    if (tipo == "error"){
        titulo = "Error";
    }
    else if (tipo == "success"){
        titulo = "OK";
    }
    swal({
        title: titulo,
        text: mensaje,
        type: tipo,
        confirmButtonText: "Ok"
    });
}