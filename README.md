# SleekHomes

Used to be a Pluxer server specific homes plugin, but that server died. RIP

Anyway here we have a free homes plugin now.

Uses HikariCP & H2 to store data.

## Permissions & configuration

- `sleekhomes.groups.<groupname>`  - allow `n` amount of homes defined in configuration `groups` section
- `sleekhomes.worldgroup.<worldgroup>` - allow player to use `worldgroup` defined in configuration `worlds.world-groups` section.  
        Default group is called obviously `default` and will be used on worlds which don't belong to any specific group
- `sleekhomes.unlimited` - allow (almost, I'd say 128 is enough) unlimited amount of homes for given player. Defaults true for OP
- `sleekhomes.home` - allow using __/home__ command. Allowed by default
- `sleekhomes.sethome` - allow using __/sethome__ command. Allowed by default
- `sleekhomes.delhome` - allow using __/delhome__ command. Allowed by default
- `sleekhomes.listhomes` - allow using __/listhomes__ command. Allowed by default

## Commands
Self-explanatory commands. Tab completion also works with home names

- /home
- /homes _or_ /listhomes
- /sethome
- /delhome
