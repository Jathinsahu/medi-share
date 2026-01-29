import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [activeTab, setActiveTab] = useState('home');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);
  const [medicines, setMedicines] = useState([]);
  const [messages, setMessages] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [showMessageModal, setShowMessageModal] = useState(false);
  const [selectedMedicine, setSelectedMedicine] = useState(null);
  const [messageData, setMessageData] = useState({
    subject: '',
    content: '',
    urgency: 'medium'
  });

  // Registration state
  const [registerData, setRegisterData] = useState({
    fullName: '',
    email: '',
    password: '',
    phone: '',
    userType: 'receiver',
    address: ''
  });

  // Login state
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  // Medicine donation state
  const [medicineData, setMedicineData] = useState({
    medicineName: '',
    genericName: '',
    manufacturer: '',
    batchNumber: '',
    expiryDate: '',
    quantity: 1,
    unit: '',
    category: '',
    storageCondition: '',
    prescriptionRequired: false
  });

  // My medicines state
  const [myMedicines, setMyMedicines] = useState([]);

  // Chat state
  const [conversations, setConversations] = useState([]); // List of conversations
  const [activeConversation, setActiveConversation] = useState(null); // Currently open chat
  const [chatMessages, setChatMessages] = useState([]); // Messages in current chat
  const [newMessage, setNewMessage] = useState(''); // New message being typed

  // Handle register change
  const handleRegisterChange = (e) => {
    const { name, value } = e.target;
    setRegisterData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // Handle login change
  const handleLoginChange = (e) => {
    const { name, value } = e.target;
    if (name === 'email') setEmail(value);
    if (name === 'password') setPassword(value);
  };

  // Handle message change
  const handleMessageChange = (e) => {
    const { name, value } = e.target;
    setMessageData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // Load medicines
  const loadMedicines = async () => {
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
      }
    } catch (error) {
      console.error('Error fetching medicines:', error);
    }
  };

  // Load messages
  const loadMessages = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) return;

      const response = await fetch('/api/v1/messages', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const data = await response.json();
        setMessages(Array.isArray(data.content) ? data.content : []);
      }
    } catch (error) {
      console.error('Error fetching messages:', error);
    }
  };

  // Load unread count
  const loadUnreadCount = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) return;

      const response = await fetch('/api/v1/messages/unread-count', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const data = await response.json();
        setUnreadCount(data.unreadCount || 0);
      }
    } catch (error) {
      console.error('Error fetching unread count:', error);
    }
  };

  // Load user's conversations
  const loadConversations = async () => {
    if (!isLoggedIn) return;
    
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/v1/chat/conversations', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      
      if (response.ok) {
        const convos = await response.json();
        setConversations(convos);
      }
    } catch (error) {
      console.error('Error loading conversations:', error);
    }
  };

  // Load messages for a specific conversation
  const loadChatMessages = async (conversationId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch(`/api/v1/chat/history/${conversationId}/all`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      
      if (response.ok) {
        const messages = await response.json();
        setChatMessages(messages);
      }
    } catch (error) {
      console.error('Error loading messages:', error);
    }
  };

  // Load user's donated medicines
  const loadMyMedicines = async () => {
    if (!isLoggedIn) return;
    
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/v1/medicines/my-donations', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      
      if (response.ok) {
        const data = await response.json();
        setMyMedicines(Array.isArray(data.content) ? data.content : []);
      }
    } catch (error) {
      console.error('Error loading my medicines:', error);
    }
  };

  // Send a new message
  const handleSendMessage = async (e) => {
    e.preventDefault();
    
    if (!newMessage.trim() || !activeConversation) return;
    
    try {
      const token = localStorage.getItem('token');
      const messageData = {
        content: newMessage,
        recipientId: activeConversation.participantId,
        requestId: activeConversation.requestId
      };
      
      const response = await fetch('/api/v1/chat/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(messageData)
      });
      
      if (response.ok) {
        const sentMessage = await response.json();
        setChatMessages([...chatMessages, sentMessage]);
        setNewMessage('');
        loadConversations(); // Refresh conversation list
      }
    } catch (error) {
      console.error('Error sending message:', error);
    }
  };

  // Handle registration
  const handleRegister = async (e) => {
    e.preventDefault();
    console.log('=== REGISTER FORM SUBMITTED ===');
    console.log('Register form data:', registerData);

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
          fullName: '',
          email: '',
          password: '',
          phone: '',
          userType: 'receiver',
          address: ''
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

  // Handle login
  const handleLogin = async (e) => {
    e.preventDefault();
    console.log('=== LOGIN FORM SUBMITTED ===');
    console.log('Login form data:', { email, password });

    try {
      console.log('Sending POST request to: /api/v1/auth/login');
      const response = await fetch('/api/v1/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password })
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
        setCurrentUser(data.user);
        setIsLoggedIn(true);

        console.log('User logged in, redirecting to home tab');
        setActiveTab('home');
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

  // Handle logout
  const handleLogout = () => {
    console.log('Logging out...');
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setCurrentUser(null);
    setIsLoggedIn(false);
    setActiveTab('home');
    
    // Clear form data
    setEmail('');
    setPassword('');
    setRegisterData({
      fullName: '',
      email: '',
      password: '',
      phone: '',
      userType: 'receiver',
      address: ''
    });
    
    // Clear message data
    setMessageData({
      subject: '',
      content: '',
      urgency: 'medium'
    });
    setSelectedMedicine(null);
    setShowMessageModal(false);
    
    console.log('Logout completed, cleared all form data');
  };

  // Handle request medicine
  const handleRequestMedicine = async (medicineId) => {
    console.log('Requesting medicine with ID:', medicineId);
    
    // Check if user is logged in
    if (!isLoggedIn || !currentUser) {
      alert('Please login first to request medicine');
      return;
    }

    try {
      const token = localStorage.getItem('token');
      if (!token) {
        alert('Please login first');
        return;
      }

      // First, get the medicine details to find the donor
      const medicine = medicines.find(m => m.medicineId === medicineId);
      if (!medicine || !medicine.donor) {
        alert('Cannot find donor information');
        return;
      }

      // Check if already requested this medicine
      const existingRequests = await fetch('/api/v1/medicines/requests/my-requests', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(res => res.json());

      const alreadyRequested = existingRequests.some(req => 
        req.medicine && req.medicine.medicineId === medicineId && req.status === 'pending'
      );

      if (alreadyRequested) {
        alert('You have already requested this medicine');
        return;
      }

      // Create standardized request message
      const requestMessage = {
        content: `REQUEST FOR MEDICINE: ${medicine.medicineName}\n\n` +
                `Patient: ${currentUser.fullName}\n` +
                `Medicine: ${medicine.medicineName}\n` +
                `Quantity Needed: 1\n` +
                `Urgency: Medium\n\n` +
                `Hello ${medicine.donor.fullName}, I would like to request this medicine. ` +
                `Please let me know if it's available and how we can arrange pickup/delivery.`,
        recipientId: medicine.donor.userId,
        requestId: null // Will be created when medicine request is made
      };

      // Send the request message
      const response = await fetch('/api/v1/chat/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(requestMessage)
      });

      if (response.ok) {
        // Create the actual medicine request
        const requestResponse = await fetch(`/api/v1/medicines/${medicineId}/request`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({
            reason: `Requested via chat system`,
            urgency: 'medium'
          })
        });

        if (requestResponse.ok) {
          alert('Request sent successfully! You can continue communication in the Messages section.');
          loadMedicines(); // Refresh medicine list to show updated status
        } else {
          const errorText = await requestResponse.text();
          alert('Failed to create request: ' + errorText);
        }
      } else {
        const errorText = await response.text();
        alert('Failed to send message: ' + errorText);
      }
    } catch (error) {
      console.error('Request error:', error);
      alert('Request failed: ' + error.message);
    }
  };

  // Handle donate medicine
  const handleDonate = async (e) => {
    e.preventDefault();
    
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/v1/medicines', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(medicineData)
      });
      
      if (response.ok) {
        alert('Medicine donated successfully!');
        setMedicineData({
          medicineName: '',
          genericName: '',
          manufacturer: '',
          batchNumber: '',
          expiryDate: '',
          quantity: 1,
          unit: '',
          category: '',
          storageCondition: '',
          prescriptionRequired: false
        });
        loadMedicines(); // Refresh available medicines
      } else {
        const errorData = await response.json();
        alert(`Failed to donate: ${errorData.message}`);
      }
    } catch (error) {
      console.error('Donation error:', error);
      alert('Failed to donate medicine: ' + error.message);
    }
  };

  // Render auth section
  const renderAuthSection = () => (
    <div className="auth-section">
      {isLoggedIn ? (
        <div className="user-info">
          <span>Welcome, {currentUser?.email} ({currentUser?.userType})</span>
          <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
      ) : (
        <div className="auth-buttons">
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
        </div>
      )}
    </div>
  );

  // Render register form
  const renderRegisterForm = () => (
    <form onSubmit={handleRegister} className="auth-form">
      <h2>Register</h2>
      <input
        type="text"
        name="fullName"
        placeholder="Full Name"
        value={registerData.fullName}
        onChange={handleRegisterChange}
        required
      />
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
        <option value="receiver">Receiver</option>
        <option value="donor">Donor</option>
        <option value="ngo">NGO</option>
        <option value="admin">Admin</option>
      </select>
      <textarea
        name="address"
        placeholder="Address"
        value={registerData.address}
        onChange={handleRegisterChange}
        required
      />
      <button type="submit">Register</button>
    </form>
  );

  // Render login form
  const renderLoginForm = () => (
    <form onSubmit={handleLogin} className="auth-form">
      <h2>Login</h2>
      <input
        type="email"
        name="email"
        placeholder="Email"
        value={email}
        onChange={handleLoginChange}
        required
      />
      <input
        type="password"
        name="password"
        placeholder="Password"
        value={password}
        onChange={handleLoginChange}
        required
      />
      <button type="submit">Login</button>
    </form>
  );

  // Render medicines list
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

  // Render messages section
  const renderMessagesSection = () => (
    <div className="messages-section">
      <h2>Messages</h2>
      {isLoggedIn ? (
        <div className="chat-container">
          {conversations.length === 0 ? (
            <div className="no-conversations">
              <p>No conversations yet</p>
              <p>Start by requesting medicines to begin chatting</p>
            </div>
          ) : (
            <div className="chat-layout">
              {/* Conversations sidebar */}
              <div className="conversations-list">
                <h3>Conversations</h3>
                {conversations.map(convo => (
                  <div 
                    key={convo.conversationId}
                    className={`conversation-item ${activeConversation?.conversationId === convo.conversationId ? 'active' : ''}`}
                    onClick={() => {
                      setActiveConversation(convo);
                      loadChatMessages(convo.conversationId);
                    }}
                  >
                    <div className="conversation-header">
                      <strong>{convo.participantName}</strong>
                      <span className="conversation-medicine">{convo.medicineName}</span>
                    </div>
                    <div className="conversation-preview">
                      {convo.lastMessage?.content.substring(0, 50)}...
                    </div>
                    <div className="conversation-status">
                      {convo.unreadCount > 0 && (
                        <span className="unread-badge">{convo.unreadCount}</span>
                      )}
                      <span className="message-time">
                        {new Date(convo.lastMessage?.createdAt).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
              
              {/* Chat area */}
              <div className="chat-area">
                {activeConversation ? (
                  <>
                    <div className="chat-header">
                      <h3>Chat with {activeConversation.participantName}</h3>
                      <p>Regarding: {activeConversation.medicineName}</p>
                    </div>
                    
                    <div className="messages-display">
                      {chatMessages.map(msg => (
                        <div 
                          key={msg.messageId} 
                          className={`message-bubble ${msg.senderId === currentUser.userId ? 'sent' : 'received'}`}
                        >
                          <div className="message-content">
                            <p>{msg.content}</p>
                            <div className="message-meta">
                              <span className="message-time">
                                {new Date(msg.createdAt).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                              </span>
                              {msg.senderId === currentUser.userId && (
                                <span className="message-status">
                                  {msg.readStatus ? '✓✓' : '✓'}
                                </span>
                              )}
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                    
                    <form className="message-input-form" onSubmit={handleSendMessage}>
                      <input
                        type="text"
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        placeholder="Type a message..."
                        className="message-input"
                      />
                      <button type="submit" className="send-button">Send</button>
                    </form>
                  </>
                ) : (
                  <div className="select-conversation">
                    <p>Select a conversation to start chatting</p>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      ) : (
        <div className="login-required">
          <p>Please login to view your messages</p>
        </div>
      )}
    </div>
  );

  // Render message modal
  const renderMessageModal = () => (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Send Message to Donor</h2>
        <p>Requesting: <strong>{selectedMedicine?.medicineName}</strong></p>
        <p>Donor: <strong>{selectedMedicine?.donor?.fullName}</strong></p>
        
        <form onSubmit={handleSendMessage}>
          <input
            type="text"
            name="subject"
            placeholder="Subject"
            value={messageData.subject}
            onChange={handleMessageChange}
            required
          />
          <textarea
            name="content"
            placeholder="Your message..."
            value={messageData.content}
            onChange={handleMessageChange}
            rows="5"
            required
          />
          <label>
            Urgency Level:
            <select
              name="urgency"
              value={messageData.urgency}
              onChange={handleMessageChange}
            >
              <option value="low">Low</option>
              <option value="medium">Medium</option>
              <option value="high">High</option>
            </select>
          </label>
          <div className="modal-buttons">
            <button type="submit">Send Message</button>
            <button type="button" onClick={() => setShowMessageModal(false)}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );

  // Render home section
  const renderHomeSection = () => (
    <div className="home-section">
      <h2>Welcome to MediShare</h2>
      <p>Connect with donors and receivers to share life-saving medicines</p>
      {!isLoggedIn && (
        <div className="cta-section">
          <button onClick={() => setActiveTab('register')} className="cta-button">
            Get Started - Register Now
          </button>
        </div>
      )}
    </div>
  );

  // Render login section
  const renderLoginSection = () => (
    <div className="auth-section">
      <h2>Login to Your Account</h2>
      <form onSubmit={handleLogin} className="auth-form">
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Login</button>
      </form>
    </div>
  );

  // Render register section
  const renderRegisterSection = () => (
    <div className="auth-section">
      <h2>Create New Account</h2>
      <form onSubmit={handleRegister} className="auth-form">
        <input
          type="text"
          placeholder="Full Name"
          value={registerData.fullName}
          onChange={(e) => setRegisterData({...registerData, fullName: e.target.value})}
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={registerData.email}
          onChange={(e) => setRegisterData({...registerData, email: e.target.value})}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={registerData.password}
          onChange={(e) => setRegisterData({...registerData, password: e.target.value})}
          required
        />
        <input
          type="tel"
          placeholder="Phone Number"
          value={registerData.phone}
          onChange={(e) => setRegisterData({...registerData, phone: e.target.value})}
        />
        <select
          value={registerData.userType}
          onChange={(e) => setRegisterData({...registerData, userType: e.target.value})}
        >
          <option value="receiver">Receiver/Patient</option>
          <option value="donor">Donor</option>
          <option value="ngo">NGO</option>
        </select>
        <textarea
          placeholder="Address"
          value={registerData.address}
          onChange={(e) => setRegisterData({...registerData, address: e.target.value})}
        />
        <button type="submit">Register</button>
      </form>
    </div>
  );

  // Render donate section
  const renderDonateSection = () => (
    <div className="donate-section">
      <h2>Donate Medicine</h2>
      <form onSubmit={handleDonate} className="donate-form">
        <input
          type="text"
          placeholder="Medicine Name"
          value={medicineData.medicineName}
          onChange={(e) => setMedicineData({...medicineData, medicineName: e.target.value})}
          required
        />
        <input
          type="text"
          placeholder="Generic Name (optional)"
          value={medicineData.genericName}
          onChange={(e) => setMedicineData({...medicineData, genericName: e.target.value})}
        />
        <input
          type="text"
          placeholder="Manufacturer"
          value={medicineData.manufacturer}
          onChange={(e) => setMedicineData({...medicineData, manufacturer: e.target.value})}
        />
        <input
          type="text"
          placeholder="Batch Number"
          value={medicineData.batchNumber}
          onChange={(e) => setMedicineData({...medicineData, batchNumber: e.target.value})}
        />
        <input
          type="date"
          placeholder="Expiry Date"
          value={medicineData.expiryDate}
          onChange={(e) => setMedicineData({...medicineData, expiryDate: e.target.value})}
          required
        />
        <input
          type="number"
          placeholder="Quantity"
          value={medicineData.quantity}
          onChange={(e) => setMedicineData({...medicineData, quantity: parseInt(e.target.value)})}
          required
        />
        <input
          type="text"
          placeholder="Unit (tablets, ml, strips)"
          value={medicineData.unit}
          onChange={(e) => setMedicineData({...medicineData, unit: e.target.value})}
        />
        <input
          type="text"
          placeholder="Category"
          value={medicineData.category}
          onChange={(e) => setMedicineData({...medicineData, category: e.target.value})}
        />
        <textarea
          placeholder="Storage Condition"
          value={medicineData.storageCondition}
          onChange={(e) => setMedicineData({...medicineData, storageCondition: e.target.value})}
        />
        <label>
          <input
            type="checkbox"
            checked={medicineData.prescriptionRequired}
            onChange={(e) => setMedicineData({...medicineData, prescriptionRequired: e.target.checked})}
          />
          Prescription Required
        </label>
        <button type="submit">Donate Medicine</button>
      </form>
    </div>
  );

  // Render my medicines section
  const renderMyMedicinesSection = () => (
    <div className="my-medicines-section">
      <h2>My Donated Medicines</h2>
      {myMedicines.length === 0 ? (
        <p>You haven't donated any medicines yet.</p>
      ) : (
        <div className="medicines-grid">
          {myMedicines.map(medicine => (
            <div key={medicine.medicineId} className="medicine-card">
              <h3>{medicine.medicineName}</h3>
              <p><strong>Status:</strong> {medicine.status}</p>
              <p><strong>Quantity:</strong> {medicine.quantity} {medicine.unit}</p>
              <p><strong>Expiry:</strong> {new Date(medicine.expiryDate).toLocaleDateString()}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );

  // Fetch data when component mounts or when user logs in
  useEffect(() => {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');

    if (token && user) {
      setIsLoggedIn(true);
      setCurrentUser(JSON.parse(user));
      loadMedicines();
      loadMessages();
      loadConversations();
      loadMyMedicines(); // Load user's donated medicines
    }
  }, []);

  return (
    <div className="App">
      <header className="app-header">
        <h1>MediShare - Medicine Donation Platform</h1>
        {renderAuthSection()}
      </header>

      <nav className="main-nav">
        <button 
          className={activeTab === 'home' ? 'active' : ''} 
          onClick={() => setActiveTab('home')}
        >
          Home
        </button>
        {isLoggedIn && (
          <>
            <button 
              className={activeTab === 'donate' ? 'active' : ''} 
              onClick={() => setActiveTab('donate')}
            >
              Donate Medicine
            </button>
            <button 
              className={activeTab === 'my-medicines' ? 'active' : ''} 
              onClick={() => setActiveTab('my-medicines')}
            >
              My Donations
            </button>
            <button 
              className={activeTab === 'messages' ? 'active' : ''} 
              onClick={() => setActiveTab('messages')}
            >
              Messages {unreadCount > 0 && `(${unreadCount})`}
            </button>
          </>
        )}
      </nav>

      <main className="main-content">
        {activeTab === 'home' && renderHomeSection()}
        {activeTab === 'login' && renderLoginSection()}
        {activeTab === 'register' && renderRegisterSection()}
        {activeTab === 'donate' && isLoggedIn && renderDonateSection()}
        {activeTab === 'my-medicines' && isLoggedIn && renderMyMedicinesSection()}
        {activeTab === 'messages' && renderMessagesSection()}
      </main>
    </div>
  );
}

export default App;