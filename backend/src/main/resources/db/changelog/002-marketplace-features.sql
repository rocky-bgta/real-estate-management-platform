--liquibase formatted sql
--changeset rocky:002
ALTER TABLE app_user ALTER COLUMN mobile DROP NOT NULL;
ALTER TABLE app_user ALTER COLUMN email SET NOT NULL;

CREATE TABLE favorite (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  property_id UUID NOT NULL REFERENCES property(id) ON DELETE CASCADE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_favorite_user_property UNIQUE(user_id, property_id)
);

CREATE TABLE property_inquiry (
  id UUID PRIMARY KEY,
  property_id UUID NOT NULL REFERENCES property(id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  message TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_favorite_user ON favorite(user_id);
CREATE INDEX idx_inquiry_property ON property_inquiry(property_id);
