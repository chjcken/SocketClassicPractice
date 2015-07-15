var directivesModule = angular.module('myApp.Directives', []);

directivesModule.directive("rankingTable", function(){
    return {
        restrict: 'E',
        templateUrl: '/templates/ranking-table.html'
    }
});

directivesModule.directive("voteButtons", function(){
    return {
        restrict: 'E',
        templateUrl: '/templates/vote-buttons.html',
        link: function(scope, element, attrs){
            //TODO move the events handler for vote buttons to this directive
        }
    }
});