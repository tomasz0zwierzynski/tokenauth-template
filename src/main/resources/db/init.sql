DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.roles;
DROP TABLE IF EXISTS public.userroles;
DROP TABLE IF EXISTS public.tokens;

CREATE TABLE public.users
(
    id bigserial NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    email character varying,
    PRIMARY KEY (id)
);

ALTER TABLE public.users
    OWNER to postgres;

ALTER TABLE public.users
    ADD CONSTRAINT "UNIQUE_email" UNIQUE (email);

ALTER TABLE public.users
    ADD CONSTRAINT "UNIQUE_username" UNIQUE (username);

ALTER TABLE public.users
    ALTER COLUMN password TYPE character varying (60) COLLATE pg_catalog."default";

CREATE TABLE public.roles
(
    id bigserial NOT NULL,
    name character varying NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.roles
    OWNER to postgres;

CREATE TABLE public.userroles
(
    userid bigint NOT NULL,
    roleid bigint NOT NULL
);

ALTER TABLE public.userroles
    OWNER to postgres;

ALTER TABLE public.userroles
    ADD CONSTRAINT "FK_userid" FOREIGN KEY (userid)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
CREATE INDEX "fki_FK_userid"
    ON public.userroles(userid);

ALTER TABLE public.userroles
    ADD CONSTRAINT "FK_roleid" FOREIGN KEY (roleid)
    REFERENCES public.roles (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
CREATE INDEX "fki_FK_roleid"
    ON public.userroles(roleid);

CREATE TABLE public.tokens
(
    id bigserial NOT NULL,
    token character varying NOT NULL,
    expires timestamp with time zone NOT NULL,
    userid bigint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.tokens
    OWNER to postgres;

ALTER TABLE public.tokens
    ADD CONSTRAINT "FK_userid" FOREIGN KEY (userid)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

INSERT INTO public.roles(
	id, name)
	VALUES (default, 'DEFAULT'), (default, 'USER'), (default, 'ADMIN');

INSERT INTO public.users(
	id, username, password, email)
	VALUES (default, 'admin', '$2a$10$6YgDbyTn0HueCmQq.WHRiO3qMNtamoN9NMRuSOrMTjUIo8yWJin56', 'admin@admin.pl'),
	(default, 'user', '$2a$10$aSfjLyfIUZqovimQDJ2QhOChTlRWTUsMuu.EMjq7EAJ6ilffbWxGC', 'user@user.pl');

INSERT INTO public.userroles(
	userid, roleid)
	VALUES (1, 1), (1, 2), (1, 3), (2, 1), (2, 2);
