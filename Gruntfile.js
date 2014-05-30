module.exports = function(grunt) {
  'use strict';

  // Project configuration.
  grunt.initConfig({
    pkg : grunt.file.readJSON('package.json'),
    watch : {
      js : {
        files : [ 'app/js/*.js', 'Gruntfile.js', 'app/css/*.less' ],
        tasks : [ 'bowercopy', 'jshint', 'uglify', 'less' ]
      }
    },
    "bower-install-simple" : {
      options : {
        color : true,
        production : false,
      }
    },
    bowercopy : {
      options : {
        srcPrefix : 'bower_components'
      },
      scripts : {
        options : {
          destPrefix : 'src/main/webapp'
        },
        files : {
          'js/vendor/angular.min.js' : 'bower_components/angular/angular.min.js',
          'js/vendor/angular.min.js.map' : 'bower_components/angular/angular.min.js.map',
          'js/vendor/angular-file-upload.min.js' : 'bower_components/angular-file-upload/angular-file-upload.min.js',
          'js/vendor/angular-file-upload.min.map' : 'bower_components/angular-file-upload/angular-file-upload.min.map',
          'js/vendor/ui-bootstrap-tpls.min.js' : 'bower_components/angular-ui-bootstrap-bower/ui-bootstrap-tpls.min.js',
          'js/vendor/xeditable.min.js' : 'bower_components/angular-xeditable/dist/js/xeditable.min.js',
          'css/vendor/xeditable.css' : 'bower_components/angular-xeditable/dist/css/xeditable.css',
          'js/vendor/bootstrap.min.js' : 'bower_components/bootstrap/dist/js/bootstrap.min.js',
          'css/vendor/bootstrap.min.css' : 'bower_components/bootstrap/dist/css/bootstrap.min.css',
          'css/fonts/glyphicons-halflings-regular.eot' : 'bower_components/bootstrap/dist/fonts/glyphicons-halflings-regular.eot',
          'css/fonts/glyphicons-halflings-regular.svg' : 'bower_components/bootstrap/dist/fonts/glyphicons-halflings-regular.svg',
          'css/fonts/glyphicons-halflings-regular.ttf' : 'bower_components/bootstrap/dist/fonts/glyphicons-halflings-regular.ttf',
          'css/fonts/glyphicons-halflings-regular.woff' : 'bower_components/bootstrap/dist/fonts/glyphicons-halflings-regular.woff',
          'js/vendor/es5-shim.min.js' : 'bower_components/es5-shim/es5-shim.min.js',
          'js/vendor/es5-shim.map' : 'bower_components/es5-shim/es5-shim.map',
          'js/vendor/jquery.min.js' : 'bower_components/jquery/dist/jquery.min.js',
          'js/vendor/jquery.min.map' : 'bower_components/jquery/dist/jquery.min.map'
        }
      }
    },
    jshint : {
      options : {
        jshintrc : '.jshintrc',
      },
      all : [ 'Gruntfile.js', 'app/js/*.js' ]
    },
    uglify : { 
      options : {
        // the banner is inserted at the top of the output
        banner : '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
      },
      dist : {
        files : {
          'src/main/webapp/js/dist/<%= pkg.name %>.min.js' : [ 'app/js/*.js' ]
        }
      }
    },
    less : {
      options : {
        banner : '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
      },
      development : {
        files : {
          "src/main/webapp/css/dist/DISP-MELM.css" : "app/css/style.less"
        }
      },
      production : {
        options : {
          banner : '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n',
          cleancss : true
        },
        files : {
          "src/main/webapp/css/dist/DISP-MELM.min.css" : "app/css/style.less"
        }
      }
    }
  });

  // Load the Grunt plugins.
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-bowercopy');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');

  // Register the default tasks.
  grunt.registerTask('default', [ 'watch' ]);
};