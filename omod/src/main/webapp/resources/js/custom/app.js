var muzimafingerPrintModule = angular.module('muzimafingerPrint', ['ui.bootstrap']);

muzimafingerPrintModule.
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/list/patients', {templateUrl: '../../moduleResources/muzimafingerPrint/partials/list/patients.html'}).

            otherwise({redirectTo: '/list/patients'});
    }]);

