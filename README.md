# PluxerHomes

Pluxer server specific homes plugin

Uses HikariCP & H2 to store data.

## Permissions & configuration

- `pluxerhomes.groups.<groupname>`  - allow `n` amount of homes defined in configuration `groups` section
- `pluxerhomes.worldgroup.<worldgroup>` - allow player to use `worldgroup` defined in configuration `worlds.world-groups` section.  
        Default group is called obviously `default` and will be used on worlds which don't belong to any specific group
- `pluxerhomes.unlimited` - allow (almost, I'd say 128 is enough) unlimited amount of homes for given player. Defaults true for OP
- `pluxerhomes.home` - allow using __/home__ command. Allowed by default
- `pluxerhomes.sethome` - allow using __/sethome__ command. Allowed by default
- `pluxerhomes.delhome` - allow using __/delhome__ command. Allowed by default

## Commands
Self-explanatory commands. Tab completion also works

- /home
- /homes _or_ /listhomes
- /sethome
- /delhome