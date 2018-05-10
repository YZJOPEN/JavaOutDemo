'use strict';

import gulp from 'gulp';
import gulpLoadPlugins from 'gulp-load-plugins';
import cssnano from 'cssnano';
import autoprefixer from 'autoprefixer';
import browserSync from 'browser-sync';

const $ = gulpLoadPlugins();
const reload = browserSync.reload;

const AUTOPREFIXER_BROWSERS = [
    'ie >= 10',
    'ie_mob >= 10',
    'ff >= 30',
    'chrome >= 34',
    'safari >= 7',
    'opera >= 23',
    'ios >= 7',
    'android >= 4.4',
    'bb >= 10'
];

// 编译 Less，添加浏览器前缀
gulp.task('styles', () => {
    return gulp
        .src(['static/less/*.less'])
        .pipe($.changed('styles', {
            extension: '.less'
        }))
        .pipe($.plumber({
            errorHandler: err => {
                console.log(err);
                this.emit('end');
            }
        }))
        .pipe($.less())
        .pipe($.postcss([autoprefixer({
            browsers: AUTOPREFIXER_BROWSERS
        })]))
        .pipe(gulp.dest('static'))
        .pipe($.postcss([cssnano()]))        
        .pipe($.size({
            title: 'styles'
        }));
});

// 监视源文件变化自动cd编译
gulp.task('watch', () => {   
    gulp.watch('static/less/*.less', ['styles']);
});

// 启动预览服务，并监视 Dist 目录变化自动刷新浏览器
gulp.task('serve',['watch'], () => {
    browserSync({
        notify: false,
        // Customize the BrowserSync console logging prefix
        logPrefix: 'ASK',
        server: '',
        port: 2020,
        ui: {
            port: 2021,
            weinre: {
                port: 2022
            }
        },
        open: false
    });

    gulp.watch(['static/**/*'], reload);
});