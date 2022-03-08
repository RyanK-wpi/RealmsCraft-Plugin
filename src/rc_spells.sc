//********************************************************
//              REALMSCRAFT SPELL MECHANICS
//********************************************************

__on_player_uses_item(player, item_tuple, hand) -> (
    l(item, count, tag) = item_tuple;
    if(item == 'carrot_on_a_stick',
        if( //Support Path
            tag_matches(tag, '{Tags:["heal_limb_focus"]}'),
                _heal_limb(player),

            tag_matches(tag, '{Tags:["repair_armor_focus"]}'),
                _repair_armor(player),

            tag_matches(tag, '{Tags:["raise_dead_focus"]}'),
                _raise_dead(player),

            tag_matches(tag, '{Tags:["cry_of_life_focus"]}'),
                _cry_of_life(player),

            tag_matches(tag, '{Tags:["seed_of_life_focus"]}'),
                _seed_of_life(player),

            //Mage Path
            tag_matches(tag, '{Tags:["magic_missile_focus"]}'),
                _magic_missile(player),

            //tag_matches(tag, '{Tags:["ward_undead_focus"]}'),
                //_ward_undead(player),

            tag_matches(tag, '{Tags:["enfeeble_being_focus"]}'),
                _enfeeble_being(player),

            tag_matches(tag, '{Tags:["regeneration_focus"]}'),
                _regeneration(player),

            tag_matches(tag, '{Tags:["lightning_bolt_focus"]}'),
                _lightning_bolt(player),

            //Seer Path
            tag_matches(tag, '{Tags:["guidance_focus"]}'),
                _guidance(player),

            //tag_matches(tag, '{Tags:["light_focus"]}'),
                //_light(player),

            //tag_matches(tag, '{Tags:["deathwatch_focus"]}'),
                //_deathwatch(player),

            tag_matches(tag, '{Tags:["divine_aid_focus"]}'),
                _divine_aid(player),

            tag_matches(tag, '{Tags:["vision_focus"]}'),
                _vision(player),

            //No match
            null
        );
    );
);

//************************************************
//              SPELL IMPLEMENTATION
//************************************************

//SUPPORT
_heal_limb(player) -> (
    target = _cast_include_player(player, 2);
    if(target != null,                                          //Ensure valid target
        if(target~'type'=='player' && target~'health' < 20,     //Can only heal players without 20 health
            (//effects
            modify(target, 'effect', 'instant_health', 1, 0, false, false);
            particle('happy_villager',target~'pos');

            //cooldown
            cd_slot = _inventory_locate(player, 'carrot_on_a_stick', '{Tags:["heal_limb_focus"]}');
            inventory_set(player, cd_slot, 1, 'barrier', '{CustomModelData:1,display:{Name:"\\"Heal Limb on Cooldown\\"",Lore:["\\"Spell Focus\\""]},Tags:["heal_limb_cooldown"]}');
            schedule(200, '_cooldown_heal_limb', player)
            )
        )
    );
);

_repair_armor(player) -> (
    target = _cast_include_player(player, 2);
    if(target != null,                                          //Ensure valid target
        if(target~'type'=='player',                             //Can only armor players that are fighets or skirmishers
            if( query(target, 'has_scoreboard_tag', 'Fighter') && target~'absorption' < 12,
                    //Fighter Armor
                    modify(target, 'effect', 'absorption');     //clear old absorption first
                    modify(target, 'effect', 'absorption', 999999, 2, false, false),

                query(target, 'has_scoreboard_tag', 'Skirmisher') && target~'absorption' < 6,
                    (//Skirmisher Armor
                    modify(target, 'effect', 'absorption');     //clear old absorption first
                    modify(target, 'effect', 'absorption', 999999, 2, false, false);
                    modify(target, 'effect', 'instant_damage', 1, 0, false, false)
                    ),

                    //if already at full armor, don't use charge
                    return();
            );
            particle('crit',target~'pos');

            //cooldown
            cd_slot = _inventory_locate(player, 'carrot_on_a_stick', '{Tags:["repair_armor_focus"]}');
            inventory_set(player, cd_slot, 1, 'barrier', '{CustomModelData:1,display:{Name:"\\"Repair Armor on Cooldown\\"",Lore:["\\"Spell Focus\\""]},Tags:["repair_armor_cooldown"]}');
            schedule(900, '_cooldown_repair_armor', player)

        );
    );
);

_raise_dead(player) -> (
    raises_remaining = scoreboard('raise_dead_count', player);
    if(raises_remaining > 0,
        target = _cast(player, 2);
    	if(target != null && query(target, 'has_scoreboard_tag', 'corpse'),
    	    (//effects
            _revive_corpse(target);

            //cooldown
            raises_remaining = raises_remaining - 1;
            scoreboard('raise_dead_count', player, raises_remaining)
    	    )
    	);
    );
	print(player, 'You have '+ raises_remaining +' raise(s) remaining.');
);

_cry_of_life(player) -> (
    col_targets = entity_area('zombie', player, l(18.3, 18.3, 18.3)); //60 ft radius
    for(col_targets,
        if(query(_, 'has_scoreboard_tag', 'corpse'),
            _revive_corpse(_)
        );
    );

    //cooldown
    cd_slot = _inventory_locate(player, 'carrot_on_a_stick', '{Tags:["cry_of_life_focus"]}');
    inventory_set(player, cd_slot, 1, 'barrier', '{CustomModelData:1,display:{Name:"\\"All Cries of Life used\\"",Lore:["\\"Spell Focus\\""]},Tags:["cry_of_life_out"]}');
);

_seed_of_life(player) -> (
    target = _cast(player, 2);
    if(target != null,
    	if(query(target, 'has_scoreboard_tag', 'corpse') && !query(target, 'has_scoreboard_tag', 'regenerating'),
    	    (//effects
    	    modify(target, 'tag', 'regenerating');
    	    schedule(1200, '_revive_corpse', target);

    	    //cooldown
    	    cd_slot = _inventory_locate(player, 'carrot_on_a_stick', '{Tags:["seed_of_life_focus"]}');
            inventory_set(player, cd_slot, 1, 'barrier', '{CustomModelData:1,display:{Name:"\\"Seed of Life on Cooldown\\"",Lore:["\\"Spell Focus\\""]},Tags:["seed_of_life_cooldown"]}');
            schedule(1200, '_cooldown_seed_of_life', player)
            )
        )
    );
);

//MAGE
_magic_missile(player) -> (
    uses = scoreboard('mm_uses', player);
    if (uses > 0,
        (//Cast Magic Missile
        mm_pos = player~'pos' + [0, player~'eye_height', 0];
        mm_dir = _projectile_target(player, 1.5);
        mm = spawn('snowball', mm_pos, mm_dir);

        entity_event(mm, 'on_removed', '_mm_collision');

        uses = uses - 1;
        scoreboard('mm_uses', player, uses);
        print(player, 'You have '+ uses +' magic missiles left!');

        //cooldown
        schedule(300, '_cooldown_magic_missile', player)
        ),

        //else no magic missiles!
        print(player, 'No Magic Missiles available!');
    );
);

_ward_undead(player) -> (
    particle('nautilus', player~'pos', 1);
    ward_targets = entity_area('undead', player, l(2.5, 2.5, 2.5));
    for(ward_targets,
        if(!query(_, 'has_scoreboard_tag', 'corpse'),       //dont ward corpses!
            run('execute as '+ query(_, 'command_name') +' at @s if block ^ ^0 ^-1 air run teleport @s ^ ^0.1 ^-0.2 facing entity '+ player +' feet')
        );
    );
);

_enfeeble_being(player) -> (
    target = _cast_include_player(player, 8);
    if(target != null,
        if(target~'type' != 'player',
            (//effects
            modify(target, 'effect', 'weakness', 2400, 0, false, false);
            modify(target, 'effect', 'mining_fatigue', 2400, 2, false, false);
            modify(target, 'effect', 'slowness', 2400, 2, false, false);
            modify(target, 'tag', 'enfeebled');
            //particle('smoke',target~'pos');

            //cooldown
            cd_slot = _inventory_locate(player, 'carrot_on_a_stick', '{Tags:["enfeeble_being_focus"]}');
            inventory_set(player, cd_slot, 1, 'barrier', '{CustomModelData:1,display:{Name:"\\"Enfeeble Being on Cooldown\\"",Lore:["\\"Spell Focus\\""]},Tags:["enfeeble_being_cooldown"]}');
            schedule(2400, '_cooldown_enfeeble_being', player)
            )
        )
    );
);

_regeneration(player) -> (
    //check if player has regeneration prepared
    if(query(player, 'has_scoreboard_tag', 'regeneration'),
        print(player, 'Regeneration is already prepared!'),

        //check if player is viable for regenerate (not swimming or dead)
        if(!player~'swimming' && !query(player, 'has_scoreboard_tag', 'dead'),
            //check if player is lying down
            if(player~'pose' == 'swimming',
                //cancel regeneration
                (print(player, 'Regeneration canceled!');
                _get_up(player, -1);
                return();
                ),

                //begin regeneration
                (l(p_x, p_y, p_z) = player~'pos';
                if(air(p_x,p_y,p_z) && block(p_x,p_y-1,p_z) != 'iron_trapdoor',     //check regeneration area
                    (num = tick_time()%1728000;                                     //scoreboard maximum is ~2bil, this is the amount of ticks in a day
                    _lay_down(player, num);
                    schedule(400, '_get_up', player, num);
                    ),

                    //else
                    print(player, 'Area unsuitable, find a clear area to prepare this spell and try again.');
                );
                )
            );
        );
    );
);

_lightning_bolt(player) -> (
    lb_pos = player~'pos' + [0, player~'eye_height', 0];
    lb_dir = _projectile_target(player, 2.5);
    lb = spawn('trident', lb_pos, lb_dir + ',{Tags:["lightning_bolt"]}');

    entity_event(lb, 'on_removed', '_lb_collision');

    //cooldown
    cd_slot = _inventory_locate(player, 'carrot_on_a_stick', '{Tags:["lightning_bolt_focus"]}');
    inventory_set(player, cd_slot, 1, 'barrier', '{CustomModelData:1,display:{Name:"\\"Lightning Bolt on Cooldown\\"",Lore:["\\"Spell Focus\\""]},Tags:["lightning_bolt_cooldown"]}');
    schedule(1200, '_cooldown_lightning_bolt', player)
);

//SEER
_guidance(player) -> (
    run('tellraw @a[tag=marshal] [{"text":"'+ player +' has used Guidance","color":"red","clickEvent":{"action":"run_command","value":"/script in realmscraft run investigate(\''+ player() +'\')"}}]');

    //cooldown
    cd_slot = _inventory_locate(player, 'carrot_on_a_stick', '{Tags:["guidance_focus"]}');
    inventory_set(player, cd_slot, 1, 'barrier', '{CustomModelData:1,display:{Name:"\\"Guidance on Cooldown\\"",Lore:["\\"Spell Focus\\""]},Tags:["guidance_cooldown"]}');
    schedule(600, '_cooldown_guidance', player)
);

_light(player) -> (
    light_pos = player~'pos'+[0, 1, 0];
    if(air(light_pos),
        for(['mainhand','offhand'],
            item_tuple = query(player, 'holds', _);
            if(item_tuple,
                l(item, count, tag) = item_tuple;
                if(tag_matches(tag, '{Tags:["light_focus"]}'),
                    set(light_pos, 'light[level=10]')
                );
            );
        );
    );
);

_deathwatch(player) -> (
//gamemode handled by death
    targets = entity_area('zombie', player~'pos' + [0, -0.5, 0], l(0, 0, 0));
    for(targets,
        if(_~'name'== player~'name' + '\'s body',
            return()
        );
    );
    map(filter(entity_list('zombie'),_~'name'== player~'name' + '\'s body'), modify(player, 'pos', _~'pos' + [0, 0.5, 0]));
);

_divine_aid(player) -> (
    run('tellraw @a[tag=marshal] [{"text":"'+ player +' has used Divine Aid","color":"yellow","clickEvent":{"action":"run_command","value":"/script in realmscraft run investigate(\''+ player() +'\')"}}]');

    //cooldown
    cd_slot = _inventory_locate(player, 'carrot_on_a_stick', '{Tags:["divine_aid_focus"]}');
    inventory_set(player, cd_slot, 1, 'barrier', '{CustomModelData:1,display:{Name:"\\"All Divine Aids used\\"",Lore:["\\"Spell Focus\\""]},Tags:["divine_aid_used"]}')
);

_vision(player) -> (
    run('tellraw @a[tag=marshal] [{"text":"'+ player +' has used Vision","color":"green","clickEvent":{"action":"run_command","value":"/script in realmscraft run investigate(\''+ player() +'\')"}}]');

    //cooldown
    cd_slot = _inventory_locate(player, 'carrot_on_a_stick', '{Tags:["vision_focus"]}');
    inventory_set(player, cd_slot, 1, 'barrier', '{CustomModelData:1,display:{Name:"\\"All Visions used\\"",Lore:["\\"Spell Focus\\""]},Tags:["vision_used"]}')
);


//************************************************
//              SPELL COOLDOWNS
//************************************************

_cooldown_heal_limb(player) -> (
    r_slot = _inventory_locate(player, 'barrier', '{Tags:["heal_limb_cooldown"]}');
    if(r_slot == null, return());
    inventory_set(player, r_slot, 1, 'carrot_on_a_stick', '{CustomModelData:101,display:{Name:"\\"Heal Limb\\"",Lore:["\\"Spell Focus\\""]},Tags:["heal_limb_focus"]}');
);

_cooldown_repair_armor(player) -> (
    r_slot = _inventory_locate(player, 'barrier', '{Tags:["repair_armor_cooldown"]}');
    if(r_slot == null, return());
    inventory_set(player, r_slot, 1, 'carrot_on_a_stick', '{CustomModelData:102,display:{Name:"\\"Repair Armor\\"",Lore:["\\"Spell Focus\\""]},Tags:["repair_armor_focus"]}');
);

_cooldown_seed_of_life(player) -> (
    r_slot = _inventory_locate(player, 'barrier', '{Tags:["seed_of_life_cooldown"]}');
    if(r_slot == null, return());
    inventory_set(player, r_slot, 1, 'carrot_on_a_stick', '{CustomModelData:203,display:{Name:"\\"Seed of Life\\"",Lore:["\\"Spell Focus\\""]},Tags:["seed_of_life_focus"]}');
);

_cooldown_magic_missile(player) -> (
    uses = scoreboard('mm_uses', player) + 1;
    scoreboard('mm_uses', player, uses);
    print(player, 'You have '+ uses +' magic missiles!');
);

_cooldown_enfeeble_being(player) -> (
    r_slot = _inventory_locate(player, 'barrier', '{Tags:["enfeeble_being_cooldown"]}');
    if(r_slot == null, return());
    inventory_set(player, r_slot, 1, 'carrot_on_a_stick', '{CustomModelData:111,display:{Name:"\\"Enfeeble Being\\"",Lore:["\\"Spell Focus\\""]},Tags:["enfeeble_being_focus"]}');
);

_cooldown_lightning_bolt(player) -> (
    r_slot = _inventory_locate(player, 'barrier', '{Tags:["lightning_bolt_cooldown"]}');
    if(r_slot == null, return());
    inventory_set(player, r_slot, 1, 'carrot_on_a_stick', '{CustomModelData:211,display:{Name:"\\"Lightning Bolt\\"",Lore:["\\"Spell Attack\\""]},Tags:["lightning_bolt_focus"]}');
);

_light_cooldown(player) -> (
    light_pos = player~'pos'+[0, 1, 0];
    if(block(light_pos) == 'light',
        set(light_pos, 'air')
    );
);

_light_cooldown_pos(p_pos) -> (
    light_pos = p_pos+[0, 1, 0];
    if(block(light_pos) == 'light',
        set(light_pos, 'air')
    );
);

_cooldown_guidance(player) -> (
    r_slot = _inventory_locate(player, 'barrier', '{Tags:["guidance_cooldown"]}');
    if(r_slot == null, return());
    inventory_set(player, r_slot, 1, 'carrot_on_a_stick', '{CustomModelData:121,display:{Name:"\\"Guidance\\"",Lore:["\\"Spell Focus\\""]},Tags:["guidance_focus"]}');
);

//************************************************
//              SPELL SUPPORT
//************************************************

_cast(p, r) -> (
    target = query(p, 'trace', r, 'entities')
);

_cast_include_player(p, r) -> (
    pitch = p~'pitch';
    if(pitch > 70 && pitch <= 90, return(p),
        target = query(p, 'trace', r, 'entities')
    );
);

_revive_corpse(corpse) -> (
    //get player name from corpse
    player_to_revive = query(corpse, 'custom_name') -'\'s body';

    //check for player to revive. (E.g. the player has logged out)
    if(player(player_to_revive),

    //tp player to corpse position
    (revive_pos = corpse~'pos';
    modify(player(player_to_revive),'pos', revive_pos);
    modify(player(player_to_revive), 'gamemode', adventure);  //set gamemode
    modify(player(player_to_revive), 'effect', 'resistance'); //remove resistance from dead players
    modify(player(player_to_revive), 'clear_tag', 'dead');    //remove dead tag, player is alive!

    //play effect and remove corpse
    l(cx, cy, cz) = corpse~'pos';
    particle('happy_villager', revive_pos);
    modify(corpse, 'remove');
    set(l(_cx, 0, _cz), 'air');     //deletes bed
    ),

    //if no player, warn marshals
    run('tellraw @a[tag=marshal] [{"text":"'+ player_to_revive +' cannot be found!","color":"red","clickEvent":{"action":"run_command","value":"/tp @s '+ corpse~'pos' +'"}}]');
    );
);

_lay_down(player, num) -> (
    //apply effects to trap player in crawling position
    set(player~'pos','iron_trapdoor[half=top]');
    modify(player, 'effect', 'slowness', 999999, 10, false, false);
    modify(player, 'effect', 'jump_boost', 999999, 128, false, false);

    //assign id for regenerating
    scoreboard('regen_id', player, num);
);

_get_up(player, num) -> (
    //clean up area, remove effects
    set(player~'pos','air');
    modify(player, 'effect', 'slowness');
    modify(player, 'effect', 'jump_boost');

    //check regen ID, if match, player has successfully regenerated.
    if(player~'pose' == 'swimming' && scoreboard('regen_id', player) == num,
        modify(player, 'tag', 'regeneration');
        print(player, 'Regeneration complete!')
    );
);

_mm_collision(entity) -> (
    mm_targets = entity_area('!snowball', entity, l(3,3,3));
    for(mm_targets,
     	if(query(_, 'nbt', '{HurtTime:10s}'),
           	if(_is_undead(_),
           	    //heal undead
           	    modify(_, 'effect', 'instant_health', 1, 0, false, false),

           	    //hurt others
           	    modify(_, 'effect', 'instant_damage', 1, 0, false, false)
           	)
        )
    );
);

_lb_collision(entity) -> (
    lb_targets = entity_area('!trident', entity, l(3,3,3));
    spawn('lightning_bolt', entity~'pos');
    for(lb_targets,
        if(query(_, 'nbt', '{HurtTime:10s}'),
            if(_is_undead(_),
                //heal undead
                modify(_, 'effect', 'instant_health', 1, 2, false, false),

                //hurt others
                modify(_, 'effect', 'instant_damage', 1, 2, false, false)
            )
        )
    );
);

_projectile_target(player, velocity) -> (
    l(d_x, d_y, d_z) = velocity*player~'look';
    if( d_x == 0, d_x = '0.0');
    if( d_y == 0, d_y = '0.0');
    if( d_z == 0, d_z = '0.0');

    return('{CustomModelData:1,Motion:[' + d_x +','+ d_y +','+ d_z + ']}');
);

_distance(v1,v2) -> sqrt(reduce(v1-v2,_*_+_a,0));

_inventory_locate(player, item, item_nbt) -> (
    slot = 0;
    while(slot<41, 50,
        slot = inventory_find(player, item, slot);
        l(item, count, tag) = inventory_get(player, slot);
        if(tag_matches(tag, item_nbt),
            return(slot),
            slot = slot + 1
        );
    );
    //if cant find item, return null
    return(slot = null);
);

_is_undead(entity) -> (
    for(entity_types('undead'),
        if(_ == entity~'type',
            return(true)
        );
    );
    return(false);
);