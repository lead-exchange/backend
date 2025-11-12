CREATE INDEX idx_recommendations_by_target
    ON recommendations (target_id, similarity_score DESC);