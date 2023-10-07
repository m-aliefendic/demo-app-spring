ALTER TABLE public.users ALTER COLUMN "language" TYPE int4 USING "language"::int8;
ALTER TABLE public.users ADD activation_token UUID NULL;