create sequence ejercicio_seq start with 1 increment by 50;
create sequence rutina_seq start with 1 increment by 50;
create table ejercicio (id bigint not null, descripcion varchar(255), dificultad varchar(255), material varchar(255), musculos_trabajados varchar(255), nombre varchar(255) not null, observaciones varchar(255), tipo varchar(255), multimedia varchar(255) array, primary key (id));
create table ejs (duracion_minutos bigint, ejercicio_id bigint not null, repeticiones bigint, rutina_id bigint not null, series bigint, primary key (ejercicio_id, rutina_id));
create table rutina (id bigint not null, descripcion varchar(255), nombre varchar(255) not null, observaciones varchar(255), primary key (id));
alter table if exists ejs add constraint FKllr3vkw1wclmt5fh6nwumee6x foreign key (ejercicio_id) references ejercicio;
alter table if exists ejs add constraint FKslr1dsh7hgcdeo1fwhx2jqwpi foreign key (rutina_id) references rutina;
