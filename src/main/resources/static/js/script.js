var stompClient = null;

window.onload = function () {
    connect();
}

function connect() {
    var socket = new SockJS(webSocket);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected (stomp): ' + frame);
        console.log('session id = ' + sessionId)
        stompClient.subscribe('/feedback/notifications/' + sessionId, function (notification) {
            var notificationObj = JSON.parse(notification.body);
            console.log("received websock message " + notificationObj.toString());
            showNotification(notificationObj.numericValue);
        });
        stompClient.send('/app/cart-status', {}, JSON.stringify({'message': 'получение количества товаров в корзине'}));
    });
}

function showNotification(message) {
    console.log("товаров в корзине: " + message);
    if (message > 0) {
        $("#cart").text("Корзина (" + message + ")");
    }
}

function addProductToCart(productId) {
    console.log("добавляем товар с id = " + productId);
    stompClient.send('/app/cart-refreshed', {}, JSON.stringify({
        'message': 'в корзину добавлен товар',
        'numericValue': productId
    }));
}
