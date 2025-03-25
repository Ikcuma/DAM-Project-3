document.addEventListener('DOMContentLoaded', function() {
    const statsGrid = document.querySelector('.stats-grid');
    
    // Opcional: Añadir botones de navegación si lo deseas
    const statsSection = document.querySelector('.stats');
    
    // Crear contenedor de botones
    const navButtons = document.createElement('div');
    navButtons.className = 'stats-nav';
    navButtons.innerHTML = `
        <button class="nav-btn prev-btn" aria-label="Anterior">&lt;</button>
        <button class="nav-btn next-btn" aria-label="Siguiente">&gt;</button>
    `;
    statsSection.appendChild(navButtons);
    
    // Funcionalidad de los botones
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');
    
    prevBtn.addEventListener('click', () => {
        statsGrid.scrollBy({ left: -300, behavior: 'smooth' });
    });
    
    nextBtn.addEventListener('click', () => {
        statsGrid.scrollBy({ left: 300, behavior: 'smooth' });
    });
    
    // Ocultar/mostrar botones según la posición del scroll
    statsGrid.addEventListener('scroll', () => {
        prevBtn.style.visibility = statsGrid.scrollLeft > 0 ? 'visible' : 'hidden';
        nextBtn.style.visibility = statsGrid.scrollLeft < (statsGrid.scrollWidth - statsGrid.clientWidth) ? 'visible' : 'hidden';
    });
    
    // Inicializar visibilidad de botones
    prevBtn.style.visibility = 'hidden';
    nextBtn.style.visibility = statsGrid.scrollWidth > statsGrid.clientWidth ? 'visible' : 'hidden';
});