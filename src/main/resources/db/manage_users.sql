ALTER TABLE public.users
    ADD COLUMN active boolean NOT NULL DEFAULT False;

ALTER TABLE public.users
    ADD COLUMN generated character varying;