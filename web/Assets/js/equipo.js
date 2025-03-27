document.addEventListener('DOMContentLoaded', function() {
    const container = document.getElementById('equipo-container');
    const nombreEquipo = container.dataset.equipo;
    
    // Cargar el JSON de jugadores
    fetch('Assets/jugadores.json')
        .then(response => {
            if (!response.ok) {
                throw new Error('No se pudo cargar el archivo JSON');
            }
            return response.json();
        })
        .then(data => {
            const equipo = data.find(e => e.equip === nombreEquipo);
            if (!equipo) {
                throw new Error(`Equipo "${nombreEquipo}" no encontrado en el JSON`);
            }
            
            // Verificación de rutas (solo para depuración)
            console.log("Ruta del escudo:", equipo.escut);
            equipo.jugadors.forEach(j => {
                console.log("Ruta de jugador:", j.foto, "para", j.nomPersona);
            });
            console.log("Ruta del entrenador:", equipo.entrenador.foto);
            
            container.innerHTML = generarHTMLCompleto(equipo);
            configurarBotonesCategoria();
            cargarJugadores(equipo);
        })
        .catch(error => {
            console.error('Error:', error);
            container.innerHTML = `
                <div class="error-message">
                    <h2>Error al cargar los datos del equipo</h2>
                    <p>${error.message}</p>
                    <a href="Equipos.html">Volver a la lista de equipos</a>
                </div>
            `;
        });

    // Genera el HTML completo de la página del equipo
    function generarHTMLCompleto(equipo) {
        return `
            <header>
                <div class="escudos-container">
                    <div class="escudos-scroll">
                        ${generarEscudosLiga()}
                    </div>
                </div>
                <div class="logo">
                    <img src="Assets/img/Escudos/logo_sin_fondo.png" alt="Logo Fútbol Stats">
                    <h1>Elite League</h1>
                </div>
                <nav>
                    <ul>
                        <li><a href="Main.html">Inicio</a></li>
                        <li><a href="Equipos.html">Equipos</a></li>
                        <li><a href="Clasificacion.html">Clasificación</a></li>
                        <li><a href="Formulario.html">Alta Jugadores</a></li>
                    </ul>
                </nav>
            </header>

            <main class="equipo-container">
                <div class="equipo-header">
                    <h1>${equipo.equip}</h1>
                    <div class="equipo-info">
                        ${generarInfoEquipo(equipo)}
                    </div>
                    <img src="${equipo.escut || 'Assets/img/Escudos/default.png'}" 
                         alt="${equipo.equip}" class="equipo-escudo-grande"
                         onerror="this.onerror=null;this.src='Assets/img/Escudos/default.png'">
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
            </main>

            <footer>
                ${generarFooter()}
            </footer>
        `;
    }

    // Genera el footer de la página
    function generarFooter() {
        return `
            <div class="footer-content">
                <div class="footer-section">
                    <h3>Sobre Nosotros</h3>
                    <p>Elite League - La mejor información sobre fútbol</p>
                </div>
                <div class="footer-section">
                    <h3>Contacto</h3>
                    <p>info@eliteleague.com</p>
                </div>
                <div class="footer-section">
                    <h3>Síguenos</h3>
                    <div class="social-icons">
                        <a href="#"><i class="fab fa-facebook"></i></a>
                        <a href="#"><i class="fab fa-twitter"></i></a>
                        <a href="#"><i class="fab fa-instagram"></i></a>
                    </div>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2023 Elite League. Todos los derechos reservados.</p>
            </div>
        `;
    }

    // Obtiene el nombre del estadio según el equipo
    function getNombreStadium(nombreEquipo) {
        const map = {
            'FC Barcelona': 'Stadium_Barcelona.webp',
            'Real Madrid CF': 'Stadium_RealMadrid.jpg',
            'Atlético de Madrid': 'Stadium_AtleticoMadrid.jpg',
            'Athletic Club': 'Stadium_AthleticClub.jpg',
            'Real Sociedad': 'Stadium_RealSociedad.jpg',
            'Sevilla FC': 'Stadium_Sevilla.jpg',
            'Real Betis': 'Stadium_RealBetis.jpg',
            'Villarreal CF': 'Stadium_Villarreal.jpg',
            'Valencia CF': 'Stadium_Valencia.jpg',
            'RC Celta de Vigo': 'Stadium_Celta_de_Vigo.jpg',
            'RCD Mallorca': 'Stadium_Mallorca.jpg',
            'Girona FC': 'Stadium_Girona.jpg',
            'CA Osasuna': 'Stadium_CA_Osasuna.jpg',
            'Getafe CF': 'Stadium_Getafe_CF.jpg',
            'UD Las Palmas': 'Stadium_UD_Las_Palmas.jpg',
            'RCD Espanyol': 'Stadium_Espanyol.jpg',
            'CD Leganés': 'Stadium_CD_Leganes.jpg',
            'Real Valladolid CF': 'Stadium_Real_Valladolid.jpg',
            'Rayo Vallecano': 'Stadium_Rayo_Vallecano.jpg',
            'Deportivo Alavés': 'Stadium_Deportivo_Alavez.jpg'
        };
        return map[nombreEquipo] || 'default_stadium.jpg';
    }

    // Genera los escudos de la liga para el header
    function generarEscudosLiga() {
        const equipos = [
            'alaves', 'athletic', 'atlmadrid', 'barcelona', 'betis',
            'celta', 'espanyol', 'getafe', 'girona', 'leganes',
            'mallorca', 'osasuna', 'rayovallecano', 'realmadrid',
            'realsociedad', 'sevilla', 'udlaspalmas', 'valencia', 'valladolid', 'villarreal'
        ];
        
        return equipos.map(equipo => 
            `<img src="Assets/img/Escudos/${equipo}.png" alt="${equipo.replace(/_/g, ' ')}"
                 onerror="this.onerror=null;this.src='Assets/img/Escudos/default.png'">`
        ).join('');
    }

    // Genera la información del equipo (estadio, fundación, etc.)
    function generarInfoEquipo(equipo) {
        const infoEquipos = {
            'Athletic Club': {
                fundacion: '1898',
                estadio: 'San Mamés',
                ciudad: 'Bilbao',
                capacidad: '53,289'
            },
            // ... (resto de equipos con su información)
            'Deportivo Alavés': {
                fundacion: '1921',
                estadio: 'Mendizorrotza',
                ciudad: 'Vitoria-Gasteiz',
                capacidad: '19,840'
            }
        };
        
        const info = infoEquipos[equipo.equip] || {};
        
        return `
            <p><strong>Fundación:</strong> ${info.fundacion || 'Datos no disponibles'}</p>
            <p><strong>Estadio:</strong> ${info.estadio || 'Datos no disponibles'}</p>
            <p><strong>Ciudad:</strong> ${info.ciudad || 'Datos no disponibles'}</p>
            ${info.capacidad ? `<p><strong>Capacidad:</strong> ${info.capacidad}</p>` : ''}
            <img src="Assets/img/Stadiuns/${getNombreStadium(equipo.equip)}" 
                 alt="Estadio ${info.estadio || equipo.equip}" 
                 class="estadio-img"
                 onerror="this.onerror=null;this.src='Assets/img/Stadiuns/default_stadium.jpg'">
        `;
    }

    // Carga los jugadores en sus respectivas categorías
    function cargarJugadores(equipo) {
        const jugadoresPorPosicion = {
            porteros: equipo.jugadors.filter(j => 
                j.posicio && (j.posicio.toLowerCase().includes('porter') || 
                j.posicio.toLowerCase() === 'portero')
            ),
            defensas: equipo.jugadors.filter(j => 
                j.posicio && (j.posicio.toLowerCase().includes('defens') ||
                j.posicio.toLowerCase() === 'defensa')
            ),
            centrocampistas: equipo.jugadors.filter(j => 
                j.posicio && (j.posicio.toLowerCase().includes('migcamp') || 
                j.posicio.toLowerCase().includes('centrocamp') ||
                j.posicio.toLowerCase().includes('medio') ||
                j.posicio.toLowerCase() === 'migcampista')
            ),
            delanteros: equipo.jugadors.filter(j => 
                j.posicio && (j.posicio.toLowerCase().includes('davant') ||
                j.posicio.toLowerCase() === 'delantero')
            )
        };
        
        mostrarJugadoresEnCategoria('porteros', jugadoresPorPosicion.porteros);
        mostrarJugadoresEnCategoria('defensas', jugadoresPorPosicion.defensas);
        mostrarJugadoresEnCategoria('centrocampistas', jugadoresPorPosicion.centrocampistas);
        mostrarJugadoresEnCategoria('delanteros', jugadoresPorPosicion.delanteros);
        mostrarEntrenador(equipo.entrenador);
    }

    // Muestra los jugadores en su categoría correspondiente
    function mostrarJugadoresEnCategoria(categoriaId, jugadores) {
        const contenedor = document.getElementById(categoriaId);
        if (!contenedor) return;
        
        if (!jugadores || jugadores.length === 0) {
            contenedor.innerHTML = '<p class="no-jugadores">No hay jugadores en esta categoría</p>';
            return;
        }
        
        contenedor.innerHTML = jugadores.map(jugador => `
            <div class="jugador-card">
                ${jugador.dorsal ? `<span class="dorsal">${jugador.dorsal}</span>` : ''}
                <div class="jugador-img">
                    <img src="${jugador.foto || 'Assets/img/Jugadores/default_player.png'}" 
                         alt="${jugador.nomPersona}"
                         onerror="this.onerror=null;this.src='Assets/img/Jugadores/default_player.png'">
                </div>
                <div class="jugador-info">
                    ${jugador.posicio ? `<p class="posicion">${formatearPosicion(jugador.posicio)}</p>` : ''}
                    <p class="nombre">${jugador.nomPersona?.toUpperCase() || 'Jugador'}</p>
                    ${jugador.qualitat ? `<p class="qualitat">Nivel: ${jugador.qualitat}</p>` : ''}
                </div>
            </div>
        `).join('');
    }

    // Formatea la posición del jugador para mostrarla mejor
    function formatearPosicion(posicion) {
        if (!posicion) return '';
        
        const posiciones = {
            'porter': 'PORTERO',
            'defensa': 'DEFENSA',
            'migcampista': 'CENTROCAMPISTA',
            'davanter': 'DELANTERO',
            'medio': 'CENTROCAMPISTA',
            'delantero': 'DELANTERO',
            'centrocampista': 'CENTROCAMPISTA'
        };
        
        const posLower = posicion.toLowerCase();
        for (const key in posiciones) {
            if (posLower.includes(key)) {
                return posiciones[key];
            }
        }
        return posicion.toUpperCase();
    }

    // Muestra la información del entrenador
    function mostrarEntrenador(entrenador) {
        const contenedor = document.getElementById('entrenador');
        if (!contenedor) return;
        
        if (!entrenador || !entrenador.nomPersona) {
            contenedor.innerHTML = '<p class="no-jugadores">Datos del entrenador no disponibles</p>';
            return;
        }
        
        contenedor.innerHTML = `
            <div class="jugador-card entrenador">
                <div class="jugador-img">
                    <img src="${entrenador.foto || 'Assets/img/Jugadores/default_coach.png'}" 
                         alt="${entrenador.nomPersona}"
                         onerror="this.onerror=null;this.src='Assets/img/Jugadores/default_coach.png'">
                </div>
                <div class="jugador-info">
                    <p class="posicion">ENTRENADOR</p>
                    <p class="nombre">${entrenador.nomPersona.toUpperCase()}</p>
                </div>
            </div>
        `;
    }

    // Configura los botones de categoría para mostrar/ocultar jugadores
    function configurarBotonesCategoria() {
        const botones = document.querySelectorAll('.categoria-btn');
        const categorias = document.querySelectorAll('.jugadores-categoria');
        
        botones.forEach(boton => {
            boton.addEventListener('click', function(e) {
                e.preventDefault();
                
                botones.forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');
                
                const categoriaId = this.dataset.categoria;
                categorias.forEach(cat => {
                    cat.classList.remove('active');
                    if (cat.id === categoriaId) {
                        cat.classList.add('active');
                    }
                });
            });
        });
    }
});