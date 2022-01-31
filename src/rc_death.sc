import('rc_spells', '_revive_corpse');
import('rc_spells', '_light_cooldown');
import('rc_spells', '_distance');
import('rc_spells', '_cast');

//********************************************************
//              REALMSCRAFT DEATH MECHANICS
//********************************************************

__on_player_dies(player) -> (
    _create_corpse(player);
);

__on_player_respawns(player) -> (
    schedule(0, '_player_dies', player);  //wait till the end of the tick to apply changes, else they will be overridden
);

__on_tick() -> (
    map(filter(entity_list('zombie'),query(_,'has_scoreboard_tag','following')),_corpse_following(_));
);

_create_corpse(player) -> (
    //get data
    corpse_name = player+'\'s body';
    l(c_x, c_y, c_z) = query(player, 'pos');

    //prepare data
    name_data = 'CustomName:"\\"'+ corpse_name +'\\""';
    head_data = 'ArmorItems:[{},{},{},{id:player_head,Count:1,tag:{SkullOwner:'+player+'}}],ArmorDropChances:[0.0f,0.0f,0.0f,0.0f]';
    pos_data = 'SleepingX:'+ floor(c_x) +',SleepingY:0,SleepingZ:'+ floor(c_z);
    game_tag_data = 'Invulnerable:1,PersistenceRequired:1,Silent:1,NoAI:1,CanPickUpLoot:0b';
    player_tag_data = 'Tags:["corpse"'+if(query(player, 'has_scoreboard_tag', 'regeneration'), ',"regenerating"','')+']';
    corpse_data = '{'+ name_data +','+ head_data +','+ pos_data + ','+ game_tag_data + ','+ player_tag_data +'}';

    //summon entities
    set(l(c_x, 0, c_z), 'red_bed[part=head]');
    new_corpse = spawn('zombie',query(player, 'pos'), corpse_data);
    game_tick();
    modify(new_corpse, 'pos', l(c_x, c_y+0.1, c_z)); //offset added to keep full body above block

    //schedule regenerating corpses to revive
    if(query(new_corpse, 'has_scoreboard_tag', 'regenerating'),
        schedule(1200, '_revive_corpse', new_corpse);
        modify(player, 'clear_tag', 'regeneration')
        );
);

_player_dies(player) -> (
    print(filter(entity_list('player'), query(_, 'has_scoreboard_tag', 'marshal')), player + ' has died!');
    modify(player, 'tag', 'dead');
    modify(player, 'effect', 'saturation', 999999, 255, false, false);                                                  //disable hunger bar
    if(query(player, 'has_scoreboard_tag', 'deathwatch'),
        //set deathwatched players to spectator
        modify(player, 'gamemode', 'spectator'),

        //Send dead players to death box, make them invulnerable
        (modify(player, 'location', 100.5, 88, 607.5, -90, 22.5);
         modify(player, 'effect', 'resistance', 999999, 5, false, false);
         )
    );
);

//************************************************
//              MANIPULATE CORPSES
//************************************************

__on_player_uses_item(player, item_tuple, hand) -> (
    l(item, count, tag) = item_tuple;
    if(item == 'carrot_on_a_stick',
        if(tag_matches(tag, '{Tags:["grab"]}'),
            _grab_corpse(player)
        );
    );
);

_grab_corpse(player) -> (
    target = _cast(player, 2);
    if(target != null && query(target, 'has_scoreboard_tag', 'corpse'),
        if(query(target, 'has_scoreboard_tag', player~'name'),
            _corpse_dropoff(target),
            _corpse_pickup(player, target)
        ),
        return()
    );
);

_corpse_pickup(player, corpse) -> (
    //handle tags
    tags_to_give = '{Tags:["corpse","following"'+if(query(corpse,'has_scoreboard_tag','regenerating'), ',"regenerating"','')+',"'+player~'name'+'"]}';
    modify(corpse, 'clear_tag', corpse~'scoreboard_tags');
    modify(corpse, 'nbt_merge', tags_to_give);

    //stand corpse up
    l(cx, cy, cz) = corpse~'pos';
    set(l(cx, 0, cz), 'air');     //deletes bed, standing corpse up.
);

_corpse_dropoff(corpse) -> (
    //handle tags
    tags_to_give = '{Tags:["corpse"'+if(query(corpse,'has_scoreboard_tag','regenerating'), ',"regenerating"','')+']}';
    modify(corpse, 'clear_tag', corpse~'scoreboard_tags');
    modify(corpse, 'nbt_merge', tags_to_give);

    //lay corpse down
    l(c_x, c_y, c_z) = corpse~'pos';
    set(l(c_x, 0, c_z), 'red_bed[part=head]');
    modify(corpse, 'nbt_merge', '{SleepingX:'+ floor(c_x) +',SleepingY:0,SleepingZ:'+ floor(c_z) + '}');
    game_tick();
    modify(corpse, 'pos', l(c_x, c_y+0.1, c_z)); //offset added to keep full body above block
);

_corpse_following(corpse) -> (
    //get player from tags
    for(corpse~'scoreboard_tags',if(_!='corpse'&&_!='following'&&_!='regenerating',player_to_follow=_));

    //drop corpse if too far away
    l(c_x,c_y,c_z) = corpse~'pos';
    l(p_x,p_y,p_z) = player(player_to_follow)~'pos';
    if(c_y < 1, modify(corpse, 'pos', l(c_x, p_y, c_z)));
    if(_distance(l(p_x,p_y,p_z), corpse~'pos') > 2, _corpse_dropoff(corpse); return());

    //slow and follow player
    modify(player(player_to_follow), 'effect', 'slowness', 1, 2, false, false);
    if(_distance(l(p_x,p_y,p_z), corpse~'pos') > 1, run('execute as '+ corpse~'command_name' +' at @s run tp @s ^ ^ ^0.3 facing entity '+ player_to_follow +' feet'));
);