module.exports = function(grunt) {
  'use strict';

  // Project configuration.
  grunt.initConfig({
    pkg : grunt.file.readJSON('package.json'),
    watch : {
      js : {
        files : [ 'src/main/webapp/js/custom/*.js', 'Gruntfile.js', 'src/main/webapp/css/custom/*.less' ],
        tasks : [ 'copy', 'jshint', 'uglify', 'less' ]
      }
    },
    copy : {
      main : {
        files : [ {
          src : 'bower_components/angular/angular.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular/angular.min.js.map',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-animate/angular-animate.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-animate/angular-animate.min.js.map',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-bootstrap/ui-bootstrap.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-dialog-service/dialogs.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-dialog-service/dialogs.min.css',
          dest : 'src/main/webapp/css/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-file-upload/angular-file-upload.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-file-upload/angular-file-upload.min.map',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-loading-bar/build/loading-bar.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-loading-bar/build/loading-bar.min.css',
          dest : 'src/main/webapp/css/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-sanitize/angular-sanitize.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-sanitize/angular-sanitize.min.js.map',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-translate/angular-translate.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-ui-sortable/sortable.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-xeditable/dist/js/xeditable.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/angular-xeditable/dist/css/xeditable.css',
          dest : 'src/main/webapp/css/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/bootstrap/dist/js/bootstrap.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/bootstrap/dist/css/bootstrap.min.css',
          dest : 'src/main/webapp/css/vendor/',
          flatten : true,
          expand : true
        }, {
          src : [ 'bower_components/bootstrap/dist/fonts/*' ],
          dest : 'src/main/webapp/css/fonts/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/es5-shim/es5-shim.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/es5-shim/es5-shim.map',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/jquery/dist/jquery.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/jquery/dist/jquery.min.map',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        }, {
          src : 'bower_components/jquery-ui/ui/minified/jquery-ui.min.js',
          dest : 'src/main/webapp/js/vendor/',
          flatten : true,
          expand : true
        } ]
      }
    },
    jshint : {
      options : {
        jshintrc : '.jshintrc',
      },
      all : [ 'Gruntfile.js', 'src/main/webapp/js/custom/*.js' ]
    },
    uglify : {
      options : {
        // the banner is inserted at the top of the output
        banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      },
      dist : {
        files : {
          'src/main/webapp/js/dist/style.min.js' : [ 'src/main/webapp/js/custom/*.js' ]
        }
      }
    },
    less : {
      options : {
        banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      },
      development : {
        files : {
          "src/main/webapp/css/dist/style.css" : "src/main/webapp/css/custom/style.less"
        }
      },
      production : {
        options : {
          banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n',
          cleancss : true
        },
        files : {
          "src/main/webapp/css/dist/style.min.css" : "src/main/webapp/css/custom/style.less"
        }
      }
    }
  });

  // Load the Grunt plugins.
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');

  // Register the default tasks.
  grunt.registerTask('default', [ 'watch' ]);
};