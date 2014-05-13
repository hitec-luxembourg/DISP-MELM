
    alter table map_element_library_icon 
        drop constraint fk_meli_to_icon;

    alter table map_element_library_icon 
        drop constraint fk_meli_to_ibrary;

    drop table map_element_icon;

    drop table map_element_library;

    drop table map_element_library_icon;

    drop sequence map_element_icon_seq;

    drop sequence map_element_library_icon_seq;

    drop sequence map_element_library_seq;

    create table map_element_icon (
        id int8 not null,
        display_name varchar(255) not null,
        pic_100px_md5 varchar(255) not null,
        size_in_bytes int8 not null,
        primary key (id),
        unique (pic_100px_md5, size_in_bytes)
    );

    create table map_element_library (
        id int8 not null,
        icon_md5 varchar(255) not null,
        major_version int4 not null,
        minor_version int4 not null,
        name varchar(255) not null,
        primary key (id),
        unique (name, major_version, minor_version)
    );

    create table map_element_library_icon (
        id int8 not null,
        icon_description_in_library varchar(255) not null,
        icon_name_in_library varchar(255) not null,
        index_of_icon_in_library int4 not null,
        icon_id int8 not null,
        library_id int8 not null,
        primary key (id),
        unique (library_id, icon_id),
        unique (library_id, icon_name_in_library),
        unique (library_id, index_of_icon_in_library)
    );

    alter table map_element_library_icon 
        add constraint fk_meli_to_icon 
        foreign key (icon_id) 
        references map_element_icon;

    alter table map_element_library_icon 
        add constraint fk_meli_to_ibrary 
        foreign key (library_id) 
        references map_element_library;

    create sequence map_element_icon_seq;

    create sequence map_element_library_icon_seq;

    create sequence map_element_library_seq;
