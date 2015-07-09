var appControllers = angular.module('appControllers', []);

appControllers.controller('ListController', ['$scope', '$http', function($scope, $http){
    $http.get('javascripts/data.json').success(function(data) {
        $scope.players = data;
        $scope.players.forEach(function(player){
            player.numberOfVotes = 0;
        });
    });

    $scope.upvote = function(i){
        $scope.players[i].numberOfVotes++;
    };

    $scope.downvote = function(i){
        if ($scope.players[i].numberOfVotes > 0){
            $scope.players[i].numberOfVotes--;
        }
    };
}]);

appControllers.controller('DetailsController', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
    $http.get('javascripts/data.json').success(function(data) {
        $scope.players = data;
        $scope.player = $scope.players[Number($routeParams.itemId)];
    });
}]);