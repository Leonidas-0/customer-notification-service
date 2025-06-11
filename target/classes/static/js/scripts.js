$(document).ready(function () {
    const apiBaseUrl = 'http://localhost:8080/api';
    const jwtToken = localStorage.getItem('jwt');

    // --- UTILS & HELPERS ---
    const showSpinner = () => $('#loading-spinner').show();
    const hideSpinner = () => $('#loading-spinner').hide();

    function apiRequest(settings) {
        showSpinner();
        $.ajax({
            ...settings,
            headers: { 'Authorization': `Bearer ${jwtToken}`, 'Content-Type': 'application/json', ...settings.headers },
            error: (xhr) => {
                console.error("API Error:", xhr.responseText);
                const errorMsg = xhr.responseJSON ? (xhr.responseJSON.error || JSON.stringify(xhr.responseJSON.details)) : 'An unknown error occurred.';
                alert(`Error: ${xhr.status} - ${errorMsg}`);
            },
            complete: () => hideSpinner()
        });
    }

    // --- AUTH & NAVIGATION ---
    if (!jwtToken && !window.location.pathname.endsWith('/login')) {
        window.location.href = '/ui/login';
    } else if (window.location.pathname !== '/ui/login') {
        const path = window.location.pathname.split('/').pop();
        $(`.list-group-item[data-page="${path}"]`).addClass('active');
    }

    $('#login-form').on('submit', function (e) {
        e.preventDefault();
        apiRequest({
            url: `${apiBaseUrl}/auth/login`, type: 'POST',
            data: JSON.stringify({ username: $('#username').val(), password: $('#password').val() }),
            success: data => { localStorage.setItem('jwt', data.token); window.location.href = '/ui/dashboard'; }
        });
    });

    $('#logout-link').on('click', (e) => { e.preventDefault(); localStorage.removeItem('jwt'); window.location.href = '/ui/login'; });

// --- CUSTOMER DASHBOARD PAGE LOGIC ---
if (window.location.pathname.endsWith('/dashboard')) {
    let currentPage = 0;
    let currentSort = 'id,asc';
    let currentSearchMode = 'simple';
    let currentSearchData = '';

    const fetchCustomers = (page = 0) => {
        currentPage = page;
        let settings = { success: handleResponse };
        if (currentSearchMode === 'advanced') {
            settings.url = `${apiBaseUrl}/customers/search?page=${page}&sort=${currentSort}`;
            settings.type = 'POST';
            settings.data = JSON.stringify(currentSearchData);
        } else {
            settings.url = `${apiBaseUrl}/customers?q=${currentSearchData}&page=${page}&sort=${currentSort}`;
            settings.type = 'GET';
        }
        apiRequest(settings);
    };
    const handleResponse = data => {
        const $tbody = $('#customer-table-body').empty();
        if (!data.content.length) {
            $tbody.append('<tr><td colspan="5" class="text-center text-muted">No customers found.</td></tr>');
        } else {
            data.content.forEach(c => $tbody.append(`<tr><td>${c.id}</td><td>${c.firstName}</td><td>${c.lastName}</td><td>${c.email}</td><td class="text-end"><button class="btn btn-sm btn-info btn-edit-customer" data-bs-toggle="modal" data-bs-target="#customerModal" data-id="${c.id}"><i class="bi bi-pencil-fill"></i></button> <button class="btn btn-sm btn-danger btn-delete-customer" data-id="${c.id}"><i class="bi bi-trash-fill"></i></button> <a href="/ui/customers/${c.id}" class="btn btn-sm btn-secondary"><i class="bi bi-gear-fill"></i> Manage</a></td></tr>`));
        }
        renderPagination(data);
        updateSortIndicators();
    };
    const renderPagination = page => {
        const $nav = $('#pagination-controls').empty();
        if (page.totalPages <= 1) return;
        let links = Array.from({ length: page.totalPages }, (_, i) => `<li class="page-item ${page.number === i ? 'active' : ''}"><a class="page-link" href="#" data-page="${i}">${i + 1}</a></li>`).join('');
        $nav.html(`<ul class="pagination"><li class="page-item ${page.first ? 'disabled' : ''}"><a class="page-link" href="#" data-page="${page.number - 1}">Prev</a></li>${links}<li class="page-item ${page.last ? 'disabled' : ''}"><a class="page-link" href="#" data-page="${page.number + 1}">Next</a></li></ul>`);
    };
    const updateSortIndicators = () => {
        $('.sortable i').removeClass('bi-sort-alpha-down bi-sort-alpha-up').addClass('bi-arrow-down-up');
        const [field, dir] = currentSort.split(',');
        const iconClass = dir === 'asc' ? 'bi-sort-alpha-down' : 'bi-sort-alpha-up';
        $(`.sortable[data-sort="${field}"] i`).removeClass('bi-arrow-down-up').addClass(iconClass);
    };

    // Use .off().on() to prevent duplicate handlers
    $('#pagination-controls').off('click').on('click', 'a', function(e) { e.preventDefault(); if (!$(this).parent().hasClass('disabled')) fetchCustomers($(this).data('page')); });
    $('#search-form').off('submit').on('submit', function(e) { e.preventDefault(); currentSearchMode = 'simple'; currentSearchData = $(this).find('input[name="query"]').val(); fetchCustomers(0); });
    $('#advanced-search-form').off('submit').on('submit', function(e) { e.preventDefault(); currentSearchMode = 'advanced'; const formData = new FormData(this); currentSearchData = Object.fromEntries(formData.entries()); for (const key in currentSearchData) if (currentSearchData[key] === '') delete currentSearchData[key]; $('#advancedSearchModal').modal('hide'); fetchCustomers(0); });
    $('#reset-search-btn').off('click').on('click', function() { $('#advanced-search-form')[0].reset(); $('#search-form')[0].reset(); currentSearchMode = 'simple'; currentSearchData = ''; fetchCustomers(0); });
    $('.sortable').off('click').on('click', function() { const sortField = $(this).data('sort'); const [field, dir] = currentSort.split(','); currentSort = `${sortField},${(sortField === field && dir === 'asc') ? 'desc' : 'asc'}`; fetchCustomers(0); });
    
    // Use event delegation for delete buttons
    $(document).off('click', '.btn-delete-customer').on('click', '.btn-delete-customer', function() {
        if (confirm('Are you sure? This will delete the customer and all related data.')) {
            apiRequest({ url: `${apiBaseUrl}/customers/${$(this).data('id')}`, type: 'DELETE', success: () => fetchCustomers(currentPage) });
        }
    });

    $('#customerModal').off('show.bs.modal').on('show.bs.modal', function(e) {
        const id = $(e.relatedTarget).data('id');
        const $form = $(this).find('form');
        if($form.length) $form[0].reset();
        $form.find('#customerId').val('');
        $(this).find('.modal-title').text(id ? 'Edit Customer' : 'Add Customer');
        if (id) {
            apiRequest({ url: `${apiBaseUrl}/customers/${id}`, success: c => {
                $form.find('#customerId').val(c.id);
                $form.find('#firstName').val(c.firstName);
                $form.find('#lastName').val(c.lastName);
                $form.find('#email').val(c.email);
            }});
        }
    });
    
    $('#customer-form').off('submit').on('submit', function(e) {
        e.preventDefault();
        const id = $('#customerId').val();
        const data = { firstName: $('#firstName').val(), lastName: $('#lastName').val(), email: $('#email').val() };
        apiRequest({
            url: id ? `${apiBaseUrl}/customers/${id}` : `${apiBaseUrl}/customers`,
            type: id ? 'PUT' : 'POST',
            data: JSON.stringify(data),
            success: () => { $('#customerModal').modal('hide'); fetchCustomers(id ? currentPage : 0); }
        });
    });

    fetchCustomers(); // Initial load
}    
// --- CUSTOMER DETAIL PAGE LOGIC ---
if (window.location.pathname.startsWith('/ui/customers/')) {
    const customerId = window.location.pathname.split('/').pop();
    if (!/^\d+$/.test(customerId)) return;

    const renderTable = (type, headers, data) => {
        const $tbody = $(`#${type}-table-body`).empty();
        if (!data.length) $tbody.append(`<tr><td colspan="${headers.length + 2}" class="text-center text-muted">No ${type} found.</td></tr>`);
        data.forEach(item => {
            const fields = headers.map(h => `<td>${item[h] ?? 'N/A'}</td>`).join('');
            $tbody.append(`<tr>${fields}<td class="text-end"><button class="btn btn-sm btn-danger btn-delete" data-type="${type}" data-id="${item.id}"><i class="bi bi-trash-fill"></i></button></td></tr>`);
        });
    };

    const fetchDetails = () => {
        apiRequest({ url: `${apiBaseUrl}/customers/${customerId}`, success: c => $('#customer-name-header').text(`${c.firstName} ${c.lastName} (ID: ${c.id})`) });
        apiRequest({ url: `${apiBaseUrl}/customers/${customerId}/addresses`, success: d => renderTable('addresses', ['id', 'type', 'country', 'city', 'street', 'zip'], d) });
        apiRequest({ url: `${apiBaseUrl}/customers/${customerId}/preferences`, success: d => renderTable('preferences', ['id', 'channel', 'optedIn'], d) });
        apiRequest({ url: `${apiBaseUrl}/customers/${customerId}/notifications`, success: d => renderTable('notifications', ['id', 'status', 'updatedAt'], d) });
    };

    $(document).off('click', '.btn-delete').on('click', '.btn-delete', function () {
        const type = $(this).data('type');
        if (confirm(`Are you sure you want to delete this ${type.slice(0, -1)}?`)) {
            apiRequest({ url: `${apiBaseUrl}/customers/${customerId}/${type}/${$(this).data('id')}`, type: 'DELETE', success: fetchDetails });
        }
    });

    // Reset forms when modals are shown
    $('#addressModal, #preferenceModal, #notificationModal').on('show.bs.modal', function() {
        const $form = $(this).find('form');
        if ($form.length) $form[0].reset();
    });

    // Handle form submissions
    $('#address-form').off('submit').on('submit', function (e) {
        e.preventDefault();
        const data = { type: $('#addressType').val(), country: $('#addressCountry').val(), city: $('#addressCity').val(), street: $('#addressStreet').val(), zip: $('#addressZip').val() };
        apiRequest({ url: `${apiBaseUrl}/customers/${customerId}/addresses`, type: 'POST', data: JSON.stringify(data), success: () => { $('#addressModal').modal('hide'); fetchDetails(); } });
    });

    $('#preference-form').off('submit').on('submit', function (e) {
        e.preventDefault();
        const data = { channel: $('#preferenceChannel').val(), optedIn: $('#preferenceOptedIn').is(':checked') };
        apiRequest({ url: `${apiBaseUrl}/customers/${customerId}/preferences`, type: 'POST', data: JSON.stringify(data), success: () => { $('#preferenceModal').modal('hide'); fetchDetails(); } });
    });

    $('#notification-form').off('submit').on('submit', function (e) {
        e.preventDefault();
        const data = { status: $('#notificationStatus').val() };
        apiRequest({ url: `${apiBaseUrl}/customers/${customerId}/notifications`, type: 'POST', data: JSON.stringify(data), success: () => { $('#notificationModal').modal('hide'); fetchDetails(); } });
    });

    fetchDetails(); // Initial load
}
    
    // --- REPORTS PAGE LOGIC ---
    if(window.location.pathname.endsWith('/reports')) {
        const renderStats = (containerId, data, labelField) => {
            const $container = $(`#${containerId}`).empty();
            const total = data.reduce((sum, item) => sum + item.count, 0);
            if(total === 0) {$container.append('<p class="text-muted text-center">No data available.</p>'); return;}
            data.forEach(item => {
                const percentage = total > 0 ? ((item.count / total) * 100).toFixed(1) : 0;
                const label = (item[labelField] || 'N/A').toString().toLowerCase().replace('_', ' ');
                $container.append(`<div class="stat-bar"><div class="stat-label">${label}</div><div class="stat-progress"><div class="stat-progress-bar" style="width: ${percentage}%;">${item.count} (${percentage}%)</div></div></div>`);
            });
        };
        apiRequest({ url: `${apiBaseUrl}/reports/preferences`, success: data => renderStats('pref-stats-body', data, 'channel')});
        apiRequest({ url: `${apiBaseUrl}/reports/notifications`, success: data => renderStats('notif-stats-body', data, 'status')});
    }

    // --- ADMINS PAGE LOGIC ---
    if(window.location.pathname.endsWith('/admins')) {
        const fetchAdmins = () => apiRequest({ url: `${apiBaseUrl}/admins`, success: data => {
            const $tbody = $('#admin-table-body').empty();
            data.forEach(admin => $tbody.append(`<tr><td>${admin.id}</td><td>${admin.username}</td><td>${admin.role}</td><td class="text-end"><button class="btn btn-sm btn-info" data-bs-toggle="modal" data-bs-target="#adminModal" data-id="${admin.id}"><i class="bi bi-pencil-fill"></i></button> <button class="btn btn-sm btn-danger btn-delete-admin" data-id="${admin.id}"><i class="bi bi-trash-fill"></i></button></td></tr>`));
        }});

$('#adminModal').on('show.bs.modal', function(e) {
    const id = $(e.relatedTarget).data('id');
    const $form = $(this).find('form');
    if($form.length) $form[0].reset();
    
    $(this).find('.modal-title').text(id ? 'Edit Admin' : 'Add Admin');
    
    if(id) {
        $form.find('#adminId').val(id);

        apiRequest({
            url: `${apiBaseUrl}/admins/${id}`, 
            success: function(admin) {
                $form.find('#username').val(admin.username);
            }
        });
    } else {
        $form.find('#adminId').val('');
    }
});


        $('#admin-form').on('submit', function(e) {
            e.preventDefault();
            const id = $('#adminId').val();
            let data = { username: $('#username').val(), password: $('#password').val(), role: 'ADMIN' };
            if (id && !data.password) delete data.password;
            apiRequest({
                url: id ? `${apiBaseUrl}/admins/${id}` : `${apiBaseUrl}/admins`,
                type: id ? 'PUT' : 'POST', data: JSON.stringify(data),
                success: () => { $('#adminModal').modal('hide'); fetchAdmins(); }
            });
        });

        $(document).on('click', '.btn-delete-admin', function() {
             if (confirm('Are you sure you want to delete this admin?')) {
                apiRequest({ url: `${apiBaseUrl}/admins/${$(this).data('id')}`, type: 'DELETE', success: fetchAdmins });
            }
        });

        fetchAdmins();
    }
});