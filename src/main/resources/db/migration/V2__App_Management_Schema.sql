-- Apps table (enhanced)
CREATE TABLE apps (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organization_id UUID NOT NULL REFERENCES organizations(id),
    name VARCHAR(100) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    description TEXT,
    app_type VARCHAR(50) NOT NULL DEFAULT 'PRODUCTION',
    domain VARCHAR(255),
    settings JSONB NOT NULL DEFAULT '{}',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_by UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(organization_id, name)
);

-- User app access table
CREATE TABLE user_app_access (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    app_id UUID NOT NULL REFERENCES apps(id),
    role VARCHAR(50) NOT NULL,
    permissions TEXT[] NOT NULL DEFAULT '{}',
    granted_by UUID NOT NULL REFERENCES users(id),
    granted_at TIMESTAMP NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    UNIQUE(user_id, app_id)
);

-- Indexes for apps
CREATE INDEX idx_apps_organization_id ON apps(organization_id);
CREATE INDEX idx_apps_app_type ON apps(app_type);
CREATE INDEX idx_apps_is_active ON apps(is_active);
CREATE INDEX idx_apps_created_by ON apps(created_by);

-- Indexes for user_app_access
CREATE INDEX idx_user_app_access_user_id ON user_app_access(user_id);
CREATE INDEX idx_user_app_access_app_id ON user_app_access(app_id);
CREATE INDEX idx_user_app_access_is_active ON user_app_access(is_active);
CREATE INDEX idx_user_app_access_user_app ON user_app_access(user_id, app_id);
