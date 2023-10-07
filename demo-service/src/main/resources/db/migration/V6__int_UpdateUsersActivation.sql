ALTER TABLE public.users
    ADD active boolean NULL;
ALTER TABLE public.users
    ADD activation_expires_on TIMESTAMP NULL;