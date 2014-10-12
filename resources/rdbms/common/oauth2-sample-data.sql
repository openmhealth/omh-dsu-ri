INSERT INTO oauth_client_details (
  client_id,
  client_secret,
  scope,
  resource_ids,
  authorized_grant_types,
  authorities
)
VALUES (
  'testClient',
  'testClientSecret',
  'read,write',
  'dataPoint',
  'authorization_code,password,refresh_token',
  'ROLE_CLIENT'
);