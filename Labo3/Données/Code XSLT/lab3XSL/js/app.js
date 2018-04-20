$(document).foundation();

$(document).ready(function() {
    $('#projections').DataTable({
        paging: false,
        info: false,
        searching: false,
        "order": [[ 1, "asc" ]],
		columnDefs: [
       		{ type: 'date-euro', targets: 1 }
     	]
    });
});