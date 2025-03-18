-- Eliminar usuario
drop user 'superadministrador'@'localhost';
drop user 'adminEquips'@'localhost';
drop user 'adminLligues'@'localhost';

-- Super Administrador
create user 'superadministrador'@'localhost' identified by 'superadministrador12345'
password expire interval 30 day
    password history 2;
grant all privileges on *.* to 'superadministrador'@'localhost' with grant option;

-- Admin Equips
create user 'adminEquips'@'localhost' identified by 'adminEquips12345'
password expire;
grant select, update on football_manager.equips to 'adminEquips'@'localhost';
grant select, update on football_manager.estadis to 'adminEquips'@'localhost';
grant select, update on football_manager.persones to 'adminEquips'@'localhost';

-- Admin Lligues
create user 'adminLligues'@'localhost' identified by 'adminLligues12345';
grant select, insert, update on football_manager.jornades to 'adminEquips'@'localhost';
grant select, insert, update on football_manager.partits to 'adminEquips'@'localhost';
grant select, insert, update on football_manager.partits_gols to 'adminEquips'@'localhost';
grant select, insert, update on football_manager.lligues to 'adminEquips'@'localhost';
alter user 'adminLligues'@'localhost' with max_queries_per_hour 2;

-- Rol Periodista
create role 'periodista';
grant select on football_manager.* to 'periodista';

-- PERIODISTAS
-- Periodista Sport
create user 'periodistaSport'@'localhost' identified by 'periodistaSport12345';
grant 'periodista' to 'periodistaSport'@'localhost';

-- Periodista AS
create user 'periodistaAS'@'localhost' identified by 'periodistaAS12345';
grant 'periodista' to 'periodistaAS'@'localhost';

-- Periodista Mundo
create user 'periodistaMundo'@'localhost' identified by 'periodistaMundo12345';
grant 'periodista' to 'periodistaMundo'@'localhost';

-- CREAR VISITAS DEL 'periodistaAS'
-- Visita 1 (Los 5 maximos goleadores)
CREATE VIEW maximo_goleadores AS
    SELECT 
        persones.nom,
        persones.cognoms,
        COUNT(partits_gols.jugadors_id) AS total_gols
    FROM
        partits_gols
            INNER JOIN
        jugadors ON partits_gols.jugadors_id = jugadors.persones_id
            INNER JOIN
        persones ON jugadors.persones_id = persones.id
    GROUP BY persones.nom , persones.cognoms
    ORDER BY total_gols DESC
    LIMIT 5;

grant select on football_manager.maximo_goleadores to 'periodistaAS'@'localhost';

select * from maximo_goleadores;

-- Visita 2 ( 3 equipo_menos goleados)
CREATE VIEW equipos_menos_goleados AS
    SELECT 
        equips.nom AS nom_equip,
        SUM(CASE
            WHEN partits.equips_id_local = equips.id THEN partits.gols_visitant
            ELSE 0
        END) + SUM(CASE
            WHEN partits.equips_id_visitant = equips.id THEN partits.gols_local
            ELSE 0
        END) AS gols_totals_rebuts
    FROM
        partits
            INNER JOIN
        equips ON partits.equips_id_local = equips.id
            OR partits.equips_id_visitant = equips.id
    GROUP BY equips.nom
    ORDER BY gols_totals_rebuts ASC
    LIMIT 3;

grant select on football_manager.equipos_menos_goleados to 'periodistaAS'@'localhost';

select * from equipos_menos_goleados;

-- Visita 3 (Los 5 partidos con mas goles)
CREATE VIEW partidos_mas_goles AS
    SELECT 
        el.nom AS equip_local,
        ev.nom AS equip_visitant,
        partits.gols_local,
        partits.gols_visitant,
        (partits.gols_local + partits.gols_visitant) AS gols_total
    FROM
        partits
            INNER JOIN
        equips el ON partits.equips_id_local = el.id
            INNER JOIN
        equips ev ON partits.equips_id_visitant = ev.id
    ORDER BY gols_total DESC
    LIMIT 5;

grant select on football_manager.partidos_mas_goles to 'periodistaAS'@'localhost';

select * from partidos_mas_goles;

-- Visita 4 (los 3 equipos con mas penaltis)
CREATE VIEW equipos_con_mas_penaltis AS
    SELECT 
        equips.nom AS nom_equip,
        COUNT(partits_gols.partits_id) AS total_penaltis
    FROM
        partits_gols
            INNER JOIN
        partits ON partits_gols.partits_id = partits.id
            INNER JOIN
        equips ON partits.equips_id_local = equips.id
            OR partits.equips_id_visitant = equips.id
    WHERE
        partits_gols.es_penal = 1
    GROUP BY equips.nom
    ORDER BY total_penaltis DESC
    LIMIT 3;

grant select on football_manager.equipos_con_mas_penaltis to 'periodistaAS'@'localhost';

select * from equipos_con_mas_penaltis;

-- CONSULTES SQL
-- variables
 -- 88 goles totales // Girona
set @nom_lliga = 'La Liga EA Sports';
set @temporada = '2024';
set @equip_local = 'FC Barcelona';
set @equip_visitant = 'Real Madrid CF';
set @porter = 'Porter';

-- 1
SELECT 
    lligues.nom 'nom_lliga',
    lligues.temporada,
    equips.any_fundacio,
    equips.nom_president,
    ciutats.nom 'nom_ciutat',
    estadis.nom 'nom_estadi',
    estadis.num_espectadors
FROM
    lligues
        INNER JOIN
    participar_lligues ON lligues.id = participar_lligues.lligues_id
        INNER JOIN
    equips ON participar_lligues.equips_id = equips.id
        INNER JOIN
    ciutats ON equips.ciutats_id = ciutats.id
        INNER JOIN
    estadis ON equips.estadis_id = estadis.id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
        AND estadis.num_espectadors BETWEEN 30000 AND 50000
ORDER BY estadis.num_espectadors DESC;

-- 2
SELECT 
    ciutats.nom, equips.nom, persones.nom, persones.cognoms
FROM
    equips
        INNER JOIN
    ciutats ON equips.ciutats_id = ciutats.id
        INNER JOIN
    entrenar_equips ON equips.id = entrenar_equips.equips_id
        INNER JOIN
    entrenadors ON entrenar_equips.entrenadors_id = entrenadors.persones_id
        INNER JOIN
    persones ON entrenadors.persones_id = persones.id
WHERE
    ciutats.nom IN ('Barcelona' , 'Sevilla', 'Madrid')
        AND persones.nom NOT LIKE 'f%'
        AND persones.cognoms LIKE '%e%';

-- 3
SELECT 
    equips.nom,
    SUM(CASE
        WHEN partits.equips_id_local = equips.id THEN partits.punts_local
        WHEN partits.equips_id_visitant = equips.id THEN partits.punts_visitant
        ELSE 0
    END) AS puntuacio_total
FROM
    partits
        INNER JOIN
    jornades ON partits.jornades_id = jornades.id
        INNER JOIN
    lligues ON jornades.lligues_id = lligues.id
        INNER JOIN
    equips ON partits.equips_id_local = equips.id
        OR partits.equips_id_visitant = equips.id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
GROUP BY equips.id
ORDER BY puntuacio_total DESC;

-- 4
set @equip = 'CA Osasuna';
SELECT 
    @equip AS nom_equip,
    'Entrenador' AS tipus_persona,
    CONCAT(persones.nom, ' ', persones.cognoms) AS Nom_complet
FROM
    equips
        INNER JOIN
    entrenar_equips ON equips.id = entrenar_equips.equips_id
        INNER JOIN
    entrenadors ON entrenar_equips.entrenadors_id = entrenadors.persones_id
        INNER JOIN
    persones ON entrenadors.persones_id = persones.id
WHERE
    equips.nom = @equip 
UNION SELECT 
    @equip AS nom_equip,
    'Jugador' AS tipus_persona,
    CONCAT(persones.nom, ' ', persones.cognoms) AS Nom_complet
FROM
    equips
        INNER JOIN
    jugadors_equips ON equips.id = jugadors_equips.equips_id
        INNER JOIN
    jugadors ON jugadors_equips.jugadors_id = jugadors.persones_id
        INNER JOIN
    persones ON jugadors.persones_id = persones.id
WHERE
    equips.nom = @equip;

-- 5
SELECT 
    posicions.posicio,
    COUNT(jugadors.persones_id) AS nombre_jugadors
FROM
    jugadors
        INNER JOIN
    posicions ON jugadors.posicions_id = posicions.id
        INNER JOIN
    persones ON jugadors.persones_id = persones.id
        INNER JOIN
    jugadors_equips ON jugadors.persones_id = jugadors_equips.jugadors_id
        INNER JOIN
    equips ON jugadors_equips.equips_id = equips.id
        INNER JOIN
    participar_lligues ON equips.id = participar_lligues.equips_id
        INNER JOIN
    lligues ON participar_lligues.lligues_id = lligues.id
WHERE
    jugadors_equips.data_baixa IS NULL
        AND lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
GROUP BY posicions.posicio
ORDER BY posicions.posicio DESC;

-- 6
SELECT 
    jornades.data,
    jornades.jornada,
    e.nom AS equip_local,
    partits.gols_local,
    partits.gols_visitant,
    e2.nom AS equip_visitant
FROM
    jornades
        INNER JOIN
    lligues ON jornades.lligues_id = lligues.id
        INNER JOIN
    partits ON jornades.id = partits.jornades_id
        INNER JOIN
    equips e ON partits.equips_id_local = e.id
        INNER JOIN
    equips e2 ON partits.equips_id_visitant = e2.id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
        AND e.nom = @equip
ORDER BY jornades.data ASC;

-- 7
SELECT 
    jornades.data,
    jornades.jornada,
    e.nom AS equip_local,
    e2.nom AS equip_visitant,
    partits.gols_local,
    partits.gols_visitant,
    partits_gols.minut,
    persones.nom AS nom_jugador,
    persones.cognoms AS cognom_jugador,
    jugadors_equips.equips_id,
    partits_gols.es_penal
FROM
    partits
        INNER JOIN
    jornades ON partits.jornades_id = jornades.id
        INNER JOIN
    lligues ON jornades.lligues_id = lligues.id
        INNER JOIN
    equips e ON partits.equips_id_local = e.id
        INNER JOIN
    equips e2 ON partits.equips_id_visitant = e2.id
        INNER JOIN
    partits_gols ON partits.id = partits_gols.partits_id
        INNER JOIN
    jugadors ON partits_gols.jugadors_id = jugadors.persones_id
        INNER JOIN
    persones ON jugadors.persones_id = persones.id
        INNER JOIN
    jugadors_equips ON jugadors.persones_id = jugadors_equips.jugadors_id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
        AND e.nom = @equip_local
        AND e2.nom = @equip_visitant
ORDER BY partits_gols.minut DESC;

-- 8
SELECT 
    CONCAT(persones.nom, ' ', persones.cognoms) AS jugador,
    COUNT(partits_gols.jugadors_id) AS gols_marcats
FROM
    partits
        INNER JOIN
    jornades ON partits.jornades_id = jornades.id
        INNER JOIN
    lligues ON jornades.lligues_id = lligues.id
        INNER JOIN
    partits_gols ON partits.id = partits_gols.partits_id
        INNER JOIN
    jugadors ON partits_gols.jugadors_id = jugadors.persones_id
        INNER JOIN
    persones ON jugadors.persones_id = persones.id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
GROUP BY jugador
ORDER BY gols_marcats
LIMIT 10;

-- 9
SELECT 
    CONCAT(persones.nom, ' ', persones.cognoms) AS jugadors,
    persones.sou,
    persones.nivell_motivacio,
    persones.data_naixement
FROM
    persones
WHERE
    persones.sou BETWEEN 700000 AND 12000000
        AND persones.nivell_motivacio >= 85
        AND YEAR(persones.data_naixement) IN (1959 , 1985, 1992);

-- 10
SELECT 
    equips.nom,
    ROUND(AVG(jugadors.qualitat), 2) AS mitja_qualitat
FROM
    equips
        INNER JOIN
    jugadors_equips ON equips.id = jugadors_equips.equips_id
        INNER JOIN
    jugadors ON jugadors_equips.jugadors_id = jugadors.persones_id
        INNER JOIN
    participar_lligues ON equips.id = participar_lligues.equips_id
        INNER JOIN
    lligues ON participar_lligues.lligues_id = lligues.id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
GROUP BY equips.nom
HAVING mitja_qualitat > 80
ORDER BY mitja_qualitat DESC;

-- 11
SELECT 
    equips.nom, equips.filial_equips_id AS equip_filial
FROM
    equips
        INNER JOIN
    equips ef ON equips.filial_equips_id = ef.id;

-- 12
SELECT 
    equips.nom, COUNT(jugadors.persones_id) AS num_jugadors
FROM
    equips
        INNER JOIN
    jugadors_equips ON equips.id = jugadors_equips.equips_id
        INNER JOIN
    jugadors ON jugadors_equips.jugadors_id = jugadors.persones_id
WHERE
    jugadors.qualitat > 85
GROUP BY equips.nom
HAVING num_jugadors >= 3;

-- 13
SELECT 
    equips.nom AS equip,
    ROUND(AVG(YEAR(CURDATE()) - YEAR(persones.data_naixement)),
            2) AS mitjana_edat
FROM
    equips
        INNER JOIN
    jugadors_equips ON equips.id = jugadors_equips.equips_id
        INNER JOIN
    jugadors ON jugadors_equips.jugadors_id = jugadors.persones_id
        INNER JOIN
    persones ON jugadors.persones_id = persones.id
GROUP BY equips.nom
ORDER BY mitjana_edat DESC;

-- 14
SELECT 
    persones.nom,
    persones.cognoms,
    COUNT(persones.id) AS total_gols
FROM
    persones
        INNER JOIN
    jugadors ON persones.id = jugadors.persones_id
        INNER JOIN
    partits_gols ON jugadors.persones_id = partits_gols.jugadors_id
        INNER JOIN
    partits ON partits_gols.partits_id = partits.id
        INNER JOIN
    jornades ON partits.jornades_id = jornades.id
        INNER JOIN
    lligues ON jornades.lligues_id = lligues.id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
GROUP BY persones.nom , persones.cognoms
ORDER BY total_gols DESC
LIMIT 1;

-- 15
SELECT 
    posicions.posicio,
    jugadors.dorsal,
    persones.nom,
    persones.cognoms,
    equips.nom AS equip,
    COUNT(partits_gols.jugadors_id) AS gols
FROM
    persones
        INNER JOIN
    jugadors ON persones.id = jugadors.persones_id
        INNER JOIN
    jugadors_equips ON jugadors.persones_id = jugadors_equips.jugadors_id
        INNER JOIN
    equips ON jugadors_equips.equips_id = equips.id
        INNER JOIN
    participar_lligues ON equips.id = participar_lligues.equips_id
        INNER JOIN
    lligues ON participar_lligues.lligues_id = lligues.id
        INNER JOIN
    posicions ON jugadors.posicions_id = posicions.id
        INNER JOIN
    partits_gols ON jugadors.persones_id = partits_gols.jugadors_id
        INNER JOIN
    partits ON partits_gols.partits_id = partits.id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
        AND posicions.posicio IN ('Defensa')
GROUP BY posicions.posicio , jugadors.dorsal , persones.nom , persones.cognoms , equips.nom
HAVING gols > 5;

-- 16
SELECT 
    @equip AS nom_equip,
    SUM(CASE
        WHEN e.nom = @equip THEN partits.gols_local
        WHEN e2.nom = @equip THEN partits.gols_visitant
        ELSE 0
    END) AS gols_totals
FROM
    partits
        INNER JOIN
    equips e ON partits.equips_id_local = e.id
        INNER JOIN
    equips e2 ON partits.equips_id_visitant = e2.id
        INNER JOIN
    jornades ON partits.jornades_id = jornades.id
        INNER JOIN
    lligues ON jornades.lligues_id = lligues.id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
        AND (e.nom = @equip OR e2.nom = @equip)
GROUP BY nom_equip;

-- 17
SELECT 
    equips.nom AS equip,
    SUM(CASE
        WHEN partits.equips_id_local = equips.id THEN partits.gols_local
        WHEN partits.equips_id_visitant = equips.id THEN partits.gols_visitant
        ELSE 0
    END) AS gols_total
FROM
    partits
        INNER JOIN
    equips ON partits.equips_id_local = equips.id
        OR partits.equips_id_visitant = equips.id
        INNER JOIN
    jornades ON partits.jornades_id = jornades.id
        INNER JOIN
    lligues ON jornades.lligues_id = lligues.id
WHERE
    lligues.nom = @nom_lliga
        AND lligues.temporada = @temporada
GROUP BY equips.nom
HAVING gols_total >= (SELECT 
        SUM(CASE
                WHEN partits.equips_id_local = e1.id THEN partits.gols_local
                WHEN partits.equips_id_visitant = e1.id THEN partits.gols_visitant
                ELSE 0
            END)
    FROM
        partits
            INNER JOIN
        equips e1 ON partits.equips_id_local = e1.id
            OR partits.equips_id_visitant = e1.id
            INNER JOIN
        jornades ON partits.jornades_id = jornades.id
            INNER JOIN
        lligues ON jornades.lligues_id = lligues.id
    WHERE
        lligues.nom = @nom_lliga
            AND lligues.temporada = @temporada
            AND e1.nom = @equip
    GROUP BY e1.nom)
ORDER BY gols_total DESC;

-- Consultes de manipulació de dades;
-- 1
select * from partits;
 
UPDATE partits
        INNER JOIN
    jornades ON partits.jornades_id = jornades.id
        INNER JOIN
    lligues ON jornades.lligues_id = lligues.id 
SET 
    partits.gols_local = 0,
    partits.gols_visitant = 0,
    partits.punts_local = 1,
    partits.punts_visitant = 1
WHERE
    jornades.jornada = 5
        AND lligues.nom = @nom_lliga
        AND lligues.tmeporada = @temporada;

-- 2
select * from ciutats;
select * from equips;

DELETE ciutats FROM ciutats
        LEFT JOIN
    equips ON ciutats.id = equips.ciutats_id 
WHERE
    equips.ciutats_id IS NULL;

-- 3
select * from persones;

UPDATE persones 
SET 
    nivell_motivacio = nivell_motivacio * 0.9
WHERE
    sou < 5000000
        AND YEAR(CURDATE()) - YEAR(data_naixement) >= 35;

-- 4
select * from jugadors;
select * from posicions;
select * from equips;

set @posicio = 'Porter';

UPDATE jugadors
        INNER JOIN
    persones ON jugadors.persones_id = persones.id
        INNER JOIN
    posicions ON jugadors.posicions_id = posicions.id
        INNER JOIN
    jugadors_equips ON jugadors.persones_id = jugadors_equips.jugadors_id
        INNER JOIN
    equips ON jugadors_equips.equips_id = equips.id 
SET 
    jugadors.qualitat = jugadors.qualitat + 3
WHERE
    posicions.posicio = @posicio
        AND equips.filial_equips_id IS NULL;

-- 5
select * from jugadors;
select * from jugadors_equips;

alter table jugadors
add column disponible tinyint not null default 0;

UPDATE jugadors
        INNER JOIN
    jugadors_equips ON jugadors.persones_id = jugadors_equips.jugadors_id 
SET 
    jugadors.disponible = 1
WHERE
    jugadors_equips.data_fitxatge BETWEEN '2024-01-01' AND '2024-03-31';

-- select de tablas
select * from lligues;
select * from jornades;
select * from partits;
select * from partits_gols;
select * from jugadors;
select * from posicions;
select * from jugadors_equips;
select * from equips;
select * from participar_lligues;
select * from ciutats;
select * from estadis;
select * from entrenar_equips;
select * from entrendors;
select * from persones;

-- 6
insert into lligues (nom, temporada) 
values ('Premier League',2024);

insert into equips(nom, any_fundacio, nom_president, ciutats_id, estadis_id)
values
('Liverpool', 1892, 'Tom Werner', 17, 25),
('Arsenal', 1886, 'Sir Chips Keswick', 18, 26),
('Chelsea', 1905, 'Todd Boehly', 18, 27),
('Manchester City', 1880, 'Khaldoon Al Mubarak', 19, 28),
('Brighton', 1901, 'Tony Bloom', 20, 29),
('Aston Villa', 1874, 'Nassef Sawiris', 21, 30);

insert into jornades (id, jornada, data, lligues_id)
values
(39, 1, '2024-08-20', 2),
(40, 1, '2024-08-20', 2),
(41, 1, '2024-08-20', 2),
(42, 1, '2024-08-20', 2),
(43, 1, '2024-08-20', 2),
(44, 1, '2024-08-20', 2);

select * from jornades;

-- 7
insert into jornades (id, jornada, data, lligues_id)
values
(45, 39, 2025-05-13, 1);

select * from equips;
insert into partits (id, gols_local, gols_visitant, punts_local, punts_visitant, jornades_id, equips_id_local, equips_id_visitant)
values
(null, 4, 2, 3, 0, 45, 1, 2);

-- 8
insert into ciutats (id, nom)
values 
(17, 'Liverpool');

insert into estadis (id, nom, num_espectadors)
values
(25, 'Anfield', 54000);

insert into equips(nom, any_fundacio, nom_president, ciutats_id, estadis_id)
values
('Liverpool', 1892, 'Tom Werner', 17, 25);

insert into persones (id, nom, cognoms, data_naixement, nivell_motivacio, sou, tipus_persona)
values
(121, 'Alisson', 'Becker', '1992-10-02', 88, 7800000, 'jugador'),
(122, 'Mohamed', 'Salah', '1992-06-15', 90, 18200000, 'jugador'),
(123, 'Jürgen', 'Klopp', '1967-06-16', 85, 16000000, 'entrenador');

insert into entrenadors (persones_id, nom_tornejos, es_selecionador)
values 
(123, 15, 0);

insert into jugadors(persones_id, dorsal, qualitat, posicions_id)
values 
(121, 1, 85, 1),
(122, 11, 90, 4);