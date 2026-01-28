-- Users Table
CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    phone VARCHAR(15),
    user_type VARCHAR(20) CHECK (user_type IN ('donor', 'receiver', 'ngo', 'admin')),
    location_lat DECIMAL(10, 8),
    location_lng DECIMAL(11, 8),
    address TEXT,
    verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW(),
    last_login TIMESTAMP
);

-- Medicines Table
CREATE TABLE medicines (
    medicine_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    donor_id UUID REFERENCES users(user_id),
    medicine_name VARCHAR(255) NOT NULL,
    generic_name VARCHAR(255),
    manufacturer VARCHAR(255),
    batch_number VARCHAR(50),
    expiry_date DATE NOT NULL,
    quantity INTEGER NOT NULL,
    unit VARCHAR(20), -- tablets, ml, strips
    category VARCHAR(50), -- antibiotic, painkiller, etc.
    storage_condition VARCHAR(100),
    prescription_required BOOLEAN,
    status VARCHAR(20) DEFAULT 'available' CHECK (status IN ('available', 'reserved', 'donated', 'expired', 'rejected')),
    verification_status VARCHAR(20) DEFAULT 'pending' CHECK (verification_status IN ('pending', 'approved', 'rejected')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Table for storing medicine image URLs
CREATE TABLE medicine_images (
    medicine_id UUID REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    image_url TEXT,
    PRIMARY KEY (medicine_id, image_url)
);

-- Requests Table (Matching System)
CREATE TABLE donation_requests (
    request_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medicine_id UUID REFERENCES medicines(medicine_id),
    requester_id UUID REFERENCES users(user_id),
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'rejected', 'completed')),
    reason TEXT, -- why they need it
    urgency VARCHAR(10) CHECK (urgency IN ('low', 'medium', 'high')),
    created_at TIMESTAMP DEFAULT NOW(),
    completed_at TIMESTAMP
);

-- Verifications Table (Quality Control)
CREATE TABLE verifications (
    verification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medicine_id UUID REFERENCES medicines(medicine_id),
    verifier_id UUID REFERENCES users(user_id),
    verification_type VARCHAR(20) CHECK (verification_type IN ('photo', 'document', 'physical')),
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'rejected')),
    notes TEXT,
    verified_at TIMESTAMP
);

-- Analytics/Impact Table
CREATE TABLE donations_completed (
    donation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medicine_id UUID REFERENCES medicines(medicine_id),
    donor_id UUID REFERENCES users(user_id),
    receiver_id UUID REFERENCES users(user_id),
    estimated_value DECIMAL(10, 2), -- monetary value saved
    feedback_rating INTEGER CHECK (feedback_rating BETWEEN 1 AND 5),
    feedback_text TEXT,
    completed_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for better performance
CREATE INDEX idx_medicines_status ON medicines(status);
CREATE INDEX idx_medicines_expiry_date ON medicines(expiry_date);
CREATE INDEX idx_medicines_category ON medicines(category);
CREATE INDEX idx_medicines_donor_id ON medicines(donor_id);
CREATE INDEX idx_donation_requests_status ON donation_requests(status);
CREATE INDEX idx_donation_requests_medicine_id ON donation_requests(medicine_id);
CREATE INDEX idx_donation_requests_requester_id ON donation_requests(requester_id);
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_users_location ON users(location_lat, location_lng);