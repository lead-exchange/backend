DROP TABLE IF EXISTS matches;

CREATE TABLE IF NOT EXISTS matches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lead_id UUID NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    estate_id UUID NOT NULL REFERENCES estates(id) ON DELETE CASCADE,
    lead_commission DECIMAL(5,2) NOT NULL,
    updated_by UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    comment TEXT,
    lead_status VARCHAR(50) NOT NULL,
    estate_status VARCHAR(50) NOT NULL,
    matched_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE(lead_id, estate_id)
    );

CREATE TABLE IF NOT EXISTS matches_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    match_id UUID NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    lead_commission DECIMAL(5,2) NOT NULL,
    updated_by UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    comment TEXT,
    created_at TIMESTAMP NOT NULL
    );