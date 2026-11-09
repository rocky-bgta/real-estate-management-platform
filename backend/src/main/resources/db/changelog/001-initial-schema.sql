--liquibase formatted sql
--changeset rocky:001
CREATE TABLE app_user (
 id UUID PRIMARY KEY, mobile VARCHAR(30) UNIQUE NOT NULL, email VARCHAR(255) UNIQUE,
 password_hash VARCHAR(255) NOT NULL, role VARCHAR(30) NOT NULL, enabled BOOLEAN NOT NULL DEFAULT TRUE,
 created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE property (
 id UUID PRIMARY KEY, owner_id UUID NOT NULL REFERENCES app_user(id), title VARCHAR(200) NOT NULL,
 description TEXT, category VARCHAR(30) NOT NULL, listing_type VARCHAR(20) NOT NULL,
 price NUMERIC(19,2) NOT NULL CHECK(price >= 0), city VARCHAR(100) NOT NULL, area VARCHAR(150) NOT NULL,
 size_sq_ft NUMERIC(12,2), bedrooms INTEGER, bathrooms INTEGER, status VARCHAR(30) NOT NULL,
 created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE property_image (id UUID PRIMARY KEY, property_id UUID NOT NULL REFERENCES property(id) ON DELETE CASCADE, object_key VARCHAR(500) NOT NULL, sort_order INTEGER NOT NULL DEFAULT 0);
CREATE TABLE booking (id UUID PRIMARY KEY, property_id UUID NOT NULL REFERENCES property(id), customer_id UUID NOT NULL REFERENCES app_user(id), status VARCHAR(30) NOT NULL, required_payment_amount NUMERIC(19,2), created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP);
CREATE TABLE advertisement (id UUID PRIMARY KEY, owner_id UUID NOT NULL REFERENCES app_user(id), property_id UUID REFERENCES property(id), duration_days INTEGER NOT NULL, price NUMERIC(19,2) NOT NULL, status VARCHAR(30) NOT NULL, expires_at TIMESTAMPTZ);
CREATE TABLE payment (id UUID PRIMARY KEY, booking_id UUID UNIQUE NOT NULL REFERENCES booking(id), amount NUMERIC(19,2) NOT NULL, status VARCHAR(30) NOT NULL, provider_reference VARCHAR(255));
CREATE INDEX idx_property_search ON property(status, listing_type, city, area, price);
