var phonecatControllers = angular.module('phonecatControllers', []);

phonecatControllers.controller('PhoneListCtrl', ['$scope', 'Phone', '$http', function ($scope, Phone, $http) {
        $scope.phones = Phone.query();
        $scope.orderProp = 'age';
        $scope.submit = function (username, password) {
            $http.get("http://localhost:8080/RestAPI/webresources/login/dologin", {
                params: {username: username, password: password}
            }).success(function (response) {
               
                if (response.status === true)
                {
                   
                     window.location.href = 'http://localhost:8080/DemoAngular-RestFul/admin.html';
                     alert("Login successfully");
                }
                 if (response.status === false)
                    alert(response.error_msg);
            });

        };

    }]);

phonecatControllers.controller('PhoneDetailCtrl', ['$scope', '$routeParams', 'Phone', function ($scope, $routeParams, Phone) {
        $scope.phone = Phone.get({phoneId: $routeParams.phoneId}, function (phone) {
            $scope.mainImageUrl = phone.images[0];
        });

        $scope.setImage = function (imageUrl) {
            $scope.mainImageUrl = imageUrl;
        }
    }]);
