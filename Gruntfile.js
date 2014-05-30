module.exports = function(grunt) {
  'use strict';

  // Project configuration.
  grunt.initConfig({
    pkg : grunt.file.readJSON('package.json'),
    watch : {
      js : {
        files : [ 'src/main/webapp/js/custom/*.js', 'Gruntfile.js' ],
        tasks : [ 'uglify', 'less' ]
      }
    },
    jshint : {
      options : {
        jshintrc : 'src/main/webapp/js/custom/.jshintrc'
      },
      all : [ 'Gruntfile.js', 'src/main/webapp/js/custom/*.js' ]
    },
    // concat : {
    // options : {
    // // define a string to put between each file in the concatenated output
    // separator : ';'
    // },
    // dist : {
    // // the files to concatenate
    // src : [ 'src/**/*.js' ],
    // // the location of the resulting JS file
    // dest : 'src/main/webapp/js/dist/<%= pkg.name %>.js'
    // }
    // },
    uglify : {
      options : {
        // the banner is inserted at the top of the output
        banner : '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
      },
      dist : {
        files : {
          'src/main/webapp/js/dist/<%= pkg.name %>.min.js' : [ 'src/main/webapp/js/custom/*.js' ]
        }
      }
    },
    less : {
      options : {
        banner : '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
      },
      development : {
        files : {
          "src/main/webapp/css/dist/DISP-MELM.css" : "src/main/webapp/css/custom/style.less"
        }
      },
      production : {
        options : {
          banner : '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n',
          cleancss : true
        },
        files : {
          "src/main/webapp/css/dist/DISP-MELM.min.css" : "src/main/webapp/css/custom/style.less"
        }
      }
    },
    bowerInstall : {
      target : {
        // Point to the files that should be updated when
        // you run `grunt bower-install`
        src : [ 'src/main/webapp/WEB-INF/jsp/js-includes.jsp' ],

        // Optional:
        // ---------
        cwd : '',
        dependencies : true,
        devDependencies : false,
        exclude : [],
        fileTypes : {},
        ignorePath : '',
        overrides : {} 
      }
    }
  // cssmin : {
  // add_banner : {
  // options : {
  // banner : '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
  // },
  // files : {
  // 'src/main/webapp/css/dist/<%= pkg.name %>.min.css' : [ 'src/main/webapp/css/dist/DISP-MELM.css' ]
  // }
  // }
  // }
  });

  // Load the Grunt plugins.
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-clean');
  // grunt.loadNpmTasks('grunt-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');
  // grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-bower-install');

  // Register the default tasks.
  grunt.registerTask('default', [ 'watch' ]);
};