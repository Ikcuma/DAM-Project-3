document.addEventListener('DOMContentLoaded', function() {
    // Datos de equipos
    const equipos = [
        { nombre: 'Deportivo Alavés', escudo: 'alaves.png' },
        { nombre: 'Athletic Club', escudo: 'athletic.png' },
        { nombre: 'Atlético de Madrid', escudo: 'atlmadrid.png' },
        { nombre: 'FC Barcelona', escudo: 'barcelona.png' },
        { nombre: 'Real Betis', escudo: 'betis.png' },
        { nombre: 'RC Celta de Vigo', escudo: 'celta.png' },
        { nombre: 'RCD Espanyol', escudo: 'espanyol.png' },
        { nombre: 'Getafe CF', escudo: 'getafe.png' },
        { nombre: 'Girona FC', escudo: 'girona.png' },
        { nombre: 'CD Leganés', escudo: 'leganes.png' },
        { nombre: 'RCD Mallorca', escudo: 'mallorca.png' },
        { nombre: 'CA Osasuna', escudo: 'osasuna.png' },
        { nombre: 'Rayo Vallecano', escudo: 'rayovallecano.png' },
        { nombre: 'Real Madrid CF', escudo: 'realmadrid.png' },
        { nombre: 'Real Sociedad', escudo: 'realsociedad.png' },
        { nombre: 'Sevilla FC', escudo: 'sevilla.png' },
        { nombre: 'UD Las Palmas', escudo: 'udlaspalmas.png' },
        { nombre: 'Valencia CF', escudo: 'valencia.png' },
        { nombre: 'Real Valladolid', escudo: 'valladolid.png' },
        { nombre: 'Villarreal CF', escudo: 'villarreal.png' }
    ];

    // Colores de equipos
    const teamColors = {
        'Deportivo Alavés': '#005bab',
        'Athletic Club': '#ed1c24',
        'Atlético de Madrid': '#cb3524',
        'FC Barcelona': '#a50044',
        'Real Betis': '#00954e',
        'RC Celta de Vigo': '#aaddff',
        'RCD Espanyol': '#0060a8',
        'Getafe CF': '#2c5ba8',
        'Girona FC': '#ff0000',
        'CD Leganés': '#00a8e1',
        'RCD Mallorca': '#ff0000',
        'CA Osasuna': '#0a3a8a',
        'Rayo Vallecano': '#c60c30',
        'Real Madrid CF': '#00529f',
        'Real Sociedad': '#0073bc',
        'Sevilla FC': '#d81e05',
        'UD Las Palmas': '#0077c8',
        'Valencia CF': '#f7a11a',
        'Real Valladolid': '#5a2894',
        'Villarreal CF': '#ffcd00'
    };

    // Elementos del DOM
    const grid = document.querySelector('.equipos-grid');
    const detalleContainer = document.getElementById('equipo-detalle-container');
    const tituloPagina = document.querySelector('h2');

    // Generar tarjetas de equipos
    equipos.forEach(equipo => {
        const card = document.createElement('div');
        card.className = 'equipo-card';
        card.style.setProperty('--team-color', teamColors[equipo.nombre] || '#f5f5f5');
        
        card.innerHTML = `
            <img src="Assets/img/Escudos/${equipo.escudo}" alt="${equipo.nombre}" class="equipo-escudo">
            <span class="equipo-nombre">${equipo.nombre}</span>
        `;
        
        card.addEventListener('click', () => mostrarDetalleEquipo(equipo));
        grid.appendChild(card);
    });

    // Mostrar detalle del equipo
    async function mostrarDetalleEquipo(equipo) {
        try {
            grid.style.display = 'none';
            tituloPagina.style.display = 'none';
            detalleContainer.style.display = 'block';
            
            const response = await fetch('Assets/jugadores.json');
            if (!response.ok) throw new Error('Error al cargar datos');
            
            const data = await response.json();
            const equipoData = data.find(e => e.equip === equipo.nombre);
            if (!equipoData) throw new Error('Equipo no encontrado');
            
            detalleContainer.innerHTML = generarHTMLEquipo(equipoData);
            cargarJugadores(equipoData);
            configurarBotonesCategoria();
            
            document.getElementById('volver-equipos').addEventListener('click', volverAListaEquipos);
        } catch (error) {
            console.error('Error:', error);
            detalleContainer.innerHTML = `
                <div class="error-message">
                    <h2>Error al cargar datos</h2>
                    <p>${error.message}</p>
                    <button id="volver-equipos" class="btn-volver">Volver</button>
                </div>
            `;
            document.getElementById('volver-equipos').addEventListener('click', volverAListaEquipos);
        }
    }

    // Generar HTML del equipo
    function generarHTMLEquipo(equipo) {
        const info = obtenerInfoEquipo(equipo.equip);
        
        return `
            <button id="volver-equipos" class="btn-volver">← Volver</button>
            
            <div class="equipo-header">
                <h1>${equipo.equip}</h1>
                <div class="equipo-info">
                    <p><strong>Fundación:</strong> ${info.fundacion || 'N/A'}</p>
                    <p><strong>Estadio:</strong> ${info.estadio || 'N/A'}</p>
                    <p><strong>Ciudad:</strong> ${info.ciudad || 'N/A'}</p>
                    ${info.capacidad ? `<p><strong>Capacidad:</strong> ${info.capacidad}</p>` : ''}
                    <img src="Assets/img/Stadiuns/${info.estadioImg || 'default_stadium.jpg'}" 
                         " class="estadio-img">
                </div>
                <img src="${equipo.escut}" alt="${equipo.equip}" class="equipo-escudo-grande">
            </div>

            <section class="plantilla">
                <h2>Plantilla 2023/2024</h2>
                
                <div class="categorias-jugadores">
                    <button class="categoria-btn active" data-categoria="porteros">Porteros</button>
                    <button class="categoria-btn" data-categoria="defensas">Defensas</button>
                    <button class="categoria-btn" data-categoria="centrocampistas">Centrocampistas</button>
                    <button class="categoria-btn" data-categoria="delanteros">Delanteros</button>
                    <button class="categoria-btn" data-categoria="entrenador">Entrenador</button>
                </div>

                <div class="jugadores-categoria active" id="porteros"></div>
                <div class="jugadores-categoria" id="defensas"></div>
                <div class="jugadores-categoria" id="centrocampistas"></div>
                <div class="jugadores-categoria" id="delanteros"></div>
                <div class="jugadores-categoria" id="entrenador"></div>
            </section>
        `;
    }

    // Información de equipos
    function obtenerInfoEquipo(nombreEquipo) {
        const infoEquipos = {
            'Athletic Club': { fundacion: '1898', estadio: 'San Mamés', ciudad: 'Bilbao', capacidad: '53,289', estadioImg: 'Stadium_AthleticClub.jpg' },
            'Atlético Madrid': { fundacion: '1903', estadio: 'Metropolitano', ciudad: 'Madrid', capacidad: '68,456', estadioImg: 'Stadium_AtleticoMadrid.jpg' },
            'FC Barcelona': { fundacion: '1899', estadio: 'Camp Nou', ciudad: 'Barcelona', capacidad: '99,354', estadioImg: 'Stadium_Barcelona.webp' },
            'Real Madrid': { fundacion: '1902', estadio: 'Santiago Bernabéu', ciudad: 'Madrid', capacidad: '85,000', estadioImg: 'Stadium_RealMadrid.jpg' },
            'Deportivo Alavés': { fundacion: '1921', estadio: 'Mendizorrotza', ciudad: 'Vitoria', capacidad: '19,840', estadioImg: 'Stadium_Deportivo_Alavez.jpg' },
            'Real Sociedad': { fundacion: '1909', estadio: 'Anoeta', ciudad: 'San Sebastián', capacidad: '39,500', estadioImg: 'Stadium_RealSociedad.jpg' },
            'Sevilla FC': { fundacion: '1890', estadio: 'Ramón Sánchez-Pizjuán', ciudad: 'Sevilla', capacidad: '43,883', estadioImg: 'Stadium_Sevilla.jpg' },
            'Real Betis': { fundacion: '1907', estadio: 'Benito Villamarín', ciudad: 'Sevilla', capacidad: '60,720', estadioImg: 'Stadium_RealBetis.jpg' },
            'Villarreal CF': { fundacion: '1923', estadio: 'Estadio de la Cerámica', ciudad: 'Villarreal', capacidad: '23,500', estadioImg: 'Stadium_Villarreal.jpg' },
            'Valencia CF': { fundacion: '1919', estadio: 'Mestalla', ciudad: 'Valencia', capacidad: '49,430', estadioImg: 'Stadium_Valencia.jpg' },
            'RC Celta de Vigo': { fundacion: '1923', estadio: 'Balaídos', ciudad: 'Vigo', capacidad: '29,000', estadioImg: 'Stadium_Celta.jpg' },
            'RCD Mallorca': { fundacion: '1916', estadio: 'Visit Mallorca Stadium', ciudad: 'Palma', capacidad: '23,142', estadioImg: 'Stadium_Mallorca.jpg' },
            'Girona FC': { fundacion: '1930', estadio: 'Montilivi', ciudad: 'Girona', capacidad: '14,624', estadioImg: 'Stadium_Girona.jpg' },
            'CA Osasuna': { fundacion: '1920', estadio: 'El Sadar', ciudad: 'Pamplona', capacidad: '23,576', estadioImg: 'Stadium_Osasuna.jpg' },
            'Getafe CF': { fundacion: '1983', estadio: 'Coliseum Alfonso Pérez', ciudad: 'Getafe', capacidad: '17,393', estadioImg: 'Stadium_Getafe.jpg' },
            'UD Las Palmas': { fundacion: '1949', estadio: 'Gran Canaria', ciudad: 'Las Palmas', capacidad: '32,400', estadioImg: 'Stadium_LasPalmas.jpg' },
            'RCD Espanyol': { fundacion: '1900', estadio: 'RCDE Stadium', ciudad: 'Barcelona', capacidad: '40,000', estadioImg: 'Stadium_Espanyol.jpg' },
            'CD Leganés': { fundacion: '1928', estadio: 'Butarque', ciudad: 'Leganés', capacidad: '12,450', estadioImg: 'Stadium_Leganes.jpg' },
            'Real Valladolid': { fundacion: '1928', estadio: 'José Zorrilla', ciudad: 'Valladolid', capacidad: '27,846', estadioImg: 'Stadium_Valladolid.jpg' },
            'Rayo Vallecano': { fundacion: '1924', estadio: 'Vallecas', ciudad: 'Madrid', capacidad: '14,708', estadioImg: 'Stadium_RayoVallecano.jpg' }
        };
        
        return infoEquipos[nombreEquipo] || {};
    }

    // Cargar jugadores
    function cargarJugadores(equipo) {
        const categorias = {
            porteros: equipo.jugadors.filter(j => j.posicio?.toLowerCase().includes('porter')),
            defensas: equipo.jugadors.filter(j => j.posicio?.toLowerCase().includes('defens')),
            centrocampistas: equipo.jugadors.filter(j => 
                j.posicio?.toLowerCase().includes('migcamp') || 
                j.posicio?.toLowerCase().includes('medio') ||
                j.posicio?.toLowerCase().includes('centrocamp')),
            delanteros: equipo.jugadors.filter(j => j.posicio?.toLowerCase().includes('davant'))
        };

        Object.entries(categorias).forEach(([categoria, jugadores]) => {
            const contenedor = document.getElementById(categoria);
            if (!contenedor) return;
            
            contenedor.innerHTML = jugadores.length ? 
                jugadores.map(j => crearTarjetaJugador(j)).join('') : 
                '<p class="no-jugadores">No hay jugadores</p>';
        });

        mostrarEntrenador(equipo.entrenador);
    }

    // Crear tarjeta de jugador
    function crearTarjetaJugador(jugador) {
        return `
            <div class="jugador-card">
                ${jugador.dorsal ? `<span class="dorsal">${jugador.dorsal}</span>` : ''}
                <div class="jugador-img">
                    <img src="${jugador.foto}" alt="${jugador.nomPersona}" onerror="this.src='Assets/img/Jugadores/default_player.png'">
                </div>
                <div class="jugador-info">
                    <p class="posicion">${formatearPosicion(jugador.posicio)}</p>
                    <p class="nombre">${jugador.nomPersona?.toUpperCase() || 'Jugador'}</p>
                    ${jugador.qualitat ? `<p class="qualitat">Nivel: ${jugador.qualitat}</p>` : ''}
                </div>
            </div>
        `;
    }

    // Mostrar entrenador
    function mostrarEntrenador(entrenador) {
        const contenedor = document.getElementById('entrenador');
        if (!contenedor || !entrenador?.nomPersona) {
            if (contenedor) contenedor.innerHTML = '<p class="no-jugadores">Sin entrenador</p>';
            return;
        }
        
        contenedor.innerHTML = `
            <div class="jugador-card entrenador">
                <div class="jugador-img">
                    <img src="${entrenador.foto}" alt="${entrenador.nomPersona}" onerror="this.src='Assets/img/Jugadores/default_coach.png'">
                </div>
                <div class="jugador-info">
                    <p class="posicion">ENTRENADOR</p>
                    <p class="nombre">${entrenador.nomPersona.toUpperCase()}</p>
                </div>
            </div>
        `;
    }

    // Formatear posición
    function formatearPosicion(posicion) {
        if (!posicion) return '';
        const pos = posicion.toLowerCase();
        
        return {
            porter: 'PORTERO',
            defensa: 'DEFENSA',
            migcampista: 'CENTROCAMPISTA',
            davanter: 'DELANTERO',
            medio: 'CENTROCAMPISTA',
            delantero: 'DELANTERO',
            centrocampista: 'CENTROCAMPISTA'
        }[pos] || posicion.toUpperCase();
    }

    // Configurar botones de categoría
    function configurarBotonesCategoria() {
        const botones = document.querySelectorAll('.categoria-btn');
        
        botones.forEach(boton => {
            boton.addEventListener('click', function() {
                botones.forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');
                
                const categoriaId = this.dataset.categoria;
                document.querySelectorAll('.jugadores-categoria').forEach(cat => {
                    cat.classList.toggle('active', cat.id === categoriaId);
                });
            });
        });
    }

    // Volver a lista de equipos
    function volverAListaEquipos() {
        detalleContainer.style.display = 'none';
        grid.style.display = 'grid';
        tituloPagina.style.display = 'block';
        detalleContainer.innerHTML = '';
    }
});