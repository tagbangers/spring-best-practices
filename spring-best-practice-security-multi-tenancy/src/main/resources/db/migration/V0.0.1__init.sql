create table persistent_logins (
	tenant_id varchar(64) not null,
	username varchar(64) not null,
	series varchar(64) primary key,
	token varchar(64) not null,
	last_used timestamp not null
);

create table user (
	id bigint generated by default as identity,
	tenant_id varchar(255),
	username varchar(255),
	password varchar(255),
	name varchar(255),
	email varchar(255),
	primary key (id)
);

insert into user (id, tenant_id, username, password, name, email) values (null, 'meikun', 'yamada', 'yamada', 'Yamada Taro', 'Yamada@example.com');
insert into user (id, tenant_id, username, password, name, email) values (null, 'meikun', 'iwaki', 'iwaki', 'Iwaki Masami', 'iwaki@example.com');
insert into user (id, tenant_id, username, password, name, email) values (null, 'benkei', 'musashibo', 'musashibo', 'Musashibo Kazuma', 'musashibo@example.com');