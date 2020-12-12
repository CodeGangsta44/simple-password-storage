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
    .controller('navigation', function ($scope, $http, $location) {
        // $scope.user = {};

        $http.get("/api/home")
            .then(
                (data) => {
                    $location.path("/home");
                    // console.log(data);
                    // $scope.user = data.data;
                },
                (error) => {
                    // window.location.href = '/login';
                    console.log("ERROR");
                }
            );
        console.log("IN NAVIGATION CONTROLLER");
    })
    .controller('login', function ($scope, $http, $location) {
        console.log("IN LOGIN CONTROLLER");
        $scope.error = "";

        $scope.credentials = {};

        $scope.login = function() {
            $http({
              method: "POST",
              url: "/login",
              data: $.param($scope.credentials),
              headers: { "Content-Type" : "application/x-www-form-urlencoded" }
            })
              .then(
                (data) => {
                  console.log(data);
                  $location.path("/home");
                },
                (error) => {
                  console.log("ERROR");
                  $scope.error = "ERROR";
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
                        window.location.href = '/login';
                    },
                    (error) => {
                        console.log("ERROR");
                    }
                );
        };
    })
    .controller('home', function ($scope, $http) {
        $scope.user = {};

            $http.get("/api/home")
                    .then(
                        (data) => {
                            console.log(data);
                            $scope.user = data.data;
                        },
                        (error) => {
                            console.log("ERROR");
                        }
                    );
        console.log("IN HOME CONTROLLER");
    });