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
create view maximo_goleadores
as select persones.nom, persones.cognoms, count(partits_gols.jugadors_id) as total_gols
from partits_gols
inner join jugadors on partits_gols.jugadors_id = jugadors.persones_id
inner join persones on jugadors.persones_id = persones.id
group by persones.nom, persones.cognoms
order by total_gols desc
limit 5;

grant select on football_manager.maximo_goleadores to 'periodistaAS'@'localhost';

select * from maximo_goleadores;

-- Visita 2 ( 3 equipo_menos goleados)
create view equipos_menos_goleados
as select equips.nom as nom_equip, 
sum( case when partits.equips_id_local = equips.id then partits.gols_visitant else 0 end) +
sum(case when partits.equips_id_visitant = equips.id then partits.gols_local else 0 end) as gols_totals_rebuts
from partits
inner join equips on partits.equips_id_local = equips.id or partits.equips_id_visitant = equips.id
group by equips.nom
order by gols_totals_rebuts asc
limit 3;

grant select on football_manager.equipos_menos_goleados to 'periodistaAS'@'localhost';

select * from equipos_menos_goleados;

-- Visita 3 (Los 5 partidos con mas goles)
create view partidos_mas_goles
as select el.nom as equip_local, ev.nom as equip_visitant, partits.gols_local, partits.gols_visitant, (partits.gols_local + partits.gols_visitant) as gols_total
from partits
inner join equips el on partits.equips_id_local = el.id
inner join equips ev on partits.equips_id_visitant = ev.id
order by gols_total desc
limit 5;

grant select on football_manager.partidos_mas_goles to 'periodistaAS'@'localhost';

select * from partidos_mas_goles;

-- Visita 4 (los 3 equipos con mas penaltis)
create view equipos_con_mas_penaltis
as select equips.nom as nom_equip, count(partits_gols.partits_id) as total_penaltis
from partits_gols 
inner join partits on partits_gols.partits_id = partits.id
inner join equips on partits.equips_id_local = equips.id or partits.equips_id_visitant = equips.id
where partits_gols.es_penal = 1
group by equips.nom
order by total_penaltis desc
limit 3;

grant select on football_manager.equipos_con_mas_penaltis to 'periodistaAS'@'localhost';

select * from equipos_con_mas_penaltis;

-- CONSULTES SQL
-- variables
set @equip = 'Girona FC'; -- 88 goles totales
set @nom_lliga = 'La Liga EA Sports';
set @temporada = '2024';
set @equip_local = 'FC Barcelona';
set @equip_visitant = 'Real Madrid CF';
set @porter = 'Porter';

-- 1
select lligues.nom 'nom_lliga', lligues.temporada, equips.any_fundacio, equips.nom_president, ciutats.nom 'nom_ciutat', estadis.nom 'nom_estadi', estadis.num_espectadors
from lligues
inner join participar_lligues on lligues.id = participar_lligues.lligues_id
inner join equips on participar_lligues.equips_id = equips.id
inner join ciutats on equips.ciutats_id = ciutats.id
inner join estadis on equips.estadis_id = estadis.id
where lligues.nom = @nom_lliga and lligues.temporada = @temporada and estadis.num_espectadors between 30000 and 50000
order by estadis.num_espectadors desc;

-- 2
select ciutats.nom, equips.nom, persones.nom, persones.cognoms
from equips
inner join ciutats on equips.ciutats_id = ciutats.id
inner join entrenar_equips on equips.id = entrenar_equips.equips_id
inner join entrenadors on entrenar_equips.entrenadors_id = entrenadors.persones_id
inner join persones on entrenadors.persones_id = persones.id
where ciutats.nom in ('Barcelona','Sevilla','Madrid') and persones.nom not like 'f%' and persones.cognoms like '%e%';

-- 3 UTILIZAR EL CASE AND WHEN
select lligues.nom 'nom_lliga', lligues.temporada 'temporada', equips.nom 'nom_equips', sum(distinct p.punts_local) as puntos_total
from equips
inner join partits p on equips.id = p.equips_id_local
inner join partits p2 on equips.id = p2.equips_id_visitant
inner join jornades on p.jornades_id = jornades.id
inner join lligues on jornades.lligues_id = lligues.id
group by lligues.nom, lligues.temporada, equips.nom
order by puntos_total desc;

select lligues.nom 'nom_lliga', lligues.temporada 'temporada', equips.nom 'nom_equips', sum((partits.equips_id_local = equips.id) * partits.punts_local + (partits.equips_id_visitant = equips.id) * partits.punts_visitant) as puntos_total
from equips
inner join partits on  equips.id in (partits.equips_id_local, partits.equips_id_visitant)
inner join jornades on partits.jornades_id = jornades.id
inner join lligues on jornades.lligues_id = lligues.id
group by lligues.nom, lligues.temporada, equips.nom
order by puntos_total desc;

-- 4
select @equip as nom_equip, 'Entrenador' as tipus_persona, concat(persones.nom, ' ', persones.cognoms) as Nom_complet
from equips
inner join entrenar_equips on equips.id = entrenar_equips.equips_id
inner join entrenadors on entrenar_equips.entrenadors_id = entrenadors.persones_id
inner join persones on entrenadors.persones_id = persones.id
where equips.nom = @equip

union

select @equip as nom_equip, 'Jugador' as tipus_persona, concat(persones.nom, ' ', persones.cognoms) as Nom_complet
from equips
inner join jugadors_equips on equips.id = jugadors_equips.equips_id
inner join jugadors on jugadors_equips.jugadors_id = jugadors.persones_id
inner join persones on jugadors.persones_id = persones.id
where equips.nom = @equip;

-- 5
select posicions.posicio, count(jugadors.persones_id) as nombre_jugadors
from jugadors
inner join posicions on jugadors.posicions_id = posicions.id
inner join persones on jugadors.persones_id = persones.id
inner join jugadors_equips on jugadors.persones_id = jugadors_equips.jugadors_id
inner join equips on jugadors_equips.equips_id = equips.id
inner join participar_lligues on equips.id = participar_lligues.equips_id
inner join lligues on participar_lligues.lligues_id = lligues.id
where jugadors_equips.data_baixa is null and lligues.nom = @nom_lliga and lligues.temporada = @temporada
group by posicions.posicio
order by posicions.posicio desc;

-- 6
select jornades.data, jornades.jornada, e.nom as equip_local , partits.gols_local, partits.gols_visitant, e2.nom as equip_visitant
from jornades
inner join lligues on jornades.lligues_id = lligues.id
inner join partits on jornades.id = partits.jornades_id
inner join equips e on partits.equips_id_local = e.id
inner join equips e2 on partits.equips_id_visitant = e2.id
where lligues.nom = @nom_lliga and lligues.temporada = @temporada and e.nom = @equip
order by jornades.data asc;

-- 7
select jornades.data, jornades.jornada, e.nom as equip_local, e2.nom as equip_visitant, partits.gols_local, partits.gols_visitant, partits_gols.minut, persones.nom as nom_jugador, persones.cognoms as cognom_jugador, jugadors_equips.equips_id, partits_gols.es_penal
from partits
inner join jornades on partits.jornades_id = jornades.id
inner join lligues on jornades.lligues_id = lligues.id
inner join equips e on partits.equips_id_local = e.id
inner join equips e2 on partits.equips_id_visitant = e2.id
inner join partits_gols on partits.id = partits_gols.partits_id
inner join jugadors on partits_gols.jugadors_id = jugadors.persones_id
inner join persones on jugadors.persones_id = persones.id
inner join jugadors_equips on jugadors.persones_id = jugadors_equips.jugadors_id
where lligues.nom = @nom_lliga and lligues.temporada = @temporada and e.nom = @equip_local and e2.nom = @equip_visitant
order by partits_gols.minut desc;

-- 8
select concat (persones.nom, ' ' , persones.cognoms) as jugador, count(partits_gols.jugadors_id) as gols_marcats
from partits
inner join jornades on partits.jornades_id = jornades.id
inner join lligues on jornades.lligues_id = lligues.id
inner join partits_gols on partits.id = partits_gols.partits_id
inner join jugadors on partits_gols.jugadors_id = jugadors.persones_id
inner join persones on jugadors.persones_id = persones.id
where lligues.nom = @nom_lliga and lligues.temporada = @temporada
group by jugador
order by gols_marcats
limit 10;

-- 9
select concat(persones.nom, ' ' , persones.cognoms) as jugadors, persones.sou, persones.nivell_motivacio, persones.data_naixement
from persones
where persones.sou between 700000 and 12000000
and persones.nivell_motivacio >= 85
and year(persones.data_naixement) in (1959, 1985, 1992);

-- 10
select equips.nom, round(avg(jugadors.qualitat), 2) as mitja_qualitat
from equips
inner join jugadors_equips on equips.id = jugadors_equips.equips_id
inner join jugadors on jugadors_equips.jugadors_id = jugadors.persones_id
inner join participar_lligues on equips.id = participar_lligues.equips_id
inner join lligues on participar_lligues.lligues_id = lligues.id
where lligues.nom = @nom_lliga
and lligues.temporada = @temporada
group by equips.nom
having mitja_qualitat > 80
order by mitja_qualitat desc;

-- 11
select equips.nom, equips.filial_equips_id as equip_filial
from equips
inner join equips ef on equips.filial_equips_id = ef.id;

-- 12
select equips.nom, count(jugadors.persones_id) as num_jugadors
from equips
inner join jugadors_equips on equips.id = jugadors_equips.equips_id
inner join jugadors on jugadors_equips.jugadors_id = jugadors.persones_id
where jugadors.qualitat > 85
group by equips.nom
having num_jugadors >= 3;

-- 13
select equips.nom as equip, round(avg(year(curdate()) - year(persones.data_naixement)), 2) as mitjana_edat
from equips
inner join jugadors_equips on equips.id = jugadors_equips.equips_id
inner join jugadors on jugadors_equips.jugadors_id = jugadors.persones_id
inner join persones on jugadors.persones_id = persones.id
group by equips.nom
order by mitjana_edat desc;

-- 14
select persones.nom, persones.cognoms, count(persones.id) as total_gols
from persones
inner join jugadors on persones.id = jugadors.persones_id
inner join partits_gols on jugadors.persones_id = partits_gols.jugadors_id
inner join partits on partits_gols.partits_id = partits.id
inner join jornades on partits.jornades_id = jornades.id
inner join lligues on jornades.lligues_id = lligues.id
where lligues.nom = @nom_lliga
and lligues.temporada = @temporada
group by persones.nom, persones.cognoms
order by total_gols desc
limit 1;

-- 15
select posicions.posicio, jugadors.dorsal, persones.nom, persones.cognoms, equips.nom as equip, count(partits_gols.jugadors_id) as gols
from persones
inner join jugadors on persones.id = jugadors.persones_id
inner join jugadors_equips on jugadors.persones_id = jugadors_equips.jugadors_id
inner join equips on jugadors_equips.equips_id = equips.id
inner join participar_lligues on equips.id = participar_lligues.equips_id
inner join lligues on participar_lligues.lligues_id = lligues.id
inner join posicions on jugadors.posicions_id = posicions.id
inner join partits_gols on jugadors.persones_id = partits_gols.jugadors_id
inner join partits on partits_gols.partits_id = partits.id
where lligues.nom = @nom_lliga
and lligues.temporada = @temporada
and posicions.posicio in ('Defensa')
group by posicions.posicio, jugadors.dorsal, persones.nom, persones.cognoms, equips.nom
having gols > 5;

-- 16 mas o menos
select 
	@equip as nom_equip, 
		sum(
			case
				when e.nom = @equip then partits.gols_local
                when e2.nom = @equip then partits.gols_visitant
                else 0
			end
        ) as gols_totals
from partits
inner join equips e on partits.equips_id_local = e.id
inner join equips e2 on partits.equips_id_visitant = e2.id
inner join jornades on partits.jornades_id = jornades.id
inner join lligues on jornades.lligues_id = lligues.id
where lligues.nom = @nom_lliga
and lligues.temporada = @temporada
and (e.nom = @equip or e2.nom = @equip)
group by nom_equip;

-- 17 ¿?
select equips.nom as equip, 
		sum(
			case
				when partits.equips_id_local = equips.id then partits.gols_local
                when partits.equips_id_visitant = equips.id then partits.gols_visitant
                else 0
			end
        ) as gols_total
from partits
inner join equips on partits.equips_id_local = equips.id or partits.equips_id_visitant = equips.id
inner join jornades on partits.jornades_id = jornades.id
inner join lligues on jornades.lligues_id = lligues.id
where lligues.nom = @nom_lliga
and lligues.temporada = @temporada
group by equips.nom
having gols_total >= (select sum(case when partits.equips_id_local = e1.id then partits.gols_local when partits.equips_id_visitant = e1.id then partits.gols_visitant else 0 end)
    from partits
    inner join equips e1 on partits.equips_id_local = e1.id or partits.equips_id_visitant = e1.id
    inner join jornades on partits.jornades_id = jornades.id
    inner join lligues on jornades.lligues_id = lligues.id
    where lligues.nom = @nom_lliga
    and lligues.temporada = @temporada
    and e1.nom = @equip
    group by e1.nom
    )
order by gols_total desc;

-- Consultes de manipulació de dades;
-- 1
select * from partits;
 
update partits
inner join jornades on partits.jornades_id = jornades.id
inner join lligues on jornades.lligues_id = lligues.id
set partits.gols_local = 0, partits.gols_visitant = 0, partits.punts_local = 1, partits.punts_visitant = 1
where jornades.jornada = 5
	and lligues.nom = @nom_lliga
    and lligues.tmeporada = @temporada;

-- 2
select * from ciutats;
select * from equips;

delete ciutats
from ciutats
left join equips on ciutats.id = equips.ciutats_id
where equips.ciutats_id is null;

-- 3
select * from persones;

update persones
set nivell_motivacio = nivell_motivacio * 0.9
where sou < 5000000 and year(curdate()) - year(data_naixement) >= 35;

-- 4
select * from jugadors;
select * from posicions;
select * from equips;

set @posicio = 'Porter';

update jugadors
inner join persones on jugadors.persones_id = persones.id
inner join posicions on jugadors.posicions_id = posicions.id
inner join jugadors_equips on jugadors.persones_id = jugadors_equips.jugadors_id
inner join equips on jugadors_equips.equips_id = equips.id
set jugadors.qualitat = jugadors.qualitat + 3
where posicions.posicio = @posicio
and equips.filial_equips_id is null;

-- 5
select * from jugadors;
select * from jugadors_equips;

alter table jugadors
add column disponible tinyint not null default 0;

update jugadors
inner join jugadors_equips on jugadors.persones_id = jugadors_equips.jugadors_id
set jugadors.disponible = 1
where jugadors_equips.data_fitxatge between '2024-01-01' and '2024-03-31';

-- 6
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


insert into lligues (nom, temporada) 
values ('Premier League',2024);

insert into ciutats (id, nom)
values 
(17, 'Liverpool'),
(18, 'Londres'),
(19, 'Manchester'),
(20, 'Brighton'),
(21, 'Birmingham');

insert into estadis (id, nom, num_espectadors)
values
(25, 'Anfield', 54000),
(26, 'Emirates Stadium', 60000),
(27, 'Stamford Bridge', 40000),
(28, 'Etihand Stadium', 53000),
(29, 'Amex Stadium', 30000),
(30, 'Villa Park', 42000);

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

insert into partits (id, gols_local, gols_visitant, punts_local, punts_visitant, jornades_id, equips_id_local, equips_id_visitant)
values
-- jornada 1
(null, 4, 2, 3, 0, 39, 1, 6),
(null, 2, 5, 0, 3, 39, 2, 5),
(null, 3, 3, 1, 1, 39, 3, 4),
-- jornada 2
(null, 0, 2, 0, 1, 40, 6, 4),
(null, 1, 1, 1, 1, 40, 5, 3),
(null, 2, 4, 0, 1, 40, 1, 2),
-- jornada 3
(null, 0, 0, 1, 1, 41, 3, 6),
(null, 4, 4, 1, 1, 41, 4, 2),
(null, 3, 2, 3, 0, 41, 5, 1),
-- jornada 4
(null, 2, 5, 0, 3, 42, 6, 5),
(null, 0, 0, 1, 1, 42, 1, 4),
(null, 3, 3, 1, 1, 42, 2, 3),
-- jornada 5
(null, 5, 1, 3, 0, 43, 4, 5),
(null, 2, 2, 1, 1, 43, 3, 1),
(null, 4, 1, 3, 0, 43, 6, 2),
-- jornada 6
(null, 2, 3, 0, 3, 44, 2, 1),
(null, 1, 5, 0, 3, 44, 5, 6),
(null, 4, 4, 1, 1, 44, 4, 3);

insert into persones (id, nom, cognoms, data_naixement, nivell_motivacio, sou, tipus_persona)
values
(121, 'Alisson', 'Becker', '1992-10-02', 88, 7800000, 'jugador'),
(122, 'Mohamed', 'Salah', '1992-06-15', 90, 18200000, 'jugador'),
(123, 'Jürgen', 'Klopp', '1967-06-16', 85, 16000000, 'entrenador'),
(124, 'David', 'Raya', '1995-09-15', 80, 1560000, 'jugador'),
(125, 'Martin', 'Odegaard', '1998-12', 84, 6000000, 'jugador'),
(126, 'Mikel', 'Arteta', '1982-03-26', 92, 10000000, 'entrenador'),
(127, 'Dorde', 'Petrovic', '1999-10-08', 81, 1300000, 'jugador'),
(128, 'Moises', 'Caicedo', '2001-11-02', 95, 6000000, 'jugador'),
(129, 'Mauricio', 'Pochettino', '1972-03-02', 90, 8000000, 'entrenador'),
(130, 'Ederson', 'Moraes', '1993-08-17', 86, 5200000, 'jugador'),
(131, 'Erling', 'Haaland', '2000-07-21', 88, 22400000, 'jugador'),
(132, 'Pep', 'Guardiola', '1971-01-18', 94, 20000000, 'entrenador'),
(133, 'Bart', 'Verbruggen', '2002-08-18', 92, 1040000, 'jugador'),
(134, 'Pervis', 'Estupiñan', '1998-01-21', 92, 2300000, 'jugador'),
(135, 'Roberto', 'Zerbi', '1979-06-06', 82, 2000000, 'entrenador'),
(136, 'Emiliano', 'Martínez', '1992-09-02', 93, 5200000, 'jugador'),
(137, 'Ollie', 'Watkins', '1995-12-30', 87, 3900000, 'jugador'),
(138, 'Unai', 'Emery', '1971-11-03', 92, 8000000, 'entrenador');

insert into entrenadors
values 
(123, 15, 0),
(126, 15, 0),
(129, 15, 0),
(132, 15, 0),
(135, 15, 0),
(138, 15, 0);

insert into jugadors(persones_id, dorsal, qualitat, posicions_id)
values 
(121, 1, 85, 1),
(122, 11, 90, 4),
(124, 22, 88, 1),
(125, 8, 82, 3),
(127, 30, 84, 1),
(128, 25, 95, 3),
(130, 31, 89, 1),
(131, 9, 84, 4),
(133, 13, 91, 1),
(134, 30, 95, 2),
(136, 1, 87, 1),
(137, 11, 77, 4);