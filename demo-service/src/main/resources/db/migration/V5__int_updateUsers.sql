ALTER TABLE public.users ALTER COLUMN "language" TYPE int4 USING "language"::int8;
ALTER TABLE public.users
    ADD activation_token UUID NULL;

INSERT INTO public.roles
    (id, role_name, description)
VALUES (1, 'user', 'user');