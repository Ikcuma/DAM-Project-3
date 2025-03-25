document.addEventListener('DOMContentLoaded', function() {
    // Seleccionar todos los botones de categoría
    const botonesCategoria = document.querySelectorAll('.categoria-btn');
    
    // Seleccionar todas las secciones de jugadores
    const categoriasJugadores = document.querySelectorAll('.jugadores-categoria');
    
    // Añadir evento click a cada botón
    botonesCategoria.forEach(boton => {
        boton.addEventListener('click', function() {
            // Remover la clase 'active' de todos los botones
            botonesCategoria.forEach(btn => btn.classList.remove('active'));
            
            // Añadir la clase 'active' solo al botón clickeado
            this.classList.add('active');
            
            // Obtener la categoría a mostrar
            const categoriaAMostrar = this.getAttribute('data-categoria');
            
            // Ocultar todas las categorías de jugadores
            categoriasJugadores.forEach(categoria => {
                categoria.classList.remove('active');
            });
            
            // Mostrar solo la categoría seleccionada
            document.getElementById(categoriaAMostrar).classList.add('active');
        });
    });
});