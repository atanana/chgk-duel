const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    entry: "./webpack/js/main.js",
    output: {
        path: 'public',
        filename: "duel.js"
    },
    module: {
        loaders: [
            {
                test: /^webpack\/\.js$/,
                exclude: /(node_modules|bower_components)/,
                loader: 'babel-loader',
                query: {
                    presets: ['es2015']
                }
            },
            {
                test: /\.scss$/,
                // test: /^webpack\/\.scss$/,
                loader: ExtractTextPlugin.extract('css-loader!sass-loader?sourceMap')
            },
            {
                test: /\.svg/,
                loader: 'svg-url-loader'
            }
        ]
    },
    resolve: {
        alias: {
            jquery: "jquery/src/jquery"
        }
    },
    plugins: [
        new ExtractTextPlugin("main.css")
    ]
};
