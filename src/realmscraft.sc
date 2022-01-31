import('rc_spells', '_revive_corpse');
import('rc_spells', '_ward_undead');
import('rc_spells', '_light');
import('rc_spells', '_light_cooldown');
import('rc_spells', '_light_cooldown_pos');
import('rc_spells', '_deathwatch');

//********************************************************
//              REALMSCRAFT GENERAL MECHANICS
//********************************************************

__on_tick() -> (
    _constant_effects();
);

__on_start() -> (
    _realmscraft_setup();
    run('script load rc_spells');
    run('script load rc_death');
);

__on_player_connects(player) -> (
    schedule(0,'_player_setup', player);
);

_realmscraft_setup() -> (
    //prepare scores
    scoreboard_add('regen_id');
    scoreboard_add('raise_dead_count');
    scoreboard_add('mm_uses');

    //gamerules
    //world
    run('gamerule commandBlockOutput false');
    run('gamerule showDeathMessages false');
    run('gamerule doDaylightCycle false');
    run('gamerule doWeatherCycle false');
    run('gamerule doFireTick false');

    //players
    run('gamerule naturalRegeneration false');
    run('gamerule doImmediateRespawn true');
    run('gamerule keepInventory true');

    //mobs
    run('gamerule doMobSpawning false');
    run('gamerule mobGriefing false');

    //completion message
    print('RealmsCraft setup complete!');
);

_player_setup(player) -> (
    modify(player, 'effect', 'saturation', 999999, 255, false, false);                                                  //disable hunger bar
    modify(player, 'spawn_point', l(100.5, 88, 607.5));                                                                 //move spawnpoint to deathbox for cleaner respawns
    if(!query(player, 'has_scoreboard_tag', 'marshal'), modify(player, 'gamemode', 'adventure'));                       //non-marshals start in adventure mode
    if(query(player, 'has_scoreboard_tag', 'spectator'), modify(player, 'gamemode', 'spectator'));                      //special spectator role
    //if(!query(player, 'has_scoreboard_tag', 'active_player'), classbot);                                              //don't let active players reset class
);

_constant_effects() -> (
    active_players = filter(entity_list('player'), query(_, 'gamemode') != 'spectator');                                //spectators cannot use spells
    //active_players = filter(entity_list('player'), query(_,'gamemode') == 'adventure');                                 //list of adventure mode players
    dead_players = filter(entity_list('player'), query(_, 'has_scoreboard_tag', 'dead'));

    //ward undead and light
    for(active_players,
        p = _;
        for(['mainhand','offhand'],
            item_tuple = query(p, 'holds', _);
            if(item_tuple,
                l(item, count, tag) = item_tuple;
                if(
                    tag_matches(tag, '{Tags:["ward_undead_focus"]}'),
                        _ward_undead(p),
                    tag_matches(tag, '{Tags:["light_focus"]}'),
                        schedule(1, '_light_cooldown_pos', p~'pos');    //clear torch at end of next tick, before placing torch(prevent torches remaining after player tp)
                        schedule(0, '_light_cooldown', p);              //clear at beginning of tick
                        schedule(0, '_light', p)                        //add torch at end of tick
                );
            );
        );
    );

    //deathwatch
    for(filter(dead_players, query(_, 'has_scoreboard_tag', 'deathwatch')),
        _deathwatch(_)
    );

    //kill tridents
    for(entity_list('trident'),
        if(//kill tridents after they hit something
            query(_, 'nbt', '{DealtDamage:1b}'),   //hit entity
                modify(_, 'remove'),
            query(_, 'nbt', '{inGround:1b}'),     //hit ground
                modify(_, 'remove')
        );
    );

    //health detriments
    for(active_players,
        hp = _ ~ 'health';
        if(
            hp > 12 && hp <= 16,
                (modify(_, 'effect', 'mining_fatigue', 1, 0, false, false)),
            hp > 8 && hp <= 12,
                (modify(_, 'effect', 'mining_fatigue', 1, 0, false, false);
                 modify(_, 'effect', 'slowness', 1, 0, false, false)),
            hp > 4 && hp <= 8,
                (modify(_, 'effect', 'mining_fatigue', 1, 1, false, false);
                 modify(_, 'effect', 'slowness', 1, 0, false, false)),
            hp <= 4,
                (modify(_, 'effect', 'mining_fatigue', 1, 1, false, false);
                 modify(_, 'effect', 'slowness', 1, 1, false, false))
        );
    );

    //particles
    map(filter(entity_list('zombie'),query(_,'has_scoreboard_tag','regenerating')),particle('heart',query(_,'pos'),1));
    map(filter(entity_list('living'),query(_,'has_scoreboard_tag','enfeebled')),particle('smoke',query(_,'pos'),1));

);

//stop players from dropping items

//************************************************
//              USEFUL COMMANDS
//************************************************

revive(player_to_revive) -> (
    //find players corpse
    map(filter(entity_list('zombie'),_==player_to_revive+'\'s body'),_revive_corpse(_));
);

revive_all() -> (
    //find players corpse
    map(filter(entity_list('zombie'),query(_,'has_scoreboard_tag','corpse')),_revive_corpse(_));
);

adventure_mode_players() -> (
    //list all players in adventure mode
    print(player(), filter(entity_list('player'), query(_,'gamemode') == 'adventure'));
);

dead_players() -> (
    //list all dead players
    print(player(), filter(entity_list('player'), query(_, 'has_scoreboard_tag', 'dead')));
);

//get info command
get_info(player_name) -> (
    //CLASS
    if( query(player(player_name), 'has_scoreboard_tag', 'Fighter'),
            class = 'Fighter',
        query(player(player_name), 'has_scoreboard_tag', 'Skirmisher'),
            class = 'Skirmisher',
        query(player(player_name), 'has_scoreboard_tag', 'Caster'),
            class = 'Caster',
            class = 'unchosen'
    );
    //ROLE
    if( query(player(player_name), 'has_scoreboard_tag', 'marshal'),
            role = 'marshal',
        query(player(player_name), 'has_scoreboard_tag', 'spectator'),
            role = 'spectator',
            role = 'player'
    );
    //DEAD
    if( query(player(player_name), 'has_scoreboard_tag', 'dead'),
        status = 'is dead';
        body_list = filter(entity_list('zombie'),_==player(player_name)~'name'+'\'s body');
        if(body_list,
            if(filter(body_list,query(_, 'has_scoreboard_tag', 'regenerating')), status = status + ' and regenerating');
            if(query(player(player_name), 'has_scoreboard_tag', 'deathwatch'), status = status + ' and deathwatching.', status = status + '.'),

            status = status + ' and does not have a body!'
        ),

        status = 'has '+ player(player_name)~'absorption' +' armor and '+ player(player_name)~'health' +' health.'
    );
    print(player(), player(player_name) +' is a '+ role +' with class '+ class +'. This player '+ status);
);
//what is players build?/do they have a build?
//what are players role

//is player dead
    //does player have corpse
        //is player regenerating
        //is player deathwatching
        //is player in deathbox

    //does player have armor (absorption)
    //how much health does player have?

//spell info?
    //regeneration active?
    //spells on cooldown? active? equipped? etc...