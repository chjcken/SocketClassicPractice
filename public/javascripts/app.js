var myApp = angular.module('myApp', [
    'ngRoute',
    'myApp.Controllers',
    'myApp.Services',
    'myApp.Filters',
    'myApp.Directives'
]);

myApp.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider){
    $routeProvider
        .when('/', {
            templateUrl: 'templates/list.html',
            controller: 'ListController'
        })
        .when('/details/:itemId', {
            templateUrl: 'templates/details.html',
            controller: 'DetailsController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);