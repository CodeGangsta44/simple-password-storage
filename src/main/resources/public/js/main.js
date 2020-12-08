angular.module('main', ['ngRoute'])
    .config(function ($routeProvider, $httpProvider) {

        $routeProvider.when('/', {
            templateUrl: 'navigation.html',
            controller: 'navigation'
        }).when('/login', {
            templateUrl: 'login.html',
            controller: 'login'
        })
        .when('/registration', {
            templateUrl: 'registration.html',
            controller: 'registration'
        })
        .when('/home', {
            templateUrl: 'home.html',
            controller: 'home'
        })
        .otherwise('/');

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

    })
    .controller('navigation', function ($scope, $http) {
        console.log("IN NAVIGATION CONTROLLER");
    })
    .controller('login', function ($scope, $http, $location) {
        console.log("IN LOGIN CONTROLLER");
        $scope.error = {};

        $scope.credentials = {};

        $scope.login = function() {
           $http.post("/login", $scope.credentials)
              .then(
                (data) => {
                  console.log(data);
                  $location.path("/home");
                },
                (error) => {
                  console.log("ERROR");
                  $scope.error = error;
                }
              );
        };
    })
    .controller('registration', function ($scope, $http, $location) {
        console.log("IN REGISTRATION CONTROLLER");

        $scope.registrationData = {};

        $scope.register = function() {
            $http.post("/registration", $scope.registrationData)
                .then(
                    (data) => {
                        console.log(data);
                        $location.path("/login");
                    },
                    (error) => {
                        console.log("ERROR");
                    }
                );
        };
    })
    .controller('home', function ($scope, $http) {
        console.log("IN HOME CONTROLLER");
    });