CREATE TABLE recommendations (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     source_id UUID NOT NULL,
     source_type VARCHAR(50) NOT NULL CHECK (source_type IN ('lead', 'estate')),
     target_id UUID NOT NULL,
     similarity_score DECIMAL(6,4) NOT NULL,
     similarity_reason TEXT,
);

CREATE INDEX idx_recommendations_source_similarity
    ON recommendations (source_id, similarity_score DESC);