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

create table chats (
  id                        bigint auto_increment not null,
  name                      varchar(50),
  constraint pk_chats primary key (id))
;

create table friends (
  user_send_req_id          bigint,
  user_get_req_id           bigint,
  request                   tinyint(1) default 0,
  date_request              datetime not null,
  date_friends              datetime not null)
;

create table games (
  id                        bigint auto_increment not null,
  active_player_id          bigint,
  active_question_id        bigint,
  game_status               varchar(1),
  topic_id                  bigint,
  date_created              datetime not null,
  date_updated              datetime not null,
  constraint ck_games_game_status check (game_status in ('A','P','F')),
  constraint uq_games_active_player_id unique (active_player_id),
  constraint uq_games_active_question_id unique (active_question_id),
  constraint pk_games primary key (id))
;

create table messages (
  user_id                   bigint,
  chat_id                   bigint,
  text                      varchar(1000),
  date_created              datetime not null)
;

create table perks (
  id                        bigint auto_increment not null,
  perk_name                 varchar(255),
  qr_code                   varchar(32),
  constraint pk_perks primary key (id))
;

create table perks_per_player (
  id                        bigint auto_increment not null,
  player_id                 bigint,
  perk_id                   bigint,
  constraint pk_perks_per_player primary key (id))
;

create table perks_per_user_and_topic (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  perk_id                   bigint,
  topic_id                  bigint,
  constraint pk_perks_per_user_and_topic primary key (id))
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
  birthday                  datetime,
  email                     varchar(60),
  password                  varchar(32),
  author                    tinyint(1) default 0,
  points                    integer,
  locked                    tinyint(1) default 0,
  last_online               datetime,
  date_created              datetime not null,
  date_updated              datetime not null,
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id))
;


create table user_has_chats (
  chats_id                       bigint not null,
  users_id                       bigint not null,
  constraint pk_user_has_chats primary key (chats_id, users_id))
;

create table user_has_perks (
  users_id                       bigint not null,
  perks_id                       bigint not null,
  constraint pk_user_has_perks primary key (users_id, perks_id))
;
alter table answers add constraint fk_answers_question_1 foreign key (question_id) references questions (id) on delete restrict on update restrict;
create index ix_answers_question_1 on answers (question_id);
alter table friends add constraint fk_friends_userSendReq_2 foreign key (user_send_req_id) references users (id) on delete restrict on update restrict;
create index ix_friends_userSendReq_2 on friends (user_send_req_id);
alter table friends add constraint fk_friends_userGetReq_3 foreign key (user_get_req_id) references users (id) on delete restrict on update restrict;
create index ix_friends_userGetReq_3 on friends (user_get_req_id);
alter table games add constraint fk_games_activePlayer_4 foreign key (active_player_id) references players (id) on delete restrict on update restrict;
create index ix_games_activePlayer_4 on games (active_player_id);
alter table games add constraint fk_games_activeQuestion_5 foreign key (active_question_id) references questions (id) on delete restrict on update restrict;
create index ix_games_activeQuestion_5 on games (active_question_id);
alter table games add constraint fk_games_topic_6 foreign key (topic_id) references topics (id) on delete restrict on update restrict;
create index ix_games_topic_6 on games (topic_id);
alter table messages add constraint fk_messages_user_7 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_messages_user_7 on messages (user_id);
alter table messages add constraint fk_messages_chat_8 foreign key (chat_id) references chats (id) on delete restrict on update restrict;
create index ix_messages_chat_8 on messages (chat_id);
alter table perks_per_player add constraint fk_perks_per_player_player_9 foreign key (player_id) references players (id) on delete restrict on update restrict;
create index ix_perks_per_player_player_9 on perks_per_player (player_id);
alter table perks_per_player add constraint fk_perks_per_player_perk_10 foreign key (perk_id) references perks_per_user_and_topic (id) on delete restrict on update restrict;
create index ix_perks_per_player_perk_10 on perks_per_player (perk_id);
alter table perks_per_user_and_topic add constraint fk_perks_per_user_and_topic_user_11 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_perks_per_user_and_topic_user_11 on perks_per_user_and_topic (user_id);
alter table perks_per_user_and_topic add constraint fk_perks_per_user_and_topic_perk_12 foreign key (perk_id) references perks (id) on delete restrict on update restrict;
create index ix_perks_per_user_and_topic_perk_12 on perks_per_user_and_topic (perk_id);
alter table perks_per_user_and_topic add constraint fk_perks_per_user_and_topic_topic_13 foreign key (topic_id) references topics (id) on delete restrict on update restrict;
create index ix_perks_per_user_and_topic_topic_13 on perks_per_user_and_topic (topic_id);
alter table players add constraint fk_players_user_14 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_players_user_14 on players (user_id);
alter table players add constraint fk_players_game_15 foreign key (game_id) references games (id) on delete restrict on update restrict;
create index ix_players_game_15 on players (game_id);
alter table questions add constraint fk_questions_topic_16 foreign key (topic_id) references topics (id) on delete restrict on update restrict;
create index ix_questions_topic_16 on questions (topic_id);



alter table user_has_chats add constraint fk_user_has_chats_chats_01 foreign key (chats_id) references chats (id) on delete restrict on update restrict;

alter table user_has_chats add constraint fk_user_has_chats_users_02 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table user_has_perks add constraint fk_user_has_perks_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table user_has_perks add constraint fk_user_has_perks_perks_02 foreign key (perks_id) references perks (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table answers;

drop table chats;

drop table user_has_chats;

drop table friends;

drop table games;

drop table messages;

drop table perks;

drop table perks_per_player;

drop table perks_per_user_and_topic;

drop table players;

drop table questions;

drop table topics;

drop table users;

drop table user_has_perks;

SET FOREIGN_KEY_CHECKS=1;

