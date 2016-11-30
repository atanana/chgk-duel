const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    entry: "./webpack/js/main.js",
    output: {
        path: 'public/dist',
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
                test: /\.(woff2?|svg)$/,
                loader: 'url-loader?limit=10000'
            },
            {
                test: /\.(ttf|eot)$/,
                loader: 'file-loader'
            },
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
