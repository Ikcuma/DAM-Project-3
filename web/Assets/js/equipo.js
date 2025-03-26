document.addEventListener('DOMContentLoaded', function() {
    // Elementos del DOM
    const botonesCategoria = document.querySelectorAll('.categoria-btn');
    const categoriasJugadores = document.querySelectorAll('.jugadores-categoria');
    
    // Mostrar la primera categoría por defecto
    function mostrarCategoriaPorDefecto() {
        // Verificar si hay categorías
        if (categoriasJugadores.length === 0) {
            console.error('No se encontraron categorías de jugadores');
            return;
        }
        
        // Ocultar todas las categorías primero
        categoriasJugadores.forEach(categoria => {
            categoria.classList.remove('active');
        });
        
        // Mostrar la primera categoría disponible
        const primeraCategoria = document.querySelector('.jugadores-categoria');
        if (primeraCategoria) {
            primeraCategoria.classList.add('active');
            
            // Activar el botón correspondiente
            const categoriaId = primeraCategoria.id;
            const botonCorrespondiente = document.querySelector(`.categoria-btn[data-categoria="${categoriaId}"]`);
            
            if (botonCorrespondiente) {
                botonesCategoria.forEach(btn => btn.classList.remove('active'));
                botonCorrespondiente.classList.add('active');
            }
        }
    }
    
    // Manejar el cambio de categoría
    function cambiarCategoria(event) {
        // Prevenir comportamiento por defecto si es un enlace
        event.preventDefault();
        
        // Remover clase 'active' de todos los botones
        botonesCategoria.forEach(btn => {
            btn.classList.remove('active');
        });
        
        // Añadir clase 'active' al botón clickeado
        this.classList.add('active');
        
        // Obtener la categoría a mostrar
        const categoriaAMostrar = this.dataset.categoria;
        
        // Validar que la categoría existe
        if (!categoriaAMostrar) {
            console.error('El botón no tiene atributo data-categoria');
            return;
        }
        
        // Ocultar todas las categorías
        categoriasJugadores.forEach(categoria => {
            categoria.classList.remove('active');
        });
        
        // Mostrar la categoría seleccionada
        const categoriaSeleccionada = document.getElementById(categoriaAMostrar);
        if (categoriaSeleccionada) {
            categoriaSeleccionada.classList.add('active');
            
            // Scroll suave a la sección
            categoriaSeleccionada.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        } else {
            console.error(`No se encontró la categoría con ID: ${categoriaAMostrar}`);
        }
    }
    
    // Event Listeners
    botonesCategoria.forEach(boton => {
        boton.addEventListener('click', cambiarCategoria);
    });
    
    // Inicialización
    mostrarCategoriaPorDefecto();
    
    // Opcional: Manejar cambios en la URL
    window.addEventListener('hashchange', function() {
        const hash = window.location.hash.substring(1);
        if (hash) {
            const botonCorrespondiente = document.querySelector(`.categoria-btn[data-categoria="${hash}"]`);
            if (botonCorrespondiente) {
                botonCorrespondiente.click();
            }
        }
    });
});