ALTER TABLE users ALTER COLUMN uid SET DEFAULT uuid_generate_v4();
ALTER TABLE posts ALTER COLUMN pid SET DEFAULT uuid_generate_v4();
ALTER TABLE chats ALTER COLUMN cid SET DEFAULT uuid_generate_v4();
ALTER TABLE log ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE imgs ALTER COLUMN id SET DEFAULT uuid_generate_v4();