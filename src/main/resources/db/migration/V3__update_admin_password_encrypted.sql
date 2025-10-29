-- Atualizar senha do admin para versão criptografada (BCrypt)
-- Senha: admin
-- Hash BCrypt gerado com BCryptPasswordEncoder (10 rounds)

UPDATE users
SET password = '$2a$10$joxOKw2yvuSNrQxZtn3F/ekAK4yZXisC1JSl8bggCATkHKw2IRX42'
WHERE username = 'admin';

-- Comentário para documentação
COMMENT ON COLUMN users.password IS 'Senha criptografada com BCrypt';
