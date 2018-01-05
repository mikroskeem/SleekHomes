-- Woo... SQL
create table if not exists `homes` (
    `id` int auto_increment not null,
    `uuid` char(36) not null,
    `worldName` varchar(1024) not null,
    `worldGroup` varchar(1024) not null,
    `homeName` varchar(128) not null,
    `x` double not null,
    `y` double not null,
    `z` double not null,
    `yaw` float not null,
    `pitch` float not null
);