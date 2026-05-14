create table curso(
    id bigint not null auto_increment,
    nome varchar(100) not null unique,
    periodo varchar(20) not null,
    ativo tinyint not null default 1,
    primary key (id)
);