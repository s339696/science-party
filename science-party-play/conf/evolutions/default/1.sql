# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table games (
  id                        bigint auto_increment not null,
  active_player             integer,
  game_status               varchar(1),
  topic_id                  bigint,
  date_created              datetime(6) not null,
  date_updated              datetime(6) not null,
  constraint ck_games_game_status check (game_status in ('A','P','F')),
  constraint pk_games primary key (id))
;

create table perks (
  id                        bigint auto_increment not null,
  perk_name                 varchar(255),
  qr_code                   varchar(32),
  constraint pk_perks primary key (id))
;

create table players (
  id                        bigint auto_increment not null,
  field_position            integer,
  player_status             varchar(1),
  user_id                   bigint,
  game_id                   bigint,
  constraint ck_players_player_status check (player_status in ('I','L','P')),
  constraint pk_players primary key (id))
;

create table topics (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_topics primary key (id))
;

create table users (
  id                        bigint auto_increment not null,
  firstname                 varchar(45),
  lastname                  varchar(45),
  birthday                  datetime(6),
  email                     varchar(60),
  password                  varchar(32),
  points                    integer,
  locked                    tinyint(1) default 0,
  last_online               datetime(6),
  date_created              datetime(6) not null,
  date_updated              datetime(6) not null,
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id))
;


create table user_has_perks (
  perks_id                       bigint not null,
  users_id                       bigint not null,
  constraint pk_user_has_perks primary key (perks_id, users_id))
;
alter table games add constraint fk_games_topic_1 foreign key (topic_id) references topics (id) on delete restrict on update restrict;
create index ix_games_topic_1 on games (topic_id);
alter table players add constraint fk_players_user_2 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_players_user_2 on players (user_id);
alter table players add constraint fk_players_game_3 foreign key (game_id) references games (id) on delete restrict on update restrict;
create index ix_players_game_3 on players (game_id);



alter table user_has_perks add constraint fk_user_has_perks_perks_01 foreign key (perks_id) references perks (id) on delete restrict on update restrict;

alter table user_has_perks add constraint fk_user_has_perks_users_02 foreign key (users_id) references users (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table games;

drop table perks;

drop table user_has_perks;

drop table players;

drop table topics;

drop table users;

SET FOREIGN_KEY_CHECKS=1;

