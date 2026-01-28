import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [medicines, setMedicines] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);
  const [activeTab, setActiveTab] = useState('browse'); // browse, donate, login, register
  const [loginForm, setLoginForm] = useState({ email: '', password: '' });
  const [registerForm, setRegisterForm] = useState({
    email: '',
    password: '',
    fullName: '',
    phone: '',
    userType: 'donor'
  });

  // const API_BASE_URL = 'http://localhost:8081/api/v1'; // Using proxy instead

  // Fetch available medicines
  useEffect(() => {
    if (activeTab === 'browse') {
      fetch(`/api/v1/medicines/available?page=0&size=20`)
        .then(response => response.json())
        .then(data => setMedicines(data.content || []))
        .catch(error => console.error('Error fetching medicines:', error));
    }
  }, [activeTab]);

  const handleLogin = async (e) => {
    e.preventDefault();
    console.log('=== LOGIN FORM SUBMITTED ===');
    console.log('Login form data:', loginForm);
    console.log('Email:', loginForm.email);
    console.log('Password length:', loginForm.password.length);
    
    try {
      console.log('Sending POST request to: /api/v1/auth/login');
      const response = await fetch(`/api/v1/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginForm),
      });
      
      console.log('=== LOGIN RESPONSE RECEIVED ===');
      console.log('Response status:', response.status);
      console.log('Response OK?', response.ok);
      
      if (response.ok) {
        const data = await response.json();
        console.log('Response data:', data);
        console.log('Token:', data.token ? `${data.token.substring(0, 20)}...` : 'No token');
        
        localStorage.setItem('token', data.token);
        console.log('Token stored in localStorage');
        
        setIsLoggedIn(true);
        setCurrentUser({ email: loginForm.email });
        console.log('User logged in, redirecting to donate tab');
        setActiveTab('donate');
      } else {
        console.error('Login failed with status:', response.status);
        const errorData = await response.text();
        console.error('Error response body:', errorData);
        alert('Login failed: ' + errorData);
      }
    } catch (error) {
      console.error('=== LOGIN ERROR ===');
      console.error('Error object:', error);
      console.error('Error message:', error.message);
      alert('Login failed: ' + error.message);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    console.log('=== REGISTRATION FORM SUBMITTED ===');
    console.log('Register form data:', registerForm);
    console.log('Email:', registerForm.email);
    console.log('Full Name:', registerForm.fullName);
    console.log('Phone:', registerForm.phone);
    console.log('User Type:', registerForm.userType);
    console.log('Password length:', registerForm.password.length);
    
    try {
      console.log('Sending POST request to: /api/v1/auth/register');
      const response = await fetch(`/api/v1/auth/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(registerForm),
      });
      
      console.log('=== REGISTRATION RESPONSE RECEIVED ===');
      console.log('Response status:', response.status);
      console.log('Response OK?', response.ok);
      
      if (response.ok) {
        const data = await response.json();
        console.log('Registration successful! Response data:', data);
        console.log('Token received:', data.token ? `${data.token.substring(0, 20)}...` : 'No token');
        alert('Registration successful! Please log in.');
        setActiveTab('login');
      } else {
        console.error('Registration failed with status:', response.status);
        const errorData = await response.text();
        console.error('Error response body:', errorData);
        alert('Registration failed: ' + errorData);
      }
    } catch (error) {
      console.error('=== REGISTRATION ERROR ===');
      console.error('Error object:', error);
      console.error('Error message:', error.message);
      alert('Registration failed: ' + error.message);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsLoggedIn(false);
    setCurrentUser(null);
    setActiveTab('browse');
  };

  const handleDonateMedicine = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const medicineData = {
      medicineName: formData.get('medicineName'),
      genericName: formData.get('genericName'),
      manufacturer: formData.get('manufacturer'),
      batchNumber: formData.get('batchNumber'),
      expiryDate: formData.get('expiryDate'),
      quantity: parseInt(formData.get('quantity')),
      unit: formData.get('unit') || 'tablets',
      category: formData.get('category'),
      storageCondition: formData.get('storageCondition'),
      prescriptionRequired: formData.get('prescriptionRequired') === 'true',
      imageUrls: [formData.get('imageUrl')] || []
    };

    try {
      const token = localStorage.getItem('token');
      const response = await fetch(`/api/v1/medicines`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(medicineData),
      });

      if (response.ok) {
        alert('Medicine donated successfully!');
        e.target.reset();
      } else {
        alert('Failed to donate medicine');
      }
    } catch (error) {
      console.error('Donate error:', error);
      alert('Failed to donate medicine');
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>MediShare</h1>
        <h2>Connecting Medicine Donors with Those in Need</h2>
        
        <nav>
          <button onClick={() => setActiveTab('browse')} className={activeTab === 'browse' ? 'active' : ''}>
            Browse Medicines
          </button>
          
          {!isLoggedIn ? (
            <>
              <button onClick={() => setActiveTab('login')} className={activeTab === 'login' ? 'active' : ''}>
                Login
              </button>
              <button onClick={() => setActiveTab('register')} className={activeTab === 'register' ? 'active' : ''}>
                Register
              </button>
            </>
          ) : (
            <>
              <button onClick={() => setActiveTab('donate')} className={activeTab === 'donate' ? 'active' : ''}>
                Donate Medicine
              </button>
              <button onClick={handleLogout}>
                Logout ({currentUser?.email})
              </button>
            </>
          )}
        </nav>
      </header>

      <main className="App-main">
        {/* Browse Medicines Tab */}
        {activeTab === 'browse' && (
          <div className="tab-content">
            <h3>Available Medicines</h3>
            <div className="medicines-grid">
              {medicines.length > 0 ? (
                medicines.map((medicine, index) => (
                  <div key={index} className="medicine-card">
                    <h4>{medicine.medicineName}</h4>
                    <p><strong>Generic:</strong> {medicine.genericName}</p>
                    <p><strong>Manufacturer:</strong> {medicine.manufacturer}</p>
                    <p><strong>Expiry:</strong> {medicine.expiryDate}</p>
                    <p><strong>Quantity:</strong> {medicine.quantity} {medicine.unit}</p>
                    <p><strong>Category:</strong> {medicine.category}</p>
                    <p><strong>Status:</strong> {medicine.status}</p>
                  </div>
                ))
              ) : (
                <p>No medicines available at the moment.</p>
              )}
            </div>
          </div>
        )}

        {/* Login Tab */}
        {activeTab === 'login' && (
          <div className="tab-content">
            <h3>Login</h3>
            <form onSubmit={handleLogin}>
              <div>
                <label>Email:</label>
                <input
                  type="email"
                  value={loginForm.email}
                  onChange={(e) => setLoginForm({...loginForm, email: e.target.value})}
                  required
                />
              </div>
              <div>
                <label>Password:</label>
                <input
                  type="password"
                  value={loginForm.password}
                  onChange={(e) => setLoginForm({...loginForm, password: e.target.value})}
                  required
                />
              </div>
              <button type="submit">Login</button>
            </form>
          </div>
        )}

        {/* Register Tab */}
        {activeTab === 'register' && (
          <div className="tab-content">
            <h3>Register</h3>
            <form onSubmit={handleRegister}>
              <div>
                <label>Email:</label>
                <input
                  type="email"
                  value={registerForm.email}
                  onChange={(e) => setRegisterForm({...registerForm, email: e.target.value})}
                  required
                />
              </div>
              <div>
                <label>Password:</label>
                <input
                  type="password"
                  value={registerForm.password}
                  onChange={(e) => setRegisterForm({...registerForm, password: e.target.value})}
                  required
                />
              </div>
              <div>
                <label>Full Name:</label>
                <input
                  type="text"
                  value={registerForm.fullName}
                  onChange={(e) => setRegisterForm({...registerForm, fullName: e.target.value})}
                  required
                />
              </div>
              <div>
                <label>Phone:</label>
                <input
                  type="tel"
                  value={registerForm.phone}
                  onChange={(e) => setRegisterForm({...registerForm, phone: e.target.value})}
                />
              </div>
              <div>
                <label>User Type:</label>
                <select
                  value={registerForm.userType}
                  onChange={(e) => setRegisterForm({...registerForm, userType: e.target.value})}
                >
                  <option value="donor">Donor</option>
                  <option value="receiver">Receiver</option>
                  <option value="ngo">NGO</option>
                  <option value="admin">Admin</option>
                </select>
              </div>
              <button type="submit">Register</button>
            </form>
          </div>
        )}

        {/* Donate Medicine Tab */}
        {activeTab === 'donate' && isLoggedIn && (
          <div className="tab-content">
            <h3>Donate Medicine</h3>
            <form onSubmit={handleDonateMedicine}>
              <div className="form-row">
                <div className="form-group">
                  <label>Medicine Name *</label>
                  <input type="text" name="medicineName" required />
                </div>
                <div className="form-group">
                  <label>Generic Name</label>
                  <input type="text" name="genericName" />
                </div>
              </div>
              
              <div className="form-row">
                <div className="form-group">
                  <label>Manufacturer</label>
                  <input type="text" name="manufacturer" />
                </div>
                <div className="form-group">
                  <label>Batch Number</label>
                  <input type="text" name="batchNumber" />
                </div>
              </div>
              
              <div className="form-row">
                <div className="form-group">
                  <label>Expiry Date *</label>
                  <input type="date" name="expiryDate" required />
                </div>
                <div className="form-group">
                  <label>Quantity *</label>
                  <input type="number" name="quantity" min="1" required />
                </div>
              </div>
              
              <div className="form-row">
                <div className="form-group">
                  <label>Unit</label>
                  <select name="unit">
                    <option value="tablets">Tablets</option>
                    <option value="ml">ML</option>
                    <option value="strips">Strips</option>
                    <option value="capsules">Capsules</option>
                    <option value="other">Other</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Category</label>
                  <input type="text" name="category" placeholder="e.g., Antibiotic, Painkiller" />
                </div>
              </div>
              
              <div className="form-row">
                <div className="form-group">
                  <label>Storage Condition</label>
                  <input type="text" name="storageCondition" placeholder="e.g., Cool, Dry place" />
                </div>
                <div className="form-group">
                  <label>Prescription Required?</label>
                  <select name="prescriptionRequired">
                    <option value="true">Yes</option>
                    <option value="false">No</option>
                  </select>
                </div>
              </div>
              
              <div className="form-group">
                <label>Image URL</label>
                <input type="url" name="imageUrl" placeholder="https://example.com/image.jpg" />
              </div>
              
              <button type="submit">Donate Medicine</button>
            </form>
          </div>
        )}

        {activeTab === 'donate' && !isLoggedIn && (
          <div className="tab-content">
            <h3>Please Login to Donate Medicine</h3>
            <p>You need to be logged in to donate medicines. Please use the login tab above.</p>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;