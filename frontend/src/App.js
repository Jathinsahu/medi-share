import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [activeTab, setActiveTab] = useState('register');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [medicines, setMedicines] = useState([]);
  const [donateFormData, setDonateFormData] = useState({
    medicineName: '',
    category: '',
    expiryDate: '',
    quantity: 1,
    unit: 'tablets',
    manufacturer: '',
    batchNumber: '',
    genericName: '',
    storageCondition: '',
    prescriptionRequired: false
  });

  // Registration state
  const [registerData, setRegisterData] = useState({
    email: '',
    password: '',
    fullName: '',
    phone: '',
    userType: 'donor'
  });

  // Login state
  const [loginData, setLoginData] = useState({
    email: '',
    password: ''
  });

  // Fetch medicines when component mounts or when user logs in
  useEffect(() => {
    if (isLoggedIn && user) {
      fetchMedicines();
    }
  }, [isLoggedIn, user]);

  const fetchMedicines = async () => {
    try {
      console.log('=== FETCHING MEDICINES ===');
      const token = localStorage.getItem('token');
      if (!token) {
        console.log('No token found, cannot fetch medicines');
        return;
      }

      const response = await fetch('/api/v1/medicines/available', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      console.log('=== MEDICINES RESPONSE RECEIVED ===');
      console.log('Response status:', response.status);
      console.log('Response OK?', response.ok);

      if (response.ok) {
        const data = await response.json();
        console.log('Medicines data:', data);
        setMedicines(Array.isArray(data.content) ? data.content : []);
      } else {
        console.error('Failed to fetch medicines:', response.status, response.statusText);
        // Handle error appropriately
      }
    } catch (error) {
      console.error('Error fetching medicines:', error);
    }
  };

  const handleRegisterChange = (e) => {
    const { name, value } = e.target;
    setRegisterData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleLoginChange = (e) => {
    const { name, value } = e.target;
    setLoginData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleDonateChange = (e) => {
    const { name, value, type, checked } = e.target;
    setDonateFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    console.log('=== REGISTER FORM SUBMITTED ===');
    console.log('Register form data:', registerData);
    console.log('Email:', registerData.email);
    console.log('Password length:', registerData.password.length);
    console.log('Full Name:', registerData.fullName);
    console.log('Phone:', registerData.phone);
    console.log('User Type:', registerData.userType);

    try {
      console.log('Sending POST request to: /api/v1/auth/register');
      const response = await fetch('/api/v1/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(registerData)
      });

      console.log('=== REGISTER RESPONSE RECEIVED ===');
      console.log('Response status:', response.status);
      console.log('Response OK?', response.ok);

      if (response.ok) {
        const data = await response.json();
        console.log('Registration successful!', data);
        alert('Registration successful!');
        
        // Clear the form data after successful registration
        setRegisterData({
          email: '',
          password: '',
          fullName: '',
          phone: '',
          userType: 'donor'
        });
        
        // Switch to login tab
        setActiveTab('login');
      } else {
        const errorData = await response.json();
        console.log('Registration failed with status:', response.status);
        console.log('Error response body:', errorData);
        alert(`Registration failed: ${errorData.message || 'Unknown error'}`);
      }
    } catch (error) {
      console.error('Registration error:', error);
      alert('Registration failed: ' + error.message);
    }
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    console.log('=== LOGIN FORM SUBMITTED ===');
    console.log('Login form data:', loginData);
    console.log('Email:', loginData.email);
    console.log('Password length:', loginData.password.length);

    try {
      console.log('Sending POST request to: /api/v1/auth/login');
      const response = await fetch('/api/v1/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData)
      });

      console.log('=== LOGIN RESPONSE RECEIVED ===');
      console.log('Response status:', response.status);
      console.log('Response OK?', response.ok);

      if (response.ok) {
        const data = await response.json();
        console.log('Login successful!', data);

        // Store token in localStorage
        localStorage.setItem('token', data.token);
        console.log('Token stored in localStorage');

        // Set user data
        setUser({
          id: data.userId,
          email: loginData.email,
          userType: data.userType
        });
        setIsLoggedIn(true);

        console.log('User logged in, redirecting to donate tab');
        setActiveTab('donate'); // Redirect to donate tab after login
      } else {
        const errorData = await response.json();
        console.log('Login failed with status:', response.status);
        console.log('Error response body:', errorData);
        alert(`Login failed: ${errorData.message || 'Invalid credentials'}`);
      }
    } catch (error) {
      console.error('Login error:', error);
      alert('Login failed: ' + error.message);
    }
  };

  const handleLogout = () => {
    console.log('Logging out...');
    localStorage.removeItem('token');
    setIsLoggedIn(false);
    setUser(null);
    setMedicines([]);
    setActiveTab('login');
    console.log('Logged out successfully');
  };

  const handleDonateMedicine = async (e) => {
    e.preventDefault();
    console.log('=== DONATE MEDICINE FORM SUBMITTED ===');
    console.log('Donate form data:', donateFormData);

    try {
      const token = localStorage.getItem('token');
      if (!token) {
        console.log('No token found, cannot donate medicine');
        alert('Please login first');
        return;
      }

      console.log('Sending POST request to: /api/v1/medicines');
      const response = await fetch('/api/v1/medicines', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(donateFormData)
      });

      console.log('=== DONATE MEDICINE RESPONSE RECEIVED ===');
      console.log('Response status:', response.status);
      console.log('Response OK?', response.ok);

      if (response.ok) {
        const data = await response.json();
        console.log('Medicine donated successfully!', data);
        alert('Medicine donated successfully!');
        
        // Clear the form data after successful donation
        setDonateFormData({
          medicineName: '',
          category: '',
          expiryDate: '',
          quantity: 1,
          unit: 'tablets',
          manufacturer: '',
          batchNumber: '',
          genericName: '',
          storageCondition: '',
          prescriptionRequired: false
        });
        
        // Refresh medicines list
        fetchMedicines();
      } else {
        const errorData = await response.json();
        console.log('Donation failed with status:', response.status);
        console.log('Error response body:', errorData);
        alert(`Donation failed: ${errorData.message || 'Unknown error'}`);
      }
    } catch (error) {
      console.error('Donation error:', error);
      alert('Donation failed: ' + error.message);
    }
  };

  const handleRequestMedicine = async (medicineId) => {
    console.log('Requesting medicine with ID:', medicineId);
    
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        console.log('No token found, cannot request medicine');
        alert('Please login first');
        return;
      }

      const response = await fetch(`/api/v1/medicines/${medicineId}/request`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          reason: 'Need this medicine for treatment'
        })
      });

      if (response.ok) {
        alert('Medicine requested successfully! The donor will be notified.');
        fetchMedicines(); // Refresh the list
      } else {
        const errorData = await response.json();
        alert(`Request failed: ${errorData.message || 'Unknown error'}`);
      }
    } catch (error) {
      console.error('Request error:', error);
      alert('Request failed: ' + error.message);
    }
  };

  const renderRegisterForm = () => (
    <form onSubmit={handleRegister} className="auth-form">
      <h2>Register</h2>
      <input
        type="email"
        name="email"
        placeholder="Email"
        value={registerData.email}
        onChange={handleRegisterChange}
        required
      />
      <input
        type="password"
        name="password"
        placeholder="Password"
        value={registerData.password}
        onChange={handleRegisterChange}
        required
      />
      <input
        type="text"
        name="fullName"
        placeholder="Full Name"
        value={registerData.fullName}
        onChange={handleRegisterChange}
        required
      />
      <input
        type="tel"
        name="phone"
        placeholder="Phone"
        value={registerData.phone}
        onChange={handleRegisterChange}
        required
      />
      <select
        name="userType"
        value={registerData.userType}
        onChange={handleRegisterChange}
        required
      >
        <option value="donor">Donor</option>
        <option value="receiver">Receiver</option>
        <option value="ngo">NGO</option>
        <option value="admin">Admin</option>
      </select>
      <button type="submit">Register</button>
    </form>
  );

  const renderLoginForm = () => (
    <form onSubmit={handleLogin} className="auth-form">
      <h2>Login</h2>
      <input
        type="email"
        name="email"
        placeholder="Email"
        value={loginData.email}
        onChange={handleLoginChange}
        required
      />
      <input
        type="password"
        name="password"
        placeholder="Password"
        value={loginData.password}
        onChange={handleLoginChange}
        required
      />
      <button type="submit">Login</button>
    </form>
  );

  const renderDonateForm = () => (
    <form onSubmit={handleDonateMedicine} className="donate-form">
      <h2>Donate Medicine</h2>
      <input
        type="text"
        name="medicineName"
        placeholder="Medicine Name"
        value={donateFormData.medicineName}
        onChange={handleDonateChange}
        required
      />
      <input
        type="text"
        name="category"
        placeholder="Category (e.g., Antibiotic, Painkiller)"
        value={donateFormData.category}
        onChange={handleDonateChange}
        required
      />
      <input
        type="date"
        name="expiryDate"
        placeholder="Expiry Date"
        value={donateFormData.expiryDate}
        onChange={handleDonateChange}
        required
      />
      <div className="number-input">
        <input
          type="number"
          name="quantity"
          min="1"
          placeholder="Quantity"
          value={donateFormData.quantity}
          onChange={handleDonateChange}
          required
        />
        <select
          name="unit"
          value={donateFormData.unit}
          onChange={handleDonateChange}
          required
        >
          <option value="tablets">Tablets</option>
          <option value="capsules">Capsules</option>
          <option value="syrup">Syrup (ml)</option>
          <option value="ointment">Ointment (gm)</option>
          <option value="drops">Drops</option>
        </select>
      </div>
      <input
        type="text"
        name="manufacturer"
        placeholder="Manufacturer"
        value={donateFormData.manufacturer}
        onChange={handleDonateChange}
      />
      <input
        type="text"
        name="batchNumber"
        placeholder="Batch Number"
        value={donateFormData.batchNumber}
        onChange={handleDonateChange}
      />
      <input
        type="text"
        name="genericName"
        placeholder="Generic Name"
        value={donateFormData.genericName}
        onChange={handleDonateChange}
      />
      <input
        type="text"
        name="storageCondition"
        placeholder="Storage Condition"
        value={donateFormData.storageCondition}
        onChange={handleDonateChange}
      />
      <label>
        <input
          type="checkbox"
          name="prescriptionRequired"
          checked={donateFormData.prescriptionRequired}
          onChange={handleDonateChange}
        />
        Prescription Required
      </label>
      <button type="submit">Donate Medicine</button>
    </form>
  );

  const renderMedicinesList = () => (
    <div className="medicines-list">
      <h2>Available Medicines</h2>
      {medicines.length === 0 ? (
        <p>No medicines available at the moment.</p>
      ) : (
        <div className="medicine-grid">
          {medicines.map((medicine) => (
            <div key={medicine.medicineId} className="medicine-card">
              <h3>{medicine.medicineName}</h3>
              <p><strong>Category:</strong> {medicine.category}</p>
              <p><strong>Expiry:</strong> {new Date(medicine.expiryDate).toLocaleDateString()}</p>
              <p><strong>Quantity:</strong> {medicine.quantity} {medicine.unit}</p>
              <p><strong>Status:</strong> {medicine.status}</p>
              <p><strong>Donor:</strong> {medicine.donor?.fullName || 'Anonymous'}</p>
              <button 
                onClick={() => handleRequestMedicine(medicine.medicineId)}
                className="request-btn"
              >
                Request This Medicine
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );

  return (
    <div className="App">
      <header className="App-header">
        <h1>MediShare - Medicine Donation Platform</h1>
        
        {isLoggedIn && (
          <div className="user-info">
            <span>Welcome, {user?.email} ({user?.userType})</span>
            <button onClick={handleLogout} className="logout-btn">Logout</button>
          </div>
        )}
        
        <nav className="tab-nav">
          {!isLoggedIn ? (
            <>
              <button 
                className={activeTab === 'register' ? 'active' : ''}
                onClick={() => setActiveTab('register')}
              >
                Register
              </button>
              <button 
                className={activeTab === 'login' ? 'active' : ''}
                onClick={() => setActiveTab('login')}
              >
                Login
              </button>
            </>
          ) : (
            <>
              <button 
                className={activeTab === 'donate' ? 'active' : ''}
                onClick={() => setActiveTab('donate')}
              >
                Donate Medicine
              </button>
              <button 
                className={activeTab === 'medicines' ? 'active' : ''}
                onClick={() => setActiveTab('medicines')}
              >
                Available Medicines
              </button>
            </>
          )}
        </nav>
      </header>

      <main className="main-content">
        {activeTab === 'register' && !isLoggedIn && renderRegisterForm()}
        {activeTab === 'login' && !isLoggedIn && renderLoginForm()}
        {activeTab === 'donate' && isLoggedIn && renderDonateForm()}
        {activeTab === 'medicines' && isLoggedIn && renderMedicinesList()}
      </main>
    </div>
  );
}

export default App;