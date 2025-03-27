document.addEventListener('DOMContentLoaded', function() {
    // Lista de equipos con sus escudos
    const equipos = [
        { nombre: 'Deportivo Alavés', escudo: 'alaves.png', pagina: 'Deportivo_Alavéz.html' },
        { nombre: 'Athletic Club', escudo: 'athletic.png', pagina: 'Athletic_Club.html' },
        { nombre: 'Atlético Madrid', escudo: 'atlmadrid.png', pagina: 'Atlético_Madrid.html' },
        { nombre: 'FC Barcelona', escudo: 'barcelona.png', pagina: 'Barcelona.html' },
        { nombre: 'Real Betis', escudo: 'betis.png', pagina: 'Real_Betis.html' },
        { nombre: 'Celta de Vigo', escudo: 'celta.png', pagina: 'Celta_de_Vigo.html' },
        { nombre: 'Espanyol', escudo: 'espanyol.png', pagina: 'Espanyol.html' },
        { nombre: 'Getafe CF', escudo: 'getafe.png', pagina: 'Getafe_CF.html' },
        { nombre: 'Girona FC', escudo: 'girona.png', pagina: 'Girona_FC.html' },
        { nombre: 'CD Leganés', escudo: 'leganes.png', pagina: 'CD_Leganes.html' },
        { nombre: 'RCD Mallorca', escudo: 'mallorca.png', pagina: 'RCD_Mallorca.html' },
        { nombre: 'CA Osasuna', escudo: 'osasuna.png', pagina: 'CA_Osasuna.html' },
        { nombre: 'Rayo Vallecano', escudo: 'rayovallecano.png', pagina: 'Rayo_Vallecano.html' },
        { nombre: 'Real Madrid', escudo: 'realmadrid.png', pagina: 'Real_Madrid.html' },
        { nombre: 'Real Sociedad', escudo: 'realsociedad.png', pagina: 'Real_Sociedad.html' }, // Nuevo equipo añadido
        { nombre: 'Sevilla FC', escudo: 'sevilla.png', pagina: 'Sevilla_FC.html' },
        { nombre: 'UD Las Palmas', escudo: 'udlaspalmas.png', pagina: 'UD_Las_Palmas.html' },
        { nombre: 'Valencia CF', escudo: 'valencia.png', pagina: 'Valencia_CF.html' },
        { nombre: 'Real Valladolid', escudo: 'valladolid.png', pagina: 'Real_Valladolid.html' },
        { nombre: 'Villarreal CF', escudo: 'villarreal.png', pagina: 'Villarreal.html' }
    ];

    // Colores para cada equipo (añadido color para Real Sociedad)
    const teamColors = {
        'Deportivo Alavés': { bg: '#005bab', text: '#005bab' },
        'Athletic Club': { bg: '#ed1c24', text: '#ed1c24' },
        'Atlético Madrid': { bg: '#cb3524', text: '#cb3524' },
        'FC Barcelona': { bg: '#a50044', text: '#a50044' },
        'Real Betis': { bg: '#00954e', text: '#00954e' },
        'Celta de Vigo': { bg: '#aaddff', text: '#aaddff' },
        'Espanyol': { bg: '#0060a8', text: '#0060a8' },
        'Getafe CF': { bg: '#2c5ba8', text: '#2c5ba8' },
        'Girona FC': { bg: '#ff0000', text: '#ff0000' },
        'CD Leganés': { bg: '#00a8e1', text: '#00a8e1' },
        'RCD Mallorca': { bg: '#ff0000', text: '#ff0000' },
        'CA Osasuna': { bg: '#0a3a8a', text: '#0a3a8a' },
        'Rayo Vallecano': { bg: '#c60c30', text: '#c60c30' },
        'Real Madrid': { bg: '#00529f', text: '#00529f' },
        'Real Sociedad': { bg: '#0073bc', text: '#ffffff' }, // Color azul característico de la Real
        'Sevilla FC': { bg: '#d81e05', text: '#d81e05' },
        'UD Las Palmas': { bg: '#0077c8', text: '#0077c8' },
        'Valencia CF': { bg: '#f7a11a', text: '#f7a11a' },
        'Real Valladolid': { bg: '#5a2894', text: '#5a2894' },
        'Villarreal CF': { bg: '#ffcd00', text: '#00529f' }
    };

    const grid = document.querySelector('.equipos-grid');

    // Generar tarjetas de equipos dinámicamente
    equipos.forEach(equipo => {
        const cardLink = document.createElement('a');
        cardLink.href = equipo.pagina;
        cardLink.className = 'equipo-card';
        
        const color = teamColors[equipo.nombre] || { bg: '#f5f5f5', text: '#333333' };
        cardLink.style.setProperty('--team-color', color.bg);
        cardLink.style.setProperty('--team-text-color', color.text);
        
        cardLink.innerHTML = `
            <img src="assets/img/Escudos/${equipo.escudo}" alt="${equipo.nombre}" class="equipo-escudo">
            <span class="equipo-nombre">${equipo.nombre}</span>
        `;
        
        grid.appendChild(cardLink);
    });
});