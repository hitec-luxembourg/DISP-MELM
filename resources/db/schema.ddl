
    drop table map_element_icon;

    drop table map_element_library;

    drop sequence map_element_icon_seq;

    drop sequence map_element_library_seq;

    create table map_element_icon (
        id int8 not null,
        path varchar(255) not null,
        pic_100px_md5 varchar(255) not null,
        size_in_bytes int4 not null,
        primary key (id)
    );

    create table map_element_library (
        id int8 not null,
        major_version int4 not null,
        minor_version int4 not null,
        name varchar(255) not null,
        primary key (id)
    );

    create sequence map_element_icon_seq;

    create sequence map_element_library_seq;
