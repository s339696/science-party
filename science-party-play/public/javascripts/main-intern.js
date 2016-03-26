// Initial and auto refresh of navigation bar
window.onload = (function () {
    updateNavigator();
});

window.setInterval(function () {
    updateNavigator();
}, 3000);


function updateNavigator() {
    $.ajax({
        url: '/notifications/private/count',
        method: 'GET',
        dataType: "text",
        success: function (data, status, xhr) {
            count = xhr.responseText;
            if (count === "0") {
                $("#notifications").text("Benachrichtigungen");
            } else {
                $("#notifications").text("Benachrichtigungen (" + xhr.responseText + ")");
            }
        },
        error: function (xhr, status, error) {
        }
    });

    $.ajax({
        url: '/messages/new/count',
        method: 'GET',
        dataType: "text",
        success: function (data, status, xhr) {
            count = xhr.responseText;
            if (count === "0") {
                $("#messages").text("Messages");
            } else {
                $("#messages").text("Messages (" + xhr.responseText + ")");
            }
        },
        error: function (xhr, status, error) {
        }
    });
}

