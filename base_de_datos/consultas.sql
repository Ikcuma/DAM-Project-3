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

grant select on football_manager.equipos_menos_goleados to 'periosistaAS'@'localhost';

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

-- 3 ¿?
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
