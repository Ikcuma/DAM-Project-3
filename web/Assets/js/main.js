// Esperamos a que el documento HTML esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    
    // =============================================
    // 1. DATOS DE LOS EQUIPOS
    // =============================================
    const equipos = [
        { nombre: 'Deportivo Alavés', escudo: 'alaves.png' },
        { nombre: 'Athletic Club', escudo: 'athletic.png' },
        { nombre: 'Atletico de Madrid', escudo: 'atlmadrid.png' },
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
        { nombre: 'Real Valladolid CF', escudo: 'valladolid.png' },
        { nombre: 'Villarreal CF', escudo: 'villarreal.png' }
    ];

    // =============================================
    // 2. COLORES DE LOS EQUIPOS
    // =============================================
    const coloresEquipos = {
        'Deportivo Alavés': '#005bab',
        'Athletic Club': '#ed1c24',
        'Atletico de Madrid': '#cb3524',
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
        'Real Valladolid CF': '#5a2894',
        'Villarreal CF': '#ffcd00'
    };

    // =============================================
    // 3. OBTENER ELEMENTOS DEL DOM
    // =============================================
    const contenedorEquipos = document.querySelector('.equipos-grid');
    const contenedorDetalle = document.getElementById('equipo-detalle-container');
    const tituloPagina = document.querySelector('h2');

    // =============================================
    // 4. FUNCIÓN PARA CREAR LAS TARJETAS DE EQUIPOS
    // =============================================
    function crearTarjetasEquipos() {
        equipos.forEach(function(equipo) {
            var tarjeta = document.createElement('div');
            tarjeta.className = 'equipo-card';
    
            var colorFondo = coloresEquipos[equipo.nombre];
            if (!colorFondo) {
                colorFondo = '#f5f5f5';
            }
            tarjeta.style.backgroundColor = colorFondo;
            
            var escudo = document.createElement('img');
            escudo.src = 'Assets/img/Escudos/' + equipo.escudo;
            escudo.alt = equipo.nombre;
            escudo.className = 'equipo-escudo';
            
            var nombre = document.createElement('span');
            nombre.className = 'equipo-nombre';
            nombre.textContent = equipo.nombre;
            
            tarjeta.appendChild(escudo);
            tarjeta.appendChild(nombre);
            
            tarjeta.addEventListener('click', function() {
                mostrarDetalleEquipo(equipo);
            });
            
            contenedorEquipos.appendChild(tarjeta);
        });
    }
    

    // =============================================
    // 5. FUNCIÓN PARA MOSTRAR EL DETALLE DEL EQUIPO
    // =============================================
    function mostrarDetalleEquipo(equipo) {
        contenedorEquipos.style.display = 'none';
        tituloPagina.style.display = 'none';
        contenedorDetalle.style.display = 'block';
    
        fetch('Assets/jugadores.json')
            .then(function (response) {
                if (!response.ok) throw new Error('No se pudieron cargar los datos');
                return response.json();
            })
            .then(function (datos) {
                var datosEquipo = null;
                for (var i = 0; i < datos.length; i++) {
                    if (datos[i].equip === equipo.nombre) {
                        datosEquipo = datos[i];
                        break;
                    }
                }
                if (!datosEquipo) throw new Error('Equipo no encontrado');
                mostrarContenidoEquipo(datosEquipo);
            })
            .catch(function (error) {
                console.error('Error:', error);
    
                var errorDiv = document.createElement('div');
                errorDiv.className = 'error-message';
    
                var h2 = document.createElement('h2');
                h2.textContent = 'Error al cargar datos';
    
                var p = document.createElement('p');
                p.textContent = error.message;
    
                var boton = document.createElement('button');
                boton.id = 'volver-equipos';
                boton.className = 'btn-volver';
                boton.textContent = 'Volver';
                boton.addEventListener('click', volverAListaEquipos);
    
                errorDiv.appendChild(h2);
                errorDiv.appendChild(p);
                errorDiv.appendChild(boton);
    
                contenedorDetalle.innerHTML = '';
                contenedorDetalle.appendChild(errorDiv);
            });
    }
    

    // =============================================
    // 6. FUNCIÓN PARA MOSTRAR EL CONTENIDO DEL EQUIPO
    // =============================================
    function mostrarContenidoEquipo(equipo) {
        contenedorDetalle.innerHTML = '';
        var infoEquipo = obtenerInfoEquipo(equipo.equip);
    
        var botonVolver = document.createElement('button');
        botonVolver.id = 'volver-equipos';
        botonVolver.className = 'btn-volver';
        botonVolver.textContent = '← Volver';
        botonVolver.addEventListener('click', volverAListaEquipos);
        contenedorDetalle.appendChild(botonVolver);
    
        var header = document.createElement('div');
        header.className = 'equipo-header';
    
        var titulo = document.createElement('h1');
        titulo.textContent = equipo.equip;
        header.appendChild(titulo);
    
        var info = document.createElement('div');
        info.className = 'equipo-info';
    
        function crearInfoItem(etiqueta, valor) {
            var p = document.createElement('p');
            var strong = document.createElement('strong');
            strong.textContent = etiqueta + ': ';
            p.appendChild(strong);
            p.appendChild(document.createTextNode(valor ? valor : 'N/A'));
            return p;
        }
    
        info.appendChild(crearInfoItem('Fundación', infoEquipo.fundacion));
        info.appendChild(crearInfoItem('Estadio', infoEquipo.estadio));
        info.appendChild(crearInfoItem('Ciudad', infoEquipo.ciudad));
    
        if (infoEquipo.capacidad) {
            info.appendChild(crearInfoItem('Capacidad', infoEquipo.capacidad));
        }
    
        header.appendChild(info);
    
        var imagenes = document.createElement('div');
        imagenes.className = 'equipo-imagenes';
    
        var escudo = document.createElement('img');
        escudo.src = equipo.escut;
        escudo.alt = equipo.equip;
        escudo.className = 'equipo-escudo-grande';
        imagenes.appendChild(escudo);
    
        if (equipo.estadi) {
            var estadioDiv = document.createElement('div');
            estadioDiv.className = 'estadio-fixed-width';
    
            var imgEstadio = document.createElement('img');
            imgEstadio.src = equipo.estadi;
            imgEstadio.alt = 'Estadio ' + equipo.equip;
            imgEstadio.className = 'equipo-estadio';
    
            estadioDiv.appendChild(imgEstadio);
            imagenes.appendChild(estadioDiv);
        }
    
        header.appendChild(imagenes);
        contenedorDetalle.appendChild(header);
    
        var plantilla = document.createElement('section');
        plantilla.className = 'plantilla';
    
        var tituloPlantilla = document.createElement('h2');
        tituloPlantilla.textContent = 'Plantilla 2023/2024';
        plantilla.appendChild(tituloPlantilla);
    
        var categorias = document.createElement('div');
        categorias.className = 'categorias-jugadores';
    
        var categoriasJugadores = [
            {texto: 'Porteros', categoria: 'porteros', active: true},
            {texto: 'Defensas', categoria: 'defensas'},
            {texto: 'Centrocampistas', categoria: 'centrocampistas'},
            {texto: 'Delanteros', categoria: 'delanteros'},
            {texto: 'Entrenador', categoria: 'entrenador'}
        ];
    
        for (var i = 0; i < categoriasJugadores.length; i++) {
            var item = categoriasJugadores[i];
            var boton = document.createElement('button');
            boton.className = 'categoria-btn';
            if (item.active) {
                boton.className += ' active';
            }
            boton.setAttribute('data-categoria', item.categoria);
            boton.textContent = item.texto;
            categorias.appendChild(boton);
        }
    
        plantilla.appendChild(categorias);
    
        var secciones = ['porteros', 'defensas', 'centrocampistas', 'delanteros', 'entrenador'];
        for (var j = 0; j < secciones.length; j++) {
            var id = secciones[j];
            var div = document.createElement('div');
            div.className = 'jugadores-categoria';
            if (id === 'porteros') {
                div.className += ' active';
            }
            div.id = id;
            plantilla.appendChild(div);
        }
    
        contenedorDetalle.appendChild(plantilla);
    
        cargarJugadores(equipo);
        configurarBotonesCategoria();
    }
    

    // =============================================
    // 7. FUNCIÓN PARA OBTENER INFORMACIÓN ADICIONAL DEL EQUIPO
    // =============================================
    function obtenerInfoEquipo(nombreEquipo) {
        const infoEquipos = {
            'Athletic Club': { fundacion: '1898', estadio: 'San Mamés', ciudad: 'Bilbao', capacidad: '53,289' },
            'Atletico de Madrid': { fundacion: '1903', estadio: 'Metropolitano', ciudad: 'Madrid', capacidad: '68,456' },
            'FC Barcelona': { fundacion: '1899', estadio: 'Camp Nou', ciudad: 'Barcelona', capacidad: '99,354' },
            'Real Madrid': { fundacion: '1902', estadio: 'Santiago Bernabéu', ciudad: 'Madrid', capacidad: '85,000' },
            'Deportivo Alavés': { fundacion: '1921', estadio: 'Mendizorrotza', ciudad: 'Vitoria', capacidad: '19,840' },
            'Real Sociedad': { fundacion: '1909', estadio: 'Anoeta', ciudad: 'San Sebastián', capacidad: '39,500' },
            'Sevilla FC': { fundacion: '1890', estadio: 'Ramón Sánchez-Pizjuán', ciudad: 'Sevilla', capacidad: '43,883' },
            'Real Betis': { fundacion: '1907', estadio: 'Benito Villamarín', ciudad: 'Sevilla', capacidad: '60,720' },
            'Villarreal CF': { fundacion: '1923', estadio: 'Estadio de la Cerámica', ciudad: 'Villarreal', capacidad: '23,500' },
            'Valencia CF': { fundacion: '1919', estadio: 'Mestalla', ciudad: 'Valencia', capacidad: '49,430' },
            'RC Celta de Vigo': { fundacion: '1923', estadio: 'Balaídos', ciudad: 'Vigo', capacidad: '29,000' },
            'RCD Mallorca': { fundacion: '1916', estadio: 'Visit Mallorca Stadium', ciudad: 'Palma', capacidad: '23,142' },
            'Girona FC': { fundacion: '1930', estadio: 'Montilivi', ciudad: 'Girona', capacidad: '14,624' },
            'CA Osasuna': { fundacion: '1920', estadio: 'El Sadar', ciudad: 'Pamplona', capacidad: '23,576' },
            'Getafe CF': { fundacion: '1983', estadio: 'Coliseum Alfonso Pérez', ciudad: 'Getafe', capacidad: '17,393' },
            'UD Las Palmas': { fundacion: '1949', estadio: 'Gran Canaria', ciudad: 'Las Palmas', capacidad: '32,400' },
            'RCD Espanyol': { fundacion: '1900', estadio: 'RCDE Stadium', ciudad: 'Barcelona', capacidad: '40,000' },
            'CD Leganés': { fundacion: '1928', estadio: 'Butarque', ciudad: 'Leganés', capacidad: '12,450' },
            'Real Valladolid': { fundacion: '1928', estadio: 'José Zorrilla', ciudad: 'Valladolid', capacidad: '27,846' },
            'Rayo Vallecano': { fundacion: '1924', estadio: 'Vallecas', ciudad: 'Madrid', capacidad: '14,708' }
        };
        
        return infoEquipos[nombreEquipo] || {};
    }

    // =============================================
    // 8. FUNCIÓN PARA CARGAR LOS JUGADORES
    // =============================================
    function cargarJugadores(equipo) {
        var porteros = filtrarJugadores(equipo.jugadors, 'porter');
        var defensas = filtrarJugadores(equipo.jugadors, 'defens');
        var centrocampistas = filtrarJugadores(equipo.jugadors, ['migcamp', 'medio', 'centrocamp']);
        var delanteros = filtrarJugadores(equipo.jugadors, 'davant');
    
        function mostrarJugadoresEnContenedor(contenedorId, jugadores, mensajeVacio) {
            var contenedor = document.getElementById(contenedorId);
            if (jugadores.length > 0) {
                contenedor.innerHTML = generarTarjetasJugadores(jugadores);
            } else {
                contenedor.innerHTML = '<p class="no-jugadores">' + mensajeVacio + '</p>';
            }
        }
    
        mostrarJugadoresEnContenedor('porteros', porteros, 'No hay porteros');
        mostrarJugadoresEnContenedor('defensas', defensas, 'No hay defensas');
        mostrarJugadoresEnContenedor('centrocampistas', centrocampistas, 'No hay centrocampistas');
        mostrarJugadoresEnContenedor('delanteros', delanteros, 'No hay delanteros');
    
        mostrarEntrenador(equipo.entrenador);
    }

    // =============================================
    // 9. FUNCIÓN PARA FILTRAR JUGADORES POR POSICIÓN
    // =============================================
    function filtrarJugadores(jugadores, terminos) {
        if (!Array.isArray(terminos)) {
            terminos = [terminos];
        }
    
        var jugadoresFiltrados = [];
    
        for (var i = 0; i < jugadores.length; i++) {
            var jugador = jugadores[i];
    
            if (!jugador.posicio) {
                continue;
            }
    
            var posicion = jugador.posicio.toLowerCase();
            var coincide = false;
    
            for (var j = 0; j < terminos.length; j++) {
                var termino = terminos[j];
    
                if (posicion.indexOf(termino) !== -1) {
                    coincide = true;
                    break;
                }
            }
    
            if (coincide) {
                jugadoresFiltrados.push(jugador);
            }
        }
    
        return jugadoresFiltrados;
    }
    

    // =============================================
    // 10. FUNCIÓN PARA GENERAR TARJETAS DE JUGADORES
    // =============================================
    function generarTarjetasJugadores(jugadores) {
        var fragment = document.createDocumentFragment();
        
        jugadores.forEach(function(jugador) {
            var jugadorCard = document.createElement('div');
            jugadorCard.className = 'jugador-card';
            
            if (jugador.dorsal) {
                var dorsalSpan = document.createElement('span');
                dorsalSpan.className = 'dorsal';
                dorsalSpan.textContent = jugador.dorsal;
                jugadorCard.appendChild(dorsalSpan);
            }
            
            var jugadorImg = document.createElement('div');
            jugadorImg.className = 'jugador-img';
            
            var img = document.createElement('img');
            img.src = jugador.foto;
            img.alt = jugador.nomPersona ? jugador.nomPersona : 'Jugador';
            img.onerror = function() {
                this.src = 'Assets/img/Jugadores/default_player.png';
            };
            
            jugadorImg.appendChild(img);
            jugadorCard.appendChild(jugadorImg);
            
            var jugadorInfo = document.createElement('div');
            jugadorInfo.className = 'jugador-info';
            
            var posicion = document.createElement('p');
            posicion.className = 'posicion';
            posicion.textContent = formatearPosicion(jugador.posicio);
            jugadorInfo.appendChild(posicion);
            
            var nombre = document.createElement('p');
            nombre.className = 'nombre';
            if (jugador.nomPersona) {
                nombre.textContent = jugador.nomPersona.toUpperCase();
            } else {
                nombre.textContent = 'Jugador';
            }
            jugadorInfo.appendChild(nombre);
            
            if (jugador.qualitat) {
                var qualitat = document.createElement('p');
                qualitat.className = 'qualitat';
                qualitat.textContent = 'Nivel: ' + jugador.qualitat;
                jugadorInfo.appendChild(qualitat);
            }
            
            jugadorCard.appendChild(jugadorInfo);
            fragment.appendChild(jugadorCard);
        });
        
        var tempDiv = document.createElement('div');
        tempDiv.appendChild(fragment);
        return tempDiv.innerHTML;
    }
    

    // =============================================
    // 11. FUNCIÓN PARA MOSTRAR EL ENTRENADOR
    // =============================================
    function mostrarEntrenador(entrenador) {
        const contenedorEntrenador = document.getElementById('entrenador');
        contenedorEntrenador.innerHTML = '';
        
        if (!entrenador || !entrenador.nomPersona) {
            const mensaje = document.createElement('p');
            mensaje.className = 'no-jugadores';
            mensaje.textContent = 'Sin entrenador';
            contenedorEntrenador.appendChild(mensaje);
            return;
        }
        
        const entrenadorCard = document.createElement('div');
        entrenadorCard.className = 'jugador-card entrenador';
        
        const jugadorImg = document.createElement('div');
        jugadorImg.className = 'jugador-img';
        
        const img = document.createElement('img');
        img.src = entrenador.foto;
        img.alt = entrenador.nomPersona;
        img.onerror = function() {
            this.src = 'Assets/img/Jugadores/default_coach.png';
        };
        
        jugadorImg.appendChild(img);
        entrenadorCard.appendChild(jugadorImg);
        
        const jugadorInfo = document.createElement('div');
        jugadorInfo.className = 'jugador-info';
        
        const posicion = document.createElement('p');
        posicion.className = 'posicion';
        posicion.textContent = 'ENTRENADOR';
        jugadorInfo.appendChild(posicion);
        
        const nombre = document.createElement('p');
        nombre.className = 'nombre';
        nombre.textContent = entrenador.nomPersona.toUpperCase();
        jugadorInfo.appendChild(nombre);
        
        entrenadorCard.appendChild(jugadorInfo);
        contenedorEntrenador.appendChild(entrenadorCard);
    }

    // =============================================
    // 12. FUNCIÓN PARA FORMATEAR LA POSICIÓN
    // =============================================
    function formatearPosicion(posicion) {
        if (!posicion) return '';
        
        const posiciones = {
            porter: 'PORTERO',
            defensa: 'DEFENSA',
            migcampista: 'CENTROCAMPISTA',
            davanter: 'DELANTERO',
            medio: 'CENTROCAMPISTA',
            delantero: 'DELANTERO',
            centrocampista: 'CENTROCAMPISTA'
        };
        
        const pos = posicion.toLowerCase();
        return posiciones[pos] || posicion.toUpperCase();
    }

    // =============================================
    // 13. FUNCIÓN PARA CONFIGURAR LOS BOTONES DE CATEGORÍAS
    // =============================================
    function configurarBotonesCategoria() {
        var botones = document.querySelectorAll('.categoria-btn');
    
        for (var i = 0; i < botones.length; i++) {
            var boton = botones[i];
    
            boton.addEventListener('click', function() {
                for (var j = 0; j < botones.length; j++) {
                    botones[j].classList.remove('active');
                }
    
                this.classList.add('active');
    
                var categoriaId = this.getAttribute('data-categoria');
                var categorias = document.querySelectorAll('.jugadores-categoria');
    
                for (var k = 0; k < categorias.length; k++) {
                    categorias[k].classList.remove('active');
                }
    
                var contenedorActivo = document.getElementById(categoriaId);
                if (contenedorActivo) {
                    contenedorActivo.classList.add('active');
                }
            });
        }
    }
    

    // =============================================
    // 14. FUNCIÓN PARA VOLVER A LA LISTA DE EQUIPOS
    // =============================================
    function volverAListaEquipos() {
        contenedorEquipos.style.display = 'grid';
        tituloPagina.style.display = 'block';
        contenedorDetalle.style.display = 'none';
        contenedorDetalle.innerHTML = '';
    }

    // =============================================
    // INICIAMOS LA APLICACIÓN
    // =============================================
    crearTarjetasEquipos();
});