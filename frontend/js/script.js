/**
 * script.js - Bank Management System Frontend Logic
 * Demonstrates: Form Validation, DOM Manipulation,
 *               Event Handling, LocalStorage, Dynamic UI
 */

// ============================================================
//  UTILITY FUNCTIONS
// ============================================================

/**
 * Show an alert message dynamically
 * @param {string} elementId - ID of alert div
 * @param {string} message   - Message to display
 * @param {string} type      - 'success' | 'danger' | 'info' | 'warning'
 */
function showAlert(elementId, message, type = 'info') {
    const alert = document.getElementById(elementId);
    if (!alert) return;
    alert.className = `alert alert-${type} show`;
    alert.innerHTML = `<span>${getAlertIcon(type)}</span> ${message}`;
    // Auto-hide after 5 seconds
    setTimeout(() => { alert.classList.remove('show'); }, 5000);
}

function getAlertIcon(type) {
    const icons = { success: '✓', danger: '✗', info: 'ℹ', warning: '⚠' };
    return icons[type] || 'ℹ';
}

/**
 * Show/hide error below a field
 */
function showFieldError(fieldId, message) {
    const field = document.getElementById(fieldId);
    const error = document.getElementById(fieldId + 'Error');
    if (field)  field.classList.add('error');
    if (error)  { error.textContent = message; error.classList.add('show'); }
}

function clearFieldError(fieldId) {
    const field = document.getElementById(fieldId);
    const error = document.getElementById(fieldId + 'Error');
    if (field)  { field.classList.remove('error'); field.classList.remove('valid'); }
    if (error)  error.classList.remove('show');
}

function markFieldValid(fieldId) {
    const field = document.getElementById(fieldId);
    if (field) { field.classList.remove('error'); field.classList.add('valid'); }
    clearFieldError(fieldId);
}

// ============================================================
//  VALIDATION HELPERS
// ============================================================

const Validators = {
    // Name: only letters and spaces, min 2 chars
    isValidName: (name) => /^[A-Za-z\s]{2,50}$/.test(name.trim()),

    // Email format
    isValidEmail: (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim()),

    // Phone: 10 digits
    isValidPhone: (phone) => /^\d{10}$/.test(phone.trim()),

    // Password: min 6 chars, at least 1 number
    isValidPassword: (pwd) => pwd.length >= 6 && /\d/.test(pwd),

    // Amount: positive number
    isValidAmount: (amount) => !isNaN(amount) && parseFloat(amount) > 0,

    // Account ID format
    isValidAccountId: (id) => /^(SAV|CUR)\d{4,}$/i.test(id.trim()),

    // Date of birth: must be in the past and age >= 18
    isValidDOB: (dateStr) => {
        const dob = new Date(dateStr);
        const today = new Date();
        if (isNaN(dob.getTime())) return false;
        const age = today.getFullYear() - dob.getFullYear();
        return dob < today && age >= 18;
    }
};

// ============================================================
//  REGISTRATION FORM VALIDATION
// ============================================================

function validateRegisterForm() {
    let isValid = true;
    const fields = ['firstName', 'lastName', 'email', 'phone', 'address', 'password', 'confirmPassword', 'dob'];
    fields.forEach(f => clearFieldError(f));

    const firstName = document.getElementById('firstName')?.value || '';
    const lastName  = document.getElementById('lastName')?.value  || '';
    const email     = document.getElementById('email')?.value     || '';
    const phone     = document.getElementById('phone')?.value     || '';
    const address   = document.getElementById('address')?.value   || '';
    const password  = document.getElementById('password')?.value  || '';
    const confirm   = document.getElementById('confirmPassword')?.value || '';
    const dob       = document.getElementById('dob')?.value       || '';

    if (!Validators.isValidName(firstName)) {
        showFieldError('firstName', 'Enter a valid first name (letters only, min 2 chars)');
        isValid = false;
    } else { markFieldValid('firstName'); }

    if (!Validators.isValidName(lastName)) {
        showFieldError('lastName', 'Enter a valid last name');
        isValid = false;
    } else { markFieldValid('lastName'); }

    if (!Validators.isValidEmail(email)) {
        showFieldError('email', 'Enter a valid email address');
        isValid = false;
    } else { markFieldValid('email'); }

    if (!Validators.isValidPhone(phone)) {
        showFieldError('phone', 'Enter a valid 10-digit phone number');
        isValid = false;
    } else { markFieldValid('phone'); }

    if (address.trim().length < 5) {
        showFieldError('address', 'Address must be at least 5 characters');
        isValid = false;
    } else { markFieldValid('address'); }

    if (!Validators.isValidPassword(password)) {
        showFieldError('password', 'Password must be at least 6 characters and contain a number');
        isValid = false;
    } else { markFieldValid('password'); }

    if (password !== confirm) {
        showFieldError('confirmPassword', 'Passwords do not match!');
        isValid = false;
    } else if (confirm.length > 0) { markFieldValid('confirmPassword'); }

    if (!Validators.isValidDOB(dob)) {
        showFieldError('dob', 'You must be at least 18 years old');
        isValid = false;
    } else { markFieldValid('dob'); }

    return isValid;
}

// Register form submission handler
const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', function (e) {
        e.preventDefault();
        if (validateRegisterForm()) {
            const btn = document.getElementById('registerBtn');
            btn.innerHTML = '<span class="spinner"></span> Registering...';
            btn.disabled = true;

            // Simulate API call
            setTimeout(() => {
                const customerId = 'CUST' + Math.floor(1000 + Math.random() * 9000);
                // Save to localStorage (demo)
                const userData = {
                    customerId,
                    firstName: document.getElementById('firstName').value,
                    lastName:  document.getElementById('lastName').value,
                    email:     document.getElementById('email').value,
                    phone:     document.getElementById('phone').value,
                };
                localStorage.setItem('currentUser', JSON.stringify(userData));
                localStorage.setItem('customerId_' + customerId, JSON.stringify(userData));

                showAlert('formAlert', `Registration successful! Your Customer ID: <strong>${customerId}</strong>. Please save it for login.`, 'success');
                registerForm.reset();
                btn.innerHTML = 'Register';
                btn.disabled = false;
            }, 1500);
        }
    });

    // Live validation on input
    ['firstName','lastName','email','phone','password','confirmPassword'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('input', () => clearFieldError(id));
    });
}

// ============================================================
//  LOGIN FORM VALIDATION
// ============================================================

function validateLoginForm() {
    let isValid = true;
    clearFieldError('loginId');
    clearFieldError('loginPassword');

    const loginId  = document.getElementById('loginId')?.value || '';
    const password = document.getElementById('loginPassword')?.value || '';

    if (loginId.trim().length < 4) {
        showFieldError('loginId', 'Enter your Customer ID');
        isValid = false;
    } else { markFieldValid('loginId'); }

    if (password.length < 1) {
        showFieldError('loginPassword', 'Enter your password');
        isValid = false;
    } else { markFieldValid('loginPassword'); }

    return isValid;
}

const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();
        if (validateLoginForm()) {
            const btn = document.getElementById('loginBtn');
            btn.innerHTML = '<span class="spinner"></span> Logging in...';
            btn.disabled = true;

            setTimeout(() => {
                const loginId = document.getElementById('loginId').value.trim();
                const stored  = localStorage.getItem('customerId_' + loginId);

                if (stored) {
                    localStorage.setItem('currentUser', stored);
                    showAlert('loginAlert', 'Login successful! Redirecting...', 'success');
                    setTimeout(() => { window.location.href = 'dashboard.html'; }, 1000);
                } else {
                    showAlert('loginAlert', 'Invalid Customer ID or Password. Try demo ID.', 'danger');
                    btn.innerHTML = 'Login';
                    btn.disabled = false;
                }
            }, 1200);
        }
    });
}

// ============================================================
//  DEPOSIT FORM VALIDATION
// ============================================================

function validateDepositForm() {
    let isValid = true;
    clearFieldError('depositAccountId');
    clearFieldError('depositAmount');

    const accountId = document.getElementById('depositAccountId')?.value || '';
    const amount    = document.getElementById('depositAmount')?.value    || '';

    if (accountId.trim().length < 4) {
        showFieldError('depositAccountId', 'Enter a valid Account ID');
        isValid = false;
    } else { markFieldValid('depositAccountId'); }

    if (!Validators.isValidAmount(amount)) {
        showFieldError('depositAmount', 'Enter a valid positive amount');
        isValid = false;
    } else { markFieldValid('depositAmount'); }

    return isValid;
}

const depositForm = document.getElementById('depositForm');
if (depositForm) {
    depositForm.addEventListener('submit', function (e) {
        e.preventDefault();
        if (validateDepositForm()) {
            const amount    = parseFloat(document.getElementById('depositAmount').value);
            const accountId = document.getElementById('depositAccountId').value.trim();
            const btn       = document.getElementById('depositBtn');
            btn.innerHTML = '<span class="spinner"></span> Processing...';
            btn.disabled = true;

            setTimeout(() => {
                // Update demo balance in localStorage
                const balKey = 'balance_' + accountId;
                const current = parseFloat(localStorage.getItem(balKey) || '1000');
                const newBal  = current + amount;
                localStorage.setItem(balKey, newBal.toFixed(2));

                // Save transaction to history
                saveTransactionToHistory(accountId, 'DEPOSIT', amount, newBal);

                showAlert('depositAlert',
                    `✓ Rs.${amount.toFixed(2)} deposited to ${accountId}. New Balance: Rs.${newBal.toFixed(2)}`,
                    'success');
                updateBalanceDisplay(newBal);
                depositForm.reset();
                btn.innerHTML = 'Deposit';
                btn.disabled = false;
            }, 1000);
        }
    });

    // Live amount calculation
    const amountInput = document.getElementById('depositAmount');
    if (amountInput) {
        amountInput.addEventListener('input', function () {
            const preview = document.getElementById('amountPreview');
            if (preview) {
                const val = parseFloat(this.value);
                preview.textContent = isNaN(val) ? '' : `Amount: Rs. ${val.toFixed(2)}`;
            }
        });
    }
}

// ============================================================
//  WITHDRAW FORM VALIDATION
// ============================================================

function validateWithdrawForm() {
    let isValid = true;
    clearFieldError('withdrawAccountId');
    clearFieldError('withdrawAmount');

    const accountId = document.getElementById('withdrawAccountId')?.value || '';
    const amount    = document.getElementById('withdrawAmount')?.value    || '';

    if (accountId.trim().length < 4) {
        showFieldError('withdrawAccountId', 'Enter a valid Account ID');
        isValid = false;
    } else { markFieldValid('withdrawAccountId'); }

    if (!Validators.isValidAmount(amount)) {
        showFieldError('withdrawAmount', 'Enter a valid positive amount');
        isValid = false;
    } else { markFieldValid('withdrawAmount'); }

    return isValid;
}

const withdrawForm = document.getElementById('withdrawForm');
if (withdrawForm) {
    withdrawForm.addEventListener('submit', function (e) {
        e.preventDefault();
        if (validateWithdrawForm()) {
            const amount    = parseFloat(document.getElementById('withdrawAmount').value);
            const accountId = document.getElementById('withdrawAccountId').value.trim();
            const btn       = document.getElementById('withdrawBtn');
            btn.innerHTML = '<span class="spinner"></span> Processing...';
            btn.disabled = true;

            setTimeout(() => {
                const balKey = 'balance_' + accountId;
                const current = parseFloat(localStorage.getItem(balKey) || '1000');

                if (amount > current) {
                    showAlert('withdrawAlert', '✗ Insufficient balance!', 'danger');
                } else {
                    const newBal = current - amount;
                    localStorage.setItem(balKey, newBal.toFixed(2));
                    saveTransactionToHistory(accountId, 'WITHDRAWAL', amount, newBal);
                    showAlert('withdrawAlert',
                        `✓ Rs.${amount.toFixed(2)} withdrawn from ${accountId}. New Balance: Rs.${newBal.toFixed(2)}`,
                        'success');
                    updateBalanceDisplay(newBal);
                    withdrawForm.reset();
                }
                btn.innerHTML = 'Withdraw';
                btn.disabled = false;
            }, 1000);
        }
    });
}

// ============================================================
//  TRANSACTION HISTORY
// ============================================================

function saveTransactionToHistory(accountId, type, amount, balanceAfter) {
    const key  = 'txns_' + accountId;
    const txns = JSON.parse(localStorage.getItem(key) || '[]');
    txns.unshift({
        id:        'TXN' + Date.now(),
        type,
        amount:    amount.toFixed(2),
        balance:   balanceAfter.toFixed(2),
        date:      new Date().toLocaleString()
    });
    localStorage.setItem(key, JSON.stringify(txns.slice(0, 50))); // keep last 50
}

function loadTransactionHistory(accountId) {
    const key  = 'txns_' + accountId;
    return JSON.parse(localStorage.getItem(key) || '[]');
}

// Render history table on history.html
const historyAccountForm = document.getElementById('historyForm');
if (historyAccountForm) {
    historyAccountForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const accountId = document.getElementById('historyAccountId').value.trim();
        const txns = loadTransactionHistory(accountId);
        const tbody = document.getElementById('historyTableBody');
        const table = document.getElementById('historyTable');

        if (!tbody) return;
        tbody.innerHTML = '';

        if (txns.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center" style="padding:2rem;color:var(--text-muted)">No transactions found for this account.</td></tr>';
        } else {
            txns.forEach(t => {
                const amtClass = t.type === 'DEPOSIT' ? 'amount-deposit' : 'amount-withdrawal';
                const badge    = t.type === 'DEPOSIT'
                    ? '<span class="badge badge-success">DEPOSIT</span>'
                    : t.type === 'WITHDRAWAL'
                    ? '<span class="badge badge-danger">WITHDRAWAL</span>'
                    : '<span class="badge badge-info">TRANSFER</span>';

                tbody.innerHTML += `
                    <tr class="fade-in">
                        <td>${t.id}</td>
                        <td>${badge}</td>
                        <td class="${amtClass}">Rs. ${t.amount}</td>
                        <td>Rs. ${t.balance}</td>
                        <td>${t.date}</td>
                    </tr>`;
            });
        }
        if (table) table.classList.remove('hidden');
    });
}

// ============================================================
//  DASHBOARD LOGIC
// ============================================================

function updateBalanceDisplay(balance) {
    const balEl = document.getElementById('currentBalance');
    if (balEl) {
        balEl.textContent = 'Rs. ' + parseFloat(balance).toFixed(2);
        // Add pulse animation
        balEl.style.transform = 'scale(1.1)';
        setTimeout(() => { balEl.style.transform = 'scale(1)'; }, 300);
    }
}

// Load user info into dashboard
window.addEventListener('DOMContentLoaded', () => {
    const user = JSON.parse(localStorage.getItem('currentUser') || 'null');

    // Populate user name in nav/sidebar if elements exist
    const userNameEls = document.querySelectorAll('.user-display-name');
    if (user) {
        userNameEls.forEach(el => {
            el.textContent = user.firstName + ' ' + user.lastName;
        });
        const avatarEl = document.querySelector('.user-avatar');
        if (avatarEl) {
            avatarEl.textContent = (user.firstName[0] || 'U').toUpperCase();
        }
    }

    // Populate customer ID field if on deposit/withdraw page
    const cidFields = document.querySelectorAll('.auto-fill-cid');
    if (user && cidFields.length) {
        cidFields.forEach(f => f.value = user.customerId || '');
    }
});

// ============================================================
//  NAVBAR HAMBURGER TOGGLE
// ============================================================
const hamburger = document.querySelector('.hamburger');
const navMenu   = document.querySelector('.navbar-nav');

if (hamburger && navMenu) {
    hamburger.addEventListener('click', () => {
        navMenu.classList.toggle('open');
    });
}

// Mark active nav link
const currentPage = window.location.pathname.split('/').pop();
document.querySelectorAll('.navbar-nav a').forEach(link => {
    if (link.getAttribute('href') === currentPage) {
        link.classList.add('active');
    }
});

// ============================================================
//  PASSWORD STRENGTH INDICATOR
// ============================================================
const passwordInput = document.getElementById('password');
const strengthBar   = document.getElementById('strengthBar');
const strengthText  = document.getElementById('strengthText');

if (passwordInput && strengthBar) {
    passwordInput.addEventListener('input', function () {
        const val = this.value;
        let strength = 0;
        if (val.length >= 6)          strength++;
        if (/[A-Z]/.test(val))        strength++;
        if (/\d/.test(val))           strength++;
        if (/[^A-Za-z0-9]/.test(val)) strength++;

        const levels = ['', 'Weak', 'Fair', 'Good', 'Strong'];
        const colors = ['', '#dc3545', '#ffc107', '#17a2b8', '#28a745'];
        const widths = ['0%', '25%', '50%', '75%', '100%'];

        strengthBar.style.width     = widths[strength] || '0%';
        strengthBar.style.background = colors[strength] || '#dee2e6';
        if (strengthText) strengthText.textContent = strength > 0 ? levels[strength] : '';
    });
}

// ============================================================
//  CONFIRM PASSWORD LIVE CHECK
// ============================================================
const confirmPwd = document.getElementById('confirmPassword');
if (confirmPwd && passwordInput) {
    confirmPwd.addEventListener('input', function () {
        if (this.value === passwordInput.value) {
            markFieldValid('confirmPassword');
        } else {
            showFieldError('confirmPassword', 'Passwords do not match');
        }
    });
}

// ============================================================
//  NUMBER FORMAT HELPER (Currency display)
// ============================================================
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR'
    }).format(amount);
}

// ============================================================
//  LOGOUT
// ============================================================
const logoutBtn = document.getElementById('logoutBtn');
if (logoutBtn) {
    logoutBtn.addEventListener('click', function (e) {
        e.preventDefault();
        localStorage.removeItem('currentUser');
        window.location.href = 'login.html';
    });
}
