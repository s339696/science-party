# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answers (
  id                        bigint auto_increment not null,
  text                      varchar(255),
  correct                   tinyint(1) default 0,
  question_id               bigint,
  constraint pk_answers primary key (id))
;

create table games (
  id                        bigint auto_increment not null,
  active_player_id          bigint,
  active_question_id        bigint,
  game_status               varchar(1),
  topic_id                  bigint,
  date_created              datetime(6) not null,
  date_updated              datetime(6) not null,
  constraint ck_games_game_status check (game_status in ('A','P','F')),
  constraint uq_games_active_player_id unique (active_player_id),
  constraint uq_games_active_question_id unique (active_question_id),
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
  constraint ck_players_player_status check (player_status in ('A','I','L','F','P','D')),
  constraint pk_players primary key (id))
;

create table questions (
  id                        bigint auto_increment not null,
  text                      varchar(1000),
  difficulty                integer,
  topic_id                  bigint,
  constraint pk_questions primary key (id))
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
  author                    tinyint(1) default 0,
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
alter table answers add constraint fk_answers_question_1 foreign key (question_id) references questions (id) on delete restrict on update restrict;
create index ix_answers_question_1 on answers (question_id);
alter table games add constraint fk_games_activePlayer_2 foreign key (active_player_id) references players (id) on delete restrict on update restrict;
create index ix_games_activePlayer_2 on games (active_player_id);
alter table games add constraint fk_games_activeQuestion_3 foreign key (active_question_id) references questions (id) on delete restrict on update restrict;
create index ix_games_activeQuestion_3 on games (active_question_id);
alter table games add constraint fk_games_topic_4 foreign key (topic_id) references topics (id) on delete restrict on update restrict;
create index ix_games_topic_4 on games (topic_id);
alter table players add constraint fk_players_user_5 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_players_user_5 on players (user_id);
alter table players add constraint fk_players_game_6 foreign key (game_id) references games (id) on delete restrict on update restrict;
create index ix_players_game_6 on players (game_id);
alter table questions add constraint fk_questions_topic_7 foreign key (topic_id) references topics (id) on delete restrict on update restrict;
create index ix_questions_topic_7 on questions (topic_id);



alter table user_has_perks add constraint fk_user_has_perks_perks_01 foreign key (perks_id) references perks (id) on delete restrict on update restrict;

alter table user_has_perks add constraint fk_user_has_perks_users_02 foreign key (users_id) references users (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table answers;

drop table games;

drop table perks;

drop table user_has_perks;

drop table players;

drop table questions;

drop table topics;

drop table users;

SET FOREIGN_KEY_CHECKS=1;

