function abrirFormulario() {
    opcion = document.getElementById("select1")

    if(opcion.value === 'insertar') {
        window.open('formInsertCliente.html', "_self")
    } else if(opcion.value === 'actualizar') {
        window.open('formUpdateCliente.html', "_self")
    } else if(opcion.value === 'eliminar') {
        window.open('formDeleteCliente.html', "_self")
    } else if(opcion.value === 'consultar') {
        window.open('./listaClientes', "_self")
    }
}