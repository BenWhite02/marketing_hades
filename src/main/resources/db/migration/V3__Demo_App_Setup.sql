-- Create demo organization
INSERT INTO organizations (id, name, domain, subscription_type, settings, is_active)
VALUES (
    'dd1e8c50-7c4a-4b5d-9a6e-2f3b8d4c5e6f'::UUID,
    'Demo Organization',
    'demo.kairos.com',
    'FREE',
    '{"maxApps": 5, "maxUsers": 50}',
    true
) ON CONFLICT DO NOTHING;

-- Function to create sandbox app for any organization
CREATE OR REPLACE FUNCTION create_sandbox_app(org_id UUID, creator_id UUID)
RETURNS UUID AS $$
DECLARE
    sandbox_app_id UUID;
BEGIN
    -- Check if sandbox already exists
    SELECT id INTO sandbox_app_id
    FROM apps
    WHERE organization_id = org_id AND app_type = 'SANDBOX';
    
    -- If not exists, create it
    IF sandbox_app_id IS NULL THEN
        INSERT INTO apps (
            id, organization_id, name, display_name, description,
            app_type, settings, is_active, created_by
        ) VALUES (
            gen_random_uuid(),
            org_id,
            'kairos-sandbox',
            'Kairos Sandbox',
            'Demo environment for exploring Kairos platform features',
            'SANDBOX',
            '{
                "maxCustomers": 1000,
                "maxCampaigns": 10,
                "maxChannels": 3,
                "dataRetentionDays": 30,
                "mockProviders": true
            }',
            true,
            creator_id
        ) RETURNING id INTO sandbox_app_id;
    END IF;
    
    RETURN sandbox_app_id;
END;
$$ LANGUAGE plpgsql;

-- Trigger to auto-create sandbox when user is created
CREATE OR REPLACE FUNCTION auto_create_sandbox_access()
RETURNS TRIGGER AS $$
DECLARE
    sandbox_app_id UUID;
BEGIN
    -- Create/get sandbox app
    SELECT create_sandbox_app(NEW.organization_id, NEW.id) INTO sandbox_app_id;
    
    -- Grant access to sandbox
    INSERT INTO user_app_access (
        user_id, app_id, role, permissions, granted_by, is_active
    ) VALUES (
        NEW.id,
        sandbox_app_id,
        'CAMPAIGN_MANAGER',
        ARRAY['CUSTOMER_VIEW', 'CUSTOMER_CREATE', 'CAMPAIGN_VIEW', 'CAMPAIGN_CREATE', 
              'MOMENT_VIEW', 'MOMENT_CREATE', 'ANALYTICS_VIEW'],
        NEW.id,
        true
    ) ON CONFLICT (user_id, app_id) DO NOTHING;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_auto_create_sandbox_access
    AFTER INSERT ON users
    FOR EACH ROW
    EXECUTE FUNCTION auto_create_sandbox_access();
